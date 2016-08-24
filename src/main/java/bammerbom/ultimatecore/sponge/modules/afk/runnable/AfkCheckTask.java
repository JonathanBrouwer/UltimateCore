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
package bammerbom.ultimatecore.sponge.modules.afk.runnable;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkDetectionListener;
import bammerbom.ultimatecore.sponge.utils.Messages;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AfkCheckTask implements Runnable {
    @Override
    public void run() {
        CommentedConfigurationNode config = Modules.AFK.get().getConfig().get().get();
        long afk = config.getNode("time", "afktime").getLong() * 1000;
        List<UUID> remove = new ArrayList<>();
        for (UUID uuid : AfkDetectionListener.afktime.keySet()) {
            Player player = Sponge.getServer().getPlayer(uuid).orElse(null);
            if (player == null) {
                remove.add(uuid);
                continue;
            }
            UltimateUser user = UltimateCore.get().getUserService().getUser(player);
            //Location check
            if (config.getNode("events", "move", "enabled").getBoolean(false) && config.getNode("events", "move", "mode").getString("").equalsIgnoreCase("task")) {
                Transform<World> nloc = new Transform<World>(player.getLocation(), player.getRotation(), player.getScale());
                if (!user.get(AfkKeys.LAST_LOCATION).isPresent()) {
                    user.offer(AfkKeys.LAST_LOCATION, nloc);
                    AfkDetectionListener.unafkCheck(player);
                }
                Transform<World> oloc = user.get(AfkKeys.LAST_LOCATION).get();
                if (!oloc.equals(nloc)) {
                    AfkDetectionListener.afktime.put(player.getUniqueId(), System.currentTimeMillis());
                    user.offer(AfkKeys.LAST_LOCATION, nloc);
                    AfkDetectionListener.unafkCheck(player);
                }
            }
            //Afk check
            long value = AfkDetectionListener.afktime.get(uuid);
            long diff = System.currentTimeMillis() - value;
            if (!user.get(AfkKeys.IS_AFK).get() && diff > afk) {
                user.offer(AfkKeys.IS_AFK, true);
                user.offer(AfkKeys.AFK_TIME, System.currentTimeMillis());
                Sponge.getServer().getBroadcastChannel().send(Messages.getFormatted("afk.broadcast.afk", "%player%", user.getUser().getName()));
                //TODO afk message
            }
        }
        for (UUID uuid : remove) {
            AfkDetectionListener.afktime.remove(uuid);
        }
    }
}
