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
package bammerbom.ultimatecore.spongeapi.api;

import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.UUID;

public class UC {

    public static ArrayList<UPlayer> uplayers = new ArrayList<>();
    public static bammerbom.ultimatecore.spongeapi.api.UServer userver = new bammerbom.ultimatecore.spongeapi.api.UServer();
    public static bammerbom.ultimatecore.spongeapi.api.UEconomy ueconomy = null;
    public static ArrayList<UWorld> uworlds = new ArrayList<>();

    public static bammerbom.ultimatecore.spongeapi.api.UPlayer getPlayer(UUID u) {
        for (UPlayer p : uplayers) {
            if (p.uuid.equals(u)) {
                return p;
            }
        }
        bammerbom.ultimatecore.spongeapi.api.UPlayer pl = new bammerbom.ultimatecore.spongeapi.api.UPlayer(u);
        if (pl.getPlayer().isOnline()) {
            uplayers.add(pl);
        }
        return pl;
    }

    public static UPlayer getPlayer(User p) {
        for (bammerbom.ultimatecore.spongeapi.api.UPlayer pl : uplayers) {
            if (pl.uuid.equals(p.getUniqueId())) {
                return pl;
            }
        }
        bammerbom.ultimatecore.spongeapi.api.UPlayer pl = new bammerbom.ultimatecore.spongeapi.api.UPlayer(p);
        if (pl.getPlayer().isOnline()) {
            uplayers.add(pl);
        }
        return pl;
    }

    public static bammerbom.ultimatecore.spongeapi.api.UPlayer getPlayer(String n) {
        for (bammerbom.ultimatecore.spongeapi.api.UPlayer pl : uplayers) {
            if (pl.name.equals(n)) {
                return pl;
            }
        }
        bammerbom.ultimatecore.spongeapi.api.UPlayer pl = new bammerbom.ultimatecore.spongeapi.api.UPlayer(r.searchOfflinePlayer(n));
        uplayers.add(pl);
        return pl;
    }

    public static bammerbom.ultimatecore.spongeapi.api.UWorld getWorld(World world) {
        for (bammerbom.ultimatecore.spongeapi.api.UWorld w : uworlds) {
            if (w.base.equals(world)) {
                return w;
            }
        }
        bammerbom.ultimatecore.spongeapi.api.UWorld w = new bammerbom.ultimatecore.spongeapi.api.UWorld(world);
        uworlds.add(w);
        return w;
    }

    public static UWorld getWorld(String world) {
        if (!r.getGame().getServer().getWorld(world).isPresent()) {
            return null;
        }
        return getWorld(r.getGame().getServer().getWorld(world).get());
    }

    public static bammerbom.ultimatecore.spongeapi.api.UServer getServer() {
        return userver;
    }

}
