package me.albert.amazingbot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ABCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("amazingbot.reload")){
            sender.sendMessage("§c你莫得权限!");
            return false;
        }
        AmazingBot.getInstance().reloadConfig();
        Bot.stop();
        Bot.start();
        return true;
    }
}
