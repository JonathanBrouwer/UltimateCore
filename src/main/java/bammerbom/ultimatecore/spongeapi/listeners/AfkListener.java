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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.item.inventory.TargetInventoryEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;

public class AfkListener {

    static Integer afktime = r.getCnfg().getInt("Afk.AfkTime");
    static Integer kicktime = r.getCnfg().getInt("Afk.KickTime");
    static Boolean kickenabled = r.getCnfg().getBoolean("Afk.KickEnabled");

    public static void start() {
        if (r.getCnfg().getBoolean("Afk.Enabled")) {
            Sponge.getEventManager().registerListeners(r.getUC(), new AfkListener());
            Sponge.getScheduler().createTaskBuilder().intervalTicks(100L).delayTicks(100L).name("UltimateCore afk thread").execute(new Runnable() {
                @Override
                public void run() {
                    for (Player pl : r.getOnlinePlayers()) {
                        Long time = UC.getPlayer(pl).getLastActivity();
                        Long seconds1 = time / 1000;
                        Long seconds2 = System.currentTimeMillis() / 1000;
                        Long dif = seconds2 - seconds1;
                        if (dif > afktime) {
                            if (!UC.getPlayer(pl).isAfk()) {
                                UC.getPlayer(pl).setAfk(true);
                                Sponge.getServer().getBroadcastChannel().send(r.mes("afkAfk", "%Player", UC.getPlayer(pl).getDisplayName()));
                            }
                        }
                        if (dif > kicktime) {
                            if (kickenabled) {
                                if (!r.perm(pl, "uc.afk.exempt", false)) {
                                    pl.kick(r.mes("afkKick"));
                                }
                            }
                        }
                        if (UC.getPlayer(pl).isAfk()) {
                            Text sub = (kickenabled && !r.perm(pl, "uc.afk.exempt", false) && dif > 1) ? r.mes("afkWarning2", "%Time", TextColorUtil.strip(DateUtil.formatDateDiff((
                                    (kicktime - dif) * 1000) + System.currentTimeMillis()))) : null;
                            pl.sendTitle(Title.builder().title(r.mes("afkWarning")).subtitle(sub).fadeIn(20).stay(120).fadeOut(20).build());
                        }

                    }
                }
            }).submit(r.getUC());
        }
    }

    @Listener(order = Order.POST)
    public void event(MessageChannelEvent.Chat e) {
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void event(TabCompleteEvent e) {
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void event(InteractEvent e) {
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void event(TargetInventoryEvent e) {
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void event(MoveEntityEvent e) {
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            if (!e.getFromTransform().getPosition().toInt().equals(e.getToTransform().getPosition().toInt())) {
                UC.getPlayer(optPlayer.get()).updateLastActivity();
            }
        }
    }

    @Listener(order = Order.POST)
    public void event(RespawnPlayerEvent e) {
        UC.getPlayer(e.getTargetEntity()).updateLastActivity();
    }

    @Listener(order = Order.POST)
    public void event(SneakToggleEvent e) { //TODO wait for api https://github.com/SpongePowered/SpongeAPI/issues/1281
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void playerJoin(ClientConnectionEvent.Login e) {
        UC.getPlayer(e.getTargetUser()).updateLastActivity();
    }

    @Listener(order = Order.POST)
    public void playerCommand(SendCommandEvent e) {
        if (e.getCommand().contains("afk")) {
            return;
        }
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void playerQuit(ClientConnectionEvent.Disconnect e) {
        if (UC.getPlayer(e.getTargetEntity()).isAfk()) {
            UC.getPlayer(e.getTargetEntity()).setAfk(false);
        }
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

    @Listener(order = Order.POST)
    public void playerKick(KickPlayerEvent e) {
        if (UC.getPlayer(e.getTargetEntity()).isAfk()) {
            UC.getPlayer(e.getTargetEntity()).setAfk(false);
        }
        Optional<Player> optPlayer = e.getCause().first(Player.class);
        if (optPlayer.isPresent()) {
            UC.getPlayer(optPlayer.get()).updateLastActivity();
        }
    }

}
