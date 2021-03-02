package com.hiddentech.persistantance.events;

import com.hiddentech.persistantance.config.Config;
import com.hiddentech.persistantance.config.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDataLoadEvent extends Event {
    private final Player player;
    private final PlayerConfig config;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    public PlayerDataLoadEvent(Player player, PlayerConfig config){
        this.player = player;
        this.config = config;
    }
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
