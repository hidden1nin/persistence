package com.hiddentech.persistantance;

import com.hiddentech.persistantance.config.Config;
import com.hiddentech.persistantance.config.PlayerConfig;
import com.hiddentech.persistantance.types.Persistent;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

public class PersistenceAPI {
    /*
    Used on player join async
     */
    public PlayerConfig registerPlayer(UUID player) {
        File file = new File(Persistence.getPlugin().getDataFolder()+"/players/", player + ".yml");
        PlayerConfig config = new PlayerConfig(player+"", file, YamlConfiguration.loadConfiguration(file));
        Persistence.getConfigHandler().RegisterPlayer(player, config);
        return config;
    }
    //when working with default use the direct saving methods to avoid infinite recursion!
    public void registerPlayerBoolean(String key,boolean value){
        Persistence.getDefaultPlayer().booleans.put(key,value);
    }
    public void registerPlayerInt(String key,Integer value){
        Persistence.getDefaultPlayer().ints.put(key,value);
    }
    public void registerPlayerLocation(String key, Location value){
        Persistence.getDefaultPlayer().locations.put(key,value);
    }
    public void registerPlayerString(String key,String value){
        Persistence.getDefaultPlayer().strings.put(key,value);
    }
    public void registerPlayerItems(String key, ArrayList<ItemStack> value){
        Persistence.getDefaultPlayer().items.put(key,value);
    }
    public void registerPlayerObject(String key,Object value){
        Persistence.getDefaultPlayer().objects.put(key,value);
    }


    public void save(UUID player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Persistence.getConfigHandler().savePlayer(player);
            }
        }.runTaskAsynchronously(Persistence.getPlugin());
    }


    public void load(UUID player){
        new BukkitRunnable(){
            @Override
            public void run() {
                Persistence.getConfigHandler().loadPlayer(player,Persistence.getDefaultPlayer());
            }
        }.runTaskAsynchronously(Persistence.getPlugin());
    }

    /*
    Below is all non-player related
     */

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
        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    Persistence.getConfigHandler().load(Persistence.getConfigHandler().getFiles().get(plugin), tClass);
                }catch (IllegalAccessException | InstantiationException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Persistence.getPlugin());
    }
}
