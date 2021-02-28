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

public class DonorChatEvent extends Event implements Listener
{
    public static List<UUID> donor;
    
    static {
        DonorChatEvent.donor = new ArrayList<UUID>();
    }
    
    public DonorChatEvent(final Main plugin) {
        super(plugin);
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin)plugin, (Listener)this);
    }
    
    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if (DonorChatEvent.donor.contains(player.getUniqueId())) {
            DonorChatEvent.donor.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onChat(final ChatEvent event) {
        if (event.getSender() instanceof ProxiedPlayer) {
            final ProxiedPlayer player = (ProxiedPlayer)event.getSender();
            final ServerInfo server = player.getServer().getInfo();
            if (event.getMessage().startsWith("/")) {
                return;
            }
            if (DonorChatEvent.donor.contains(player.getUniqueId())) {
                if (player.hasPermission(Main.configuration.getString("permission.staff"))) {
                    for (final ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
                        if (online.hasPermission(Main.configuration.getString("permission.staff")) && !SilentEvent.silent.contains(online.getUniqueId())) {
                            online.sendMessage(Color.translate(Main.configuration.getString("donorchat.message").replace("%server%", server.getName()).replace("%player%", player.getName()).replace("%message%", event.getMessage())));
                            event.setCancelled(true);
                        }
                    }
                }
                else {
                    DonorChatEvent.donor.remove(player.getUniqueId());
                }
            }
        }
    }
}
