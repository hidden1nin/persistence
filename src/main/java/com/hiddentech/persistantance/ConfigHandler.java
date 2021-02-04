package com.hiddentech.persistantance;

import com.hiddentech.persistantance.types.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
    public HashMap<String, Config> getFiles() {
        return files;
    }

    private HashMap<String,Config> files = new HashMap<>();
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
        }

        if(object instanceof PersistentString) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".string",((PersistentString) object).getString());
        }
        if(object instanceof PersistentInt) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".int",((PersistentInt) object).getInt());
        }
        if(object instanceof PersistentInventory) {
            config.getFileConfiguration().set(config.getName()+"."+object.getId()+".inventory", Arrays.stream(((PersistentInventory) object).getInventory().getContents()).toArray());
        }
        //TODO add other storage types
        saveFile(config);
    }


    public <T extends Persistent> void load(Config config, Class<T> tClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
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
            }

            if(config.getFileConfiguration().contains(config.getName()+"."+key+".string")){
                objectData.setString(config.getFileConfiguration().getString(config.getName()+"."+key+".string"));
            }
            if(config.getFileConfiguration().contains(config.getName()+"."+key+".int")){
                objectData.setInt(config.getFileConfiguration().getInt(config.getName()+"."+key+".int"));
            }
            if(config.getFileConfiguration().contains(config.getName()+"."+key+".inventory")){
                objectData.setContents((List< ItemStack > )config.getFileConfiguration().get(config.getName()+"."+key+".inventory"));
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
