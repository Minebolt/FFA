package com.SwiftDustMC.FFA;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener
        implements Listener
{
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if ((event.getEntity() instanceof Player))
        {
            Player damaged = (Player)event.getEntity();
            if ((event.getDamager() instanceof Player))
            {
                Player damager = (Player)event.getDamager();
                if (((MineBolt.isIn.containsKey(damaged.getName())) && (!MineBolt.isIn.containsKey(damager.getName()))) || ((!MineBolt.isIn.containsKey(damaged.getName())) && (MineBolt.isIn.containsKey(damager.getName()))))
                {
                    damager.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You cannot damage that player! Either you are not in the arena or they are not in the arena!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
