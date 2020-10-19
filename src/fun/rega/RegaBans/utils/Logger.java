package fun.rega.RegaBans.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import fun.rega.RegaBans.*;

public class Logger {
  public static void info(Object text) {
    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[" + Main.plugin.getName() + "] " + text);
  }
  
  public static void warning(Object text) {
    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + Main.plugin.getName() + "] " + text);
  }
  
  public static void error(Object text) {
    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + Main.plugin.getName() + "] " + text);
  }
  
  public static void debug(Object text) {
    if (!Main.config.getBoolean("debug"))
      return; 
    Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + Main.plugin.getName() + "] " + text);
  }
}