package com.SwiftDustMC.FFA;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MineBoltFFA
        extends JavaPlugin
{
    private FFA cmd;
    public static HashMap<String, ItemStack[]> PlayerInventories = new HashMap();
    public static HashMap<String, ItemStack[]> PlayerArmor = new HashMap();
    public static HashMap<String, Location> PlayerLocations = new HashMap();
    public static HashMap<String, Integer> KillStreak = new HashMap();
    public static HashMap<String, String> LocationSave = new HashMap();
    public static HashMap<String, String> isIn = new HashMap();

    public void onEnable()
    {
        loadConfig();
        this.cmd = new FFA(this);
        getCommand("ffa").setExecutor(this.cmd);
        try
        {
            isIn = loadStatus("PlayerStatus.bin");
            KillStreak = loadKills("PlayerKills.bin");
            LocationSave = loadLocations("PlayerLocations.bin");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file(s) not found. Generating file...");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file(s) not found. Generating file...");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for (String s : LocationSave.keySet())
        {
            String[] loc = ((String)LocationSave.get(s)).split(" ");
            World w = Bukkit.getWorld(loc[0]);
            int x = Integer.valueOf(loc[1]).intValue();
            int y = Integer.valueOf(loc[2]).intValue();
            int z = Integer.valueOf(loc[3]).intValue();
            Location l = new Location(w, x, y, z);
            PlayerLocations.put(s, l);
        }
        for (String s : getConfig().getStringList("Save-List"))
        {
            ItemStack[] item = null;
            Object o = getConfig().get("Save." + s + ".Inventory");
            if ((o instanceof ItemStack[]))
            {
                item = (ItemStack[])o;
            }
            else if ((o instanceof List))
            {
                List<String> itemlist = new ArrayList();
                itemlist = (List)o;
                item = (ItemStack[])itemlist.toArray(new ItemStack[0]);
            }
            PlayerInventories.put(s, item);
            o = getConfig().get("Save." + s + ".Armor");
            if ((o instanceof ItemStack[]))
            {
                item = (ItemStack[])o;
            }
            else if ((o instanceof List))
            {
                List<String> itemlist = new ArrayList();
                itemlist = (List)o;
                item = (ItemStack[])itemlist.toArray(new ItemStack[0]);
            }
            PlayerArmor.put(s, item);
            getConfig().set("Save." + s, null);
            getConfig().getStringList("Save-List").remove(s);
        }
        saveConfig();
        getServer().getPluginManager().registerEvents(new RegionSelection(), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);
        getServer().getPluginManager().registerEvents(new KillStreaks(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        long delay = getConfig().getInt("Heal-Delay") * 20;
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                for (String s : ArenaListener.heal)
                {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(s);
                    if (player.isOnline())
                    {
                        Player p = Bukkit.getPlayer(s);
                        if (p.getHealth() < 20.0D) {
                            p.setHealth(p.getHealth() + MineBoltFFA.this.getConfig().getInt("Heal-Amount"));
                        }
                    }
                    else
                    {
                        ArenaListener.heal.remove(s);
                    }
                }
            }
        }, delay, delay);
        System.out.println("MineBoltFFA version [" + getDescription().getVersion() + "] loaded");
    }

    public void onDisable()
    {
        for (String s : PlayerLocations.keySet())
        {
            Location loc = (Location)PlayerLocations.get(s);
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            String world = loc.getWorld().toString();
            String location = world + " " + Integer.toString(x) + " " + Integer.toString(y) + " " + Integer.toString(z);
            LocationSave.put(s, location);
        }
        for (String s : PlayerInventories.keySet())
        {
            ItemStack[] item = (ItemStack[])PlayerInventories.get(s);
            getConfig().set("Save." + s + ".Inventory", item);
            getConfig().set("creator.Inventory", item);
            List<String> list = getConfig().getStringList("Save-List");
            list.add(s);
            getConfig().set("Save-List", list);
        }
        for (String s : PlayerArmor.keySet())
        {
            ItemStack[] item = (ItemStack[])PlayerArmor.get(s);
            getConfig().set("Save." + s + ".Armor", item);
        }
        saveConfig();
        try
        {
            saveLocations(LocationSave, "PlayerLocations.bin");
            saveKills(KillStreak, "PlayerKills.bin");
            saveStatus(isIn, "PlayerStatus.bin");
        }
        catch (IOException e)
        {
            System.out.println("[MineBoltFFA] Error saving player data!");
        }
        System.out.println("MineBoltFFA version [" + getDescription().getVersion() + "] unloaded");
    }

    public static void saveInventories(HashMap<String, ItemStack[]> PlayerInventories, String path)
            throws IOException
    {
        System.out.println("[MineBoltFFA] Saving player data file...");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/MineBolt/PlayerInventories.bin"));
            oos.writeObject(PlayerInventories);
            oos.flush();
            oos.close();
            System.out.println("[MineBoltFFA] Player data file successfully saved.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file not found. Generating file...");
        }
    }

    public static void saveArmor(HashMap<String, ItemStack[]> PlayerArmor, String path)
            throws IOException
    {
        System.out.println("[MineBoltFFA] Saving player data file...");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/MineBolt/PlayerArmor.bin"));
            oos.writeObject(PlayerArmor);
            oos.flush();
            oos.close();
            System.out.println("[MineBoltFFA] Player data file successfully saved.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file not found. Generating file...");
        }
    }

    public static void saveKills(HashMap<String, Integer> KillStreak, String path)
            throws IOException
    {
        System.out.println("[MineBoltFFA] Saving player data file...");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/MineBolt/PlayerKills.bin"));
            oos.writeObject(KillStreak);
            oos.flush();
            oos.close();
            System.out.println("[MineBoltFFA] Player data file successfully saved.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file not found. Generating file...");
        }
    }

    public static void saveLocations(HashMap<String, String> LocationSave, String path)
            throws IOException
    {
        System.out.println("[MineBoltFFA] Saving player data file...");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/MineBolt/PlayerLocations.bin"));
            oos.writeObject(LocationSave);
            oos.flush();
            oos.close();
            System.out.println("[MineBoltFFA] Player data file successfully saved.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file not found. Generating file...");
        }
    }

    public static void saveStatus(HashMap<String, String> isIn, String path)
            throws IOException
    {
        System.out.println("[MineBoltFFA] Saving player data file...");
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins/MineBolt/PlayerStatus.bin"));
            oos.writeObject(isIn);
            oos.flush();
            oos.close();
            System.out.println("[MineBoltFFA] Player data file successfully saved.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[MineBoltFFA] Player data file not found. Generating file...");
        }
    }

    public static HashMap<String, ItemStack[]> loadInventories(String path)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins/MineBolt/PlayerInventories.bin"));
        Object result = ois.readObject();
        ois.close();
        return (HashMap)result;
    }

    public static HashMap<String, String> loadLocations(String path)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins/MineBolt/PlayerLocations.bin"));
        Object result = ois.readObject();
        ois.close();
        return (HashMap)result;
    }

    public static HashMap<String, Integer> loadKills(String path)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins/MineBolt/PlayerKills.bin"));
        Object result = ois.readObject();
        ois.close();
        return (HashMap)result;
    }

    public static HashMap<String, String> loadStatus(String path)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins/MineBolt/PlayerStatus.bin"));
        Object result = ois.readObject();
        ois.close();
        return (HashMap)result;
    }

    public static HashMap<String, ItemStack[]> loadArmor(String path)
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins/MineBolt/PlayerArmor.bin"));
        Object result = ois.readObject();
        ois.close();
        return (HashMap)result;
    }

    public void loadConfig()
    {
        FileConfiguration cfg = getConfig();
        FileConfigurationOptions cfgOptions = cfg.options();
        List<String> list = new ArrayList();
        cfg.addDefault("Arenas", list);
        cfg.addDefault("Delay-Time", Integer.valueOf(10));
        cfg.addDefault("Heal-Amount", Integer.valueOf(1));
        cfg.addDefault("Heal-Delay", Integer.valueOf(3));
        if (!getConfig().contains("KillStreak"))
        {
            List<String> l = new ArrayList();
            l.add("276 1");
            l.add("275 1");
            l.add("322 2");
            l.add(Integer.toString(Material.IRON_SWORD.getId()) + " 1");
            cfg.addDefault("KillStreak.5.Items", l);
            List<String> llss = new ArrayList();
            llss.add("276 1");
            cfg.addDefault("KillStreak.5.Remove", llss);
            cfg.addDefault("Arena", "");
            cfg.addDefault("KillStreak.5.Message", "&bYou have been awarded&a 1&b iron sword and &a 1&b stone axe and &a 2&b golden apples.");
            List<String> ls = new ArrayList();
            cfg.addDefault("KillStreak.10.Armor", Integer.toString(Material.DIAMOND_HELMET.getId()) + " " + Integer.toString(Material.DIAMOND_CHESTPLATE.getId()) + " " + Integer.toString(Material.DIAMOND_LEGGINGS.getId()) + " " + Integer.toString(Material.DIAMOND_BOOTS.getId()));
            ls.add(Integer.toString(Material.IRON_AXE.getId()) + " 1");
            ls.add(Integer.toString(Material.BOW.getId()) + " 1");
            ls.add(Integer.toString(Material.ARROW.getId()) + " 64");
            ls.add(Integer.toString(Material.GOLDEN_APPLE.getId()) + " 3");
            ls.add(Integer.toString(Material.DIAMOND_SWORD.getId()) + " 1");
            cfg.addDefault("KillStreak.10.Items", ls);
            List<String> llll = new ArrayList();
            llll.add("275 1");
            llll.add(Integer.toString(Material.IRON_SWORD.getId()) + " 1");
            cfg.addDefault("KillStreak.10.Remove", llll);
            cfg.addDefault("KillStreak.10.Message", "&bYou have been awarded&a diamond armor&b, Swiftness 2,&a 1&b diamond sword,&a 1&b iron axe,&a 1&b bow,&a 64&b arrows, and&a 3&b gold apples.");
            List<String> lst = new ArrayList();
            lst.add("speed 180");
            cfg.addDefault("KillStreak.10.Potions", lst);
            List<String> ll = new ArrayList();
            ll.add(Integer.toString(Material.GOLDEN_APPLE.getId()) + " 6");
            ll.add(Integer.toString(Material.DIAMOND_AXE.getId()) + " 1h");
            cfg.addDefault("KillStreak.15.Items", ll);
            List<String> lllll = new ArrayList();
            lllll.add(Integer.toString(Material.IRON_AXE.getId()) + " 1");
            cfg.addDefault("KillStreak.15.Remove", lllll);
            List<String> lls = new ArrayList();
            lls.add("speed 180");
            cfg.addDefault("KillStreak.15.Potions", lls);
            cfg.addDefault("KillStreak.15.Message", "&bYou have been awarded&a 6 golden apples&b and&a Swiftness 2&a.");
        }
        List<String> liiist = new ArrayList();
        cfg.addDefault("Save-List", liiist);
        cfgOptions.copyDefaults(true);
        cfgOptions.header("This is the MineBoltFFA configuration file." + System.getProperty("line.separator"));
        cfgOptions.copyHeader(true);
        saveConfig();
    }
}
