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
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class AfkListener implements Listener {

    public static void start() {
        if (r.getCnfg().getBoolean("Afk.Enabled")) {
            Bukkit.getPluginManager().registerEvents(new AfkListener(), r.getUC());
            Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
                @Override
                public void run() {
                    for (Player pl : r.getOnlinePlayers()) {
                        Long time = UC.getPlayer(pl).getLastActivity();
                        Long seconds1 = time / 1000;
                        Long seconds2 = System.currentTimeMillis() / 1000;
                        Long dif = seconds2 - seconds1;
                        if (dif > r.getCnfg().getInt("Afk.AfkTime")) {
                            if (!UC.getPlayer(pl).isAfk()) {
                                UC.getPlayer(pl).setAfk(true);
                                Bukkit.broadcastMessage(r.mes("afkAfk", "%Player", UC.getPlayer(pl).getNick() == null ? pl.getDisplayName() : UC.getPlayer(pl).getNick()));
                            }
                        }
                        if (dif > r.getCnfg().getInt("Afk.KickTime")) {
                            if (r.getCnfg().getBoolean("Afk.KickEnabled")) {
                                if (!r.perm(pl, "uc.afk.excempt", false, false)) {
                                    pl.kickPlayer(r.mes("afkKick"));
                                }
                            }
                        }

                    }
                }
            }, 100L, 100L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(AsyncPlayerChatEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerBedEnterEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerBedLeaveEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerChatTabCompleteEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerEditBookEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerInteractEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerInteractEntityEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerItemHeldEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerMoveEvent e) {
        if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            UC.getPlayer(e.getPlayer()).updateLastActivity();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerRespawnEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerToggleSneakEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(PlayerVelocityEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin(PlayerJoinEvent e) {
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/afk") || e.getMessage().equalsIgnoreCase("afk")) {
            return;
        }
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) {
        if (UC.getPlayer(e.getPlayer()).isAfk()) {
            UC.getPlayer(e.getPlayer()).setAfk(false);
        }
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @EventHandler
    public void playerKick(PlayerKickEvent e) {
        if (UC.getPlayer(e.getPlayer()).isAfk()) {
            UC.getPlayer(e.getPlayer()).setAfk(false);
        }
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

}
