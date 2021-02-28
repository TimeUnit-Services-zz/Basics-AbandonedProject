package dev.lazze.basics.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class StringUtils {
    public static String formatMilisecondsToSeconds(Long time) {
        float seconds = ((float)time.longValue() + 0.0F) / 1000.0F;
        String string = String.format("%1$.1f", new Object[] { Float.valueOf(seconds) });
        return string;
    }

    public static String formatMilisecondsToMinutes(Long cooldown) {
        return String.format("%02d:%02d", new Object[] { Long.valueOf(cooldown.longValue() / 1000L / 60L), Long.valueOf(cooldown.longValue() / 1000L % 60L) });
    }

    public static boolean isPremium(String name) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((new URL("https://api.mojang.com/users/profiles/minecraft/" + name)).openStream()));
            if (bufferedReader.readLine() != null) {
                bufferedReader.close();
                return true;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
