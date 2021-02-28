package dev.lazze.basics.utils;

import net.md_5.bungee.*;
import net.md_5.bungee.api.connection.*;

public class Message
{
    public static void sendMessage(final String message) {
        for (final ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
            online.sendMessage(Color.translate(message));
        }
    }
    
    public static void sendMessage(final String message, final String permission) {
        for (final ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
            if (online.hasPermission(permission)) {
                online.sendMessage(Color.translate(message));
            }
        }
    }
}
