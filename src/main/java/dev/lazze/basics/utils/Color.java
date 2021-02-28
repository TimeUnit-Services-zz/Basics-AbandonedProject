package dev.lazze.basics.utils;

import net.md_5.bungee.api.*;

public class Color
{
    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
