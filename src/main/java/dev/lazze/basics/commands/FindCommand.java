package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class FindCommand extends Command {
  private Main plugin;
  
  public FindCommand(Main base) {
    super("find", null, new String[] { "search" });
    this.plugin = base;
    this.plugin.getProxy().getPluginManager().registerCommand((Plugin)this.plugin, this);
  }
  
  public void execute(CommandSender sender, String[] args) {
    if (args.length < 1) {
      sender.sendMessage(Color.translate("&cUsage: /find <playerName>"));
    } else {
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (target == null || !target.isConnected()) {
        sender.sendMessage(Color.translate("&cPlayer '" + args[0] + "' not found."));
        return;
      } 
      sender.sendMessage(Color.translate("&dPlayer &f" + target.getName() + " &dis now playing on &f" + target.getServer().getInfo().getName() + " &d."));
    } 
  }
}

