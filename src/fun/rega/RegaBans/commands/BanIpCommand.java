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

public class BanIpCommand implements CommandExecutor {
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("RegaBans.commands.banip")) {
      sender.sendMessage("�8[�cRegaBans�8] �c� ��� ������������ ����.");
      return true;
    } 
    if (args.length == 0) {
      sender.sendMessage("�8[�cRegaBans�8] �6���������� �������������: �c/" + label + " [�����] [�������] - �������� ������ �� IP ��������.");
      sender.sendMessage("�8[�cRegaBans�8] �6���������� �������������: �c/" + label + " [ip] [�������] - �������� IP ��������.");
      return true;
    } 
    if (args.length > 0) {
      boolean silent = false;
      CheckKeyResult s = Utils.checkKey(args, "-s");
      if (s.getResult()) {
        args = s.getArguments();
        silent = true;
      } 
      if (sender instanceof Player) {
        String str = ((Player)sender).getAddress().getHostString();
        if (args[0].equalsIgnoreCase(str) || args[0].equalsIgnoreCase(sender.getName())) {
          sender.sendMessage("�8[�cRegaBans�8] �c�� �������� �������� ���� ����.");
          return true;
        } 
      } 
      for (Player player : Bukkit.getOnlinePlayers()) {
        if ((args[0].equalsIgnoreCase(player.getName()) || args[0].equals(player.getAddress().getHostString())) && 
          !Utils.checkAccess(AccessType.BANIP, sender, player.getName())) {
          sender.sendMessage("�8[�cRegaBans�8] �c����� ����� ������ �� ����.");
          return true;
        } 
      } 
      String ip = args[0];
      Player pl = Utils.getPlayer(ip);
      if (pl != null) {
        ip = Utils.getPlayer(ip).getAddress().getHostString();
      } else if (!Utils.checkIp(ip)) {
        sender.sendMessage("�8[�cRegaBans�8] �c����� �� � ���� ��� ����������� ������ IP �����.");
        return true;
      } 
      if (args.length == 1) {
        sender.sendMessage("�8[�cRegaBans�8] �c������� �������.");
        return true;
      } 
      Ban b = BanManager.getBanByPlayer(args[0]);
      if (BanManager.getBanByPlayer(args[0]) != null) {
        sender.sendMessage("�8[�cRegaBans�8] �c����� ��� " + ((b.getType() == BanType.BAN) ? "�������" : ((b.getType() == BanType.MUTE) ? "�������" : "������� �� IP")) + ".");
        return true;
      } 
      String reason = Utils.buildReason(args, 1);
      BanManager.addBan(ip, BanType.BANIP, sender.getName(), 0L, reason);
      if (!silent) {
        Bukkit.broadcastMessage(Utils.buildMessage("broadcast_banip", new MRep[] { new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } else {
        sender.sendMessage(Utils.buildMessage("broadcast_banip", new MRep[] { new MRep("%player%", ip), new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) }));
      } 
      if (pl != null)
        pl.kickPlayer(Utils.buildMessage("denymsg_banip", new MRep[] { new MRep("%owner%", sender.getName()), new MRep("%reason%", reason) })); 
      return true;
    } 
    return true;
  }
}
