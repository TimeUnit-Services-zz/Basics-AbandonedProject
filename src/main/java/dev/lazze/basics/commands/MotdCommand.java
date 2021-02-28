package dev.lazze.basics.commands;


import dev.lazze.basics.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MotdCommand extends Command {
   private Main plugin;
   public MotdCommand() {
      super("motd", Main.configuration.getString("permission.staff"), new String[] { "mtd", "mdt" });
   }

   @SuppressWarnings("deprecation")
public void execute(CommandSender sender, String[] args) {
      if (args.length == 1) {
         if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("wovel.op")) {
               this.plugin.reloadConfig();
               sender.sendMessage(t("&aThe config.yml has been reloaded succesfully."));
            } else {
               sender.sendMessage(t("&cNo Permissions."));
            }
         } else {
            sender.sendMessage("&cCorrect Usage: /Main reload");
         }
      } else {
         sender.sendMessage("&cToo many arguments.");
      }

   }

   public static String t(String i) {
      return ChatColor.translateAlternateColorCodes('&', i);
   }
}
