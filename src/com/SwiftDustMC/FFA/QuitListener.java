package com.SwiftDustMC.FFA;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener
        implements Listener
{
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        if (ArenaListener.heal.contains(player.getName())) {
            ArenaListener.heal.remove(player.getName());
        }
    }
}
