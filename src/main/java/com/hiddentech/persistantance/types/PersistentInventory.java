package com.hiddentech.persistantance.types;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface PersistentInventory extends Persistent{
    List<ItemStack> getInventory();
}
