package dev.lazze.basics.events;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.event.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.config.*;
import java.util.*;

public class StaffChatEvent extends Event implements Listener
{
    public static List<UUID> staff;
    
    static {
        StaffChatEvent.staff = new ArrayList<UUID>();
    }
    
    public StaffChatEvent(Main plugin) {
        super(plugin);
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin)plugin, (Listener)this);
    }
    
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (StaffChatEvent.staff.contains(player.getUniqueId())) {
            StaffChatEvent.staff.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer)event.getSender();
            ServerInfo server = player.getServer().getInfo();
            if (event.getMessage().startsWith("/")) {
                return;
            }
            if (StaffChatEvent.staff.contains(player.getUniqueId())) {
                if (player.hasPermission(Main.configuration.getString("permission.staff"))) {
                    for (ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                        if (online.hasPermission(Main.configuration.getString("permission.staff")) && !SilentEvent.silent.contains(online.getUniqueId())) {
                            online.sendMessage(Color.translate(Main.configuration.getString("staffchat.message").replace("%player%", player.getName()).replace("%message%", event.getMessage()).replace("%server%", player.getServer().getInfo().getName())));
                            event.setCancelled(true);
                        }
                    }
                }
                else {
                    StaffChatEvent.staff.remove(player.getUniqueId());
                }
            }
        }
    }
}
