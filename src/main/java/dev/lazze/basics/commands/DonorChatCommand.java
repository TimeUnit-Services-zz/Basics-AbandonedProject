package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.DonorChatEvent;
import dev.lazze.basics.events.HighStaffChatEvent;
import dev.lazze.basics.events.StaffChatEvent;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;

public class DonorChatCommand extends Command
{
    public DonorChatCommand() {
        super("donorchat", Main.configuration.getString("permission.staff"), new String[] { "donorsc", "donatorchat", "dc", "dsc" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Color.translate("&cPlayer use only!"));
            return;
        }
        final ProxiedPlayer player = (ProxiedPlayer)sender;
        if (!player.hasPermission(Main.configuration.getString("permission.staff"))) {
            player.sendMessage(Color.translate("&cNo Permissions."));
            return;
        }
        if (player.getServer().getInfo().getName().equalsIgnoreCase(new StringBuilder().append(Main.configuration.getStringList("disabled-donor-in")).toString())) {
            player.sendMessage(Color.translate("&cHighStaff Chat is disabled on this server."));
            return;
        }
        if (DonorChatEvent.donor.contains(player.getUniqueId())) {
            DonorChatEvent.donor.remove(player.getUniqueId());
            player.sendMessage(Color.translate("&cYour Donor Chat has been disabled."));
        }
        else {
            HighStaffChatEvent.staff.remove(player.getUniqueId());
            StaffChatEvent.staff.remove(player.getUniqueId());
            DonorChatEvent.donor.add(player.getUniqueId());
            player.sendMessage(Color.translate("&aYour Donor Chat has been enabled."));
        }
    }
}
