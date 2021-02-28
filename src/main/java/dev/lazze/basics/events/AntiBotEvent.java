package dev.lazze.basics.events;

import java.io.*;

import dev.lazze.basics.Main;
import dev.lazze.basics.antibot.BotAttack;
import dev.lazze.basics.antibot.BotBoth;
import net.md_5.bungee.config.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.event.*;
import java.util.*;
import net.md_5.bungee.api.event.*;

public class AntiBotEvent extends Event implements Listener
{
    public static String[] st;
    public static int i;
    public static File f;
    public static Configuration config;
    public static int limit;
    public static int startup_multiplier;
    public static int startup_time;
    public static List<ProxiedPlayer> ignore;
    
    static {
        AntiBotEvent.ignore = new ArrayList<ProxiedPlayer>();
    }
    
    public AntiBotEvent(final Main plugin) {
        super(plugin);
        AntiBotEvent.i = 0;
        AntiBotEvent.limit = 3;
        BotBoth.timeout = 7;
        AntiBotEvent.startup_multiplier = 10;
        AntiBotEvent.startup_time = 30;
        AntiBotEvent.st = new String[AntiBotEvent.limit + 2];
        ProxyServer.getInstance().getPluginManager().registerListener((Plugin) Main.getInstance(), (Listener)this);
    }
    
    @EventHandler
    public void onProxyPing(final ProxyPingEvent event) {
        final String ip = event.getConnection().getAddress().getAddress().getHostAddress();
        if (!BotBoth.pings.contains(ip)) {
            BotBoth.pings.add(ip);
        }
    }
    
    @EventHandler(priority = -64)
    public void onPreLogin(final PreLoginEvent event) {
        if (BotBoth.joins > AntiBotEvent.limit * AntiBotEvent.startup_multiplier) {
            int ping = 0;
            int nevv = 0;
            int length = 0;
            boolean pinging = false;
            boolean nevv2 = false;
            final List<String> list = new ArrayList<String>();
            String[] st;
            for (int x = (st = AntiBotEvent.st).length, i = 0; i < x; ++i) {
                final String s = st[i];
                if (s != null) {
                    if (Boolean.parseBoolean(s.split(" ")[0])) {
                        ++ping;
                    }
                    if (Boolean.parseBoolean(s.split(" ")[1])) {
                        ++nevv;
                    }
                    list.add(s.split(" ")[2]);
                }
            }
            String type = BotBoth.getNickType(list);
            if (type.equals("null")) {
                length = BotBoth.getLength(list);
                if (length != 0) {
                    type = "length";
                }
            }
            if (ping > AntiBotEvent.limit) {
                pinging = true;
            }
            if (nevv > AntiBotEvent.limit) {
                nevv2 = true;
            }
            BotBoth.attacks.add(new BotAttack(System.currentTimeMillis(), pinging, nevv2, type, length));
            BotBoth.joins = 0;
        }
        final String name = event.getConnection().getName();
        final String ip = event.getConnection().getAddress().getAddress().getHostAddress();
        for (final BotAttack a : BotBoth.attacks) {
            if (!event.isCancelled() && a.handleLogin(true, name, ip)) {
                event.setCancelled(true);
                return;
            }
        }
        ++BotBoth.joins;
        ++AntiBotEvent.i;
        if (AntiBotEvent.i > AntiBotEvent.limit + 1) {
            AntiBotEvent.i = 0;
        }
        final String s2 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(BotBoth.pingedServer(ip))))) + " " + BotBoth.isNew(name) + " " + name;
        AntiBotEvent.st[AntiBotEvent.i] = s2;
        if (BotBoth.isFakeNickname(name)) {
            event.setCancelled(true);
            return;
        }
        event.registerIntent((Plugin) Main.getInstance());
        Main.getInstance().getProxy().getScheduler().runAsync((Plugin) Main.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
                for (final BotAttack a : BotBoth.attacks) {
                    if (a.handleLogin(false, name, ip)) {
                        event.setCancelled(true);
                        event.completeIntent((Plugin) Main.getInstance());
                        return;
                    }
                }
                event.completeIntent((Plugin) Main.getInstance());
            }
        });
        BotBoth.attacks.isEmpty();
    }
    
    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        Main.getInstance().getProxy().getScheduler().runAsync((Plugin) Main.getInstance(), (Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (event.getPlayer().isConnected()) {
                    BotBoth.addPlayer(event.getPlayer().getName());
                }
            }
        });
    }
}
