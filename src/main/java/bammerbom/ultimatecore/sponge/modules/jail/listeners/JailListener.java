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
package bammerbom.ultimatecore.sponge.modules.jail.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.data.event.DataOfferEvent;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailData;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.utils.SpawnUtil;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class JailListener {

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player p = event.getTargetEntity();
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(JailKeys.JAIL).isPresent()) {
            if (Modules.JAIL.get().getConfig().get().get().getNode("teleport-on-join").getBoolean()) {
                JailData data = up.get(JailKeys.JAIL).get();
                Jail jail = GlobalData.get(JailKeys.JAILS).get().stream().filter(j -> j.getName().equalsIgnoreCase(data.getJail())).findAny().orElse(null);
                if (jail != null) {
                    Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), jail.getLocation(), tel -> {
                    }, (tel, reason) -> {
                    }, false, true);
                    tp.start();
                }
            }
        }
    }

    @Listener(order = Order.POST)
    public void onJailSwitch(DataOfferEvent<JailData> event, @First Player p) {
        if (event.getKey().equals(JailKeys.JAIL)) {
            if (event.getValue().isPresent()) {
                //Player is jailed
                JailData data = event.getValue().get();
                Jail jail = GlobalData.get(JailKeys.JAILS).get().stream().filter(j -> j.getName().equalsIgnoreCase(data.getJail())).findAny().orElse(null);
                if (jail != null) {
                    Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), jail.getLocation(), tel -> {
                    }, (tel, reason) -> {
                    }, false, true);
                    tp.start();
                }
            } else {
                //Player is unjailed
                if (Modules.JAIL.get().getConfig().get().get().getNode("spawn-on-unjail").getBoolean()) {
                    Transform<World> loc;
                    if (Modules.SPAWN.isPresent()) {
                        loc = SpawnUtil.getSpawnLocation(p);
                    } else {
                        loc = new Transform<>(p.getWorld().getSpawnLocation());
                    }
                    Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), loc, tel -> {
                    }, (tel, reason) -> {
                    }, false, true);
                    tp.start();
                }
            }
        }
    }

    @Listener
    public void onCommand(SendCommandEvent event, @First Player p) {
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        JailData data = up.get(JailKeys.JAIL).orElse(null);
        if (data != null) {
            try {
                List<String> allowedcommands = Modules.JAIL.get().getConfig().get().get().getNode("allowed-commands").getList(TypeToken.of(String.class));
                if (!allowedcommands.contains(event.getCommand())) {
                    event.setCancelled(true);
                    Messages.send(p, "jail.event.command", "%time%", (data.getEndtime() == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.formatDateDiff(data.getEndtime())), "%reason%", data.getReason());
                }
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load allowed commands while jailed from config.");
            }
        }
    }

    @Listener
    public void onBlockChange(ChangeBlockEvent event, @First Player p) {
        if (event instanceof ChangeBlockEvent.Pre || event instanceof ChangeBlockEvent.Post) {
            return;
        }
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(JailKeys.JAIL).isPresent()) {
            if (!Modules.JAIL.get().getConfig().get().get().getNode("allow-block-modify").getBoolean()) {
                JailData data = up.get(JailKeys.JAIL).get();
                event.setCancelled(true);
                Messages.send(p, "jail.event.block", "%time%", (data.getEndtime() == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.formatDateDiff(data.getEndtime())), "%reason%", data.getReason());
            }
        }
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event, @First Player p) {
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(JailKeys.JAIL).isPresent()) {
            if (!Modules.JAIL.get().getConfig().get().get().getNode("allow-chat").getBoolean()) {
                JailData data = up.get(JailKeys.JAIL).get();
                event.setCancelled(true);
                Messages.send(p, "jail.event.chat", "%time%", (data.getEndtime() == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.formatDateDiff(data.getEndtime())), "%reason%", data.getReason());
            }
        }
    }
}
