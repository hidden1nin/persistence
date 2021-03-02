package com.hiddentech.persistantance.player;

import com.hiddentech.persistantance.Persistence;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private final UUID player;

    public PlayerData(UUID player){
        this.player = player;
    }
    public HashMap<String, Boolean> booleans = new HashMap<>();
    public HashMap<String, Integer> ints = new HashMap<>();
    public HashMap<String, Location> locations = new HashMap<>();
    public HashMap<String, Object> objects = new HashMap<>();
    public HashMap<String, String> strings = new HashMap<>();
    public HashMap<String, ArrayList<ItemStack>> items = new HashMap<>();
    //attempts to get the value, if not present fall back to default, if default does not exist then use false
    public boolean getBoolean(String string){
        Bukkit.broadcastMessage("onGet = "+this.booleans.getOrDefault(string, Persistence.getDefaultPlayer().booleans.getOrDefault(string,false)));
        return this.booleans.getOrDefault(string, Persistence.getDefaultPlayer().booleans.getOrDefault(string,false));
    }
    public void setBoolean(String string,Boolean value){
        //save it if not equal to default or if default saving is on
        if(value!=Persistence.getDefaultPlayer().booleans.getOrDefault(string,false)||!Persistence.isRemoveDefaults()){
            Persistence.getConfigHandler().getPlayerFiles().get(player).getFileConfiguration().set(string,value);
            Persistence.getConfigHandler().saveFile(Persistence.getConfigHandler().getPlayerFiles().get(player));
            //add it to the hashmap
            booleans.put(string,value);
        }else {
            //remove defaults from config
            Persistence.getConfigHandler().getPlayerFiles().get(player).getFileConfiguration().set(string,null);
            Persistence.getConfigHandler().saveFile(Persistence.getConfigHandler().getPlayerFiles().get(player));
            booleans.remove(string);
        }
    }

    //attempts to get the value, if not present fall back to default, if default does not exist then use 0
    public Integer getInt(String string){
        return ints.getOrDefault(string, Persistence.getDefaultPlayer().ints.getOrDefault(string,0));
    }
    public void setInt(String string,Integer value){
        //save it if not equal to default or if default saving is on
        if(!value.equals(Persistence.getDefaultPlayer().ints.getOrDefault(string, 0)) ||!Persistence.isRemoveDefaults()){
            Persistence.getConfigHandler().getPlayerFiles().get(player).getFileConfiguration().set(string,value);
            Persistence.getConfigHandler().saveFile(Persistence.getConfigHandler().getPlayerFiles().get(player));
            //add it to the hashmap
            ints.put(string,value);
        }else {
            //remove defaults from config
            Persistence.getConfigHandler().getPlayerFiles().get(player).getFileConfiguration().set(string,null);
            Persistence.getConfigHandler().saveFile(Persistence.getConfigHandler().getPlayerFiles().get(player));
            ints.remove(string);
        }
    }
}
