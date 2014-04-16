package com.SwiftDustMC.FFA;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FFA
        implements CommandExecutor
{
    public static ArrayList<String> tpWait = new ArrayList();
    private MineBoltFFA plugin;
    private int id;

    public FFA(MineBoltFFA plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player player = (Player)sender;
        final Player p = player;
        if (!(sender instanceof Player))
        {
            System.out.println("You can only use this command in-game!");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("ffa"))
        {
            player.sendMessage(ChatColor.GOLD + "-----" + ChatColor.RED + "MineBolt-FFA" + ChatColor.GOLD + "-----");
            if (sender.hasPermission("ffa.ffa"))
            {
                int sec=0;
                if (args.length == 0)
                {
                    if (this.plugin.getConfig().getStringList("Arenas").size() != 0)
                    {
                        String arenaname = (String)this.plugin.getConfig().getStringList("Arenas").get(0);
                        int x = this.plugin.getConfig().getInt("Arena." + arenaname + ".spawn.x");
                        int y = this.plugin.getConfig().getInt("Arena." + arenaname + ".spawn.y");
                        int z = this.plugin.getConfig().getInt("Arena." + arenaname + ".spawn.z");
                        World world = player.getWorld();
                        final Location loc = new Location(world, x, y, z);
                        final int times = this.plugin.getConfig().getInt("Delay-Time");
                        tpWait.add(player.getName());
                        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + "Teleporting to the arena in: " + ChatColor.GREEN + times + ChatColor.DARK_AQUA + " seconds.");
                        final ArrayList<Integer> in = new ArrayList();
                        for (int i = 1; i <= times; i++)
                        {
                            in.add(Integer.valueOf(this.id));
                            if (!tpWait.contains(p.getName()))
                            {
                                for (Integer il : in) {
                                    this.plugin.getServer().getScheduler().cancelTask(il.intValue());
                                }
                                break;
                            }
                            final int ii = i;
                            sec = ii * 20;
                            this.id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
                            {
                                public void run()
                                {
                                    in.add(Integer.valueOf(FFA.this.id));
                                    if (!FFA.tpWait.contains(p.getName())) {
                                        for (Integer i : in) {
                                            FFA.this.plugin.getServer().getScheduler().cancelTask(i.intValue());
                                        }
                                    }
                                    if (ii != times)
                                    {
                                        if (FFA.tpWait.contains(p.getName()))
                                        {
                                            int diff = times - ii;
                                            p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + "Teleporting to the arena in: " + ChatColor.GREEN + diff + ChatColor.DARK_AQUA + " seconds.");
                                        }
                                    }
                                    else if ((ii == times) &&
                                            (FFA.tpWait.contains(p.getName())))
                                    {
                                        FFA.tpWait.remove(p.getName());
                                        p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + "Teleporting commencing...");
                                        p.teleport(loc);
                                        MineBoltFFA.PlayerLocations.put(p.getName(), p.getLocation());
                                    }
                                }
                            }, sec);
                        }
                    }
                }
                else if (args.length == 1)
                {
                    if (args[0].equalsIgnoreCase("leave"))
                    {
                        if (sender.hasPermission("ffa.leave")) {
                            if (MineBoltFFA.isIn.containsKey(player.getName()))
                            {
                                if (MineBoltFFA.PlayerLocations.containsKey(player.getName()))
                                {
                                    final int times = this.plugin.getConfig().getInt("Delay-Time");
                                    for (int i = 1; i <= times; i++)
                                    {
                                        final int ii = i;
                                        int sec1 = ii * 20;
                                        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
                                        {
                                            public void run()
                                            {
                                                if (ii != times)
                                                {
                                                    int diff = times - ii;
                                                    p.sendMessage(ChatColor.DARK_AQUA + "Teleporting out of the arena in: " + ChatColor.GREEN + diff + ChatColor.DARK_AQUA + " seconds.");
                                                }
                                                else if (ii == times)
                                                {
                                                    p.sendMessage(ChatColor.DARK_AQUA + "Teleporting commencing...");
                                                    p.teleport((Location)MineBoltFFA.PlayerLocations.get(p.getName()));
                                                    MineBoltFFA.PlayerLocations.put(p.getName(), p.getLocation());
                                                    MineBoltFFA.PlayerLocations.remove(p.getName());
                                                }
                                            }
                                        }, sec);
                                    }
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You never teleported to an arena!");
                                }
                            }
                            else {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You are not currently in an arena!");
                            }
                        }
                    }
                    else if ((args[0].equalsIgnoreCase("ver")) || (args[0].equalsIgnoreCase("version"))) {
                        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_GRAY + " This is Version 1.0");
                    } else if (args[0].equalsIgnoreCase("wand"))
                    {
                        if (sender.hasPermission("ffa.wand"))
                        {
                            if (player.getInventory().firstEmpty() != -1)
                            {
                                player.getInventory().addItem(new ItemStack[] { new ItemStack(Material.WOOD_HOE, 1) });
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + " Right click one corner and left click other corner.");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Your inventory is full!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("list"))
                    {
                        if (sender.hasPermission("ffa.list")) {
                            if (this.plugin.getConfig().getStringList("Arenas").size() != 0)
                            {
                                String list = this.plugin.getConfig().getStringList("Arenas").toString().replace("[", "");
                                String list1 = list.replace("]", "");
                                String[] list2 = list1.split(", ");
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.AQUA + " List of arenas:");
                                for (int i = 0; i < list2.length; i++) {
                                    player.sendMessage(ChatColor.GREEN + "" + (i + 1) + ". " + ChatColor.YELLOW + list2[i]);
                                }
                            }
                            else
                            {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " There are no arenas currently set up.");
                            }
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Unknown command.");
                    }
                }
                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create"))
                    {
                        if (sender.hasPermission("ffa.create"))
                        {
                            if ((RegionSelection.selectLoc1.containsKey(player)) && (RegionSelection.selectLoc2.containsKey(player)))
                            {
                                if (!this.plugin.getConfig().getStringList("Arenas").contains(args[1]))
                                {
                                    Location loc1 = (Location)RegionSelection.selectLoc1.get(player);
                                    String world = loc1.getWorld().getName();
                                    this.plugin.getConfig().addDefault("Arena." + args[1] + ".locations.location1.world", world);
                                    int x = loc1.getBlockX();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location1.x", Integer.valueOf(x));
                                    int y = loc1.getBlockY();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location1.y", Integer.valueOf(y));
                                    int z = loc1.getBlockZ();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location1.z", Integer.valueOf(z));
                                    Location loc2 = (Location)RegionSelection.selectLoc2.get(player);
                                    int x1 = loc2.getBlockX();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location2.x", Integer.valueOf(x1));
                                    int y1 = loc2.getBlockY();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location2.y", Integer.valueOf(y1));
                                    int z1 = loc2.getBlockZ();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.location2.z", Integer.valueOf(z1));
                                    this.plugin.saveConfig();
                                    List<String> list = this.plugin.getConfig().getStringList("Arenas");
                                    list.add(args[1]);
                                    this.plugin.getConfig().set("Arenas", list);
                                    this.plugin.saveConfig();
                                    this.plugin.reloadConfig();
                                    RegionSelection.selectLoc1.remove(player);
                                    RegionSelection.selectLoc2.remove(player);
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Arena successfully created!");
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Arena already exists!");
                                }
                            }
                            else {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You must first select a region!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission to do this!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setheal"))
                    {
                        if (sender.hasPermission("ffa.setheal"))
                        {
                            if ((RegionSelection.selectLoc1.containsKey(player)) && (RegionSelection.selectLoc2.containsKey(player)))
                            {
                                if (this.plugin.getConfig().getStringList("Arenas").contains(args[1]))
                                {
                                    Location loc1 = (Location)RegionSelection.selectLoc1.get(player);
                                    String world = loc1.getWorld().getName();
                                    this.plugin.getConfig().addDefault("Arena." + args[1] + ".locations.heal1.world", world);
                                    int x = loc1.getBlockX();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal1.x", Integer.valueOf(x));
                                    int y = loc1.getBlockY();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal1.y", Integer.valueOf(y));
                                    int z = loc1.getBlockZ();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal1.z", Integer.valueOf(z));
                                    Location loc2 = (Location)RegionSelection.selectLoc2.get(player);
                                    int x1 = loc2.getBlockX();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal2.x", Integer.valueOf(x1));
                                    int y1 = loc2.getBlockY();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal2.y", Integer.valueOf(y1));
                                    int z1 = loc2.getBlockZ();
                                    this.plugin.getConfig().set("Arena." + args[1] + ".locations.heal2.z", Integer.valueOf(z1));
                                    this.plugin.saveConfig();
                                    this.plugin.reloadConfig();
                                    RegionSelection.selectLoc1.remove(player);
                                    RegionSelection.selectLoc2.remove(player);
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Heal region successfully created!");
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Arena does not exist!");
                                }
                            }
                            else {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You must first select a region!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission to do this!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("remove"))
                    {
                        if (sender.hasPermission("ffa.remove"))
                        {
                            if (this.plugin.getConfig().getStringList("Arenas").contains(args[1]))
                            {
                                this.plugin.getConfig().set(args[1], null);
                                List<String> a = this.plugin.getConfig().getStringList("Arenas");
                                a.remove(args[1]);
                                this.plugin.getConfig().set("Arenas", a);
                                this.plugin.saveConfig();
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Arena removed.");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Arena does not exist!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setspawn"))
                    {
                        if (sender.hasPermission("ffa.setspawn"))
                        {
                            if (this.plugin.getConfig().getStringList("Arenas").contains(args[1]))
                            {
                                Location loc = player.getLocation();
                                int x = loc.getBlockX();
                                int y = loc.getBlockY();
                                int z = loc.getBlockZ();
                                this.plugin.getConfig().set("Arena." + args[1] + ".spawn.x", Integer.valueOf(x));
                                this.plugin.getConfig().set("Arena." + args[1] + ".spawn.y", Integer.valueOf(y));
                                this.plugin.getConfig().set("Arena." + args[1] + ".spawn.z", Integer.valueOf(z));
                                this.plugin.saveConfig();
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Arena spawn successfully set.");
                            }
                            else
                            {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Arena does not exist!");
                            }
                        }
                        else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("time"))
                    {
                        if (sender.hasPermission("ffa.time")) {
                            try
                            {
                                int i = Integer.parseInt(args[1]);
                                this.plugin.getConfig().set("Delay-Time", Integer.valueOf(i));
                                this.plugin.saveConfig();
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.GREEN + " Teleport delay successfully set.");
                            }
                            catch (NumberFormatException e)
                            {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You must enter a number!");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " You do not have permission!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("join"))
                    {
                        if ((sender.hasPermission("ffa.join")) &&
                                (this.plugin.getConfig().getStringList("Arenas").contains(args[1])))
                        {
                            int x = this.plugin.getConfig().getInt("Arena." + args[1] + ".spawn.x");
                            int y = this.plugin.getConfig().getInt("Arena." + args[1] + ".spawn.y");
                            int z = this.plugin.getConfig().getInt("Arena." + args[1] + ".spawn.z");
                            World world = player.getWorld();
                            final Location loc = new Location(world, x, y, z);
                            final int times = this.plugin.getConfig().getInt("Delay-Time");
                            tpWait.add(player.getName());
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + "Teleporting to the arena in: " + ChatColor.GREEN + times + ChatColor.DARK_AQUA + " seconds.");
                            ArrayList<Integer> in = new ArrayList();
                            for (int i = 1; i <= times; i++)
                            {
                                in.add(Integer.valueOf(this.id));
                                if (!tpWait.contains(p.getName()))
                                {
                                    for (Integer il : in) {
                                        this.plugin.getServer().getScheduler().cancelTask(il.intValue());
                                    }
                                    break;
                                }
                                final int ii = i;
                                int sec1 = ii * 20;
                                this.id = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
                                {
                                    public void run()
                                    {
                                        if (FFA.tpWait.contains(p.getName())) {
                                            if ((ii == times) &&
                                                    (FFA.tpWait.contains(p.getName())))
                                            {
                                                FFA.tpWait.remove(p.getName());
                                                p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.DARK_AQUA + "Teleporting commencing...");
                                                p.teleport(loc);
                                                MineBoltFFA.PlayerLocations.put(p.getName(), p.getLocation());
                                            }
                                        }
                                    }
                                }, sec1);
                            }
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "MineBolt" + ChatColor.RED + "-" + ChatColor.DARK_RED + "FFA" + ChatColor.GOLD + "]" + ChatColor.RED + " Unknown command.");
                    }
                }
            }
        }
        return true;
    }
}
