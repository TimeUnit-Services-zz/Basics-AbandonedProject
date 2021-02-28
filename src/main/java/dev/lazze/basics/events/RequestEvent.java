package dev.lazze.basics.events;

import dev.lazze.basics.Main;
import net.md_5.bungee.api.plugin.*;
import java.util.*;
import net.md_5.bungee.api.connection.*;

public class RequestEvent extends Event implements Listener
{
    public static Map<UUID, Long> requestCooldowns;
    
    static {
        RequestEvent.requestCooldowns = new HashMap<UUID, Long>();
    }
    
    public RequestEvent(final Main plugin) {
        super(plugin);
    }
    
    public static void disable() {
        RequestEvent.requestCooldowns.clear();
    }
    
    public static void applyCooldown(final ProxiedPlayer player) {
        RequestEvent.requestCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 180000L);
    }
    
    public static boolean isActive(final ProxiedPlayer player) {
        return RequestEvent.requestCooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() < RequestEvent.requestCooldowns.get(player.getUniqueId());
    }
    
    public static long getMillisecondsLeft(final ProxiedPlayer player) {
        if (RequestEvent.requestCooldowns.containsKey(player.getUniqueId())) {
            return Math.max(RequestEvent.requestCooldowns.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
}
