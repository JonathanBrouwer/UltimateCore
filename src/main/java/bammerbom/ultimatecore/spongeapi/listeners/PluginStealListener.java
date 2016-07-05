///*
// * This file is part of UltimateCore, licensed under the MIT License (MIT).
// *
// * Copyright (c) Bammerbom
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package bammerbom.ultimatecore.spongeapi.listeners;
//
//import bammerbom.ultimatecore.spongeapi.r;
//import org.bukkit.Bukkit;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerCommandPreprocessEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PluginStealListener implements Listener {
//
//    public static List<String> commands = new ArrayList<>();
//
//    public static void start() {
//        commands.add("plugins");
//        commands.add("pl");
//        commands.add("version");
//        commands.add("ver");
//        commands.add("icanhasbukkit");
//        commands.add("about");
//        Bukkit.getPluginManager().registerEvents(new PluginStealListener(), r.getUC());
//    }
//
//    @EventHandler
//    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
//        String m = e.getMessage().startsWith("/") ? e.getMessage().replaceFirst("/", "") : e.getMessage();
//        if (m.contains(" ")) {
//            m = m.split(" ")[0];
//        }
//        if (commands.contains(m.toLowerCase().replace("bukkit:", ""))) {
//            if (!r.perm(e.getPlayer(), "uc.plugins", false, true)) {
//                e.setCancelled(true);
//            }
//        }
//        if (m.equalsIgnoreCase("?") || m.equalsIgnoreCase("bukkit:?") || m.equalsIgnoreCase("help") || m.equalsIgnoreCase("bukkit:help")) {
//            if (!r.perm(e.getPlayer(), "uc.help", false, true)) {
//                e.setCancelled(true);
//            }
//        }
//    }
//
//    @EventHandler
//    public void onCommandTab(TabCompleteEvent ev) {
//        String command = ev.getBuffer().contains(" ") ? ev.getBuffer().split(" ")[0] : ev.getBuffer();
//        if (command.equalsIgnoreCase("/")) {
//            if (!r.perm(ev.getSender(), "uc.plugins", false, true)) {
//                ev.setCancelled(true);
//            }
//        }
//        command = command.replaceFirst("/", "");
//        if (commands.contains(command.replace("bukkit:", ""))) {
//            if (!r.perm(ev.getSender(), "uc.plugins", false, true)) {
//                ev.setCancelled(true);
//            }
//        }
//        if (command.equalsIgnoreCase("?") || command.equalsIgnoreCase("bukkit:?") || command.equalsIgnoreCase("help") || command.equalsIgnoreCase("bukkit:help")) {
//            if (!r.perm(ev.getSender(), "uc.help", false, true)) {
//                ev.setCancelled(true);
//            }
//        }
//    }
//
//
//}
