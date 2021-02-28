package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.DonorChatEvent;
import dev.lazze.basics.events.HighStaffChatEvent;
import dev.lazze.basics.events.StaffChatEvent;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;

public class StaffChatCommand extends Command
{
    public StaffChatCommand() {
        super("staffchat", Main.configuration.getString("permission.staff"), new String[] { "sc", "schat" });
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Color.translate("&cPlayer use only!"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        if (!player.hasPermission(Main.configuration.getString("permission.staff"))) {
            player.sendMessage(Color.translate("&cNo Permissions."));
            return;
        }
        if (player.getServer().getInfo().getName().equalsIgnoreCase(new StringBuilder().append(Main.configuration.getStringList("disabled-staffchat-in")).toString())) {
            player.sendMessage(Color.translate("&cStaff Chat is disabled on this server."));
            return;
        }
        if (StaffChatEvent.staff.contains(player.getUniqueId())) {
            StaffChatEvent.staff.remove(player.getUniqueId());
            player.sendMessage(Color.translate("&cYour Staff Chat has been disabled."));
        }
        else {
            DonorChatEvent.donor.remove(player.getUniqueId());
            HighStaffChatEvent.staff.remove(player.getUniqueId());
            StaffChatEvent.staff.add(player.getUniqueId());
            player.sendMessage(Color.translate("&aYour Staff Chat has been enabled."));
        }
    }
}
