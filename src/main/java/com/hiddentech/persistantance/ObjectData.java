package com.hiddentech.persistantance;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ObjectData {
    private Location location;
    private String string;
    private List<ItemStack> inventory;
    private int anInt;
    private int id;
    public ObjectData(){ }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getString() {
        return this.string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<ItemStack> getContents() {
        return inventory;
    }

    public void setContents(List<ItemStack> inventory) {
        this.inventory = inventory;
    }

    public void setInt(int anInt) {
        this.anInt = anInt;
    }
    public int getAnInt() {
        return this.anInt;
    }
}
