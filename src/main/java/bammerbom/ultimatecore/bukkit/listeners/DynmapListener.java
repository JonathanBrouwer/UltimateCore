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
package bammerbom.ultimatecore.bukkit.listeners;

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.api.UPlayer;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.DynmapWebChatEvent;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.util.*;

public class DynmapListener implements Listener {

    static Plugin dynmap;
    static DynmapAPI api;
    static MarkerAPI markerapi;
    static Layer warplayer;
    static boolean reload;
    static boolean stop = false;
    static boolean warpsE = r.getCnfg().getBoolean("Dynmap.Warps");
    static private Set<UUID> hiddenasserts = new HashSet<>();

    public static void start() {
        reload = false;
        if (r.getCnfg().getBoolean("Dynmap.Enable") && Bukkit.getPluginManager().getPlugin("dynmap") != null && Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
            dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
            api = (DynmapAPI) dynmap;
            Bukkit.getPluginManager().registerEvents(new DynmapListener(), r.getUC());
            markerapi = api.getMarkerAPI();
            if (markerapi == null) {
                return;
            }
            if (reload) {
                if (warpsE) {
                    if (warplayer != null) {
                        if (warplayer.set != null) {
                            warplayer.set.deleteMarkerSet();
                            warplayer.set = null;
                        }
                        warplayer = null;
                    }
                    warplayer = new WarpsLayer("Warp: [%name%]");
                }
            } else {
                reload = true;
                warplayer = new WarpsLayer("Warp: [%name%]");
            }
            //Update task
            Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
                @Override
                public void run() {
                    if (!stop) {
                        //Markers
                        if (warpsE) {
                            warplayer.updateMarkerSet();
                        }
                        //Players
                        for (Player pl : r.getOnlinePlayers()) {
                            if (UC.getPlayer(pl).isVanish() && !hiddenasserts.contains(pl.getUniqueId())) {
                                api.assertPlayerInvisibility(pl, true, r.getUC());
                                hiddenasserts.add(pl.getUniqueId());
                            }
                        }
                        List<UUID> toRemove = new ArrayList<>();
                        for (UUID id : hiddenasserts) {
                            if (Bukkit.getPlayer(id) == null) {
                                continue;
                            }
                            if (!UC.getPlayer(id).isVanish()) {
                                api.assertPlayerInvisibility(Bukkit.getPlayer(id), false, r.getUC());
                                toRemove.add(id);
                            }
                        }
                        hiddenasserts.removeAll(toRemove);
                    }
                }
            }, 100L, 100L);
        }
    }

    public static void stop() {
        if (warplayer != null) {
            warplayer.cleanup();
            warplayer = null;
        }
        stop = true;
    }

    //Listeners
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDynmapWebChat(DynmapWebChatEvent e) {
        String name = e.getName();
        if (name != null) {
            Player p = r.searchPlayer(name);
            if (p != null) {
                UPlayer pl = UC.getPlayer(p);
                if (pl != null && (pl.isBanned() || pl.isMuted())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        HashSet<UUID> newasserts = new HashSet<>();
        for (Player pl : r.getOnlinePlayers()) {
            if (UC.getPlayer(pl).isVanish() && !hiddenasserts.contains(pl.getUniqueId())) {
                api.assertPlayerInvisibility(pl, true, r.getUC());
            }
            newasserts.add(pl.getUniqueId());
            hiddenasserts.remove(pl.getUniqueId());
        }
        for (UUID id : hiddenasserts) {
            api.assertPlayerInvisibility(Bukkit.getPlayer(id), false, r.getUC());
        }
        hiddenasserts = newasserts;
    }

    //Layers
    private static class WarpsLayer extends Layer {

        public WarpsLayer(String fmt) {
            super("warps", "Warps", "portal", fmt);
        }

        @Override
        public Map<String, Location> getMarkers() {
            return UC.getServer().getWarps();
        }
    }

    public static abstract class Layer {

        MarkerSet set;
        MarkerIcon deficon;
        String labelfmt;
        Set<String> hidden;
        Map<String, Marker> markers = new HashMap<>();

        public Layer(String id, String deflabel, String deficon, String deflabelfmt) {
            this.set = markerapi.getMarkerSet("ultimatecore." + id);
            if (this.set == null) {
                this.set = markerapi.createMarkerSet("ultimatecore." + id, deflabel, null, false);
            } else {
                this.set.setMarkerSetLabel(deflabel);
            }
            if (this.set == null) {
                return;
            }
            this.set.setLayerPriority(10);
            this.set.setHideByDefault(false);
            int minzoom = 0;
            if (minzoom > 0) {
                this.set.setMinZoom(minzoom);
            }
            this.deficon = markerapi.getMarkerIcon(deficon);
            if (this.deficon == null) {
                this.deficon = markerapi.getMarkerIcon(deficon);
            }
            this.labelfmt = deflabelfmt;
            List<String> lst = r.getCnfg().getStringList("Dynmap.Hiddenwarps");
            if (lst != null) {
                this.hidden = new HashSet<>(lst);
            }
        }

        void cleanup() {
            if (this.set != null) {
                this.set.deleteMarkerSet();
                this.set = null;
            }
            this.markers.clear();
        }

        boolean isVisible(String id, String wname) {
            return !((this.hidden != null) && (!this.hidden.isEmpty()) && ((this.hidden.contains(id)) || (this.hidden.contains("world:" + wname))));
        }

        void updateMarkerSet() {
            Map<String, Marker> newmap = new HashMap<>();

            Map<String, Location> marks = getMarkers();
            for (String name : marks.keySet()) {
                Location loc = marks.get(name);

                String wname = loc.getWorld().getName();
                if (wname == null || name == null) {
                    continue;
                }

                if (isVisible(name, wname)) {
                    String id = wname + "/" + name;

                    String label = this.labelfmt.replace("%name%", name);

                    Marker m = this.markers.remove(id);
                    if (m == null) {
                        m = this.set.createMarker(id, label, wname, loc.getX(), loc.getY(), loc.getZ(), this.deficon, false);
                    } else {
                        m.setLocation(wname, loc.getX(), loc.getY(), loc.getZ());
                        m.setLabel(label);
                        m.setMarkerIcon(this.deficon);
                    }
                    newmap.put(id, m);
                }
            }
            for (Marker oldm : this.markers.values()) {
                oldm.deleteMarker();
            }

            this.markers.clear();
            this.markers = newmap;
        }

        public abstract Map<String, Location> getMarkers();
    }

}
