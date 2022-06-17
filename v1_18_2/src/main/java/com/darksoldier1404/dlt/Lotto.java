package com.darksoldier1404.dlt;

import com.darksoldier1404.dlt.commands.DLTACommand;
import com.darksoldier1404.dlt.commands.DLTUCommand;
import com.darksoldier1404.dlt.events.DLTEvent;
import com.darksoldier1404.dlt.functions.DLTFunction;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Lotto extends JavaPlugin {
    private static Lotto plugin;
    public static DataContainer data;
    public static BukkitTask task;

    public static Lotto getInstance() {
        return plugin;
    }

    public static DataContainer getData() {
        return data;
    }

    public void onEnable() {
        plugin = this;
        data = new DataContainer(plugin);
        plugin.getServer().getPluginManager().registerEvents(new DLTEvent(), plugin);
        getCommand("로또").setExecutor(new DLTUCommand());
        getCommand("dlta").setExecutor(new DLTACommand());
        DLTFunction.initTask();
    }

    public void onDisable() {
        data.save();

    }
}
