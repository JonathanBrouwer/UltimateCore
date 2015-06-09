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
package bammerbom.ultimatecore.spongeapi_old.api;

import bammerbom.ultimatecore.spongeapi_old.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi_old.configuration.Config;
import bammerbom.ultimatecore.spongeapi_old.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi_old.listeners.AutomessageListener;
import bammerbom.ultimatecore.spongeapi_old.r;
import bammerbom.ultimatecore.spongeapi_old.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi_old.resources.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.util.*;

public class UServer {

    static HashMap<String, Location> jails = null;
    static String motd = "";

    //Receiver, Sender
    static Map<UUID, UUID> tp = new HashMap<>();
    static Map<UUID, UUID> tph = new HashMap<>();
    //Warps
    static HashMap<String, Location> warps = null;
    //Silence
    static Boolean silence = null;
    static Long silencetime = null;

    public static void start() {
        if (!StringUtil.nullOrEmpty(motd)) {
            motd = "";
        }
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
    public List<OfflinePlayer> getBannedOnlinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        BanList bans = Bukkit.getBanList(BanList.Type.NAME);
        for (BanEntry en : bans.getBanEntries()) {
            OfflinePlayer p = r.searchOfflinePlayer(en.getTarget());
            if (p.isOnline()) {
                pls.add(p);
            }
        }
        return pls;
    }

    public List<OfflinePlayer> getBannedOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        BanList bans = Bukkit.getBanList(BanList.Type.NAME);
        for (BanEntry en : bans.getBanEntries()) {
            pls.add(r.searchOfflinePlayer(en.getTarget()));
        }
        return pls;
    }

    //Deaf
    public List<OfflinePlayer> getDeafOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isDeaf()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getDeafOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isDeaf()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Mutes
    public List<OfflinePlayer> getMutedOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isMuted()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getMutedOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isMuted()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Jails
    public List<OfflinePlayer> getJailedOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isJailed()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getJailedOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isJailed()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Enchantingtable
    public List<OfflinePlayer> getInCommandEnchantingtablePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isInCommandEnchantingtable()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInCommandEnchantingtableOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInCommandEnchantingtable()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Freeze
    public List<OfflinePlayer> getFrozenOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isFrozen()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getFrozenOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isFrozen()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //God
    public List<OfflinePlayer> getGodOfflinePlayers() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isGod()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getGodOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isGod()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    //Invsee
    public List<Player> getInOnlineInventoryOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInOnlineInventory()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInOfflineInventoryOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
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
            jails = new HashMap<>();
            for (String s : new Config(UltimateFileLoader.Djails).getKeys(true)) {
                Location loc = LocationUtil.convertStringToLocation(new Config(UltimateFileLoader.Djails).getString(s));
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
        JsonConfig c = new JsonConfig(UltimateFileLoader.Djails);
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
        JsonConfig c = new JsonConfig(UltimateFileLoader.Djails);
        c.set(n, null);
        c.save();
    }

    public ArrayList<Player> getOnlineJailed() {
        ArrayList<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isJailed()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public ArrayList<OfflinePlayer> getOfflineJailed() {
        ArrayList<OfflinePlayer> pls = new ArrayList<>();
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
        StringBuilder b = new StringBuilder("");
        Integer i = 0;
        for (Player pl : r.getOnlinePlayers()) {
            i++;
            if (!StringUtil.nullOrEmpty(b.toString())) {
                b.append(", ");
            }
            b.append(UC.getPlayer(pl).getDisplayName());
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
        return mt;
    }

    public String getMotd(Player p) {
        String mt = motd;
        mt = mt.replace("{PLAYER}", UC.getPlayer(p).getDisplayName());
        mt = mt.replace("{NAME}", UC.getPlayer(p).getDisplayName());
        mt = mt.replace("{RAWNAME}", p.getName());
        mt = mt.replace("{WORLD}", p.getWorld().getName());
        mt = mt.replace("{WORLDNAME}", p.getWorld().getName());
        mt = mt.replace("{COORDS}", p.getLocation().getBlockX() + ", " + p.getLocation().getBlockY() + ", " +
                p.getLocation().getBlockZ());
        StringBuilder b = new StringBuilder();
        Integer i = 0;
        for (Player pl : r.getOnlinePlayers()) {
            if (p instanceof Player && !p.canSee(pl)) {
                continue;
            }
            i++;
            if (!StringUtil.nullOrEmpty(b.toString())) {
                b.append(", ");
            }
            b.append(UC.getPlayer(pl).getDisplayName());
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

    public List<OfflinePlayer> getInTeleportMenuOffline() {
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isInTeleportMenu()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getInTeleportMenuOnline() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isInTeleportMenu()) {
                pls.add(pl);
            }
        }
        return pls;
    }

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
        List<OfflinePlayer> pls = new ArrayList<>();
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            if (UC.getPlayer(pl).isVanish()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<Player> getVanishOnlinePlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isVanish()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public HashMap<String, Location> getWarps() {
        if (warps != null) {
            return warps;
        }
        warps = new HashMap<>();
        JsonConfig conf = new JsonConfig(UltimateFileLoader.Dwarps);
        if (!conf.contains("warps")) {
            return warps;
        }
        for (String hname : conf.listKeys("warps", false)) {
            warps.put(hname, LocationUtil.convertStringToLocation(conf.getString("warps." + hname)));
        }
        return warps;
    }

    public void setWarps(HashMap<String, Location> nh) {
        warps = nh;
        JsonConfig conf = new JsonConfig(UltimateFileLoader.Dwarps);
        conf.set("warps", null);
        for (String s : nh.keySet()) {
            conf.set("warps." + s, LocationUtil.convertLocationToString(nh.get(s.toLowerCase())));
        }
        conf.save();
    }

    public ArrayList<String> getWarpNames() {
        ArrayList<String> h = new ArrayList<>();
        h.addAll(getWarps().keySet());
        return h;
    }

    public void addWarp(String s, Location l) {
        HashMap<String, Location> h = getWarps();
        h.put(s.toLowerCase(), l);
        setWarps(h);
    }

    public void removeWarp(String s) {
        HashMap<String, Location> h = getWarps();
        h.remove(s.toLowerCase());
        setWarps(h);
    }

    public Location getWarp(String s) {
        for (String w : getWarps().keySet()) {
            if (w.equalsIgnoreCase(s)) {
                return getWarps().get(w);
            }
        }
        return null;
    }

    public void clearWarps() {
        setWarps(new HashMap<String, Location>());
    }

    public List<Player> getAfkPlayers() {
        List<Player> pls = new ArrayList<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isAfk()) {
                pls.add(pl);
            }
        }
        return pls;
    }

    public List<String> getAutomessageMessages() {
        return AutomessageListener.messages;
    }

    public String getAutomessageCurrentmessage() {
        return AutomessageListener.currentmessage;
    }

    public UKit getKit(String s) {
        return new UKit(s);
    }

    public List<UKit> getKits() {
        ArrayList<String> kitnames = new ArrayList<>(new Config(UltimateFileLoader.Dkits).getKeys(false));
        ArrayList<UKit> kits = new ArrayList<>();
        for (String s : kitnames) {
            kits.add(getKit(s));
        }
        return kits;
    }

    public List<String> getKitNames() {
        return new ArrayList<>(new Config(UltimateFileLoader.Dkits).getKeys(false));
    }

    public boolean isDebug() {
        return r.isDebug();
    }

    public void setDebug(Boolean value) {
        r.setDebug(value);
    }

    public JsonConfig getGlobalConfig() {
        return new JsonConfig(UltimateFileLoader.Dglobal);
    }

    public boolean isSilenced() {
        if (silence == null) {
            silence = getGlobalConfig().getBoolean("silence");
            if (silence == null) {
                silence = false;
            }
        }
        if (getSilenceTime() >= 1 && getSilenceTimeLeft() <= 1 && silence) {
            setSilenced(false);
            //TODO broadcast unsilence message r.sendMes(getOnlinePlayer(), "unsilenceTarget");
            return false;
        }
        return silence;
    }

    public void setSilenced(boolean s) {
        silence = s;
        JsonConfig c = getGlobalConfig();
        c.set("silence", s);
        c.set("silencetime", null);
        c.save();
    }

    public Long getSilenceTime() {
        if (silencetime != null) {
            return silencetime;
        }
        if (!getGlobalConfig().contains("silencetime")) {
            return 0L;
        }
        silencetime = getGlobalConfig().getLong("silencetime");
        return getGlobalConfig().getLong("silencetime");

    }

    public Long getSilenceTimeLeft() {
        return getSilenceTime() - System.currentTimeMillis();
    }

    public void setSilenced(Boolean fr, Long time) {
        JsonConfig conf = getGlobalConfig();
        if (silencetime == null || silencetime == 0L) {
            silencetime = -1L;
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("silence", fr);
        conf.set("silencetime", time);
        conf.save();
        silence = fr;
        silencetime = fr ? time : -1L;
    }

    public Location getGlobalSpawn() {
        if (!new JsonConfig(UltimateFileLoader.Dspawns).contains("global")) {
            return null;
        }
        String s = new JsonConfig(UltimateFileLoader.Dspawns).getString("global");
        Location loc = LocationUtil.convertStringToLocation(s);
        return loc;
    }

    public void setSpawn(Location loc, Boolean world, String group, Boolean firstjoin) {
        String path = "global";
        if (firstjoin) {
            path = "global.firstjoin";
        } else if (world && StringUtil.nullOrEmpty(group)) {
            path = "worlds.world." + loc.getWorld().getName() + ".global";
        } else if (!StringUtil.nullOrEmpty(group) && !world) {
            path = "global.group." + group;
        } else if (!StringUtil.nullOrEmpty(group) && world) {
            path = "worlds.world." + loc.getWorld().getName() + ".group." + group;
        }
        String s = LocationUtil.convertLocationToString(loc);
        JsonConfig conf = new JsonConfig(UltimateFileLoader.Dspawns);
        conf.set(path, s);
        conf.save();
    }

    public boolean delSpawn(World world, String group, Boolean firstjoin) {
        String path = "global";
        if (firstjoin) {
            path = "global.firstjoin";
        } else if (!StringUtil.nullOrEmpty(world) && StringUtil.nullOrEmpty(group)) {
            path = "worlds.world." + world.getName() + ".global";
        } else if (!StringUtil.nullOrEmpty(group) && StringUtil.nullOrEmpty(world)) {
            path = "global.group." + group;
        } else if (!StringUtil.nullOrEmpty(group) && !StringUtil.nullOrEmpty(world)) {
            path = "worlds.world." + world.getName() + ".group." + group;
        }
        JsonConfig conf = new JsonConfig(UltimateFileLoader.Dspawns);
        if (conf.contains(path)) {
            conf.set(path, null);
            conf.save();
            return true;
        } else {
            return false;
        }
    }


}
