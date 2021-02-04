package com.hiddentech.persistantance;

import com.hiddentech.persistantance.types.Persistent;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class PersistenceAPI {
    public Config register(String pluginname) {
        File file = new File(Persistence.getPlugin().getDataFolder(), pluginname + ".yml");
        Config config = new Config(pluginname, file, YamlConfiguration.loadConfiguration(file));
        Persistence.getConfigHandler().Register(pluginname, config);
        return config;
    }

    public boolean save(String pluginname) {
        return Persistence.getConfigHandler().saveFile(Persistence.getConfigHandler().getFiles().get(pluginname));
    }

    public void save(String plugin,Persistent object) {
        Persistence.getConfigHandler().saveObject(object, Persistence.getConfigHandler().getFiles().get(plugin));
    }
    public void save(Config config,Persistent object) {
        Persistence.getConfigHandler().saveObject(object,config);
    }
    public boolean save(Config config) {
        return Persistence.getConfigHandler().saveFile(config);
    }
    public void delete(Config config,Persistent object) {
        Persistence.getConfigHandler().removeObject(object,config);
    }
    public void delete(String config,Persistent object) {
        Persistence.getConfigHandler().removeObject(object,Persistence.getConfigHandler().getFiles().get(config));
    }
    public Config getConfig(String pluginname) {
        return Persistence.getConfigHandler().getFiles().get(pluginname);
    }
    public int getNextID(String pluginname) {
        return Persistence.getConfigHandler().getFiles().get(pluginname).getNextID();
    }
    public <T extends Persistent> void load(String plugin, Class<T> tClass){
        try {
            Persistence.getConfigHandler().load(Persistence.getConfigHandler().getFiles().get(plugin), tClass);
        }catch (IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }
    }
}
