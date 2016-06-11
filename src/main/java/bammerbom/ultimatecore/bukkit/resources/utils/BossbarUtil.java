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
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.r;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BossbarUtil implements Listener {

    static HashMap<UUID, BossBar> current = new HashMap<>();
    static HashMap<BossBar, Integer> taskids = new HashMap<>();
    static HashMap<BossBar, Boolean> taskcancel = new HashMap<>();

    public static void stop() {
        for (UUID u : current.keySet()) {
            BossBar bar = current.get(u);
            bar.removeAll();
        }
        current.clear();
    }

    public static BossBar setMessage(Player player, String message, BarColor color, BarStyle style) {
        if (hasBar(player)) {
            removeBar(player);
        }
        BossBar bar = Bukkit.getServer().createBossBar(message, color, style);
        bar.addPlayer(player);
        current.put(player.getUniqueId(), bar);
        return bar;
    }

    public static BossBar setMessage(String message, float percent, BarColor color, BarStyle style) {
        BossBar bar = Bukkit.getServer().createBossBar(message, color, style);
        bar.setProgress(percent / 100);
        for (Player player : r.getOnlinePlayers()) {
            if (hasBar(player)) {
                removeBar(player);
            }
            bar.addPlayer(player);
            current.put(player.getUniqueId(), bar);
        }
        return bar;
    }

    public static BossBar setMessage(Player player, String message, float percent, BarColor color, BarStyle style) {
        if (hasBar(player)) {
            removeBar(player);
        }
        BossBar bar = Bukkit.getServer().createBossBar(message, color, style);
        bar.setProgress(percent / 100);
        bar.addPlayer(player);
        current.put(player.getUniqueId(), bar);
        return bar;
    }

    public static void setMessage(String message, int seconds, BarColor color, BarStyle style) {
        Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);
        final BossBar bar = Bukkit.getServer().createBossBar(message, color, style);
        final double dragonHealthMinus = 1.0 / seconds;

        taskids.put(bar, Bukkit.getScheduler().runTaskTimer(r.getUC(), new BukkitRunnable() {
            @Override
            public void run() {
                if (bar.getProgress() - dragonHealthMinus <= 0.01) {
                    for (Player p : bar.getPlayers()) {
                        current.remove(p);
                        bar.removePlayer(p);
                    }
                    taskcancel.put(bar, true);
                } else {
                    bar.setProgress(bar.getProgress() - dragonHealthMinus);
                }
            }
        }, 20L, 20L).getTaskId());
        ArrayList<Integer> removed = new ArrayList<>();
        for (BossBar barc : taskcancel.keySet()) {
            Integer id = taskids.get(barc);
            Bukkit.getScheduler().cancelTask(id);
            removed.add(id);
        }
        for (Integer i : removed) {
            taskids.remove(i);
            taskcancel.remove(i);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasBar(player)) {
                removeBar(player);
            }
            bar.addPlayer(player);
            current.put(player.getUniqueId(), bar);
        }
    }

    public static void setMessage(final Player player, String message, int seconds, BarColor color, BarStyle style) {
        if (hasBar(player)) {
            removeBar(player);
        }
        Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);
        final BossBar bar = Bukkit.getServer().createBossBar(message, color, style);
        final double dragonHealthMinus = 1.0 / seconds;

        taskids.put(bar, Bukkit.getScheduler().runTaskTimer(r.getUC(), new BukkitRunnable() {
            @Override
            public void run() {
                if (bar.getProgress() - dragonHealthMinus <= 0.01) {
                    for (Player p : bar.getPlayers()) {
                        current.remove(p);
                        bar.removePlayer(p);
                    }
                    taskcancel.put(bar, true);
                } else {
                    bar.setProgress(bar.getProgress() - dragonHealthMinus);
                }
            }
        }, 20L, 20L).getTaskId());
        ArrayList<Integer> removed = new ArrayList<>();
        for (BossBar barc : taskcancel.keySet()) {
            Integer id = taskids.get(barc);
            Bukkit.getScheduler().cancelTask(id);
            removed.add(id);
        }
        for (Integer i : removed) {
            taskids.remove(i);
            taskcancel.remove(i);
        }

        bar.addPlayer(player);
        current.put(player.getUniqueId(), bar);
    }

    public static boolean hasBar(Player player) {
        return current.containsKey(player.getUniqueId());
    }

    public static void removeBar(Player player) {
        if (!hasBar(player)) {
            return;
        }
        BossBar bar = current.get(player.getUniqueId());
        bar.removePlayer(player);
        current.remove(player.getUniqueId());
    }

    public static BossBar getBar(Player player) {
        if (!hasBar(player)) {
            return null;
        }
        return current.get(player.getUniqueId());
    }
}