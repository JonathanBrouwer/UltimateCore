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

import bammerbom.ultimatecore.spongeapi.UltimateSign;
import bammerbom.ultimatecore.spongeapi.UltimateSigns;
import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    public static void start() {
        Bukkit.getPluginManager().registerEvents(new bammerbom.ultimatecore.spongeapi.listeners.SignListener(), r.getUC());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSignChange(SignChangeEvent e) {
        //Signs
        for (UltimateSign s : UltimateSigns.signs) {
            if (ChatColor.stripColor(e.getLine(0)).equalsIgnoreCase("[" + s.getName() + "]")) {
                s.onCreate(e);
            }
        }
        //Color signs
        if (!r.perm(e.getPlayer(), "uc.sign.colored", false, false)) {
            return;
        }
        e.setLine(0, ChatColor.translateAlternateColorCodes('&', e.getLine(0)));
        e.setLine(1, ChatColor.translateAlternateColorCodes('&', e.getLine(1)));
        e.setLine(2, ChatColor.translateAlternateColorCodes('&', e.getLine(2)));
        e.setLine(3, ChatColor.translateAlternateColorCodes('&', e.getLine(3)));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSignInteract(PlayerInteractEvent e) {
        //Signs
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (e.getClickedBlock() == null || e.getClickedBlock().getType() == null || e.getClickedBlock().getType().equals(Material.AIR)) {
            return;
        }
        if (!(e.getClickedBlock().getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) e.getClickedBlock().getState();
        for (UltimateSign s : UltimateSigns.signs) {
            if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[" + s.getName() + "]")) {
                s.onClick(e.getPlayer(), sign);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSignDestroy(BlockBreakEvent e) {
        //Signs
        if (e.getBlock() == null || e.getBlock().getType() == null || e.getBlock().getType().equals(Material.AIR)) {
            return;
        }
        if (!(e.getBlock().getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) e.getBlock().getState();
        for (UltimateSign s : UltimateSigns.signs) {
            if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[" + s.getName() + "]")) {
                s.onDestroy(e);
            }
        }
    }

}
