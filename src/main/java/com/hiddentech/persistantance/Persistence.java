package com.hiddentech.persistantance;

import com.hiddentech.persistantance.listeners.PlayerJoinListener;
import com.hiddentech.persistantance.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Persistence extends JavaPlugin {


    public static PlayerData getDefaultPlayer() {
        return defaultPlayer;
    }
    public static boolean isRemoveDefaults() {
        return removeDefaults;
    }
    public static Persistence getPlugin() {
        return persistence;
    }
    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }
    public static PersistenceAPI getApi() {
        return api;
    }

    private static boolean removeDefaults = true;
    private static Persistence persistence;
    private static ConfigHandler configHandler;
    private static PlayerData defaultPlayer;

    //TODO add per player data storage!
    //TODO add inventory storage

    private static PersistenceAPI api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        persistence = this;
        configHandler = new ConfigHandler();
        api = new PersistenceAPI();
        defaultPlayer = new PlayerData(null);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        addDefaults();
    }

    private void addDefaults() {
        if(Persistence.getPlugin().getConfig().contains("RemoveDefaults")){
            removeDefaults =getConfig().getBoolean("RemoveDefaults");
        }else{
            getConfig().set("RemoveDefaults",removeDefaults);
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
