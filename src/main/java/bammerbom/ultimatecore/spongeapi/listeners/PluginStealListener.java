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

import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PluginStealListener implements Listener {

    public static void start() {
        Bukkit.getPluginManager().registerEvents(new bammerbom.ultimatecore.spongeapi.listeners.PluginStealListener(), r.getUC());
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            TabCompleteListener.start();
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String m = e.getMessage().startsWith("/") ? e.getMessage().replaceFirst("/", "") : e.getMessage();
        if (m.contains(" ")) {
            m = m.split(" ")[0];
        }
        if (m.equalsIgnoreCase("plugins") || m.equalsIgnoreCase("bukkit:plugins") || m.equalsIgnoreCase("pl") || m.equalsIgnoreCase("bukkit:pl") || m.equalsIgnoreCase("about") || m
                .equalsIgnoreCase("bukkit:about") || m.equalsIgnoreCase("version") || m.equalsIgnoreCase("bukkit:version") || m.equalsIgnoreCase("ver") || m.equalsIgnoreCase("bukkit:ver")) {
            if (!r.perm(e.getPlayer(), "uc.plugins", false, true)) {
                e.setCancelled(true);
            }
        }
        if (m.equalsIgnoreCase("?") || m.equalsIgnoreCase("bukkit:?") || m.equalsIgnoreCase("help") || m.equalsIgnoreCase("bukkit:help")) {
            if (!r.perm(e.getPlayer(), "uc.help", false, true)) {
                e.setCancelled(true);
            }
        }
    }

    static class TabCompleteListener {

        public static void start() {
            ((com.comphenix.protocol.ProtocolManager) r.getProtocolManager()).addPacketListener(new com.comphenix.protocol.events.PacketAdapter(r
                    .getUC(), com.comphenix.protocol.events.ListenerPriority.NORMAL, new com.comphenix.protocol.PacketType[]{com.comphenix.protocol.PacketType.Play.Client.TAB_COMPLETE}) {
                @Override
                public void onPacketReceiving(com.comphenix.protocol.events.PacketEvent event) {
                    if (event.getPacketType() == com.comphenix.protocol.PacketType.Play.Client.TAB_COMPLETE) {
                        com.comphenix.protocol.events.PacketContainer packet = event.getPacket();
                        String m = packet.getStrings().read(0).toLowerCase();
                        if (m.contains("/")) {
                            m = m.replaceFirst("/", "");
                        }
                        if (m.contains(" ")) {
                            m = m.split(" ")[0];
                        }
                        if (m.equalsIgnoreCase("plugins") || m.equalsIgnoreCase("bukkit:plugins") || m.equalsIgnoreCase("pl") || m.equalsIgnoreCase("bukkit:pl") || m.equalsIgnoreCase("about") || m
                                .equalsIgnoreCase("bukkit:about") || m.equalsIgnoreCase("version") || m.equalsIgnoreCase("bukkit:version") || m.equalsIgnoreCase("ver") || m
                                .equalsIgnoreCase("bukkit:ver")) {
                            if (!r.perm(event.getPlayer(), "uc.plugins", false, false)) {
                                event.setCancelled(true);
                            }
                        }
                        if (m.equalsIgnoreCase("?") || m.equalsIgnoreCase("bukkit:?") || m.equalsIgnoreCase("help") || m.equalsIgnoreCase("bukkit:help")) {
                            if (!r.perm(event.getPlayer(), "uc.help", false, false)) {
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            });
        }
    }

}
