package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.*;

public class ConnectCommand extends Command
{
    private Main plugin;
    
    public ConnectCommand(Main base) {
        super("connect", (String)null, new String[] {});
        this.plugin = base;
        this.plugin.getProxy().getPluginManager().registerCommand((Plugin)this.plugin, (Command)this);
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "You must be player to execute this command.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        if (args.length < 1) {
            player.sendMessage(ChatColor.GRAY + "You're currently connected on " + ChatColor.WHITE + ChatColor.BOLD + player.getServer().getInfo().getName());
            player.sendMessage(ChatColor.RED + "Usage: /join <server>");
        }
        else {
            ServerInfo server = ProxyServer.getInstance().getServerInfo(args[0]);
            if (server == null) {
                player.sendMessage(ChatColor.RED + "Server '" + args[0] + "' doesn't exists.");
                return;
            }
            player.sendMessage(ChatColor.GRAY + "Connecting to " + ChatColor.WHITE + args[0] + ChatColor.GRAY + '.');
            player.connect(server);
        }
    }
}
