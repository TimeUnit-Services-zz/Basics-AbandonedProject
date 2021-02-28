package dev.lazze.basics.utils;

import java.util.HashMap;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Cooldowns {
  public static void createCooldown(String k) {
    if (cooldown.containsKey(k))
      throw new IllegalArgumentException("Cooldown already exists."); 
    cooldown.put(k, new HashMap<>());
  }
  
  public static HashMap<UUID, Long> getCooldownMap(String k) {
    if (cooldown.containsKey(k))
      return cooldown.get(k); 
    return null;
  }
  
  public static void addCooldown(String k, ProxiedPlayer p, int seconds) {
    if (!cooldown.containsKey(k))
      throw new IllegalArgumentException(k + " does not exist"); 
    long next = System.currentTimeMillis() + seconds * 1000L;
    ((HashMap<UUID, Long>)cooldown.get(k)).put(p.getUniqueId(), Long.valueOf(next));
  }
  
  public static boolean isOnCooldown(String k, ProxiedPlayer p) {
    return (cooldown.containsKey(k) && ((HashMap)cooldown.get(k)).containsKey(p.getUniqueId()) && System.currentTimeMillis() <= ((Long)((HashMap)cooldown.get(k)).get(p.getUniqueId())).longValue());
  }
  
  public static int getCooldownForPlayerInt(String k, ProxiedPlayer p) {
    return (int)(((Long)((HashMap)cooldown.get(k)).get(p.getUniqueId())).longValue() - System.currentTimeMillis()) / 1000;
  }
  
  public static long getCooldownForPlayerLong(String k, ProxiedPlayer p) {
    return (int)(((Long)((HashMap)cooldown.get(k)).get(p.getUniqueId())).longValue() - System.currentTimeMillis());
  }
  
  public static void removeCooldown(String k, ProxiedPlayer p) {
    if (!cooldown.containsKey(k))
      throw new IllegalArgumentException(k + " does not exist"); 
    ((HashMap)cooldown.get(k)).remove(p.getUniqueId());
  }
  
  private static HashMap<String, HashMap<UUID, Long>> cooldown = new HashMap<>();
}

