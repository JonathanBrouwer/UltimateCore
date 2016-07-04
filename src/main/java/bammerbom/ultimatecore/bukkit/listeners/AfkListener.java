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
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class AfkListener implements Listener {

    static Integer afktime = r.getCnfg().getInt("Afk.AfkTime");
    static Integer kicktime = r.getCnfg().getInt("Afk.KickTime");
    static Boolean kickenabled = r.getCnfg().getBoolean("Afk.KickEnabled");

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
                        if (dif > afktime) {
                            if (!UC.getPlayer(pl).isAfk()) {
                                UC.getPlayer(pl).setAfk(true);
                                Bukkit.broadcastMessage(r.mes("afkAfk", "%Player", UC.getPlayer(pl).getDisplayName()));
                            }
                        }
                        if (dif > kicktime) {
                            if (kickenabled) {
                                if (!r.perm(pl, "uc.afk.exempt", false, false)) {
                                    pl.kickPlayer(r.mes("afkKick"));
                                }
                            }
                        }
                        if (UC.getPlayer(pl).isAfk()) {
                            String sub = (kickenabled && !r.perm(pl, "uc.afk.exempt", false, false) && dif > 1) ? r.mes("afkWarning2", "%Time", ChatColor.stripColor(DateUtil
                                    .formatDateDiff(((kicktime - dif) * 1000) + System.currentTimeMillis()))) : null;
                            TitleUtil.sendTitle(pl, 0, 120, 20, r.mes("afkWarning"), sub);
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
        if (e.getMessage().startsWith("/afk") || e.getMessage().startsWith("afk")) {
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
