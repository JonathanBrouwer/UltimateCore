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
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.*;

public class AfkListener {

    static Integer afktime = r.getCnfg().getInt("Afk.AfkTime");
    static Integer kicktime = r.getCnfg().getInt("Afk.KickTime");
    static Boolean kickenabled = r.getCnfg().getBoolean("Afk.KickEnabled");

    public static void start() {
        if (r.getCnfg().getBoolean("Afk.Enabled")) {
            r.getGame().getEventManager().register(r.getUC(), new AfkListener());
            r.getGame().getScheduler().getTaskBuilder().delay(100L).interval(100L).name("UC: Afk task").execute(new Runnable() {
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
                                r.getGame().getServer().getBroadcastSink().sendMessage(r.mes("afkAfk", "%Player", UC.getPlayer(pl).getDisplayName()));
                            }
                        }
                        if (dif > kicktime) {
                            if (kickenabled) {
                                if (!r.perm(pl, "uc.afk.exempt", false, false)) {
                                    pl.kick(r.mes("afkKick"));
                                }
                            }
                        }

                    }
                }
            }).submit(r.getUC());
        }
    }

    @Subscribe(order = Order.POST)
    public void event(PlayerEvent e) {
        if (e instanceof PlayerChangeHealthEvent || e instanceof PlayerChangeGameModeEvent || e instanceof PlayerMoveEvent || e instanceof PlayerResourcePackStatusEvent || e instanceof
                PlayerUpdateEvent) {
            return;
        }
        UC.getPlayer(e.getUser()).updateLastActivity();

    }

    @Subscribe(order = Order.POST)
    public void playerQuit(PlayerQuitEvent e) {
        if (UC.getPlayer(e.getUser()).isAfk()) {
            UC.getPlayer(e.getUser()).setAfk(false);
        }
        UC.getPlayer(e.getUser()).updateLastActivity();
    }

    @Subscribe(order = Order.POST)
    public void playerKick(PlayerKickEvent e) {
        if (UC.getPlayer(e.getUser()).isAfk()) {
            UC.getPlayer(e.getUser()).setAfk(false);
        }
        UC.getPlayer(e.getUser()).updateLastActivity();
    }

}
