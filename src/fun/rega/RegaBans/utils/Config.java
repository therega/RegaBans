package fun.rega.RegaBans.utils;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import fun.rega.RegaBans.*;

public class Config {
  public static FileConfiguration get(String name) {
    File f = new File(Main.plugin.getDataFolder(), name);
    if (Main.plugin.getResource(name) == null)
      return save((FileConfiguration)YamlConfiguration.loadConfiguration(f), name); 
    if (!f.exists())
      Main.plugin.saveResource(name, false); 
    return (FileConfiguration)YamlConfiguration.loadConfiguration(f);
  }
  
  public static FileConfiguration save(FileConfiguration config, String name) {
    try {
      config.save(new File(Main.plugin.getDataFolder(), name));
    } catch (IOException e) {
      Logger.error(e.getMessage());
    } 
    return config;
  }
}
