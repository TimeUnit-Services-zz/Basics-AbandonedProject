package dev.lazze.basics.events;

import dev.lazze.basics.Main;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.event.*;

public class MaintenanceEventA implements Listener
{
    private Main plugin;
    
    public MaintenanceEventA(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        if (!Main.configuration.getBoolean("maintenance.whitelisted")) {
            return;
        }
        ServerPing ping = event.getResponse();
        ping.setVersion(new ServerPing.Protocol(ChatColor.translateAlternateColorCodes('&', Main.configuration.getString("maintenance.ping-message")), 1));
    }
}
