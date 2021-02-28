package dev.lazze.basics.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public class MOTDListerner extends JavaPlugin implements Listener {
  private static MOTDListerner instance;
  
  int taskId;
  
  public MOTDListerner() {
    this.taskId = -1;
    instance = this;
  }
  
  public static MOTDListerner getInstance() {
    return instance;
  }

  public void onEnable() {
    FileConfiguration config = getConfig();
    Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)this);
    config.options().copyDefaults(true);
    saveDefaultConfig();
    startCountdown();
  }
  
  public void startCountdown() {
    BukkitScheduler scheduler = getServer().getScheduler();
    this.taskId = scheduler.scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
          public void run() {
            if (dev.lazze.basics.events.MOTDListerner.this.getCountdown() == 0) {
              dev.lazze.basics.events.MOTDListerner.this.cancelTask(dev.lazze.basics.events.MOTDListerner.this.taskId);
              dev.lazze.basics.events.MOTDListerner.this.taskId = -1;
              Bukkit.getServer().dispatchCommand((CommandSender) dev.lazze.basics.events.MOTDListerner.this.getServer().getConsoleSender(), dev.lazze.basics.events.MOTDListerner.this.getConfig().getString("command-finish"));
              System.out.println("Command-Finish has been successfully executed!");
            } 
          }
        }, 0L, 20L);
  }
  
  public void cancelTask(int id) {
    Bukkit.getServer().getScheduler().cancelTask(id);
  }
  
  public int getCountdown() {
    FileConfiguration config = getConfig();
    String dateStop = config.getString("date");
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    Date date = null;
    try {
      format.setTimeZone(TimeZone.getTimeZone(config.getString("timezone")));
      date = format.parse(dateStop);
    } catch (ParseException e) {
      e.printStackTrace();
    } 
    Date current = new Date();
    long diff = date.getTime() - current.getTime();
    if (diff > 0L) {
      Integer seconds = Integer.valueOf((int)TimeUnit.MILLISECONDS.toSeconds(diff));
      return seconds.intValue();
    } 
    return 0;
  }
  
  @EventHandler
  public void onPing(ServerListPingEvent e) {
    FileConfiguration config = getConfig();
    String message = config.getString("motd").replaceAll("%time", getTime())
      .replaceAll("%newline", "\n");
    e.setMotd(t(message));
    e.setMaxPlayers(config.getInt("slots"));
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (cmd.getName().equalsIgnoreCase("motdcountdown"))
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("reload")) {
          if (sender.hasPermission("motdcountdown.reload")) {
            reloadConfig();
            sender.sendMessage(t("&cMOTDCountdown reloaded successfully!"));
            return true;
          } 
        } else {
          sender.sendMessage(t("&cError, Invalid Usage, use: /motdcountdown reload"));
        } 
      } else {
        sender.sendMessage(t("&cError, Invalid Usage, use: /motdcountdown reload"));
      }  
    return true;
  }
  
  public String getTime() {
    FileConfiguration config = getConfig();
    String dateStop = config.getString("date");
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
    Date date = null;
    try {
      format.setTimeZone(TimeZone.getTimeZone(config.getString("timezone")));
      date = format.parse(dateStop);
    } catch (ParseException e) {
      e.printStackTrace();
    } 
    Date current = new Date();
    long diff = date.getTime() - current.getTime();
    if (diff >= 0L) {
      if (config.getInt("clock-type") == 1) {
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long rhours = (days == 0L) ? hours : (hours % days * 24L);
        long rminutes = (hours == 0L) ? minutes : (minutes % hours * 60L);
        long rseconds = (minutes == 0L) ? seconds : (seconds % minutes * 60L);
        StringBuilder sb = new StringBuilder();
        if (days > 1L)
          sb.append(String.valueOf(days) + " days"); 
        if (days == 1L)
          sb.append(String.valueOf(days) + " day"); 
        if (rhours > 1L)
          sb.append(String.valueOf((days > 0L) ? ", " : "") + rhours + " hours"); 
        if (rhours == 1L)
          sb.append(String.valueOf((days > 0L) ? ", " : "") + rhours + " hour"); 
        if (rminutes > 1L)
          sb.append(String.valueOf(((days > 0L && hours <= 0L) || hours > 0L) ? ", " : "") + rminutes + " minutes"); 
        if (rminutes == 1L)
          sb.append(String.valueOf(((days > 0L && hours <= 0L) || hours > 0L) ? ", " : "") + rminutes + " minute"); 
        if (rseconds > 1L)
          sb.append(String.valueOf((((days > 0L || hours > 0L) && minutes <= 0L) || minutes > 0L) ? ", " : "") + rseconds + " seconds"); 
        if (rseconds == 1L)
          sb.append(String.valueOf((((days > 0L || hours > 0L) && minutes <= 0L) || minutes > 0L) ? ", " : "") + rseconds + " second"); 
        return sb.toString();
      } 
      if (config.getInt("clock-type") == 2) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long rhours = (days == 0L) ? hours : (hours % days * 24L);
        long rminutes = (hours == 0L) ? minutes : (minutes % hours * 60L);
        long rseconds = (minutes == 0L) ? seconds : (seconds % minutes * 60L);
        return String.valueOf(days) + ":" + rhours + ":" + rminutes + ":" + rseconds;
      } 
    } else {
      return config.getString(t("time-value-end"));
    } 
    return null;
  }
  
  public static String t(String i) {
    return ChatColor.translateAlternateColorCodes('&', i);
  }
}
