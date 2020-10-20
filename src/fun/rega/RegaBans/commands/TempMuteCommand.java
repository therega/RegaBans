package fun.rega.RegaBans.commands;

import fun.rega.RegaBans.utils.*;
import fun.rega.RegaBans.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class TempMuteCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (!sender.hasPermission("RegaBans.commands.tempmute")) {
            sender.sendMessage("§8[§cRegaBans§8] §cУ вас недостаточно прав.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [игрок] [время] [причина] - Наложить мут на игрока на время.");
            return true;
        }
        if (args.length <= 0) {
            return true;
        }
        boolean silent = false;
        final CheckKeyResult s = Utils.checkKey(args, "-s");
        if (s.getResult()) {
            args = s.getArguments();
            silent = true;
        }
        if (sender.getName().equalsIgnoreCase(args[0])) {
            sender.sendMessage("§8[§cRegaBans§8] §cВы не можете наложить мут на себя.");
            return true;
        }
        if (Utils.getPlayer(args[0]) == null && !sender.hasPermission("RegaBans.offline")) {
            sender.sendMessage("§8[§cRegaBans§8] §cИгрок должен быть в сети.");
            return true;
        }
        if (!Utils.checkAccess(AccessType.TEMPMUTE, sender, args[0])) {
            sender.sendMessage("§8[§cRegaBans§8] §cИгрок имеет защиту от мута.");
            return true;
        }
        if (args.length == 1) {
            sender.sendMessage("§8[§cRegaBans§8] §cУкажите время.");
            return true;
        }
        final BuildTimeResult t = Utils.buildTime(args[1]);
        if (!t.getResult()) {
            sender.sendMessage("§8[§cRegaBans§8] §cВремя указано не правильно.");
            return true;
        }
        final long time = t.getTime();
        if (time <= 0L) {
            sender.sendMessage("§8[§cRegaBans§8] §cВремя указано не правильно.");
            return true;
        }
        final long ct = Utils.checkTime(AccessType.MUTE, sender, time);
        if (ct != -1L && ct * 60L < time) {
            sender.sendMessage("§8[§cRegaBans§8] §cПревышает максимальное допустимое время. (Максимальное: " + ct + " минут)");
            return true;
        }
        if (args.length == 2) {
            sender.sendMessage("§8[§cRegaBans§8] §cУкажите причину.");
            return true;
        }
        if (args.length == 2) {
            sender.sendMessage("§8[§cRegaBans§8] §cУкажите причину.");
            return true;
        }
        final Ban b = BanManager.getBanByPlayer(args[0]);
        if (BanManager.getBanByPlayer(args[0]) != null) {
            sender.sendMessage("§8[§cRegaBans§8] §cИгрок уже " + ((b.getType() == BanType.BAN) ? "забанен" : ((b.getType() == BanType.MUTE) ? "замучен" : "забанен по IP")) + ".");
            return true;
        }
        final String reason = Utils.buildReason(args, 2);
        BanManager.addBan(args[0], BanType.MUTE, sender.getName(), System.currentTimeMillis() + time * 1000L, reason);
        if (!silent) {
            Bukkit.broadcastMessage(Utils.buildMessage("broadcast_tempmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
        }
        else {
            sender.sendMessage(Utils.buildMessage("broadcast_tempmute", new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
        }
        return true;
    }
}
