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

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

public class UC {

    public static ArrayList<UPlayer> uplayers = new ArrayList<>();
    public static UServer userver = new UServer();
    public static UEconomy ueconomy = null;
    public static ArrayList<UWorld> uworlds = new ArrayList<>();

    public static UPlayer getPlayer(UUID u) {
        for (UPlayer p : uplayers) {
            if (p.uuid.equals(u)) {
                return p;
            }
        }
        UPlayer pl = new UPlayer(u);
        if (pl.getPlayer().isOnline()) {
            uplayers.add(pl);
        }
        return pl;
    }

    public static UPlayer getPlayer(OfflinePlayer p) {
        for (UPlayer pl : uplayers) {
            if (pl.uuid.equals(p.getUniqueId())) {
                return pl;
            }
        }
        UPlayer pl = new UPlayer(p);
        if (pl.getPlayer().isOnline()) {
            uplayers.add(pl);
        }
        return pl;
    }

    public static UPlayer getPlayer(String n) {
        for (UPlayer pl : uplayers) {
            if (pl.name.equals(n)) {
                return pl;
            }
        }
        UPlayer pl = new UPlayer(r.searchOfflinePlayer(n));
        uplayers.add(pl);
        return pl;
    }

    public static UWorld getWorld(World world) {
        for (UWorld w : uworlds) {
            if (w.base.equals(world)) {
                return w;
            }
        }
        UWorld w = new UWorld(world);
        uworlds.add(w);
        return w;
    }

    public static UWorld getWorld(String world) {
        if (Bukkit.getWorld(world) == null) {
            return null;
        }
        return getWorld(Bukkit.getWorld(world));
    }

    public static UServer getServer() {
        return userver;
    }

}
