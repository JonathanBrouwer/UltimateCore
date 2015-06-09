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
package bammerbom.ultimatecore.spongeapi.listeners;

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UKit;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class JoinLeaveListener implements Listener {

    public static void start() {
        Bukkit.getPluginManager().registerEvents(new bammerbom.ultimatecore.spongeapi.listeners.JoinLeaveListener(), r.getUC());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void JoinMessage(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            //Message
            Bukkit.broadcastMessage(r.mes("firstJoin", "%Player", e.getPlayer().getDisplayName()));
            //Kits
            for (UKit kit : UC.getServer().getKits()) {
                if (kit.firstJoin()) {
                    final List<ItemStack> items = kit.getItems();
                    final Map<Integer, ItemStack> leftOver = e.getPlayer().getInventory().addItem(items.toArray(new ItemStack[items.size()]));
                    for (final ItemStack is : leftOver.values()) {
                        e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), is);
                    }
                    kit.setLastUsed(e.getPlayer(), System.currentTimeMillis());
                }
            }
            //Teleport to spawn
            LocationUtil.teleportUnsafe(e.getPlayer(), UC.getPlayer(e.getPlayer()).getSpawn(true) != null ? UC.getPlayer(e.getPlayer()).getSpawn(true) : e.getPlayer().getWorld()
                    .getSpawnLocation(), TeleportCause.PLUGIN, false);
        }
        if (UC.getPlayer(e.getPlayer()).isBanned()) {
            e.setJoinMessage(null);
            return;
        }
        e.setJoinMessage(r.mes("joinMessage", "%Player", UC.getPlayer(e.getPlayer()).getDisplayName()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void QuitMessage(PlayerQuitEvent e) {
        if (UC.getPlayer(e.getPlayer()).isBanned()) {
            e.setQuitMessage(null);
            return;
        }
        e.setQuitMessage(r.mes("quitMessage", "%Player", UC.getPlayer(e.getPlayer()).getDisplayName()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void KickMessage(PlayerKickEvent e) {
        e.setLeaveMessage(r.mes("quitMessage", "%Player", UC.getPlayer(e.getPlayer()).getDisplayName()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void JoinFail(PlayerLoginEvent e) {
        switch (e.getResult()) {
            case ALLOWED:
                break;
            case KICK_FULL:
                e.setKickMessage(r.mes("fullMessage"));
                break;
            case KICK_BANNED:
                break;
            case KICK_WHITELIST:
                e.setKickMessage(r.mes("whitelistMessage"));
                break;
            case KICK_OTHER:
                break;
            default:
                break;
        }
    }
}
