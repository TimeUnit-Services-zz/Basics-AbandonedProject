package dev.lazze.basics.utils;

import net.md_5.bungee.*;
import net.md_5.bungee.api.connection.*;

public class Message
{
    public static void sendMessage(String message) {
        for (ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
            online.sendMessage(Color.translate(message));
        }
    }
    
    public static void sendMessage(String message, String permission) {
        for (ProxiedPlayer online : BungeeCord.getInstance().getPlayers()) {
            if (online.hasPermission(permission)) {
                online.sendMessage(Color.translate(message));
            }
        }
    }
}
