package com.hiddentech.persistantance.listeners;

import com.hiddentech.persistantance.Persistence;
import com.hiddentech.persistantance.config.PlayerConfig;
import com.hiddentech.persistantance.events.DataLoadCallBack;
import com.hiddentech.persistantance.events.PlayerDataLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        if(Persistence.getConfigHandler().getPlayerFiles().containsKey(event.getPlayer().getUniqueId()))return;
        loadData(event.getPlayer(),((player, result) -> {
            PlayerDataLoadEvent dataLoadEvent = new PlayerDataLoadEvent(event.getPlayer(), result);
            Bukkit.getPluginManager().callEvent(dataLoadEvent);
        }));

    }

    public void loadData(Player player,final DataLoadCallBack loadCallBack){
        //load data async
        Bukkit.getScheduler().runTaskAsynchronously(Persistence.getPlugin(), () -> {
            PlayerConfig config  = Persistence.getApi().registerPlayer(player.getUniqueId());
            Persistence.getConfigHandler().loadPlayer(player.getUniqueId());
            //re-sync and do the event
            Bukkit.getScheduler().runTask(Persistence.getPlugin(), () -> {
                // call the callback with the result
                loadCallBack.onQueryDone(player,config);
            });
        });
    }
}
