package dev.lazze.basics.commands;

import dev.lazze.basics.Main;
import dev.lazze.basics.utils.Color;
import dev.lazze.basics.utils.Cooldowns;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BroadcastCommand extends Command
{
    private final Main plugin;
    
    public BroadcastCommand(final Main base) {
        super("broadcast", "permission.op", new String[0]);
        this.plugin = base;
        this.plugin.getProxy().getPluginManager().registerCommand((Plugin)this.plugin, (Command)this);
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "You must be player to execute this command.");
            return;
        }
        final ProxiedPlayer player = (ProxiedPlayer)sender;
        if (!player.getServer().getInfo().getName().contains("Kits")) {
            player.sendMessage(ChatColor.RED + "You must be on Kits server to use this command.");
            return;
        }
        if (Cooldowns.isOnCooldown("broadcast_cooldown", player)) {
            player.sendMessage(ChatColor.RED + "You can use this command only every 6 hours.");
            return;
        }
        for (final ProxiedPlayer online : this.plugin.getProxy().getPlayers()) {
            final TextComponent message = new TextComponent("(Click here to join)");
            message.setColor(ChatColor.GRAY);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play " + player.getServer().getInfo().getName()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to join to the Game").create()));
            online.sendMessage(Color.translate("&f" + player.getName() + " &7is now hosting a &dSumo &7event."));
            online.sendMessage((BaseComponent)message);
        }
        if (!player.hasPermission("basics.command.broadcast.bypass")) {
            Cooldowns.addCooldown("", player, 21600);
            player.sendMessage(Color.translate("&aThe broadcast was sent successfully. Now you must wait &l6 hours &ato use this command again."));
        }
        else {
            player.sendMessage(Color.translate("&aThe broadcast was sent successfully."));
        }
    }
}
