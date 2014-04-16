package com.SwiftDustMC.FFA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArenaListener
        implements Listener
{
    public static ArrayList<String> heal = new ArrayList();
    private MineBoltFFA plugin;

    public ArenaListener(MineBoltFFA plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntrance(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (player.isDead()) {
            return;
        }
        Location loc = event.getPlayer().getLocation();
        String list = this.plugin.getConfig().getStringList("Arenas").toString().replace("[", "");
        String list1 = list.replace("]", "");
        String[] list2 = list1.split(", ");
    }

}