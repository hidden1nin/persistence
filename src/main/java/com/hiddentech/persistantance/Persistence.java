package com.hiddentech.persistantance;

import org.bukkit.plugin.java.JavaPlugin;

public final class Persistence extends JavaPlugin {
    private static Persistence persistence;
    private static ConfigHandler configHandler;

    public static PersistenceAPI getApi() {
        return api;
    }

    private static PersistenceAPI api;
    public static Persistence getPlugin() {
        return persistence;
    }

    public static ConfigHandler getConfigHandler() {
        return configHandler;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        persistence = this;
        configHandler = new ConfigHandler();
        api = new PersistenceAPI();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
