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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;

public class RespawnListener {

    public static void start() {
        if (!r.getCnfg().getBoolean("InstantRespawn")) {
            return;
        }
        Sponge.getEventManager().registerListeners(r.getUC(), new RespawnListener());
    }

    @Listener(order = Order.LAST)
    public void onDeath(final DestructEntityEvent.Death e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player) e.getTargetEntity();
        if (!r.perm(p, "uc.instantrespawn", false)) {
            return;
        }
        Sponge.getScheduler().createTaskBuilder().delayTicks(2L).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO does this work?
                    p.getWorld().spawnEntity(p, Cause.builder().build());
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Failed to force respawn.");
                }
            }
        }).name("Respawn delay task").submit(r.getUC());
    }

}
