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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.personalmessage.api.PersonalmessageEvent;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyKeys;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;

import java.util.Arrays;
import java.util.List;

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
                if (!p.hasPermission(SpyPermissions.UC_SPY_MESSAGESPY_SEE.get())) {
                    continue;
                }
                if (s.getIdentifier().equals(p.getIdentifier()) || t.getIdentifier().equals(p.getIdentifier())) {
                    continue;
                }
                if (!UltimateCore.get().getUserService().getUser(p).get(SpyKeys.MESSAGESPY_ENABLED).get()) {
                    continue;
                }
                p.sendMessage(Messages.getFormatted("spy.format.messagespy", "%player%", VariableUtil.getNameSource(s), "%target%", VariableUtil.getNameSource(t), "%message%", ev.getPMUnformattedMessage()));
            }
        }
    }

    @Listener
    public void onCommand(SendCommandEvent e) {
        Player t = e.getCause().first(Player.class).orElse(null);
        if (t == null) return;
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (!p.hasPermission(SpyPermissions.UC_SPY_COMMANDSPY_SEE.get())) {
                continue;
            }
            if (p == t) {
                continue;
            }
            if (!UltimateCore.get().getUserService().getUser(p).get(SpyKeys.COMMANDSPY_ENABLED).get()) {
                continue;
            }
            //Ignored commands
            List<String> ignored = Arrays.asList("personalmessage", "pm", "dm", "msg", "w", "whisper", "tell", "reply", "respond", "r");
            if (ignored.contains(e.getCommand())) continue;
            p.sendMessage(Messages.getFormatted("spy.format.commandspy", "%player%", VariableUtil.getNameEntity(t), "%message%", e.getCommand() + " " + e.getArguments()));
        }
    }
}
