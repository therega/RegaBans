package fun.rega.RegaBans.commands;

import fun.rega.RegaBans.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class CheckCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("RegaBans.commands.check")) {
            sender.sendMessage("§8[§cRegaBans§8] §cУ вас недостаточно прав.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [ip] - Узнать о бане IP адреса.");
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [игрок] - Узнать о бане игрока.");
            return true;
        }
        if (args.length > 0) {
            final Ban ban = BanManager.getBanByPlayer(args[0]);
            if (ban == null) {
                sender.sendMessage("§8[§cRegaBans§8] §cБан не найден.");
                return true;
            }
            sender.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-= §b| §dRegaBans §| §8=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            sender.sendMessage(" §8> §aИгрок: " + ChatColor.YELLOW + ban.getPlayer());
            sender.sendMessage(" §8> §aЗабнен админом: " + ChatColor.YELLOW + ban.getOwner());
            sender.sendMessage(" §8> §aТип бана: " + ChatColor.YELLOW + ban.getType());
            sender.sendMessage(" §8> §aПричина: " + ChatColor.YELLOW + ban.getReason());
            sender.sendMessage(" §8> §aРазбан через: " + ChatColor.YELLOW + ((ban.getExpire() == 0L) ? "никогда" : new StringBuilder().append(ban.getTimeLeft()).toString()));
            sender.sendMessage("§8-=-=-=-=-=-=-=-=-=-=-=-=-=-= §b| §dRegaBans §| §8=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        }
        return true;
    }
}
