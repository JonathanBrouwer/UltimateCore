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
package bammerbom.ultimatecore.sponge.modules.spawn.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.utils.SpawnUtil;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.time.Instant;
import java.util.Arrays;

public class SpawnListener {
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player p = event.getTargetEntity();

        //TODO beter first join detection
        //Firstjoin message
        Instant first = p.getJoinData().firstPlayed().get();
        Instant last = p.getJoinData().lastPlayed().get();
        Long diff = first.getEpochSecond() - last.getEpochSecond();

        if (diff < 2 && diff > -2) {
            //User joined for the first time
            //If first spawn is set
            if (GlobalData.get(SpawnKeys.FIRST_SPAWN).isPresent()) {
                //Teleport
                Transform loc = GlobalData.get(SpawnKeys.FIRST_SPAWN).get();
                Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), loc, tel -> {
                }, (tel, reason) -> {
                }, false, true);
                tp.start();
            }
        } else {
            ModuleConfig config = Modules.SPAWN.get().getConfig().get();
            if (config.get().getNode("spawn-on-join").getBoolean(false)) {
                Transform loc = SpawnUtil.getSpawnLocation(p);
                Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), loc, tel -> {
                }, (tel, reason) -> {
                }, false, true);
                tp.start();
            }
        }
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        Player p = event.getTargetEntity();
        ModuleConfig config = Modules.SPAWN.get().getConfig().get();
        if (config.get().getNode("spawn-on-join").getBoolean(false)) {
            Transform loc = SpawnUtil.getSpawnLocation(p);
            Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), loc, tel -> {
            }, (tel, reason) -> {
            }, false, true);
            tp.start();
        }
    }
}
