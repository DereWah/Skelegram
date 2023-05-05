package org.derewah.skelegram;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

public class Commands implements CommandExecutor {

    private final Skelegram plugin;
    public Commands(Skelegram plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) args = new String[]{"help"};
        if (command.getName().equalsIgnoreCase("skelegram")) {
            if (sender.hasPermission("skelegram.reload")) {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(AQUA + "------------------ " + BLUE + "Skelegram" + AQUA + " ------------------");
                    sender.sendMessage(" ");
                    sender.sendMessage(AQUA + "/skelegram help" + WHITE + " Show this help message.");
                    sender.sendMessage(AQUA + "/skelegram reload" + WHITE + " Reload the addon.");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    Skelegram.getInstance().reloadConfig();
                    sender.sendMessage(AQUA + "["+BLUE+"Skelegram"+AQUA+"]"+GREEN+" Config reloaded.");
                    return false;
                } else {
                    sender.sendMessage(AQUA + "["+BLUE+"Skelegram"+AQUA+"]"+RED+" Invalid command. /skelegram help");
                }
                return false;
            } else{
            sender.sendMessage(AQUA + "["+BLUE+"Skelegram"+AQUA+"]"+RED+" You don't have the permission " + DARK_RED + "Skelegram.reload"+RED+".");
            }
            return false;
        }
        return false;
    }
}