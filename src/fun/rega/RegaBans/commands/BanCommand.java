package fun.rega.RegaBans.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fun.rega.RegaBans.*;
import fun.rega.RegaBans.utils.AccessType;
import fun.rega.RegaBans.utils.BanType;
import fun.rega.RegaBans.utils.CheckKeyResult;
import fun.rega.RegaBans.utils.MRep;
import fun.rega.RegaBans.utils.Utils;

public class BanCommand implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("RegaBans.commands.ban")) {
      sender.sendMessage("§8[§cRegaBans§8] §cУ вас недостаточно прав");
      return true;
    } 
    if (args.length == 0) {
      sender.sendMessage("§8[§cRegaBans§8] §6Правильно использовапние: §c/" + label + " [игрок] [причина] - Забанить игрока навсегда.");
      return true;
    } 
    if (args.length > 0) {
      boolean silent = false;
      CheckKeyResult s = Utils.checkKey(args, "-s");
      if (s.getResult()) {
        args = s.getArguments();
        silent = true;
      } 
      if (sender.getName().equalsIgnoreCase(args[0])) {
        sender.sendMessage("§8[§cRegaBans§8] §cВы не можете забанить сами себя.");
        return true;
      } 
      if (Utils.getPlayer(args[0]) == null && !sender.hasPermission("RegaBans.offline")) {
        sender.sendMessage("§8[§cRegaBans§8] §cИгрок должен быть в сети.");
        return true;
      } 
      if (!Utils.checkAccess(AccessType.BAN, sender, args[0])) {
        sender.sendMessage("§8[§cRegaBans§8] §cИгрок имеет защиту от банов.");
        return true;
      } 
      if (args.length == 1) {
        sender.sendMessage("§8[§cRegaBans§8] §cУкажите причину.");
        return true;
      } 
      Ban b = BanManager.getBanByPlayer(args[0]);
      if (BanManager.getBanByPlayer(args[0]) != null) {
        sender.sendMessage("§8[§cRegaBans§8] §cИгрок уже " + ((b.getType() == BanType.BAN) ? "забанен" : ((b.getType() == BanType.MUTE) ? "замучен" : "забанен по IP")) + ".");
        return true;
      } 
      String reason = Utils.buildReason(args, 1);
      BanManager.addBan(args[0], BanType.BAN, sender.getName(), 0L, reason);
      if (!silent) {
        Bukkit.broadcastMessage(Utils.buildMessage("broadcast_ban", new MRep[] { new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } else {
        sender.sendMessage(Utils.buildMessage("broadcast_ban", new MRep[] { new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } 
      Player pl = Utils.getPlayer(args[0]);
      if (pl != null)
        pl.kickPlayer(Utils.buildMessage("denymsg_ban", new MRep[] { new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) })); 
      return true;
    } 
    return true;
  }
}
