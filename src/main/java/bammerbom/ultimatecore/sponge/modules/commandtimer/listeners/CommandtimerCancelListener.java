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
package bammerbom.ultimatecore.sponge.modules.commandtimer.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.CommandtimerKeys;
import bammerbom.ultimatecore.sponge.modules.commandtimer.api.Warmup;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;

public class CommandtimerCancelListener {

    public static void registerEvents() {
        ModuleConfig config = Modules.COMMANDTIMER.get().getConfig().get();
        CommentedConfigurationNode node = config.get();
        if (node.getNode("warmup", "cancel-move").getBoolean(true)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), MoveEntityEvent.class, event -> {
                if (event.getTargetEntity() instanceof Player) {
                    Player p = (Player) event.getTargetEntity();
                    UltimateUser up = UltimateCore.get().getUserService().getUser(p);
                    for (Warmup warmup : up.get(CommandtimerKeys.USER_WARMUPS).get().values()) {
                        Messages.send(p, "commandtimer.warmup.cancel.move");
                        warmup.cancel();
                    }
                }
            });
        }
        if (node.getNode("warmup", "cancel-damage").getBoolean(true)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), DamageEntityEvent.class, event -> {
                if (event.getTargetEntity() instanceof Player) {
                    Player p = (Player) event.getTargetEntity();
                    UltimateUser up = UltimateCore.get().getUserService().getUser(p);
                    for (Warmup warmup : up.get(CommandtimerKeys.USER_WARMUPS).get().values()) {
                        Messages.send(p, "commandtimer.warmup.cancel.damage");
                        warmup.cancel();
                    }
                }
            });
        }
        if (node.getNode("warmup", "cancel-command").getBoolean(false)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), SendCommandEvent.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    UltimateUser up = UltimateCore.get().getUserService().getUser(p);
                    for (Warmup warmup : up.get(CommandtimerKeys.USER_WARMUPS).get().values()) {
                        Messages.send(p, "commandtimer.warmup.cancel.command");
                        warmup.cancel();
                    }
                }
            });
        }
        if (node.getNode("warmup", "cancel-destroy").getBoolean(true)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), ChangeBlockEvent.Break.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    UltimateUser up = UltimateCore.get().getUserService().getUser(p);
                    for (Warmup warmup : up.get(CommandtimerKeys.USER_WARMUPS).get().values()) {
                        Messages.send(p, "commandtimer.warmup.cancel.destroy");
                        warmup.cancel();
                    }
                }
            });
        }
        if (node.getNode("warmup", "cancel-place").getBoolean(true)) {
            Sponge.getEventManager().registerListener(UltimateCore.get(), ChangeBlockEvent.Place.class, event -> {
                if (event.getCause().first(Player.class).isPresent()) {
                    Player p = event.getCause().first(Player.class).get();
                    UltimateUser up = UltimateCore.get().getUserService().getUser(p);
                    for (Warmup warmup : up.get(CommandtimerKeys.USER_WARMUPS).get().values()) {
                        Messages.send(p, "commandtimer.warmup.cancel.place");
                        warmup.cancel();
                    }
                }
            });
        }
    }
}
