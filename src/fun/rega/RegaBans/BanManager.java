package fun.rega.RegaBans;


import java.util.HashMap;
import fun.rega.RegaBans.*;
import fun.rega.RegaBans.utils.BanType;
import fun.rega.RegaBans.utils.MySQL;


public class BanManager {
  public static HashMap<String, Ban> bans = new HashMap<>();
  
  public static Ban addBan(String player, BanType type, String owner, long expire, String reason) {
    player = player.toLowerCase();
    Ban b = new Ban(player, type, owner, System.currentTimeMillis(), expire, reason);
    MySQL.execute("INSERT IGNORE INTO `regabans` (`player`, `type`, `owner`, `time`, `expire`, `reason`) VALUES ('" + b.getPlayer() + "', '" + b.getType() + "', '" + b.getOwner() + "', '" + b.getTime() + "', '" + b.getExpire() + "', '" + b.getReason() + "')");
    bans.put(player, b);
    return b;
  }
  
  public static void removeBan(String player) {
    player = player.toLowerCase();
    MySQL.execute("DELETE FROM `regabans` WHERE `player`='" + player + "'");
    bans.remove(player);
  }
  
  public static Ban getBanByPlayer(String player) {
    player = player.toLowerCase();
    return bans.get(player);
  }
}
