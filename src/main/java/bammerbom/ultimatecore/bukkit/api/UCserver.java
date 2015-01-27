/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.bukkit.api;

import bammerbom.ultimatecore.bukkit.UltimateFileLoader;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.util.*;

public class UCserver {

    static HashMap<String, Location> jails = null;
    static String motd = "";

    static {
        try {
            File file = new File(r.getUC().getDataFolder(), "motd.txt");
            if (!file.exists()) {
                r.getUC().saveResource("motd.txt", true);
            }
            ArrayList<String> lines = FileUtil.getLines(file);
            for (String str : lines) {
                motd = motd + ChatColor.translateAlternateColorCodes('&', str) + "\n";
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to load MOTD");
        }
    }

    //Ban
    public List<OfflinePlayer> getBannedOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isBanned()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getBannedOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isBanned()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Deaf
    public List<OfflinePlayer> getDeafOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isDeaf()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getDeafOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isDeaf()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Enchantingtable
    public List<OfflinePlayer> getInCommandEnchantingtablePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isInCommandEnchantingtable()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInCommandEnchantingtableOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInCommandEnchantingtable()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Freeze
    public List<OfflinePlayer> getFrozenOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isFrozen()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getFrozenOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isFrozen()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //God
    public List<OfflinePlayer> getGodOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isGod()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getGodOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isGod()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Invsee
    public List<Player> getInOnlineInventoryOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInOnlineInventory()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInOfflineInventoryOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInOfflineInventory()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Jails
    public HashMap<String, Location> getJails() {
        if (jails == null) {
            jails = new HashMap<String, Location>();
            for (String s : new Config(UltimateFileLoader.DFjails).getKeys(true)) {
                Location loc = LocationUtil.convertStringToLocation(new Config(UltimateFileLoader.DFjails).getString(s));
                jails.put(s, loc);
            }
        }
        return jails;
    }

    public ArrayList<String> getJailsL() {
        return new ArrayList<>(getJails().keySet());
    }

    public void addJail(String n, Location l) {
        if (getJails().containsKey(n)) {
            jails.remove(n);
        }
        jails.put(n, l);
        Config c = new Config(UltimateFileLoader.DFjails);
        c.set(n, LocationUtil.convertLocationToString(l));
        c.save();
    }

    public Location getJail(String n) {
        if (!getJails().containsKey(n)) {
            return null;
        }
        return getJails().get(n);
    }

    public void removeJail(String n) {
        getJails();
        jails.remove(n);
        Config c = new Config(UltimateFileLoader.DFjails);
        c.set(n, null);
        c.save();
    }

    public ArrayList<Player> getOnlineJailed() {
        ArrayList<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isJailed()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public ArrayList<OfflinePlayer> getOfflineJailed() {
        ArrayList<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isJailed()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Motd
    public String getMotd() {
        String mt = motd;
        StringBuilder b = new StringBuilder();
        Integer i = 0;
        for (Player pl : r.getOnlinePlayers()) {
            i++;
            if (!StringUtil.nullOrEmpty(b.toString())) {
                b.append(", ");
            }
            b.append(UC.getPlayer(pl).getNick());
        }
        mt = mt.replace("{ONLINE}", b.toString());
        mt = mt.replace("{PLAYERS}", b.toString());
        mt = mt.replace("{PLAYERLIST}", b.toString());
        mt = mt.replace("{TIME}", DateFormat.getTimeInstance(2, Locale.getDefault()).format(new Date()));
        mt = mt.replace("{DATE}", DateFormat.getDateInstance(2, Locale.getDefault()).format(new Date()));
        mt = mt.replace("{TPS}", PerformanceUtil.getTps() + "");
        mt = mt.replace("{UPTIME}", ChatColor.stripColor(DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
        StringBuilder pb = new StringBuilder();
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!StringUtil.nullOrEmpty(pb.toString())) {
                pb.append(", ");
            }
            pb.append(pl.getDescription().getName());
        }
        mt = mt.replace("{PLAYERCOUNT}", i + "");
        mt = mt.replace("{PLUGINS}", pb.toString());
        mt = mt.replace("{VERSION}", Bukkit.getServer().getVersion());
        mt = mt.replace("{WORLD}", ChatColor.stripColor(r.mes("notAvailable")));
        mt = mt.replace("{WORLDNAME}", ChatColor.stripColor(r.mes("notAvailable")));
        mt = mt.replace("{COORDS}", ChatColor.stripColor(r.mes("notAvailable")));
        mt = mt.replace("{PLAYER}", ChatColor.stripColor(r.mes("notAvailable")));
        mt = mt.replace("{NAME}", ChatColor.stripColor(r.mes("notAvailable")));
        mt = mt.replace("{RAWNAME}", ChatColor.stripColor(r.mes("notAvailable")));
        return motd;
    }

    public String getMotd(Player p) {
        String mt = motd;
        mt = mt.replace("{PLAYER}", UC.getPlayer(p).getNick());
        mt = mt.replace("{NAME}", UC.getPlayer(p).getNick());
        mt = mt.replace("{RAWNAME}", p.getName());
        if (p instanceof Player) {
            Player pl = (Player) p;
            mt = mt.replace("{WORLD}", pl.getWorld().getName());
            mt = mt.replace("{WORLDNAME}", pl.getWorld().getName());
            mt = mt.replace("{COORDS}", pl.getLocation().getBlockX() + ", " + pl.getLocation().getBlockY() + ", " + pl.getLocation().getBlockZ());
        } else {
            mt = mt.replace("{WORLD}", ChatColor.stripColor(r.mes("notAvailable")));
            mt = mt.replace("{WORLDNAME}", ChatColor.stripColor(r.mes("notAvailable")));
            mt = mt.replace("{COORDS}", ChatColor.stripColor(r.mes("notAvailable")));
        }
        StringBuilder b = new StringBuilder();
        Integer i = 0;
        for (Player pl : r.getOnlinePlayers()) {
            if (p instanceof Player && !((Player) p).canSee(pl)) {
                continue;
            }
            i++;
            if (!StringUtil.nullOrEmpty(b.toString())) {
                b.append(", ");
            }
            b.append(UC.getPlayer(pl).getNick());
        }
        mt = mt.replace("{ONLINE}", b.toString());
        mt = mt.replace("{PLAYERCOUNT}", i + "");
        mt = mt.replace("{PLAYERS}", b.toString());
        mt = mt.replace("{PLAYERLIST}", b.toString());
        mt = mt.replace("{TIME}", DateFormat.getTimeInstance(2, Locale.getDefault()).format(new Date()));
        mt = mt.replace("{DATE}", DateFormat.getDateInstance(2, Locale.getDefault()).format(new Date()).replace("-", " "));
        mt = mt.replace("{TPS}", PerformanceUtil.getTps() + "");
        mt = mt.replace("{UPTIME}", ChatColor.stripColor(DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
        StringBuilder pb = new StringBuilder();
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!StringUtil.nullOrEmpty(pb.toString())) {
                pb.append(", ");
            }
            pb.append(pl.getDescription().getName());
        }
        mt = mt.replace("{PLUGINS}", pb.toString());
        mt = mt.replace("{VERSION}", Bukkit.getServer().getVersion());
        return mt;
    }
    //Spawn
    static Location spawn = null;

    public void setSpawn(Location loc) {
        spawn = loc;
        String s = LocationUtil.convertLocationToString(loc);
        Config conf = new Config(UltimateFileLoader.DFspawns);
        conf.set("global", s);
        conf.save();
    }

    public Location getSpawn() {
        if (spawn == null) {
            if (!new Config(UltimateFileLoader.DFspawns).contains("global")) {
                return null;
            }
            String s = new Config(UltimateFileLoader.DFspawns).getString("global");
            Location loc = LocationUtil.convertStringToLocation(s);
            spawn = loc;
            return loc;
        }
        return spawn;
    }

    public List<OfflinePlayer> getInTeleportMenuOffline() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isInTeleportMenu()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInTeleportMenuOnline() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInTeleportMenu()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Receiver, Sender
    static Map<UUID, UUID> tp = new HashMap<>();
    static Map<UUID, UUID> tph = new HashMap<>();

    public Map<UUID, UUID> getTeleportRequests() {
        return tp;
    }

    public Map<UUID, UUID> getTeleportHereRequests() {
        return tph;
    }

    public void addTeleportRequest(UUID u1, UUID u2) {
        tp.put(u1, u2);
    }

    public void addTeleportHereRequest(UUID u1, UUID u2) {
        tph.put(u1, u2);
    }

    public void removeTeleportRequest(UUID u) {
        tp.remove(u);
    }

    public void removeTeleportHereRequest(UUID u) {
        tph.remove(u);
    }

    //Vanish
    //Deaf
    public List<OfflinePlayer> getVanishOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<OfflinePlayer>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isVanish()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getVanishOnlinePlayers() {
        List<Player> pls = new ArrayList<Player>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isVanish()) {
                pls.add(pl);
            }
        }
        return pls;
    }
}
