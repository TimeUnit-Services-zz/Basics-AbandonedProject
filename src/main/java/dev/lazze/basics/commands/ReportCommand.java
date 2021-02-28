package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.ReportEvent;
import dev.lazze.basics.events.SilentEvent;
import dev.lazze.basics.utils.Color;
import dev.lazze.basics.utils.StringUtils;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.*;
import net.md_5.bungee.api.config.*;


public class ReportCommand extends Command
{
    public ReportCommand() {
        super("report", "", new String[] { "hacker" });
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Color.translate("&cPlayer use only!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        ServerInfo server = player.getServer().getInfo();
        if (server.getName().equalsIgnoreCase("Hub")) {
            player.sendMessage(Color.translate("&cReport is disabled on this server."));
            return;
        }
        if (args.length < 2) {
            player.sendMessage(Color.translate("&cCorrect Usage: /report <player> <reason>"));
        }
        else if (ReportEvent.isActive(player)) {
            player.sendMessage(Color.translate("&cYou can't use this command for another " + StringUtils.formatMilisecondsToMinutes(ReportEvent.getMillisecondsLeft(player))));
        }
        else {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Color.translate("&cThat player is not online!"));
                return;
            }
            ServerInfo serverTarget = target.getServer().getInfo();
            if (serverTarget == null) {
                sender.sendMessage(Color.translate("&cThat player is not online!"));
                return;
            }
            if (target == player) {
                player.sendMessage(Color.translate("&cYou can't report yourself!"));
                return;
            }
            ReportEvent.applyCooldown(player);
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                message.append(args[i]).append(" ");
            }
            player.sendMessage(Color.translate("&aYou have reported " + target.getName() + " &aand all staff is now alerted!"));
            for (ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
                if (online.hasPermission(Main.configuration.getString("report.permission")) && !SilentEvent.silent.contains(online.getUniqueId())) {
                    ProxiedPlayer proxiedPlayer = online;
                    Main.getInstance();
                    proxiedPlayer.sendMessage(Color.translate(Main.configuration.getString("report.message").replace("%server-target%", serverTarget.getName().toUpperCase()).replace("%player%", player.getName()).replace("%target%", target.getName()).replace("%message%", message).replace("{nl}", "\n")));
                }
            }
        }
    }
}
