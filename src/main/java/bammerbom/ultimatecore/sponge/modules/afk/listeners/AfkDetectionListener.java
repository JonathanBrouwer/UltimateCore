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
package bammerbom.ultimatecore.sponge.modules.afk.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.runnable.AfkCheckTask;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.HashMap;
import java.util.UUID;

public class AfkDetectionListener {
    public static HashMap<UUID, Long> afktime = new HashMap<>();

    public static void start() {
        CommentedConfigurationNode config = Modules.AFK.get().getConfig().get().get();
        //Start check task
        Sponge.getScheduler().createTaskBuilder().intervalTicks(config.getNode("time", "afk-check-interval").getInt(60)).name("UC Afk check task").execute(new AfkCheckTask()).submit
                (UltimateCore.get());
        //Register events
        Sponge.getEventManager().registerListener(UltimateCore.get(), ClientConnectionEvent.Join.class, event -> {
            Player p = event.getTargetEntity();
            afktime.put(p.getUniqueId(), System.currentTimeMillis());
        });
        if (config.getNode("events", "move", "enabled").getBoolean(false) && config.getNode("events", "move", "mode").getString("").equalsIgnoreCase("event")) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), MoveEntityEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "chat").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), MessageChannelEvent.Chat.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "command").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), SendCommandEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent() && !UltimateCore.get().getCommandService().get("afk").get().getAliases().contains(event.getCommand())) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "interact").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), InteractEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "tabcomplete").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), TabCompleteEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "death").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), DestructEntityEvent.Death.class, event -> {
                if (event.getTargetEntity() instanceof Player) {
                    Player p = (Player) event.getTargetEntity();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
        if (config.getNode("events", "respawn").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), RespawnPlayerEvent.class, event -> {
                Player p = event.getTargetEntity();
                afktime.put(p.getUniqueId(), System.currentTimeMillis());
                unafkCheck(p);
            });
        }
        if (config.getNode("events", "inventory").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), InteractInventoryEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    afktime.put(p.getUniqueId(), System.currentTimeMillis());
                    unafkCheck(p);
                }
            });
        }
    }

    public static void unafkCheck(Player pl) {
        UltimateUser user = UltimateCore.get().getUserService().getUser(pl);
        CommentedConfigurationNode config = Modules.AFK.get().getConfig().get().get();
        long afk = config.getNode("time", "afktime").getLong() * 1000;
        long value = AfkDetectionListener.afktime.get(pl.getUniqueId());
        long diff = System.currentTimeMillis() - value;
        if (user.get(AfkKeys.IS_AFK).get() && diff < afk) {
            user.offer(AfkKeys.IS_AFK, false);
            Sponge.getServer().getBroadcastChannel().send(Messages.getFormatted("afk.broadcast.nolonger", "%player%", user.getUser().getName(), "%time%", TimeUtil.formatDateDiff(user.get
                    (AfkKeys.AFK_TIME).get(), 2, null)));
        }
    }
}
