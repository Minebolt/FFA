package com.SwiftDustMC.FFA;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RegionSelection
        implements Listener
{
    public static HashMap<Player, Location> selectLoc1 = new HashMap();
    public static HashMap<Player, Location> selectLoc2 = new HashMap();

    @EventHandler
    public void onSelect(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() == Material.WOOD_HOE)
        {
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK)
            {
                if ((player.hasPermission("ffa.create")) || (player.isOp()))
                {
                    Block block = event.getClickedBlock();
                    Location loc = block.getLocation();
                    int x = loc.getBlockX();
                    int y = loc.getBlockY();
                    int z = loc.getBlockZ();
                    selectLoc1.put(player, loc);
                    if ((selectLoc1.get(player) != null) && (selectLoc2.get(player) != null)) {
                        player.sendMessage(ChatColor.GREEN + "Location [" + x + ", " + y + ", " + z + "]" + " added.");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "Location [" + x + ", " + y + ", " + z + "]" + " added. Please left click the other corner.");
                    }
                }
            }
            else if ((action == Action.LEFT_CLICK_BLOCK) && (
                    (player.hasPermission("ffa.create")) || (player.isOp())))
            {
                Block block = event.getClickedBlock();
                int id = event.getClickedBlock().getType().getId();
                Location loc = block.getLocation();
                int x = loc.getBlockX();
                int y = loc.getBlockY();
                int z = loc.getBlockZ();
                block.setTypeId(id);
                selectLoc2.put(player, loc);
                if ((selectLoc1.get(player) != null) && (selectLoc2.get(player) != null)) {
                    player.sendMessage(ChatColor.GREEN + "Location [" + x + ", " + y + ", " + z + "]" + " added.");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Location [" + x + ", " + y + ", " + z + "]" + " added. Please right click the other corner.");
                }
            }
        }
    }
}
