package fun.rega.RegaBans.commands;

import fun.rega.RegaBans.*;
import fun.rega.RegaBans.utils.Config;
import fun.rega.RegaBans.utils.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class RBCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("RegaBans.commands.rb")) {
            sender.sendMessage("�8[�cRegaBans�8] �c� ��� ������������ ����.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("�8[�cRegaBans�8] �6���������� �������������: �c/" + label + " reload - ������������� ������.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Main.config = Config.get("config.yml");
            MySQL.disconnect();
            MySQL.connect();
            sender.sendMessage("�8[�cRegaBans�8] �a������ ������� ������������.");
            return true;
        }
        return true;
    }
}
