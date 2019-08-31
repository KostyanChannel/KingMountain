package ru.kdev.kingmountain.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kdev.kingmountain.KingMountain;

public class KMGoMp implements CommandExecutor {
    private KingMountain plugin = KingMountain.instance;

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player)
        {
            Player p = (Player) sender;
            if(p.hasPermission("kingmountain.gomp"))
            {
                if(!plugin.getConfig().getBoolean("started"))
                {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.notstarted")));
                    return false;
                }
                Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("location.start.world")), plugin.getConfig().getInt("location.start.x"), plugin.getConfig().getInt("location.start.y"), plugin.getConfig().getInt("location.start.z"));
                plugin.playersOnMP.put(p, true);
                p.teleport(loc);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.teleported")));
            }
        } else {
            sender.sendMessage("This command only for player!");
        }
        return false;
    }
}
