package dev.lazze.basics.events;

import java.util.*;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import dev.lazze.basics.utils.Message;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.event.*;
import java.util.concurrent.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.config.*;
import net.md_5.bungee.api.event.*;

public class PlayerEvent extends Event implements Listener
{
    public static int blockLengh;
    public static int block;
    public static boolean enabled;
    public static List<String> bots;
    public static boolean spam;
    public static int blocked;
    public static boolean edo;
    public static List<UUID> bridge;
    
    static {
        PlayerEvent.blockLengh = 16;
        PlayerEvent.block = 16;
        PlayerEvent.enabled = false;
        PlayerEvent.bots = new ArrayList<String>();
        PlayerEvent.spam = false;
        PlayerEvent.blocked = 0;
        PlayerEvent.edo = false;
        PlayerEvent.bridge = new ArrayList<UUID>();
    }
    
    public PlayerEvent(final Main plugin) {
        super(plugin);
        this.resetSpam();
        this.resetFully();
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin)plugin, (Listener)this);
    }
    
    @EventHandler(priority = -64)
    public void onLog(final PreLoginEvent event) {
        final String name = event.getConnection().getName();
        if (PlayerEvent.edo && name.length() >= PlayerEvent.blockLengh) {
            event.setCancelled(true);
            ++PlayerEvent.blocked;
            return;
        }
        if (name.length() == PlayerEvent.blockLengh) {
            if (PlayerEvent.enabled || PlayerEvent.spam) {
                event.setCancelled(true);
                ++PlayerEvent.blocked;
                return;
            }
            if (!PlayerEvent.bots.contains(name)) {
                PlayerEvent.bots.add(name);
            }
            if (PlayerEvent.bots.size() > 100) {
                PlayerEvent.spam = true;
                Message.sendMessage(Color.translate("&cAntiBot has ben automatically &aenabled"), Main.configuration.getString("antibot.permission"));
                PlayerEvent.bots.clear();
            }
        }
    }
    
    public void resetSpam() {
        ProxyServer.getInstance().getScheduler().schedule((Plugin) Main.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                if (PlayerEvent.spam) {
                    ProxyServer.getInstance().getConsole().sendMessage("!!! BLOCKED " + PlayerEvent.blocked + " CONNECTIONS !!!");
                    ProxyServer.getInstance().getConsole().sendMessage("!!! BLOCKED " + PlayerEvent.blocked + " CONNECTIONS !!!");
                    ProxyServer.getInstance().getConsole().sendMessage("!!! BLOCKED " + PlayerEvent.blocked + " CONNECTIONS !!!");
                    PlayerEvent.blocked = 0;
                    PlayerEvent.spam = false;
                    Message.sendMessage(Color.translate("&cAntiBot has ben automatically &cdisabled"), Main.configuration.getString("permission.staff"));
                }
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }
    
    public void resetFully() {
        ProxyServer.getInstance().getScheduler().schedule((Plugin) Main.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                PlayerEvent.enabled = false;
            }
        }, 5L, 5L, TimeUnit.MINUTES);
    }
    
    @EventHandler
    public void onPlayerJoin(final ServerSwitchEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if (!player.hasPermission("basics.staff")) {
            return;
        }
        final ServerInfo server = player.getServer().getInfo();
        if (server.getName().equalsIgnoreCase("UHC-1") || server.getName().equalsIgnoreCase("UHC-2")) {
            player.connect(server);
        }
        Message.sendMessage(Main.configuration.getString("staff-join.message").replace("%player%", player.getName()).replace("%server%", server.getName()), Main.configuration.getString("permission.staff"));
    }
    
    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if (!player.hasPermission("basics.staff")) {
            return;
        }
        Message.sendMessage(Color.translate(Main.configuration.getString("staff-leave.message")).replace("%player%", player.getName()), Main.configuration.getString("permission.staff"));
    }
    
    @EventHandler
    public void onChat(final ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }
        final ProxiedPlayer player = (ProxiedPlayer)event.getSender();
        final ServerInfo server = player.getServer().getInfo();
        final String message = event.getMessage().toLowerCase();
        if (server.getName().equalsIgnoreCase("Hub") && (event.getMessage().equalsIgnoreCase("/HCF") || event.getMessage().equalsIgnoreCase("/kits") || event.getMessage().equalsIgnoreCase("/uhc"))) {
            event.setCancelled(true);
            player.sendMessage(Color.translate("&cPlease connect to that server trough hub! To go to the hub use /hub or /lobby"));
        }
    }
}
