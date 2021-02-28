package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.*;

public class HubCommand extends Command
{
    public HubCommand() {
        super("hub", "", new String[] { "lobby" });
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Color.translate("&cPlayer use only!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        if (player.hasPermission(Main.configuration.getString("hub.permission")) && player.getServer().getInfo().getName().equalsIgnoreCase(Main.configuration.getString("hub.server-name"))) {
            player.sendMessage(Color.translate("&cYou are already connected to the " + Main.configuration.getString("hub.server-name") + "!"));
            return;
        }
        if (!player.hasPermission(Main.configuration.getString("permission.user"))) {
            ServerInfo target = ProxyServer.getInstance().getServerInfo(Main.configuration.getString("hub.server-name"));
            player.connect(target);
        }
        else if (player.hasPermission(Main.configuration.getString("permission.user"))) {
            ServerInfo target = ProxyServer.getInstance().getServerInfo(Main.configuration.getString("hub.server-name"));
            player.connect(target);
        }
    }
}
