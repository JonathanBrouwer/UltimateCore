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
package bammerbom.ultimatecore.sponge.modules.ban.listeners;

import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.ban.Ban;

public class BanListener {
    @Listener
    @IsCancelled(value = Tristate.UNDEFINED)
    public void onConnect(ClientConnectionEvent.Login event) {
        BanService service = Sponge.getServiceManager().provide(BanService.class).get();
        if (service.isBanned(event.getProfile())) {
            Ban ban = service.getBanFor(event.getProfile()).get();
            Long time = ban.getExpirationDate().map(date -> (date.toEpochMilli() - System.currentTimeMillis())).orElse(-1L);
            event.setCancelled(true);
            event.setMessage(Messages.getFormatted("ban.banned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", ban.getReason().orElse(Messages.getFormatted("ban.command.ban.defaultreason"))));
        } else if (service.isBanned(event.getConnection().getAddress().getAddress())) {
            Ban ban = service.getBanFor(event.getConnection().getAddress().getAddress()).get();
            Long time = ban.getExpirationDate().map(date -> (date.toEpochMilli() - System.currentTimeMillis())).orElse(-1L);
            event.setCancelled(true);
            event.setMessage(Messages.getFormatted("ban.ipbanned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", ban.getReason().orElse(Messages.getFormatted("ban.command.ban.defaultreason"))));
        }

    }
}
