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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class RespawnListener implements Listener {

    public static void start() {
        if (!r.getCnfg().getBoolean("InstantRespawn")) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), r.getUC());
    }

    @Listener(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        if (!r.perm(e.getEntity(), "uc.instantrespawn", true, false)) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                Player p = e.getEntity();
                try {
                    p.spigot().respawn();
                } catch (Exception ex) {
                    try {
                        Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
                        Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + "" +
                                ".PacketPlayInClientCommand").newInstance();
                        Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + "" +
                                ".EnumClientCommand");

                        for (Object ob : enumClass.getEnumConstants()) {
                            if (ob.toString().equals("PERFORM_RESPAWN")) {
                                packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                            }
                        }

                        Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                        con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                    } catch (Exception ex2) {
                        ErrorLogger.log(ex2, "Failed to force respawn.");
                    }
                }
            }
        }, 2L);
    }

}
