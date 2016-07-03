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
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;

import java.lang.reflect.Field;

public class UnknownCommandListener implements Listener {

    public static void start() {
        if (!r.getCnfg().getBoolean("Command.UnknownCommand")) {
            return;
        }
        Sponge.getEventManager().registerListeners(r.getUC(), new UnknownCommandListener());
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
                ErrorLogger.log(e, "Failed to load command map.");
                return null;
            }
            f.setAccessible(true);
            try {
                return (SimpleCommandMap) f.get(Bukkit.getPluginManager());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                ErrorLogger.log(e, "Command map is modded.");
                return null;
            }
        }
        return null;
    }

    @Listener(order = Order.DEFAULT)
    public void onPlayerCommandPreprocess(SendCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getResult().equals(CommandResult.empty())) String cmd = event.getMessage();
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
