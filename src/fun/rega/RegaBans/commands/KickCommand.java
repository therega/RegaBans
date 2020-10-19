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

public class KickCommand implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("RegaBans.commands.kick")) {
      sender.sendMessage("§8[§cRegaBans§8] §cÓ âàñ íåäîñòàòî÷íî ïğàâ.");
      return true;
    } 
    if (args.length == 0) {
      sender.sendMessage("§8[§cRegaBans§8] §6Ïğàâèëüíîå èñïîëüçîâàíèå: §c/" + label + " [èãğîê] [ïğè÷èíà] - Êèêíóòü èãğîêà.");
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
        sender.sendMessage("§8[§cRegaBans§8] §cÂû íå ìîæåòå êèêíóòü ñàìè ñåáÿ.");
        return true;
      } 
      Player pl = Utils.getPlayer(args[0]);
      if (pl == null) {
        sender.sendMessage("§8[§cRegaBans§8] §cÈãğîê íå â ñåòè.");
        return true;
      } 
      if (!Utils.checkAccess(AccessType.KICK, sender, args[0])) {
        sender.sendMessage("§8[§cRegaBans§8] §cÈãğî èìååò çàùèòó îò êèêà.");
        return true;
      } 
      if (args.length == 1) {
        sender.sendMessage("§8[§cRegaBans§8] §cÓêàæèòå ïğè÷èíó.");
        return true;
      } 
      String reason = Utils.buildReason(args, 1);
      if (!silent) {
        Bukkit.broadcastMessage(Utils.buildMessage("broadcast_kick", new MRep[] { new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } else {
        sender.sendMessage(Utils.buildMessage("broadcast_kick", new MRep[] { new MRep("%player%", args[0]), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } 
      pl.kickPlayer(Utils.buildMessage("denymsg_kick", new MRep[] { new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      return true;
    } 
    return true;
  }
}
