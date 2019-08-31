package ru.kdev.kingmountain.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.kdev.kingmountain.KingMountain;

public class KMListener implements Listener {
    private KingMountain plugin = KingMountain.instance;

    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        if(!plugin.playersOnMP.containsKey(p)) return;
        if(e.getTo().getBlockX() == plugin.getConfig().getInt("location.win.x") && e.getTo().getBlockY() == plugin.getConfig().getInt("location.win.y") && e.getTo().getBlockZ() == plugin.getConfig().getInt("location.win.z"))
        {
            if(plugin.waitWinners.containsKey(p)) return;
            plugin.waitWinners.put(p, Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    if(!plugin.winners.containsKey(p)) plugin.winners.put(p, plugin.getConfig().getInt("seconds"));
                    if(plugin.winners.get(p) == plugin.getConfig().getInt("seconds")) p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.timetowin").replace("<second>", plugin.winners.get(p).toString())));
                    if(plugin.winners.get(p) != 0)
                    {
                        plugin.winners.replace(p, plugin.winners.get(p), plugin.winners.get(p)-1);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.timetowin").replace("<second>", plugin.winners.get(p).toString())));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.youwin")));
                        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("locale.winner").replace("<winner>", p.getName())));
                        for(String cmd : plugin.getConfig().getStringList("prize"))
                        {
                            cmd = cmd.replace("<winner>", p.getName());
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
                            plugin.winners.remove(p, plugin.winners.get(p));
                            plugin.playersOnMP.clear();
                            plugin.getConfig().set("started", false);
                            if(!plugin.waitWinners.containsKey(p)) return;
                            plugin.waitWinners.get(p).cancel();
                            plugin.waitWinners.remove(p, plugin.waitWinners.get(p));
                        }
                    }
                }
            }, 0L, 20L));
        } else {
            if(!plugin.winners.containsKey(p)) return;
            plugin.winners.remove(p, plugin.winners.get(p));
            if(!plugin.waitWinners.containsKey(p)) return;
            plugin.waitWinners.get(p).cancel();
            plugin.waitWinners.remove(p, plugin.waitWinners.get(p));
        }
    }
}
