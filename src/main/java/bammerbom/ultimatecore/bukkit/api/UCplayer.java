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
import bammerbom.ultimatecore.bukkit.commands.CmdEnchantingtable;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.InventoryUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import org.bukkit.*;
import org.bukkit.BanList.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.*;

public class UCplayer {

    static Random ra = new Random();
    String name = null;
    UUID uuid = null;
    //Back last location
    Location lastLocation = null;
    //Ban
    Boolean banned = null;
    Long bantime = null;
    String banreason = null;
    //Deaf
    Boolean deaf = null;
    Long deaftime = null;
    //Freeze
    Boolean freeze = null;
    Long freezetime = null;
    //God
    Boolean god = null;
    Long godtime = null;
    //Home
    HashMap<String, Location> homes = null;
    //Inventory
    Boolean onlineInv = false;
    Boolean offlineInv = false;
    //Jail
    Boolean jailed = null;
    Long jailtime = null;
    String jail = null;
    //Reply
    UUID reply = null;
    //Spy
    Boolean spy = null;
    //Mute
    Boolean mute = null;
    Long mutetime = null;
    //Nick
    String nickname = null;
    //Powertool
    Boolean pte = null;
    HashMap<Material, List<String>> pts = null;
    Boolean inRecipeView = false;

    public UCplayer(OfflinePlayer p) {
        name = p.getName();
        uuid = p.getUniqueId();
    }

    public UCplayer(UUID uuid) {
        OfflinePlayer p = r.searchOfflinePlayer(uuid);
        name = p.getName();
        this.uuid = p.getUniqueId();
    }

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public Player getOnlinePlayer() {
        if (!getPlayer().isOnline()) {
            return null;
        }
        return getPlayer().getPlayer();
    }

    //Last connect
    public long getLastConnectMillis() {
        final Config conf = getPlayerConfig();
        if (conf.get("lastconnect") != null) {
            return conf.getLong("lastconnect");
        } else {
            return getPlayer().getLastPlayed();
        }
    }

    public void updateLastConnectMillis() {
        final Config conf = getPlayerConfig();
        conf.set("lastconnect", System.currentTimeMillis());
        conf.save();
    }

    //Configuration
    public Config getPlayerConfig() {
        return UltimateFileLoader.getPlayerConfig(getPlayer());
    }

    public File getPlayerFile() {
        return UltimateFileLoader.getPlayerFile(getPlayer());
    }

    public void setLastLocation() {
        if (!getPlayer().isOnline()) {
            return;
        }
        setLastLocation(getOnlinePlayer().getLocation());
    }

    public Location getLastLocation() {
        if (lastLocation == null) {
            return LocationUtil.convertStringToLocation(getPlayerConfig().getString("lastlocation"));
        }
        return lastLocation;
    }

    public void setLastLocation(Location loc) {
        lastLocation = loc;
        Config conf = getPlayerConfig();
        conf.set("lastlocation", loc == null ? null : LocationUtil.convertLocationToString(loc));
        conf.save();
    }

    public boolean isBanned() {
        if (!getPlayerConfig().contains("banned")) {
            banned = false;
            return false;
        }
        if (getBanTime() >= 1 && getBanTimeLeft() <= 1 && getPlayerConfig().getBoolean("banned")) {
            unban();
            return false;
        }
        if (banned != null) {
            return banned;
        }
        BanList list = Bukkit.getBanList(Type.NAME);
        if (getPlayer() == null || getPlayer().getName() == null) {
            return false;
        }
        banned = getPlayerConfig().getBoolean("banned") || list.isBanned(getPlayer().getName());
        return getPlayerConfig().getBoolean("banned") || list.isBanned(getPlayer().getName());
    }

    public Long getBanTime() {
        if (bantime != null) {
            return bantime;
        }
        if (!getPlayerConfig().contains("bantime")) {
            return 0L;
        }
        bantime = getPlayerConfig().getLong("bantime");
        return getPlayerConfig().getLong("bantime");

    }

    public Long getBanTimeLeft() {
        return getBanTime() - System.currentTimeMillis();
    }

    public String getBanReason() {
        if (!getPlayerConfig().contains("banreason")) {
            return "";
        }
        banreason = getPlayerConfig().getString("banreason");
        return getPlayerConfig().getString("banreason");
    }

    public void unban() {
        banned = false;
        bantime = 0L;
        banreason = "";
        Config conf = getPlayerConfig();
        conf.set("banned", false);
        conf.set("bantime", null);
        conf.set("banreason", null);
        conf.save();
        BanList list = Bukkit.getBanList(Type.NAME);
        if (list.isBanned(getPlayer().getName())) {
            list.pardon(getPlayer().getName());
        }
    }

    public void ban(Long time, String reason) {
        Config conf = getPlayerConfig();
        if (time == null || time == 0L) {
            time = -1L;
        }
        if (reason == null) {
            reason = r.mes("banDefaultReason");
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("banned", true);
        conf.set("bantime", time);
        conf.set("banreason", reason);
        conf.save();
        BanList list = Bukkit.getBanList(Type.NAME);
        list.addBan(getPlayer().getName(), reason, null, "");
    }

    public void ban(Long time) {
        ban(time, null);
    }

    public void ban(String reason) {
        ban(null, reason);
    }

    public void ban() {
        ban(null, null);
    }

    public boolean isDeaf() {
        if (!getPlayerConfig().contains("deaf")) {
            deaf = false;
            return false;
        }
        if (getDeafTime() >= 1 && getDeafTimeLeft() <= 1 && getPlayerConfig().getBoolean("deaf")) {
            setDeaf(false);
            if (getPlayer().isOnline()) {
                r.sendMes(getOnlinePlayer(), "undeafTarget");
            }
            return false;
        }
        if (deaf != null) {
            return deaf;
        }
        deaf = getPlayerConfig().getBoolean("deaf");
        return getPlayerConfig().getBoolean("deaf");
    }

    public void setDeaf(Boolean deaf) {
        setDeaf(deaf, -1L);
    }

    public Long getDeafTime() {
        if (deaftime != null) {
            return deaftime;
        }
        if (!getPlayerConfig().contains("deaftime")) {
            return 0L;
        }
        deaftime = getPlayerConfig().getLong("deaftime");
        return getPlayerConfig().getLong("deaftime");

    }

    public Long getDeafTimeLeft() {
        return getDeafTime() - System.currentTimeMillis();
    }

    public void setDeaf(Boolean dea, Long time) {
        Config conf = getPlayerConfig();
        if (deaftime == null || deaftime == 0L) {
            deaftime = -1L;
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("deaf", dea);
        conf.set("deaftime", time);
        conf.save();
        deaf = dea;
        deaftime = deaf ? time : 0L;
    }

    //Enchantingtable
    public boolean isInCommandEnchantingtable() {
        return CmdEnchantingtable.inCmd.contains(uuid);
    }

    public boolean isFrozen() {
        if (!getPlayerConfig().contains("freeze")) {
            freeze = false;
            return false;
        }
        if (getFrozenTime() >= 1 && getFrozenTimeLeft() <= 1 && getPlayerConfig().getBoolean("freeze")) {
            setFrozen(false);
            if (getPlayer().isOnline()) {
                r.sendMes(getOnlinePlayer(), "unfreezeTarget");
            }
            return false;
        }
        if (freeze != null) {
            return freeze;
        }
        freeze = getPlayerConfig().getBoolean("freeze");
        return getPlayerConfig().getBoolean("freeze");
    }

    public void setFrozen(Boolean fr) {
        setFrozen(fr, -1L);
    }

    public Long getFrozenTime() {
        if (freezetime != null) {
            return freezetime;
        }
        if (!getPlayerConfig().contains("freezetime")) {
            return 0L;
        }
        freezetime = getPlayerConfig().getLong("freezetime");
        return getPlayerConfig().getLong("freezetime");

    }

    public Long getFrozenTimeLeft() {
        return getFrozenTime() - System.currentTimeMillis();
    }

    public void setFrozen(Boolean fr, Long time) {
        Config conf = getPlayerConfig();
        if (freezetime == null || freezetime == 0L) {
            freezetime = -1L;
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("freeze", fr);
        conf.set("freezetime", time);
        conf.save();
        freeze = fr;
        ;
        freezetime = fr ? time : 0L;
    }

    public boolean isGod() {
        if (!getPlayerConfig().contains("god")) {
            god = false;
            return false;
        }
        if (getGodTime() >= 1 && getGodTimeLeft() <= 1 && getPlayerConfig().getBoolean("god")) {
            setGod(false);
            if (getPlayer().isOnline()) {
                r.sendMes(getOnlinePlayer(), "ungodTarget");
            }
            return false;
        }
        if (god != null) {
            return god;
        }
        god = getPlayerConfig().getBoolean("god");
        return getPlayerConfig().getBoolean("god");
    }

    public void setGod(Boolean fr) {
        setGod(fr, -1L);
    }

    public Long getGodTime() {
        if (godtime != null) {
            return godtime;
        }
        if (!getPlayerConfig().contains("godtime")) {
            return 0L;
        }
        godtime = getPlayerConfig().getLong("godtime");
        return getPlayerConfig().getLong("godtime");

    }

    public Long getGodTimeLeft() {
        return getGodTime() - System.currentTimeMillis();
    }

    public void setGod(Boolean fr, Long time) {
        Config conf = getPlayerConfig();
        if (godtime == null || godtime == 0L) {
            godtime = -1L;
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("god", fr);
        conf.set("godtime", time);
        conf.save();
        god = fr;
        godtime = fr ? time : 0L;
    }

    public HashMap<String, Location> getHomes() {
        if (homes != null) {
            return homes;
        }
        homes = new HashMap<String, Location>();
        Config conf = getPlayerConfig();
        if (!conf.contains("homes")) {
            return homes;
        }
        for (String hname : conf.getConfigurationSection("homes").getKeys(false)) {
            homes.put(hname, LocationUtil.convertStringToLocation(conf.getString("homes." + hname)));
        }
        return homes;
    }

    public void setHomes(HashMap<String, Location> nh) {
        homes = nh;
        Config conf = getPlayerConfig();
        conf.set("homes", null);
        for (String s : nh.keySet()) {
            conf.set("homes." + s, LocationUtil.convertLocationToString(nh.get(s.toLowerCase())));
        }
        conf.save();
    }

    public ArrayList<String> getHomeNames() {
        ArrayList<String> h = new ArrayList<>();
        h.addAll(getHomes().keySet());
        return h;
    }

    public void addHome(String s, Location l) {
        HashMap<String, Location> h = getHomes();
        h.put(s.toLowerCase(), l);
        setHomes(h);
    }

    public void removeHome(String s) {
        HashMap<String, Location> h = getHomes();
        h.remove(s.toLowerCase());
        setHomes(h);
    }

    public Location getHome(String s) {
        return getHomes().get(s.toLowerCase());
    }

    public void clearHomes() {
        setHomes(new HashMap<String, Location>());
    }

    public boolean isInOnlineInventory() {
        return onlineInv;
    }

    public void setInOnlineInventory(Boolean b) {
        onlineInv = b;
    }

    public boolean isInOfflineInventory() {
        return offlineInv;
    }

    public void setInOfflineInventory(Boolean b) {
        offlineInv = b;
    }

    public void updateLastInventory() {
        Config conf = getPlayerConfig();
        conf.set("lastinventory", InventoryUtil.convertInventoryToString(getOnlinePlayer().getInventory()));
        conf.save();
    }

    public Inventory getLastInventory() {
        Config conf = getPlayerConfig();
        if (!conf.contains("lastinventory")) {
            return null;
        }
        return InventoryUtil.convertStringToInventory(conf.getString("lastinventory"), r.mes("inventoryOfflineTitle", "%Name", name));
    }

    public void jail() {
        jail(null, null);
    }

    public void jail(String n) {
        jail(n, null);
    }

    public void jail(Long l) {
        jail(new ArrayList<>(UC.getServer().getJails().keySet()).get(ra.nextInt(UC.getServer().getJails().keySet().size())), l);
    }

    public void jail(String n, Long l) {
        jailed = true;
        jail = n;
        if (l >= 1) {
            l = l + System.currentTimeMillis();
        }
        jailtime = l;
        Config conf = getPlayerConfig();
        conf.set("jailed", true);
        conf.set("jail", n);
        conf.set("jailtime", l == null ? 0L : l);
        conf.save();
    }

    public void unjail() {
        jailed = false;
        jail = null;
        jailtime = null;
        Config conf = getPlayerConfig();
        conf.set("jailed", false);
        conf.set("jail", null);
        conf.set("jailtime", null);
        conf.save();

    }

    public boolean isJailed() {
        if (!getPlayerConfig().contains("jailed")) {
            return false;
        }
        if (getJailTimeLeft() <= 1 && getPlayerConfig().getBoolean("jailed") && !(getJailTimeLeft() <= -1)) {
            unjail();
            if (getPlayer().isOnline()) {
                r.sendMes(getOnlinePlayer(), "unjailTarget");
                unjail();
            }
            return false;
        }
        if (jailed != null) {
            return jailed;
        }
        jailed = getPlayerConfig().getBoolean("jailed");
        return getPlayerConfig().getBoolean("jailed");
    }

    public Long getJailTime() {
        if (jailtime != null) {
            return jailtime;
        }
        if (!getPlayerConfig().contains("jailtime")) {
            return 0L;
        }
        jailtime = getPlayerConfig().getLong("jailtime");
        return getPlayerConfig().getLong("jailtime");

    }

    public Long getJailTimeLeft() {
        return getJailTime() - System.currentTimeMillis();
    }

    public String getJail() {
        if (jail != null) {
            return jail;
        }
        if (!isJailed()) {
            return null;
        }
        return getPlayerConfig().getString("jail");
    }

    public Player getReply() {
        if (reply != null) {
            return Bukkit.getPlayer(reply);
        }
        if (!getPlayerConfig().contains("reply")) {
            return null;
        }
        return Bukkit.getPlayer(UUID.fromString(getPlayerConfig().getString("reply")));
    }

    public void setReply(Player pl) {
        reply = pl.getUniqueId();
        Config conf = getPlayerConfig();
        conf.set("reply", pl.getUniqueId().toString());
        conf.save();
    }

    public boolean isSpy() {
        if (spy != null) {
            return spy;
        }
        if (!getPlayerConfig().contains("spy")) {
            return false;
        }
        return getPlayerConfig().getBoolean("spy");
    }

    public void setSpy(Boolean sp) {
        spy = sp;
        Config conf = getPlayerConfig();
        conf.set("spy", sp);
        conf.save();
    }

    public boolean isMuted() {
        if (!getPlayerConfig().contains("mute")) {
            mute = false;
            return false;
        }
        if (getMuteTime() >= 1 && getMuteTimeLeft() <= 1 && getPlayerConfig().getBoolean("mute")) {
            setMuted(false);
            if (getPlayer().isOnline()) {
                r.sendMes(getOnlinePlayer(), "unmuteTarget");
            }
            return false;
        }
        if (mute != null) {
            return mute;
        }
        mute = getPlayerConfig().getBoolean("mute");
        return getPlayerConfig().getBoolean("mute");
    }

    public void setMuted(Boolean fr) {
        setMuted(fr, -1L);
    }

    public Long getMuteTime() {
        if (mutetime != null) {
            return mutetime;
        }
        if (!getPlayerConfig().contains("mutetime")) {
            return 0L;
        }
        mutetime = getPlayerConfig().getLong("mutetime");
        return getPlayerConfig().getLong("mutetime");

    }

    public Long getMuteTimeLeft() {
        return getMuteTime() - System.currentTimeMillis();
    }

    public void setMuted(Boolean fr, Long time) {
        Config conf = getPlayerConfig();
        if (mutetime == null || mutetime == 0L) {
            mutetime = -1L;
        }
        if (time >= 1) {
            time = time + System.currentTimeMillis();
        }
        conf.set("mute", fr);
        conf.set("mutetime", time);
        conf.save();
        mute = fr;
        mutetime = fr ? time : -1L;
    }

    public String getNick() {
        if (nickname != null) {
            return nickname;
        }
        Config data = getPlayerConfig();
        if (data.get("nick") == null) {
            return null;
        }
        String nick = ChatColor.translateAlternateColorCodes('&', data.getString("nick"));
        nickname = nick + ChatColor.RESET;
        if (getPlayer().isOnline()) {
            getPlayer().getPlayer().setDisplayName(nickname.replace("&y", ""));
        }
        if (getPlayer().isOnline() && r.perm((Player) getPlayer(), "uc.chat.rainbow", false, false)) {
            nick = nick.replaceAll("&y", r.getRandomChatColor() + "");
        }
        return nick + ChatColor.RESET;
    }

    public void setNick(String str) {
        nickname = str == null ? null : str + ChatColor.RESET;
        if (str != null) {
            if (getPlayer().isOnline()) {
                getPlayer().getPlayer().setDisplayName(nickname.replace("&y", ""));
            }
        }
        if (getPlayer().isOnline()) {
            getPlayer().getPlayer().setDisplayName(nickname.replace("&y", ""));
        }
        Config data = getPlayerConfig();
        data.set("nick", str);
        data.save(UltimateFileLoader.getPlayerFile(getPlayer()));
    }

    public void clearAllPowertools() {
        for (Material mat : pts.keySet()) {
            clearPowertool(mat);
        }
        if (pts != null) {
            pts.clear(); //Just to make sure
        }
    }

    public void clearPowertool(Material mat) {
        if (pts == null) {
            Config data = getPlayerConfig();
            pts = new HashMap<Material, List<String>>();
            if (data.contains("powertool")) {
                for (String s : data.getConfigurationSection("powertool").getValues(false).keySet()) {
                    ArrayList<String> l = (ArrayList<String>) data.getStringList("powertool." + s);
                    pts.put(Material.getMaterial(s), l);
                }
            }
        }
        pts.remove(mat);
        Config data = getPlayerConfig();
        data.set("powertool." + mat.toString(), null);
        data.save();
    }

    public List<String> getPowertools(Material mat) {
        if (mat == null || mat == Material.AIR) {
            return null;
        }
        if (pts == null) {
            Config data = getPlayerConfig();
            pts = new HashMap<Material, List<String>>();
            if (data.contains("powertool")) {
                for (String s : data.getConfigurationSection("powertool").getValues(false).keySet()) {
                    ArrayList<String> l = (ArrayList<String>) data.getStringList("powertool." + s);
                    pts.put(Material.getMaterial(s), l);
                }
            }
        }
        if (pts.containsKey(mat)) {
            return new ArrayList<>(pts.get(mat));
        }
        return null;
    }

    public boolean hasPowertools() {
        if (pts == null) {
            Config data = getPlayerConfig();
            pts = new HashMap<Material, List<String>>();
            if (data.contains("powertool")) {
                for (String s : data.getConfigurationSection("powertool").getValues(false).keySet()) {
                    ArrayList<String> l = (ArrayList<String>) data.getStringList("powertool." + s);
                    pts.put(Material.getMaterial(s), l);
                }
            }
        }
        return !pts.isEmpty();
    }

    public boolean hasPowertool(Material mat) {
        if (pts == null) {
            Config data = getPlayerConfig();
            pts = new HashMap<Material, List<String>>();
            if (data.contains("powertool")) {
                for (String s : data.getConfigurationSection("powertool").getValues(false).keySet()) {
                    ArrayList<String> l = (ArrayList<String>) data.getStringList("powertool." + s);
                    pts.put(Material.getMaterial(s), l);
                }
            }
        }
        return pts.containsKey(mat);
    }

    public void setPowertool(Material mat, List<String> cmds) {
        Config data = getPlayerConfig();
        if (pts == null) {
            pts = new HashMap<Material, List<String>>();
            if (data.contains("powertool")) {
                for (String s : data.getConfigurationSection("powertool").getValues(false).keySet()) {
                    ArrayList<String> l = (ArrayList<String>) data.getStringList("powertool." + s);
                    pts.put(Material.getMaterial(s), l);
                }
            }
        }
        pts.put(mat, cmds);
        data.set("powertool." + mat.toString(), cmds);
        data.save();
    }

    public void addPowertool(Material mat, String c) {
        List<String> ps = getPowertools(mat);
        ps.add(c);
        setPowertool(mat, ps);
    }

    public void removePowertool(Material mat, String c) {
        List<String> ps = getPowertools(mat);
        if (!ps.contains(c)) {
            return;
        }
        ps.add(c);
        setPowertool(mat, ps);
    }

    public boolean isInRecipeView() {
        return inRecipeView;
    }

    public void setInRecipeView(Boolean b) {
        inRecipeView = b;
    }

}
