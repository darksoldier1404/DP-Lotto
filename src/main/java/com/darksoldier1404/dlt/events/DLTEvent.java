package com.darksoldier1404.dlt.events;

import com.darksoldier1404.dlt.Lotto;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("all")
public class DLTEvent implements Listener {
    private final Lotto plugin = Lotto.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.data.initUserData(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.data.saveAndLeave(e.getPlayer().getUniqueId());
    }
}
