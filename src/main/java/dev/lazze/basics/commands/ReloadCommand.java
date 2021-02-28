package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("configreload", Main.configuration.getString("permission.op"), new String[] { "creload", "reload" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        Main.getInstance().reloadConfig();
        sender.sendMessage(Color.translate("&aYou have reloaded the configuration file!"));
    }
}
