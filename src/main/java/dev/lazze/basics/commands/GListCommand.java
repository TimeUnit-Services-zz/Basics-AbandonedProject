package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class GListCommand extends Command {
  private final Main plugin;
  
  public GListCommand(Main base) {
    super("globallist", null, new String[] { "glist" });
    this.plugin = base;
    this.plugin.getProxy().getPluginManager().registerCommand((Plugin)this.plugin, this);
  }
  
  public void execute(CommandSender sender, String[] args) {
    sender.sendMessage(Color.translate("&aShowing all online players..."));
    for (ServerInfo server : ProxyServer.getInstance().getServers().values())
      sender.sendMessage(Color.translate("&d" + server.getName() + " &7[" + server.getPlayers().size() + "&7]: &f" + server.getPlayers().toString().replace("]", "").replace("[", ""))); 
    sender.sendMessage("");
    sender.sendMessage(Color.translate("&7Online Players: &f" + ProxyServer.getInstance().getPlayers().size()));
  }
}

