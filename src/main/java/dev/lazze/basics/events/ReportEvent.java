package dev.lazze.basics.events;

import dev.lazze.basics.Main;
import net.md_5.bungee.api.plugin.*;
import java.util.*;
import net.md_5.bungee.api.connection.*;

public class ReportEvent extends Event implements Listener
{
    public static Map<UUID, Long> reportCooldowns;
    
    static {
        ReportEvent.reportCooldowns = new HashMap<UUID, Long>();
    }
    
    public ReportEvent(Main plugin) {
        super(plugin);
    }
    
    public static void disable() {
        ReportEvent.reportCooldowns.clear();
    }
    
    public static void applyCooldown(ProxiedPlayer player) {
        ReportEvent.reportCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 180000L);
    }
    
    public static boolean isActive(ProxiedPlayer player) {
        return ReportEvent.reportCooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() < ReportEvent.reportCooldowns.get(player.getUniqueId());
    }
    
    public static long getMillisecondsLeft(ProxiedPlayer player) {
        if (ReportEvent.reportCooldowns.containsKey(player.getUniqueId())) {
            return Math.max(ReportEvent.reportCooldowns.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
}
