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
package bammerbom.ultimatecore.sponge.modules.instantrespawn.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.instantrespawn.api.InstantrespawnPermissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InstantrespawnListener {
    @Listener
    public void onDeath(DestructEntityEvent.Death event) {
        if (event.getTargetEntity() instanceof Player) {
            Player p = (Player) event.getTargetEntity();
            UUID u = p.getUniqueId();
            if (Modules.INSTANTRESPAWN.get().getConfig().get().get().getNode("permission").getBoolean() && !p.hasPermission(InstantrespawnPermissions.UC_INSTANTRESPAWN.get())) {
                return;
            }
            Long delay = Modules.INSTANTRESPAWN.get().getConfig().get().get().getNode("delay").getLong();
            Sponge.getScheduler().createTaskBuilder().delay(delay, TimeUnit.MILLISECONDS).name("Respawn task").execute(() -> {
                Player p2 = Sponge.getServer().getPlayer(u).get();
                p2.respawnPlayer();
            }).submit(UltimateCore.get());
        }
    }
}
