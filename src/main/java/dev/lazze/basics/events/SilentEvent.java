package dev.lazze.basics.events;

import dev.lazze.basics.Main;
import net.md_5.bungee.api.plugin.*;

import java.util.*;


public class SilentEvent extends Event implements Listener
{
    public static List<UUID> silent;
    
    static {
        SilentEvent.silent = new ArrayList<UUID>();
    }
    
    public SilentEvent(final Main plugin) {
        super(plugin);
    }
}
