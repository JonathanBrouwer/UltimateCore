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
package bammerbom.ultimatecore.sponge.modules.spy.listeners;

import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.personalmessage.api.PersonalmessageEvent;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;

public class SpyListener {
    @Listener
    public void onWhisper(MessageChannelEvent e) {
        if (!Modules.PERSONALMESSAGE.isPresent()) {
            return;
        }
        if (e instanceof PersonalmessageEvent) {
            PersonalmessageEvent ev = (PersonalmessageEvent) e;
            CommandSource s = ev.getPMSender();
            CommandSource t = ev.getPMTarget();

            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                if (!p.hasPermission(SpyPermissions.UC_MESSAGESPY_SEE.get())) {
                    continue;
                }
                if (s.getIdentifier().equals(p.getIdentifier()) || t.getIdentifier().equals(p.getIdentifier())) {
                    continue;
                }
                p.sendMessage(Messages.getFormatted("spy.format.messagespy", "%player%", VariableUtil.getName(s), "%target%", VariableUtil.getName(t), "%message%", ev
                        .getPMUnformattedMessage()));
            }
        }
    }
}
