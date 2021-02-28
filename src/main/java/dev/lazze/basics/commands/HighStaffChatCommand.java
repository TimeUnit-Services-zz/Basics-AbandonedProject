package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.DonorChatEvent;
import dev.lazze.basics.events.HighStaffChatEvent;
import dev.lazze.basics.events.StaffChatEvent;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;

public class HighStaffChatCommand extends Command
{
    public HighStaffChatCommand() {
        super("highstaffchat", Main.configuration.getString("permission.staff"), new String[] { "highsc", "ownerchat", "oc", "hsc" });
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
        if (player.getServer().getInfo().getName().equalsIgnoreCase(new StringBuilder().append(Main.configuration.getStringList("disabled-highstaffchat-in")).toString())) {
            player.sendMessage(Color.translate("&cHighStaff Chat is disabled on this server."));
            return;
        }
        if (HighStaffChatEvent.staff.contains(player.getUniqueId())) {
            HighStaffChatEvent.staff.remove(player.getUniqueId());
            player.sendMessage(Color.translate("&cHighStaff Chat has been Disabled."));
        }
        else {
            DonorChatEvent.donor.remove(player.getUniqueId());
            StaffChatEvent.staff.remove(player.getUniqueId());
            HighStaffChatEvent.staff.add(player.getUniqueId());
            player.sendMessage(Color.translate("&aHighStaff Chat has been Enabled."));
        }
    }
}
