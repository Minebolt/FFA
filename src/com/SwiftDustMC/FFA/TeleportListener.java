package com.SwiftDustMC.FFA;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TeleportListener
        implements Listener
{
    private MineBoltFFA plugin;

    public TeleportListener(MineBoltFFA plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Location loc = event.getTo();
        if (MineBoltFFA.isIn.containsKey(player.getName()))
        {
            ItemStack[] items = (ItemStack[])MineBoltFFA.PlayerInventories.get(player.getName());
            player.getInventory().clear();
            for (int a = 0; a < items.length; a++) {
                if (items[a] != null) {
                    player.getInventory().addItem(new ItemStack[] { items[a] });
                }
            }
            player.getInventory().setArmorContents((ItemStack[])MineBoltFFA.PlayerArmor.get(player.getName()));
            MineBoltFFA.PlayerInventories.remove(player.getName());
            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_GREEN + " You have left the arena. Your inventory has been restored.");
            MineBoltFFA.isIn.remove(player.getName());
        }
        else if (!MineBoltFFA.isIn.containsKey(player.getName()))
        {
            String list = this.plugin.getConfig().getStringList("Arenas").toString().replace("[", "");
            String list1 = list.replace("]", "");
            String[] list2 = list1.split(", ");
            for (int i = 0; i < list2.length; i++)
            {
                int x = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location1.x");
                int y = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location1.y");
                int z = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location1.z");

                int x1 = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location2.x");
                int y1 = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location2.y");
                int z1 = this.plugin.getConfig().getInt("Arena." + list2[i] + ".locations.location2.z");
                if (((x <= loc.getBlockX()) && (loc.getBlockX() <= x1)) || ((x1 <= loc.getBlockX()) && (loc.getBlockX() <= x) && (
                        ((y <= loc.getBlockY()) && (loc.getBlockY() <= y1)) || ((y1 <= loc.getBlockY()) && (loc.getBlockY() <= y) && (
                                ((z <= loc.getBlockZ()) && (loc.getBlockZ() <= z1)) || ((z1 <= loc.getBlockZ()) && (loc.getBlockZ() <= z) &&
                                        (!MineBoltFFA.isIn.containsKey(player.getName()))))))))
                {
                    MineBoltFFA.PlayerInventories.put(player.getName(), player.getInventory().getContents());
                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + " Welcome to the arena! Your inventory has been saved.");
                    MineBoltFFA.isIn.put(player.getName(), list2[i]);
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
                    player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
                    player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
                    player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
                    player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.STONE_SWORD, 1) });
                    player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.ROTTEN_FLESH, 64) });
                }
            }
        }
    }
}
