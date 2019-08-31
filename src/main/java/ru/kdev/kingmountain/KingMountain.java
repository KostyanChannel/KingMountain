package ru.kdev.kingmountain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.kdev.kingmountain.cmds.KMCommand;
import ru.kdev.kingmountain.cmds.KMGoMp;
import ru.kdev.kingmountain.listener.KMListener;
import ru.kdev.kingmountain.metrics.MetricsLite;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KingMountain extends JavaPlugin {
    public static KingMountain instance = null;
    public Map<Player, Boolean> playersOnMP = new HashMap<Player, Boolean>();
    public Map<Player, Integer> winners = new HashMap<Player, Integer>();
    public Map<Player, BukkitTask> waitWinners = new HashMap<Player, BukkitTask>();

    @Override
    public void onEnable() {
        File confFile = new File(getDataFolder() + File.separator + "config.yml");
        if(!confFile.exists())
        {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        instance = this;

        MetricsLite metricsLite = new MetricsLite(this);

        Bukkit.getServer().getPluginManager().registerEvents(new KMListener(), this);
        this.getCommand("kingmountain").setExecutor(new KMCommand());
        this.getCommand("gomp").setExecutor(new KMGoMp());
    }
}
