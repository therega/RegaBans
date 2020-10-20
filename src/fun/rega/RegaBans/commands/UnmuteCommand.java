package fun.rega.RegaBans.commands;

import fun.rega.RegaBans.utils.*;
import fun.rega.RegaBans.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class UnmuteCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (!sender.hasPermission("RegaBans.commands.unmute")) {
            sender.sendMessage("§8[§cRegaBans§8] §cУ вас недостаточно прав.");
            return true;
        }
        boolean silent = false;
        final CheckKeyResult s = Utils.checkKey(args, "-s");
        if (s.getResult()) {
            args = s.getArguments();
            silent = true;
        }
        if (args.length == 0) {
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [игрок] - Снять мут с игрока.");
            return true;
        }
        if (args.length <= 0) {
            return true;
        }
        final Ban ban = BanManager.getBanByPlayer(args[0]);
        if (ban == null || (ban != null && ban.getType() != BanType.MUTE)) {
            sender.sendMessage("§8[§cRegaBans§8] §cНа игрока не наложен мут.");
            return true;
        }
        BanManager.removeBan(ban.getPlayer());
        if (!silent) {
            Bukkit.broadcastMessage(Utils.buildMessage("broadcast_unmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName())));
        }
        else {
            sender.sendMessage(Utils.buildMessage("broadcast_unmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName())));
        }
        return true;
    }
}
