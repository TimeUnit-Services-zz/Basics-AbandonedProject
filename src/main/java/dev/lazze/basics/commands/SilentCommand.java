package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.events.SilentEvent;
import dev.lazze.basics.utils.Color;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.*;
import net.md_5.bungee.api.chat.*;

public class SilentCommand extends Command
{
    public SilentCommand() {
        super("silent", Main.configuration.getString("permission.staff"), new String[] { "filter" });
    }
    
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage((BaseComponent)new TextComponent(Color.translate("&cPlayer use only!")));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer)sender;
        if (SilentEvent.silent.contains(player.getUniqueId())) {
            SilentEvent.silent.remove(player.getUniqueId());
            player.sendMessage((BaseComponent)new TextComponent(Color.translate("&cYou have Disabled silent mode.")));
            return;
        }
        SilentEvent.silent.add(player.getUniqueId());
        player.sendMessage((BaseComponent)new TextComponent(Color.translate("&aYou have enabled silent mode.")));
    }
}
