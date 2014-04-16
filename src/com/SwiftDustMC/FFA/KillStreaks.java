package com.SwiftDustMC.FFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillStreaks
        implements Listener
{
    public static ArrayList<String> killed = new ArrayList();
    private MineBoltFFA plugin;

    public KillStreaks(MineBoltFFA plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        if ((player.getKiller() instanceof Player))
        {
            Player killer = player.getKiller();
            if ((MineBoltFFA.isIn.containsKey(player.getName())) && (MineBoltFFA.isIn.containsKey(killer.getName())))
            {
                if (MineBoltFFA.KillStreak.containsKey(killer.getName())) {
                    MineBoltFFA.KillStreak.put(killer.getName(), Integer.valueOf(((Integer)MineBoltFFA.KillStreak.get(killer.getName())).intValue() + 1));
                } else {
                    MineBoltFFA.KillStreak.put(killer.getName(), Integer.valueOf(1));
                }
                int kills = ((Integer)MineBoltFFA.KillStreak.get(killer.getName())).intValue();
                event.getDrops().clear();
                killer.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.AQUA + " You got a " + ChatColor.GREEN + kills + ChatColor.AQUA + " streak!");
                if (this.plugin.getConfig().contains("KillStreak." + kills + ".Message"))
                {
                    String msg = this.plugin.getConfig().getString("KillStreak." + kills + ".Message");
                    killer.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + " " + colorizeText(msg));
                }
                if (this.plugin.getConfig().contains("KillStreak." + kills))
                {
                    killed.add(killer.getName());
                    String name = (String)MineBoltFFA.isIn.get(killer.getName());
                    if (name.equalsIgnoreCase((String)this.plugin.getConfig().getStringList("Arenas").get(0))) {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " " + killer.getName() + ChatColor.DARK_AQUA + " is on a killstreak! Type " + ChatColor.DARK_PURPLE + "/ffa " + ChatColor.DARK_AQUA + " to stop them!");
                    } else {
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " " + killer.getName() + ChatColor.DARK_AQUA + " is on a killstreak! Type " + ChatColor.DARK_PURPLE + "/ffa join " + name + ChatColor.DARK_AQUA + " to stop them!");
                    }
                }
                if (killed.contains(player.getName()))
                {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 250");
                    killer.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.AQUA + " You have been awarded" + ChatColor.GREEN + "$250" + ChatColor.AQUA + " for ending a killstreak!");
                }
                if (kills % 5 == 0)
                {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + killer.getName() + " 250");
                    killer.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.AQUA + " You have been awarded" + ChatColor.GREEN + "$250" + ChatColor.AQUA + " for getting 5 kills.");
                }
                if (this.plugin.getConfig().contains("KillStreak." + kills + ".Items")) {
                    for (String s : this.plugin.getConfig().getStringList("KillStreak." + kills + ".Items"))
                    {
                        String[] items = s.split(" ");
                        int itemnum = Integer.parseInt(items[0]);
                        int amt = Integer.parseInt(items[1]);
                        killer.getInventory().addItem(new ItemStack[] { new ItemStack(Material.getMaterial(itemnum), amt) });
                    }
                }
                if (this.plugin.getConfig().contains("KillStreak." + kills + ".Remove")) {
                    for (String s : this.plugin.getConfig().getStringList("KillStreak." + kills + ".Remove"))
                    {
                        String[] items = s.split(" ");
                        int itemnum = Integer.parseInt(items[0]);
                        int amt = Integer.parseInt(items[1]);
                        if (killer.getInventory().contains(new ItemStack(Material.getMaterial(itemnum), amt))) {
                            killer.getInventory().remove(new ItemStack(Material.getMaterial(itemnum), amt));
                        }
                    }
                }
                if (this.plugin.getConfig().contains("KillStreak." + kills + ".Armor"))
                {
                    String s = this.plugin.getConfig().getString("KillStreak." + kills + ".Armor");
                    String[] aa = s.split(" ");
                    int h = Integer.valueOf(aa[0]).intValue();
                    int c = Integer.valueOf(aa[1]).intValue();
                    int ar = Integer.valueOf(aa[2]).intValue();
                    int b = Integer.valueOf(aa[3]).intValue();
                    if (h != 0) {
                        killer.getInventory().setHelmet(new ItemStack(Material.getMaterial(h)));
                    }
                    if (c != 0) {
                        killer.getInventory().setChestplate(new ItemStack(Material.getMaterial(c)));
                    }
                    if (ar != 0) {
                        killer.getInventory().setLeggings(new ItemStack(Material.getMaterial(ar)));
                    }
                    if (b != 0) {
                        killer.getInventory().setBoots(new ItemStack(Material.getMaterial(b)));
                    }
                }
                if (this.plugin.getConfig().contains("KillStreak." + kills + ".Potions")) {
                    for (String s : this.plugin.getConfig().getStringList("KillStreak." + kills + ".Potions"))
                    {
                        String[] eff = s.split(" ");
                        int time = Integer.parseInt(eff[1]) * 20;
                        if (eff[0].equalsIgnoreCase("blindness")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time, 1));
                        } else if (eff[0].equalsIgnoreCase("confusion")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, time, 1));
                        } else if (eff[0].equalsIgnoreCase("defense")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time, 1));
                        } else if (eff[0].equalsIgnoreCase("dig")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time, 1));
                        } else if (eff[0].equalsIgnoreCase("fireproof")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, time, 1));
                        } else if (eff[0].equalsIgnoreCase("hurt")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.HARM, time, 1));
                        } else if (eff[0].equalsIgnoreCase("heal")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, time, 1));
                        } else if (eff[0].equalsIgnoreCase("hunger")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, time, 1));
                        } else if (eff[0].equalsIgnoreCase("strength")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time, 1));
                        } else if (eff[0].equalsIgnoreCase("jump")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time, 1));
                        } else if (eff[0].equalsIgnoreCase("poison")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, time, 1));
                        } else if (eff[0].equalsIgnoreCase("regeneration")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, time, 1));
                        } else if (eff[0].equalsIgnoreCase("slow")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time, 1));
                        } else if (eff[0].equalsIgnoreCase("slowdig")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time, 1));
                        } else if (eff[0].equalsIgnoreCase("speed")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, 1));
                        } else if (eff[0].equalsIgnoreCase("underwater")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, time, 1));
                        } else if (eff[0].equalsIgnoreCase("weakness")) {
                            killer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time, 1));
                        }
                    }
                }
            }
        }
        if (MineBoltFFA.isIn.containsKey(player.getName()))
        {
            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_GREEN + " You have died. Inventory restored.");
            ItemStack[] items = (ItemStack[])MineBoltFFA.PlayerInventories.get(player.getName());
            player.getInventory().clear();
            for (int a = 0; a < items.length; a++) {
                if (items[a] != null) {
                    player.getInventory().addItem(new ItemStack[] { items[a] });
                }
            }
            player.getInventory().setArmorContents((ItemStack[])MineBoltFFA.PlayerArmor.get(player.getName()));
            MineBoltFFA.PlayerInventories.remove(player.getName());
            MineBoltFFA.isIn.remove(player.getName());
            if (MineBoltFFA.KillStreak.containsKey(player.getName()))
            {
                int kills = ((Integer)MineBoltFFA.KillStreak.get(player.getName())).intValue();
                player.sendMessage(ChatColor.GOLD + " You got " + ChatColor.GREEN + kills + ChatColor.GOLD + " kills.");
                MineBoltFFA.KillStreak.remove(player.getName());
                killed.remove(player.getName());
            }
        }
    }

    public String colorizeText(String string)
    {
        string = string.replaceAll("&0", ChatColor.BLACK.toString());
        string = string.replaceAll("&1", ChatColor.DARK_BLUE.toString());
        string = string.replaceAll("&2", ChatColor.DARK_GREEN.toString());
        string = string.replaceAll("&3", ChatColor.DARK_AQUA.toString());
        string = string.replaceAll("&4", ChatColor.DARK_RED.toString());
        string = string.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
        string = string.replaceAll("&6", ChatColor.GOLD.toString());
        string = string.replaceAll("&7", ChatColor.GRAY.toString());
        string = string.replaceAll("&8", ChatColor.DARK_GRAY.toString());
        string = string.replaceAll("&9", ChatColor.BLUE.toString());
        string = string.replaceAll("&a", ChatColor.GREEN.toString());
        string = string.replaceAll("&b", ChatColor.AQUA.toString());
        string = string.replaceAll("&c", ChatColor.RED.toString());
        string = string.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
        string = string.replaceAll("&e", ChatColor.YELLOW.toString());
        string = string.replaceAll("&f", ChatColor.WHITE.toString());
        return string;
    }
}
