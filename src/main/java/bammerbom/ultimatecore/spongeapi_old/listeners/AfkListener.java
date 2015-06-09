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
package bammerbom.ultimatecore.spongeapi_old.listeners;

import bammerbom.ultimatecore.spongeapi_old.api.UC;
import bammerbom.ultimatecore.spongeapi_old.r;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerEvent;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.text.Texts;

public class AfkListener {

    static Integer afktime = r.getCnfg().getInt("Afk.AfkTime");
    static Integer kicktime = r.getCnfg().getInt("Afk.KickTime");
    static Boolean kickenabled = r.getCnfg().getBoolean("Afk.KickEnabled");

    public static void start() {
        if (r.getCnfg().getBoolean("Afk.Enabled")) {
            r.getGame().getEventManager().register(r.getUC(), new AfkListener());
            r.getGame().getSyncScheduler().runRepeatingTask(r.getUC(), new Runnable() {
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
                                r.getGame().getServer().broadcastMessage(r.mes("afkAfk", "%Player", UC.getPlayer(pl).getDisplayName()));
                            }
                        }
                        if (dif > kicktime) {
                            if (kickenabled) {
                                if (!r.perm(pl, "uc.afk.excempt", false, false)) {
                                    pl.kick(Texts.of(r.mes("afkKick").toString()));
                                }
                            }
                        }

                    }
                }
            }, 100L);
        }
    }

    @Subscribe(order = Order.POST)
    public void event(PlayerEvent e) {
        if (e instanceof PlayerMoveEvent && !((PlayerMoveEvent) e).getOldLocation().getBlockPosition().equals(((PlayerMoveEvent) e).getNewLocation().getBlockPosition())) {
            UC.getPlayer(e.getPlayer()).updateLastActivity();
        }
        UC.getPlayer(e.getPlayer()).updateLastActivity();
    }

    @Subscribe(order = Order.POST)
    public void event(CommandEvent e) {
        if (!e.getCommand().equalsIgnoreCase("afk") && r.isPlayer(e.getSource())) {
            UC.getPlayer((Player) e.getSource()).updateLastActivity();
        }
    }

}
