package com.hiddentech.persistantance.events;

import com.hiddentech.persistantance.config.PlayerConfig;
import org.bukkit.entity.Player;

public interface DataLoadCallBack {
    void onQueryDone(Player player, PlayerConfig result);
}
