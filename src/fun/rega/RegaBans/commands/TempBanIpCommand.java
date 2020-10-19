package fun.rega.RegaBans.commands;

import java.util.Iterator;
import fun.rega.RegaBans.*;
import fun.rega.RegaBans.utils.AccessType;
import fun.rega.RegaBans.utils.BanType;
import fun.rega.RegaBans.utils.BuildTimeResult;
import fun.rega.RegaBans.utils.CheckKeyResult;
import fun.rega.RegaBans.utils.MRep;
import fun.rega.RegaBans.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class TempBanIpCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, String[] args) {
        if (!sender.hasPermission("RegaBans.commands.tempbanip")) {
            sender.sendMessage("§8[§cRegaBans§8] §cУ вас недостаточно прав.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [ip] [время] [причина] - Забанить IP адрес на время.");
            sender.sendMessage("§8[§cRegaBans§8] §6Правильное использование: §c/" + label + " [игрок] [время] [причина] - Забанить игрока по IP адресу на время.");
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
        if (sender instanceof Player) {
            final String ip = ((Player)sender).getAddress().getHostString();
            if (args[0].equalsIgnoreCase(ip) || args[0].equalsIgnoreCase(sender.getName())) {
                sender.sendMessage("§8[§cRegaBans§8] §cВы не можете забанить сами себя.");
                return true;
            }
        }
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if ((args[0].equalsIgnoreCase(pl.getName()) || args[0].equals(pl.getAddress().getHostString())) && !Utils.checkAccess(AccessType.TEMPBANIP, sender, pl.getName())) {
                sender.sendMessage("§8[§cRegaBans§8] §cИгрок имеет защиту от бана.");
                return true;
            }
        }
        String ip = args[0];
        final Player pl2 = Utils.getPlayer(ip);
        if (pl2 != null) {
            ip = Utils.getPlayer(ip).getAddress().getHostString();
        }
        else if (!Utils.checkIp(ip)) {
            sender.sendMessage("§8[§cRegaBans§8] §cИгрок не в сети или неправильно указан IP адрес.");
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
            sender.sendMessage("§8[§cRegaBans§8] §cВремя должно быть положительным.");
            return true;
        }
        final long ct = Utils.checkTime(AccessType.BANIP, sender, time);
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
        final Ban b = BanManager.getBanByPlayer(ip);
        if (BanManager.getBanByPlayer(ip) != null) {
            sender.sendMessage("§8[§cRegaBans§8] §cИгрок уже " + ((b.getType() == BanType.BAN) ? "забанен" : ((b.getType() == BanType.MUTE) ? "замучен" : "забанен по IP")) + ".");
            return true;
        }
        final String reason = Utils.buildReason(args, 2);
        BanManager.addBan(ip, BanType.BANIP, sender.getName(), System.currentTimeMillis() + time * 1000L, reason);
        if (!silent) {
            Bukkit.broadcastMessage(Utils.buildMessage("broadcast_tempbanip", new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
        }
        else {
            sender.sendMessage(Utils.buildMessage("broadcast_tempbanip", new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
        }
        if (pl2 != null) {
            pl2.kickPlayer(Utils.buildMessage("denymsg_tempbanip", new MRep("%owner%", sender.getName()), new MRep("%reason%", reason), new MRep("%time%", String.valueOf(t.getRaw()) + " " + t.getUnit())));
        }
        return true;
    }
}
