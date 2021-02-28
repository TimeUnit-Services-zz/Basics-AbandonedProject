package dev.lazze.basics.commands.server;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.*;

public class UHCCommand extends Command
{
    private Main plugin;
    
    public UHCCommand(Main base) {
        super("uhc", (String)null, new String[0]);
        this.plugin = base;
        this.plugin.getProxy().getPluginManager().registerCommand((Plugin)this.plugin, (Command)this);
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "You must be player to execute this command.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        ServerInfo server = ProxyServer.getInstance().getServerInfo("UHC");
        player.sendMessage(Color.translate("&dSending to &fUHC&d."));
        player.connect(server);
    }
}
