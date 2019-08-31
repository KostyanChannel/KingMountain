package ru.kdev.kingmountain.cmds;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.kdev.kingmountain.KingMountain;

import java.util.ArrayList;
import java.util.List;

public class KMCommand implements CommandExecutor, TabCompleter {
    private KingMountain plugin = KingMountain.instance;

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player)
        {
            final Player p = (Player) sender;
            if(args.length > 0)
            {
                if(p.hasPermission("kingmountain.admin"))
                {
                    if(args[0].equals("startpos"))
                    {
                        plugin.getConfig().set("location.start.x", p.getLocation().getBlockX());
                        plugin.getConfig().set("location.start.y", p.getLocation().getBlockY());
                        plugin.getConfig().set("location.start.z", p.getLocation().getBlockZ());
                        plugin.getConfig().set("location.start.world", p.getLocation().getWorld().getName());
                        plugin.saveConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.startpos")));
                    }
                    else if(args[0].equals("winpos"))
                    {
                        plugin.getConfig().set("location.win.x", p.getLocation().getBlockX());
                        plugin.getConfig().set("location.win.y", p.getLocation().getBlockY());
                        plugin.getConfig().set("location.win.z", p.getLocation().getBlockZ());
                        plugin.getConfig().set("location.win.world", p.getLocation().getWorld().getName());
                        plugin.saveConfig();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.winpos")));
                    }
                    else if(args[0].equals("start"))
                    {
                        plugin.getConfig().set("started", true);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.started")));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.noperms")));
                }
            } else {
                for(String str : plugin.getConfig().getStringList("locale.help"))
                {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                }
            }
        } else {
            sender.sendMessage("This command only for player!");
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("kingmountain.admin"))
            return Lists.newArrayList();
        if(sender.hasPermission("kingmountain.admin") && args.length == 1)
            return filter(Lists.newArrayList("startpos", "start", "winpos"), args);
        return Lists.newArrayList();
    }

    private List<String> filter(ArrayList<String> list, String[] args) {
        String last = args[args.length - 1].toLowerCase();
        List<String> result = new ArrayList<String>();
        for (String s : list)
            if (s.startsWith(last))
                result.add(s);
        return result;
    }
}
