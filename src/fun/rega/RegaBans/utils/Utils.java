package fun.rega.RegaBans.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fun.rega.RegaBans.*;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class Utils {
  public static String buildReason(String[] args, int start) {
    StringBuilder r = new StringBuilder();
    for (int i = start; i < args.length; i++) {
      if (args[i] != null)
        r.append(String.valueOf(args[i]) + " "); 
    } 
    return r.toString().substring(0, r.toString().length() - 1);
  }
  
  public static BuildTimeResult buildTime(String time) {
    long multi = 1L;
    String unit = null;
    if (time.endsWith("s")) {
      time = time.replaceAll("s", "");
      unit = "секунд";
    } else if (time.endsWith("m")) {
      time = time.replaceAll("m", "");
      multi = 60L;
      unit = "минут";
    } else if (time.endsWith("h")) {
      time = time.replaceAll("h", "");
      multi = 3600L;
      unit = "часов";
    } else if (time.endsWith("d")) {
      time = time.replaceAll("d", "");
      multi = 86400L;
      unit = "дней";
    } else if (time.endsWith("mo")) {
      time = time.replaceAll("mo", "");
      multi = 2678400L;
      unit = "месяцев";
    } else if (time.endsWith("y")) {
      time = time.replaceAll("y", "");
      multi = 31536000L;
      unit = "лет";
    } else {
      return new BuildTimeResult(false);
    } 
    try {
      return new BuildTimeResult(true, Long.parseLong(time) * multi, Long.parseLong(time), unit);
    } catch (Exception e) {
      return new BuildTimeResult(false);
    } 
  }
  
  public static String buildMessage(String msg, MRep... args) {
    msg = Main.config.getString("messages." + msg);
    byte b;
    int i;
    MRep[] arrayOfMRep;
    for (i = (arrayOfMRep = args).length, b = 0; b < i; ) {
      MRep arg = arrayOfMRep[b];
      msg = msg.replaceAll(arg.getKey(), arg.getValue());
      b++;
    } 
    return msg.replaceAll("%newline%", "\n");
  }
  
  public static boolean checkAccess(AccessType type, CommandSender sender, String target) {
    if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null)
      return true; 
    if (sender instanceof org.bukkit.command.ConsoleCommandSender)
      return true; 
    String ty = type.toString().toLowerCase();
    if (Main.config.getStringList("protect.players_protected").contains(target))
      return false; 
    if (Main.config.getStringList("protect.players_overrided").contains(sender.getName()))
      return true; 
    PermissionUser t = PermissionsEx.getUser(target);
    boolean sover = sender.hasPermission("RegaBans.override." + ty);
    boolean tbya = t.has("RegaBans.bypass.a." + ty);
    boolean tbyb = t.has("RegaBans.bypass.b." + ty);
    if (sover && tbyb)
      return false; 
    if (!sover && (tbya || tbyb))
      return false; 
    return true;
  }
  
  public static Player getPlayer(String str) {
    for (Player pl : Bukkit.getOnlinePlayers()) {
      if (pl.getName().equalsIgnoreCase(str))
        return pl; 
    } 
    return null;
  }
  
  public static CheckKeyResult checkKey(String[] args, String key) {
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = args).length, b = 0; b < i; ) {
      String arg = arrayOfString[b];
      if (arg.equalsIgnoreCase(key)) {
        List<String> list = new ArrayList<>(Arrays.asList(args));
        list.removeAll(Arrays.asList((Object[])new String[] { key }));
        args = list.<String>toArray(args);
        return new CheckKeyResult(list.<String>toArray(args));
      } 
      b++;
    } 
    return new CheckKeyResult();
  }
  
  public static long checkTime(AccessType type, CommandSender sender, long time) {
    if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null)
      return -1L; 
    if (sender instanceof org.bukkit.command.ConsoleCommandSender)
      return -1L; 
    for (String str : Main.config.getConfigurationSection("limits." + type.toString().toLowerCase() + ".max_time").getValues(false).keySet()) {
      if (PermissionsEx.getPermissionManager().getUser(sender.getName()).inGroup(str))
        return Main.config.getLong("limits." + type.toString().toLowerCase() + ".max_time." + str); 
    } 
    return -1L;
  }
  
  public static boolean checkIp(String text) {
    Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    Matcher m = p.matcher(text);
    return m.find();
  }
}
