package com.hiddentech.persistantance;

import com.hiddentech.persistantance.config.Config;
import com.hiddentech.persistantance.config.PlayerConfig;
import com.hiddentech.persistantance.player.PlayerData;
import com.hiddentech.persistantance.types.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ConfigHandler {
    public HashMap<String, Config> getFiles() {
        return files;
    }
    public HashMap<UUID, PlayerConfig> getPlayerFiles() {
        return playerFiles;
    }
    private HashMap<String,Config> files = new HashMap<>();
    private HashMap<UUID,PlayerConfig> playerFiles = new HashMap<>();
    private HashMap<UUID,PlayerData> playerData = new HashMap<>();
    /*
    Player related
    */

    protected HashMap<UUID, PlayerData> getPlayerData() {
        return playerData;
    }
    public PlayerData getPlayerData(UUID player) {
        return playerData.getOrDefault(player,Persistence.getDefaultPlayer());
    }
    public void RegisterPlayer(UUID name, PlayerConfig config) {
        playerFiles.put(name,config);
        saveFile(config);
    }

    public boolean saveFile(PlayerConfig config) {
        try {
            config.getFileConfiguration().options().copyDefaults(true);
            config.getFileConfiguration().save(config.getFile());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadPlayer(UUID player) {
        loadPlayer(player,Persistence.getDefaultPlayer());
    }
    public void loadPlayer(UUID player, PlayerData defaultPlayer) {
        PlayerData playerData = new PlayerData(player);
        PlayerConfig playerConfig = playerFiles.get(player);
        //loop through all default values
        for(String key:defaultPlayer.booleans.keySet()){
            //cache the default to avoid another lookup
            Boolean defaultBool = defaultPlayer.booleans.get(key);
            //if the player config contains the boolean then use it
            if(playerConfig.getFileConfiguration().contains(key)) {
                //cache the players boolean to avoid another lookup
                Boolean bool = playerConfig.getFileConfiguration().getBoolean(key);
                //check if it matches the default and if so remove it to save space
                if (defaultBool == bool&&Persistence.isRemoveDefaults()) {
                    playerConfig.getFileConfiguration().set(key, null);
                }
                //assign the players value
                playerData.booleans.put(key,bool);
                //continue to the next boolean
                continue;
            }
            //if the player does not have a saved value then just use the default one.
            //player.booleans.put(key,defaultBool);
            //instead of storing the default value many many times instead i can use hashmap.getordefault, much more efficient!
        }
        //TODO add the rest of the data types
        //add it to our stored player stuff
        getPlayerData().put(player, playerData);
    }

    public void savePlayer(UUID player) {
        savePlayer(player,Persistence.getDefaultPlayer());
    }
    public void savePlayer(UUID player, PlayerData defaultPlayer) {
        PlayerData playerSave = playerData.get(player);
        PlayerConfig playerConfig = playerFiles.get(player);
        //loop through all player values
        for(String key:playerSave.booleans.keySet()){
            //cache the default to avoid another lookup
            Boolean defaultBool = defaultPlayer.booleans.get(key);
                //cache the players boolean to avoid another lookup
                Boolean bool = playerSave.booleans.get(key);
                //save if we are allowed to save defaults or if they are not equal
                if (!Persistence.isRemoveDefaults()||defaultBool != bool) {
                    playerConfig.getFileConfiguration().set(key, bool);
                }
            }
        //TODO add the rest of the data types
        saveFile(playerConfig);
    }
    /*
    Non-Player Objects
     */

    public void Register(String name,Config config) {
        files.put(name,config);
        saveFile(config);
    }
    public boolean saveFile(Config config) {
        try {
            config.getFileConfiguration().options().copyDefaults(true);
            config.getFileConfiguration().save(config.getFile());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveObject(Persistent object, Config config) {
        if(object == null) return;
        if(object instanceof PersistentLocation) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".location",((PersistentLocation) object).getLocation());
            config.getFileConfiguration().set("AutoRemoveAir",((PersistentLocation) object).removeAir());
        }

        if(object instanceof PersistentString) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".string",((PersistentString) object).getString());
        }
        if(object instanceof PersistentInt) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".int",((PersistentInt) object).getInt());
        }
        if(object instanceof PersistentInventory) {
            for(int i =0;i<((PersistentInventory) object).getInventory().size()-1;i++) {
                config.getFileConfiguration().set(config.getName() + "." + object.getId() + ".inventory."+i, ((PersistentInventory) object).getInventory().get(i));
            }
        }
        //TODO add other storage types
        saveFile(config);
    }


    public <T extends Persistent> void load(Config config, Class<T> tClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        //auto removes locations that are air
        boolean air = false;
        if(config.getFileConfiguration().contains("AutoRemoveAir")){
            air =config.getFileConfiguration().getBoolean("AutoRemoveAir");
        }

        ConfigurationSection section = config.getFileConfiguration().getConfigurationSection(config.getName());
        if(section==null) return;
        Constructor<T> constructor;
        try {
            constructor =tClass.getConstructor(ObjectData.class);
        } catch (NoSuchMethodException e){
            e.printStackTrace();
            Bukkit.broadcastMessage(tClass.getSimpleName()+" class does not have initializing constructor!!!");
            return;
        }
        for(String key:section.getKeys(false)){
            Validate.notNull(config.getFileConfiguration().get(config.getName()+"."+key),"Trying to load null");
            ObjectData objectData = new ObjectData();
            objectData.setId(Integer.parseInt(key));
            //checks if config has location data for that class
            if(config.getFileConfiguration().contains(config.getName()+"."+key+".location")){
                objectData.setLocation((Location) config.getFileConfiguration().get(config.getName()+"."+key+".location"));
                if(air&&objectData.getLocation().getBlock().getType()== Material.AIR){
                    config.getFileConfiguration().set(config.getName()+"."+key,null);
                    return;
                }
            }

            if(config.getFileConfiguration().contains(config.getName()+"."+key+".string")){
                objectData.setString(config.getFileConfiguration().getString(config.getName()+"."+key+".string"));
            }
            if(config.getFileConfiguration().contains(config.getName()+"."+key+".int")){
                objectData.setInt(config.getFileConfiguration().getInt(config.getName()+"."+key+".int"));
            }
            if(config.getFileConfiguration().contains(config.getName()+"."+key+".inventory")){
                ConfigurationSection sectionItems = config.getFileConfiguration().getConfigurationSection(config.getName()+"."+key+".inventory");
                List<ItemStack> itemStacks = new ArrayList<>();
                for(String item:sectionItems.getKeys(false))
                itemStacks.add((ItemStack) config.getFileConfiguration().get(item));
                objectData.setContents(itemStacks);
            }

            //TODO add other storage types
            constructor.newInstance(objectData);
        }
    }

    public void removeObject(Persistent object, Config config) {
        config.getFileConfiguration().set(config.getName()+"."+object.getId(),null);
        saveFile(config);
    }

}
