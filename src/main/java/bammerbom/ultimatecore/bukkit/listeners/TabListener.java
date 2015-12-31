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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class TabListener implements Listener {

    static HashMap<String, String> tabPrefixes = new HashMap<>();

    public static void start() {
        if (!r.getCnfg().contains("Chat.Tab.Enabled")) {
            return;
        }
        if (!r.getCnfg().getBoolean("Chat.Tab.Enabled")) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new TabListener(), r.getUC());
        final String def = r.getCnfg().getString("Chat.Tab.TabDefault");
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            for (Player p : r.getOnlinePlayers()) {
                if (r.getVault() == null || r.getVault().getPermission() == null) {
                    String name = def + UC.getPlayer(p).getDisplayName();
                    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                    if (name.length() > 15) {
                        name = name.substring(0, 14);
                    }
                    if (!p.getPlayerListName().equals(name)) {
                        p.setPlayerListName(name);
                    }
                    continue;
                }
                String group = r.getPrimaryGroup(p);
                if (group == null || group.equalsIgnoreCase("")) {
                    String name = def + UC.getPlayer(p).getDisplayName();
                    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                    if (name.length() > 15) {
                        name = name.substring(0, 14);
                    }
                    if (!p.getPlayerListName().equals(name)) {
                        p.setPlayerListName(name);
                    }
                    continue;
                }
                String prefix = r.getCnfg().getString("Chat.Tab." + group);
                if (prefix == null || prefix.equalsIgnoreCase("")) {
                    String name = def + UC.getPlayer(p).getDisplayName();
                    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                    if (name.length() > 15) {
                        name = name.substring(0, 14);
                    }
                    if (!p.getPlayerListName().equals(name)) {
                        p.setPlayerListName(name);
                    }
                    continue;
                }
                String name = prefix + UC.getPlayer(p).getDisplayName();
                name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                if (name.length() > 15) {
                    name = name.substring(0, 14);
                }
                if (!p.getPlayerListName().equals(name)) {
                    p.setPlayerListName(name);
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                for (Player p : r.getOnlinePlayers()) {
                    if (r.getVault() == null || r.getVault().getPermission() == null) {
                        String name = def + UC.getPlayer(p).getDisplayName();
                        name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                        if (name.length() > 15) {
                            name = name.substring(0, 14);
                        }
                        if (!p.getPlayerListName().equals(name)) {
                            p.setPlayerListName(name);
                        }
                        continue;
                    }
                    String group = r.getPrimaryGroup(p);
                    if (group == null || group.equalsIgnoreCase("")) {
                        String name = def + UC.getPlayer(p).getDisplayName();
                        name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                        if (name.length() > 15) {
                            name = name.substring(0, 14);
                        }
                        if (!p.getPlayerListName().equals(name)) {
                            p.setPlayerListName(name);
                        }
                        continue;
                    }
                    String prefix = r.getCnfg().getString("Chat.Tab." + group);
                    if (prefix == null || prefix.equalsIgnoreCase("")) {
                        String name = def + UC.getPlayer(p).getDisplayName();
                        name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                        if (name.length() > 15) {
                            name = name.substring(0, 14);
                        }
                        if (!p.getPlayerListName().equals(name)) {
                            p.setPlayerListName(name);
                        }
                        continue;
                    }
                    String name = prefix + UC.getPlayer(p).getDisplayName();
                    name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
                    if (name.length() > 15) {
                        name = name.substring(0, 14);
                    }
                    if (!p.getPlayerListName().equals(name)) {
                        p.setPlayerListName(name);
                    }
                }
            }
        }, 100L, 100L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final String def = r.getCnfg().getString("Chat.Tab.TabDefault");
        if (r.getVault() == null || r.getVault().getPermission() == null) {
            String name = def + UC.getPlayer(e.getPlayer()).getDisplayName();
            name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
            if (name.length() > 15) {
                name = name.substring(0, 14);
            }
            e.getPlayer().setPlayerListName(name);
            return;
        }
        String group = r.getPrimaryGroup(e.getPlayer());
        if (group == null || group.equalsIgnoreCase("")) {
            String name = def + UC.getPlayer(e.getPlayer()).getDisplayName();
            name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
            if (name.length() > 15) {
                name = name.substring(0, 14);
            }
            e.getPlayer().setPlayerListName(name);
            return;
        }
        String prefix = r.getCnfg().getString("Chat.Tab." + group);
        if (prefix == null || prefix.equalsIgnoreCase("")) {
            String name = def + UC.getPlayer(e.getPlayer()).getDisplayName();
            name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
            if (name.length() > 15) {
                name = name.substring(0, 14);
            }
            e.getPlayer().setPlayerListName(name);
            return;
        }
        String name = prefix + UC.getPlayer(e.getPlayer()).getDisplayName();
        name = ChatColor.translateAlternateColorCodes('&', name).replaceAll("&y", "");
        if (name.length() > 15) {
            name = name.substring(0, 14);
        }
        e.getPlayer().setPlayerListName(name);
    }
}
