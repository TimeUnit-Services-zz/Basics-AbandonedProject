package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.RequestEvent;
import dev.lazze.basics.events.SilentEvent;
import dev.lazze.basics.utils.Color;
import dev.lazze.basics.utils.StringUtils;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.*;
import net.md_5.bungee.api.config.*;


public class RequestCommand extends Command
{
    public RequestCommand() {
        super("request", "", new String[] { "helpop", "staffhelp" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Color.translate("&cPlayer use only!"));
            return;
        }
        final ProxiedPlayer player = (ProxiedPlayer)sender;
        final ServerInfo server = player.getServer().getInfo();
        if (player.getServer().getInfo().getName().equalsIgnoreCase(new StringBuilder().append(Main.configuration.getStringList("disabled-request-in")).toString())) {
            player.sendMessage(Color.translate("&cRequest is disabled on this server."));
            return;
        }
        if (args.length == 0) {
            player.sendMessage(Color.translate("&cCorrect Usage: /request <reason>"));
        }
        else if (RequestEvent.isActive(player)) {
            player.sendMessage(Color.translate("&cYou can't use this command for another " + StringUtils.formatMilisecondsToMinutes(RequestEvent.getMillisecondsLeft(player))));
        }
        else {
            RequestEvent.applyCooldown(player);
            final StringBuilder message = new StringBuilder();
            for (int i = 0; i < args.length; ++i) {
                message.append(args[i]).append(" ");
            }
            if (player.getServer().getInfo().getName().equalsIgnoreCase("HCF") || player.getServer().getInfo().getName().equalsIgnoreCase("KitMap")) {
                if (message.toString().toLowerCase().contains("spawn") && message.toString().toLowerCase().contains("coords")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("koth") && message.toString().toLowerCase().contains("coords")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("conquest") && message.toString().toLowerCase().contains("coords")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("border")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /help"));
                    return;
                }
                if (message.toString().toLowerCase().contains("algun") && message.toString().toLowerCase().contains("staff")) {
                    player.sendMessage(Color.translate("&cIf you need some help, you can use your teamspeak3"));
                    return;
                }
                if (message.toString().toLowerCase().contains("some") || message.toString().toLowerCase().contains("staff")) {
                    player.sendMessage(Color.translate("&cIf you need some help, you can use your teamspeak3"));
                    return;
                }
                if (message.toString().toLowerCase().contains("end") || message.toString().toLowerCase().contains("portal")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("nether") || message.toString().toLowerCase().contains("portal")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("end") || message.toString().toLowerCase().contains("exit")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("nether")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
                if (message.toString().toLowerCase().contains("exit")) {
                    player.sendMessage(Color.translate("&cFor check that you can type /coords"));
                    return;
                }
            }
            player.sendMessage(Color.translate("&aYour request request has been submitted, we will take maximum 3 minutes in answer it!"));
            for (final ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
                if (!SilentEvent.silent.contains(online.getUniqueId()) && online.hasPermission(Main.configuration.getString("request.permission"))) {
                    final ProxiedPlayer proxiedPlayer = online;
                    Main.getInstance();
                    proxiedPlayer.sendMessage(Color.translate(Main.configuration.getString("request.message").replace("%server%", player.getServer().getInfo().getName()).replace("%player%", player.getName()).replace("%message%", message).replace("{nl}", "\n")));
                }
            }
        }
    }
}
