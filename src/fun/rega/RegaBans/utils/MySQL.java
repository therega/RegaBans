package fun.rega.RegaBans.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import fun.rega.RegaBans.*;

public class MySQL {
  public static Connection connection = null;
  
  public static void connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      connection = DriverManager.getConnection("jdbc:mysql://" + Main.config.getString("mysql.host") + ":" + Main.config.getInt("mysql.port") + "/" + Main.config.getString("mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + Main.config.getString("mysql.username") + "&password=" + Main.config.getString("mysql.password"));
      executeSync("CREATE TABLE IF NOT EXISTS `regabans` (`id` int NOT NULL AUTO_INCREMENT, `player` varchar(16) NOT NULL, `type` varchar(16) NOT NULL, `owner` varchar(16) NOT NULL, `time` varchar(16) NOT NULL, `expire` varchar(16) NOT NULL, `reason` varchar(255) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `uniq` (`player`)) DEFAULT CHARSET=utf8 AUTO_INCREMENT=0");
      Logger.info("Соединение с базой данных успешно установлено.");
    } catch (Exception e) {
      Logger.error("MySQL ERROR: " + e.getMessage());
    } 
  }
  
  public static boolean hasConnected() {
    try {
      if (!connection.isClosed())
        return true; 
      return false;
    } catch (Exception e) {
      return false;
    } 
  }
  
  public static String strip(String str) {
    str = str.replaceAll("<[^>]*>", "");
    str = str.replace("\\", "\\\\");
    str = str.trim();
    return str;
  }
  
  public static void execute(final String query) {
    Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
          public void run() {
            try {
              if (!MySQL.hasConnected())
                MySQL.connect(); 
              Statement st = MySQL.connection.createStatement();
              st.execute(MySQL.strip(query));
              st.close();
              Logger.debug(MySQL.strip(query));
            } catch (Exception e) {
              Logger.error(e.getMessage());
            } 
          }
        });
  }
  
  public static void executeSync(String query) {
    try {
      if (!hasConnected())
        connect(); 
      Statement st = connection.createStatement();
      st.execute(strip(query));
      st.close();
      Logger.debug(strip(query));
    } catch (Exception e) {
      Logger.error(e.getMessage());
    } 
  }
  
  public static void getBans() {
    BanManager.bans.clear();
    try {
      int i = 0;
      ResultSet bans = executeQuery("SELECT * FROM `regabans`");
      while (bans.next()) {
        try {
          boolean delete = false;
          BanType type = BanType.getByName(bans.getString("type"));
          if (type == null)
            delete = true; 
          if (bans.getLong("expire") > 0L && bans.getLong("expire") < System.currentTimeMillis())
            delete = true; 
          if (delete) {
            execute("DELETE FROM `regabans` WHERE `player`='" + bans.getString("player") + "'");
            continue;
          } 
          Ban b = new Ban(bans.getString("player").toLowerCase(), BanType.getByName(bans.getString("type")), bans.getString("owner"), bans.getLong("time"), bans.getLong("expire"), bans.getString("reason"));
          BanManager.bans.put(bans.getString("player"), b);
          i++;
        } catch (Exception e) {
          Logger.error(e.getMessage());
        } 
      } 
      Logger.info("Загружено " + i + " банов.");
    } catch (Exception e) {
      Logger.error(e.getMessage());
    } 
  }
  
  public static void getBansAsync() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, (Runnable)new Runnable() {
          @Override
          public void run() {
              BanManager.bans.clear();
              try {
                  final ResultSet bans = MySQL.executeQuery("SELECT * FROM `regabans`");
                  while (bans.next()) {
                      try {
                          boolean delete = false;
                          final BanType type = BanType.getByName(bans.getString("type"));
                          if (type == null) {
                              delete = true;
                          }
                          if (bans.getLong("expire") > 0L && bans.getLong("expire") < System.currentTimeMillis()) {
                              delete = true;
                          }
                          if (delete) {
                              MySQL.execute("DELETE FROM `regabans` WHERE `player`='" + bans.getString("player") + "'");
                          }
                          else {
                              final Ban b = new Ban(bans.getString("player").toLowerCase(), BanType.getByName(bans.getString("type")), bans.getString("owner"), bans.getLong("time"), bans.getLong("expire"), bans.getString("reason"));
                              BanManager.bans.put(bans.getString("player"), b);
                          }
                      }
                      catch (Exception e) {
                          Logger.error(e.getMessage());
                      }
                  }
                  for (final Player pl : Bukkit.getOnlinePlayers()) {
                      final Ban b = BanManager.getBanByPlayer(pl.getName());
                      if (b != null && b.getType() != BanType.MUTE) {
                          pl.kickPlayer("§8[§cRegaBans§8] §cВы были забанены извне.");
                      }
                  }
              }
              catch (Exception e2) {
                  Logger.error(e2.getMessage());
              }
          }
      }, 0L, 100L);
  }
  
  public static ResultSet executeQuery(final String query) throws Exception {
      if (!hasConnected()) {
          connect();
      }
      Logger.debug(strip(query));
      final Statement st = MySQL.connection.createStatement();
      try {
          return MySQL.connection.createStatement().executeQuery(strip(query));
      }
      finally {
          st.close();
      }
  }
  
  public static void disconnect() {
      try {
          if (MySQL.connection != null) {
              MySQL.connection.close();
          }
      }
      catch (Exception ex) {}
  }
}