package dev.lazze.basics.antibot;

import dev.lazze.basics.events.AntiBotEvent;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;



public class AntiBotUtils
{
    public static void sendMessage(final String message) {
        for (final ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
            if (staff.hasPermission("basics.op") && !AntiBotEvent.ignore.contains(staff)) {
                staff.sendMessage(Color.translate(message));
            }
        }
    }
}
