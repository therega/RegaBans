package fun.rega.RegaBans;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import fun.rega.RegaBans.*;
import fun.rega.RegaBans.commands.*;
import fun.rega.RegaBans.utils.Config;
import fun.rega.RegaBans.utils.Logger;
import fun.rega.RegaBans.utils.MySQL;


public class Main extends JavaPlugin {
  public static FileConfiguration config;
  
  public static Plugin plugin;
  
  public void onEnable() {
	  
	  Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "RegaBans" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin enabled!");
      Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "RegaBans" + ChatColor.GRAY + "] " + ChatColor.GREEN + "My site - " + ChatColor.RED + "www.rega.fun");
	  
    long time = System.currentTimeMillis();
    plugin = (Plugin)this;
    config = Config.get("config.yml");
    MySQL.getBans();
    MySQL.getBansAsync();
    getCommand("check").setExecutor((CommandExecutor)new CheckCommand());
    getCommand("rb").setExecutor((CommandExecutor)new RBCommand());
    getCommand("kick").setExecutor((CommandExecutor)new KickCommand());
    getCommand("ban").setExecutor((CommandExecutor)new BanCommand());
    getCommand("tempban").setExecutor((CommandExecutor)new TempBanCommand());
    getCommand("unban").setExecutor((CommandExecutor)new UnbanCommand());
    getCommand("banip").setExecutor((CommandExecutor)new BanIpCommand());
    getCommand("tempbanip").setExecutor((CommandExecutor)new TempBanIpCommand());
    getCommand("unbanip").setExecutor((CommandExecutor)new UnbanIpCommand());
    getCommand("mute").setExecutor((CommandExecutor)new MuteCommand());
    getCommand("tempmute").setExecutor((CommandExecutor)new TempMuteCommand());
    getCommand("unmute").setExecutor((CommandExecutor)new UnmuteCommand());
    Bukkit.getPluginManager().registerEvents(new EventListener(), (Plugin)this);
    Logger.info("(" + ChatColor.YELLOW + (System.currentTimeMillis() - time) + " ms" + ChatColor.GREEN + ")");
  }
  
  public void onDisable() {
    Logger.info("Plugin disabled");
  }
}
