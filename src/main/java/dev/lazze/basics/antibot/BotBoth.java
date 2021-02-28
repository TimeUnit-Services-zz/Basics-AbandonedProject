package dev.lazze.basics.antibot;

import java.time.*;
import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.*;

public class BotBoth
{
    public static ArrayList<String> starts;
    public static ArrayList<String> ends;
    public static ArrayList<String> nicks;
    public static ArrayList<String> pings;
    public static ArrayList<String> players;
    public static ArrayList<BotAttack> attacks;
    public static File f;
    public static File Log;
    public static File nickfile;
    public static int joins;
    public static int timeout;
    
    static {
        BotBoth.starts = new ArrayList<String>();
        BotBoth.ends = new ArrayList<String>();
        BotBoth.nicks = new ArrayList<String>();
        BotBoth.pings = new ArrayList<String>();
        BotBoth.players = new ArrayList<String>();
        BotBoth.attacks = new ArrayList<BotAttack>();
    }
    
    public static boolean isFakeNickname(String name) {
        if (starts(name) != null) {
            name = name.substring(starts(name).length());
            if (starts(name) != null) {
                name = name.substring(starts(name).length());
                for (final String s : BotBoth.ends) {
                    if (name.equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static String starts(final String what) {
        for (final String s : BotBoth.starts) {
            if (what.startsWith(s)) {
                return s;
            }
        }
        return null;
    }
    
    public static String getTime() {
        final LocalTime lt = LocalTime.now();
        String h;
        if (lt.getHour() < 10) {
            h = "0" + lt.getHour();
        }
        else {
            h = new StringBuilder(String.valueOf(lt.getHour())).toString();
        }
        String m;
        if (lt.getMinute() < 10) {
            m = "0" + lt.getMinute();
        }
        else {
            m = new StringBuilder(String.valueOf(lt.getMinute())).toString();
        }
        String s;
        if (lt.getSecond() < 10) {
            s = "0" + lt.getSecond();
        }
        else {
            s = new StringBuilder(String.valueOf(lt.getSecond())).toString();
        }
        final String t = String.valueOf(String.valueOf(String.valueOf(String.valueOf(h)))) + ":" + m + ":" + s;
        return t;
    }
    
    public static void log(final String name, final String IP) {
        try {
            final BufferedWriter buf = new BufferedWriter(new FileWriter(BotBoth.Log, true));
            final LocalDate date = LocalDate.now();
            buf.write(String.valueOf(String.valueOf(String.valueOf(String.valueOf(date.getDayOfMonth())))) + "." + date.getMonth() + "." + date.getYear() + " " + getTime() + " - " + name + " - " + IP);
            buf.write(System.getProperty("line.separator"));
            buf.close();
        }
        catch (Exception ex) {}
    }
    
    public static void load(final File file) {
        file.mkdirs();
        BotBoth.f = new File(file, "players.yml");
        BotBoth.Log = new File(file, "Connections.log");
        BotBoth.nickfile = new File(file, "nicks.txt");
        if (!BotBoth.Log.exists()) {
            try {
                BotBoth.Log.createNewFile();
            }
            catch (Exception ex) {}
        }
        if (!BotBoth.f.exists()) {
            try {
                BotBoth.f.createNewFile();
            }
            catch (Exception ex2) {}
        }
        try {
            final URL website = new URL("http://craftplex.eu/download/nicks.txt");
            final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            final FileOutputStream fos = new FileOutputStream("plugins/AntiBot/nicks.txt");
            fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        }
        catch (Exception ex3) {}
        try {
            final BufferedReader br = new BufferedReader(new FileReader(BotBoth.nickfile));
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                BotBoth.nicks.add(line);
            }
            br.close();
        }
        catch (Exception ex4) {}
        try {
            final BufferedReader br = new BufferedReader(new FileReader(BotBoth.f));
            while (true) {
                final String line = br.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                BotBoth.players.add(line);
            }
            br.close();
        }
        catch (Exception ex5) {}
        BotBoth.nickfile.delete();
        BotBoth.starts.add("_Itz");
        BotBoth.starts.add("Actor");
        BotBoth.starts.add("Beach");
        BotBoth.starts.add("Build");
        BotBoth.starts.add("Craft");
        BotBoth.starts.add("Crazy");
        BotBoth.starts.add("Elder");
        BotBoth.starts.add("Games");
        BotBoth.starts.add("Hello");
        BotBoth.starts.add("Hyder");
        BotBoth.starts.add("Hydra");
        BotBoth.starts.add("Hydro");
        BotBoth.starts.add("Hyper");
        BotBoth.starts.add("Kills");
        BotBoth.starts.add("Nitro");
        BotBoth.starts.add("Plays");
        BotBoth.starts.add("Slime");
        BotBoth.starts.add("Super");
        BotBoth.starts.add("Tower");
        BotBoth.starts.add("Worms");
        BotBoth.ends.add("11");
        BotBoth.ends.add("50");
        BotBoth.ends.add("69");
        BotBoth.ends.add("99");
        BotBoth.ends.add("HD");
        BotBoth.ends.add("LP");
        BotBoth.ends.add("XD");
        BotBoth.ends.add("YT");
    }
    
    public static int getLength(final List<String> list) {
        for (int i = 1; i < 17; ++i) {
            int num = 0;
            for (final String s : list) {
                if (s.length() == i) {
                    ++num;
                }
            }
            if (num > 2) {
                return i;
            }
        }
        return 0;
    }
    
    public static String getNickType(final List<String> list) {
        int nicks = 0;
        for (final String s : list) {
            if (getNickType(s).equals("nicks")) {
                ++nicks;
            }
        }
        if (nicks > 2) {
            return "nicks";
        }
        return "null";
    }
    
    public static String getNickType(final String name) {
        if (BotBoth.nicks.contains(name)) {
            return "nicks";
        }
        return "null";
    }
    
    public static void addPlayer(final String p) {
        if (!BotBoth.players.contains(p)) {
            try {
                final BufferedWriter buf = new BufferedWriter(new FileWriter(BotBoth.f, true));
                buf.write(p);
                buf.write(System.getProperty("line.separator"));
                buf.close();
            }
            catch (Exception ex) {}
            BotBoth.players.add(p);
        }
    }
    
    public static boolean isNew(final String p) {
        return BotBoth.players.contains(p);
    }
    
    public static boolean pingedServer(final String ip) {
        return BotBoth.pings.contains(ip);
    }
    
    public static void cancelAttack(final long name) {
        for (final BotAttack a : BotBoth.attacks) {
            if (a.getName() == name) {
                BotBoth.attacks.remove(a);
            }
        }
        if (BotBoth.attacks.isEmpty()) {
            BotBoth.pings.clear();
        }
    }
}
