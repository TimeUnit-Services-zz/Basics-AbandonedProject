package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;

public class MaintenanceCommand extends Command
{
    private Main plugin;
    
    public MaintenanceCommand(Main plugin) {
        super("maintenance", Main.configuration.getString("maintenance.permission"), new String[] { "globalwl", "globalwhitelist" });
        this.plugin = plugin;
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Main.configuration.getString("maintenance.permission"))) {
            sender.sendMessage(ChatColor.RED + "No Permissions.");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Correct Usage: /maintenance <on/off>");
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                Main.configuration.set("maintenance.whitelisted", (Object)true);
                this.plugin.saveConfig();
                this.plugin.reloadConfig();
                ProxyServer.getInstance().getPlayers().forEach(player -> {
                    if (!player.hasPermission(Main.configuration.getString("permission.staff"))) {
                        player.disconnect(Color.translate(Main.configuration.getString("maintenance.kick-message").replace("{nl}", "\n")));
                    }
                    return;
                });
                sender.sendMessage(ChatColor.GREEN + "Successfully turned on the network maintenance.");
            }
            else if (args[0].equalsIgnoreCase("off")) {
                Main.configuration.set("maintenance.whitelisted", (Object)false);
                this.plugin.saveConfig();
                this.plugin.reloadConfig();
                sender.sendMessage(ChatColor.RED + "Successfully turned off the network maintenance.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Correct Usage: /maintenance <on/off>");
            }
        }
        else {
            sender.sendMessage(ChatColor.RED + "Correct Usage: /maintenance <on/off>");
        }
    }
}
