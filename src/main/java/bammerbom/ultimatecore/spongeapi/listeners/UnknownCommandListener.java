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
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.SimplePluginManager;

public class UnknownCommandListener implements Listener {

    public static void start() {
        if (!r.getCnfg().getBoolean("Command.UnknownCommand")) {
            return;
        }
        Bukkit.getServer().getPluginManager().registerEvents(new UnknownCommandListener(), r.getUC());
    }

    public boolean isCmdRegistered(String cmd) {
        return getCommandMap().getCommand(cmd.replaceFirst("/", "")) != null;
    }

    private SimpleCommandMap getCommandMap() {
        if ((Bukkit.getPluginManager() instanceof SimplePluginManager)) {
            Field f;
            try {
                f = SimplePluginManager.class.getDeclaredField("commandMap");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                return null;
            }
            f.setAccessible(true);
            try {
                return (SimpleCommandMap) f.get(Bukkit.getPluginManager());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        String cmd = event.getMessage();
        if (cmd == null || cmd.equalsIgnoreCase("")) {
            r.sendMes(event.getPlayer(), "unknownCommand");
            event.setCancelled(true);
            return;
        }
        cmd = cmd.split(" ")[0];

        if (!isCmdRegistered(cmd)) {
            r.sendMes(event.getPlayer(), "unknownCommand");
            event.setCancelled(true);
        }

    }
}
