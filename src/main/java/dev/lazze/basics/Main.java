package dev.lazze.basics;

import dev.lazze.basics.antibot.BotBoth;
import dev.lazze.basics.commands.*;
import dev.lazze.basics.events.*;
import dev.lazze.basics.utils.Cooldowns;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.Bukkit;

import java.nio.file.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.*;


public class Main extends Plugin implements Listener
{
    public static Main instance;
    public static Console console;
    public static PluginManager pluginmanager;
    public static String incomingBungeeBroadcastChannel;
    public static String incomingAnnounceChannel;
    public static String incomingFilterChannel;
    public static String incomingCommandChannel;
    public static String incomingBanChannel;
    public static String incomingAlertsChannel;
    public static String incomingAuthChannel;
    public static String incomingPermissionsChannel;
    public static String outgoingPremiumChannel;
    public static Configuration configuration;


    static {
        Main.incomingBungeeBroadcastChannel = "BungeeBroadcast";
        Main.incomingAnnounceChannel = "Announce";
        Main.incomingFilterChannel = "Filter";
        Main.incomingCommandChannel = "Command";
        Main.incomingBanChannel = "AutoBan";
        Main.incomingAlertsChannel = "Alerts";
        Main.incomingAuthChannel = "Auth";
        Main.incomingPermissionsChannel = "Permissions";
        Main.outgoingPremiumChannel = "Premium";
    }
    
    public static Main getInstance() {
        return Main.instance;
    }
    
    public void onEnable() {
        (Main.instance = this).loadConfig();
        BotBoth.load(this.getDataFolder());
        registerCooldowns();
        this.setupBoth();
        this.clearBots();
        new AntiBotEvent(this);
        new PlayerEvent(this);
        new SilentEvent(this);
        new StaffChatEvent(this);
        new HighStaffChatEvent(this);
        new FindCommand(this);
        new BroadcastCommand(this);
        new GListCommand(this);
        new DonorChatEvent(this);
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new MaintenanceEventA(this));
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)new MaintenanceEventB(this));
        ProxyServer.getInstance().getPluginManager().registerCommand((Plugin)this, (Command)new ReloadCommand());
        if (Main.configuration.getBoolean("booleans.ReportCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new ReportCommand());
        }
        if (Main.configuration.getBoolean("booleans.RequestCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new RequestCommand());
        }
        if (Main.configuration.getBoolean("booleans.MaintenanceCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new MaintenanceCommand(this));
        }
        if (Main.configuration.getBoolean("booleans.StaffChatCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new StaffChatCommand());
        }
        if (Main.configuration.getBoolean("booleans.DonorChatCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new DonorChatCommand());
        }
        if (Main.configuration.getBoolean("booleans.SilentCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new SilentCommand());
        }
        if (Main.configuration.getBoolean("booleans.ConnectCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new ConnectCommand(this));
        }
        if (Main.configuration.getBoolean("booleans.HighStaffChatCommand")) {
            ProxyServer.getInstance().getPluginManager().registerCommand((Plugin) this, (Command) new HighStaffChatCommand());
        }
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new MotdCommand());
        getProxy().getPluginManager().registerListener(this, this);
        ProxyServer.getInstance().registerChannel(Main.incomingBungeeBroadcastChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingAnnounceChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingFilterChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingCommandChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingBanChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingAlertsChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingAuthChannel);
        ProxyServer.getInstance().registerChannel(Main.incomingPermissionsChannel);
        ProxyServer.getInstance().registerChannel(Main.outgoingPremiumChannel);
    }
    
    public void onDisable() {
    }
    
    private void loadConfig() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                Throwable t = null;
                try {
                    final InputStream in = this.getResourceAsStream("config.yml");
                    try {
                        Files.copy(in, file.toPath(), new CopyOption[0]);
                    }
                    finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t2 = t = null;
                    }
                    else {
                        final Throwable t2 = null;
                        if (t != t2) {
                            t.addSuppressed(t2);
                        }
                    }
                }
                if (t == null) {
                    final Throwable t2 = t = null;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Main.configuration = ConfigurationProvider.getProvider( YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(Main.configuration, new File(this.getDataFolder(), "config.yml"));
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to save configuration", e);
        }
    }
    
    public void reloadConfig() {
        try {
            Main.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }
    
    public void clearBots() {
        this.getProxy().getScheduler().schedule((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                BotBoth.attacks.clear();
                BotBoth.pings.clear();
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }
    Throwable t = null;
    @EventHandler
    public void onPing(final ProxyPingEvent e) {
        final ServerPing sp = e.getResponse();
        final String message = Main.configuration.getString("motd").replaceAll("%time", this.getTime()).replaceAll("%newline", "\n").replace("%heart%", "�?�").replace("%star%", "★").replace("%crown%", "♕").replace("%sad%", "☹").replace("%bigarrow%", "➤").replace("%airplane%", "✈").replace("%arrowlittle%", "▸").replace("%yesv%", "✓").replace("%nov%", "✘").replace("%star2%", "✦").replace("%idk%", "?").replace("%right%", "☞").replace("%death%", "☠").replace("%church%", "✞").replace("|", "\u2503").replace("%arroww%", "➥");
        sp.getPlayers().setMax(Main.configuration.getInt("slots"));
        sp.setDescription(t(message));
        e.setResponse(sp);
    }
    public String getTime() {
        final Configuration config = Main.configuration;
        final String dateStop = config.getString("date");
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        Date date = null;
        try {
            format.setTimeZone(TimeZone.getTimeZone(config.getString("timezone")));
            date = format.parse(dateStop);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        final Date current = new Date();
        final long diff = date.getTime() - current.getTime();
        if (diff < 0L) {
            return config.getString(t("time-value-end"));
        }
        if (config.getInt("clock-type") == 1) {
            final long days = TimeUnit.MILLISECONDS.toDays(diff);
            final long hours = TimeUnit.MILLISECONDS.toHours(diff);
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            final long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            final long rhours = (days == 0L) ? hours : (hours % (days * 24L));
            final long rminutes = (hours == 0L) ? minutes : (minutes % (hours * 60L));
            final long rseconds = (minutes == 0L) ? seconds : (seconds % (minutes * 60L));
            final StringBuilder sb = new StringBuilder();
            if (days > 1L) {
                sb.append(String.valueOf(days) + "d");
            }
            if (days == 1L) {
                sb.append(String.valueOf(days) + "d");
            }
            if (rhours > 1L) {
                sb.append(String.valueOf((days > 0L) ? ", " : "") + rhours + "h");
            }
            if (rhours == 1L) {
                sb.append(String.valueOf((days > 0L) ? ", " : "") + rhours + "h");
            }
            if (rminutes > 1L) {
                sb.append(String.valueOf(((days > 0L && hours <= 0L) || hours > 0L) ? ", " : "") + rminutes + "m");
            }
            if (rminutes == 1L) {
                sb.append(String.valueOf(((days > 0L && hours <= 0L) || hours > 0L) ? ", " : "") + rminutes + "m");
            }
            if (rseconds > 1L) {
                sb.append(String.valueOf((((days > 0L || hours > 0L) && minutes <= 0L) || minutes > 0L) ? ", " : "") + rseconds + "s");
            }
            if (rseconds == 1L) {
                sb.append(String.valueOf((((days > 0L || hours > 0L) && minutes <= 0L) || minutes > 0L) ? ", " : "") + rseconds + "s");
            }
            return sb.toString();
        }
        if (config.getInt("clock-type") == 2) {
            final long seconds2 = TimeUnit.MILLISECONDS.toSeconds(diff);
            final long minutes2 = TimeUnit.MILLISECONDS.toMinutes(diff);
            final long hours2 = TimeUnit.MILLISECONDS.toHours(diff);
            final long days2 = TimeUnit.MILLISECONDS.toDays(diff);
            final long rhours = (days2 == 0L) ? hours2 : (hours2 % (days2 * 24L));
            final long rminutes = (hours2 == 0L) ? minutes2 : (minutes2 % (hours2 * 60L));
            final long rseconds = (minutes2 == 0L) ? seconds2 : (seconds2 % (minutes2 * 60L));
            return String.valueOf(days2) + ":" + rhours + ":" + rminutes + ":" + rseconds;
        }
        return null;
    }
    public void setupBoth() {
        this.getProxy().getScheduler().schedule((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                BotBoth.joins = 0;
            }
        }, 0L, 1L, TimeUnit.SECONDS);
        this.getProxy().getScheduler().runAsync((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(AntiBotEvent.startup_time * 1000);
                }
                catch (InterruptedException ex) {}
                AntiBotEvent.startup_multiplier = 1;
            }
        });
    }

    public static Main getPlugin() {
        return Main.instance;
    }
    
    public static Console getConsole() {
        return Main.console;
    }
    
    public static PluginManager getPluginManager() {
        return Main.pluginmanager;
    }
    public static String t(final String i) {
        return ChatColor.translateAlternateColorCodes('&', i);
    }
    private void registerCooldowns() {
        Cooldowns.createCooldown("broadcast_cooldown");
    }



}