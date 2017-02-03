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
package bammerbom.ultimatecore.sponge.modules.connectionmessages.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.language.utils.TextUtil;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.connectionmessages.api.ConnectionmessagesKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

import java.time.Instant;

public class ConnectionmessagesListener {
    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        Player p = e.getTargetEntity();
        ModuleConfig config = Modules.CONNECTIONMESSAGES.get().getConfig().get();

        //Firstjoin message
        Instant first = p.getJoinData().firstPlayed().get();
        Instant last = p.getJoinData().lastPlayed().get();
        Long diff = first.getEpochSecond() - last.getEpochSecond();

        //TODO better way. User files?
        if (diff < 2 && diff > -2) {
            //User joined for the first time
            if (config.get().getNode("firstjoin", "enable").getBoolean()) {
                Text fjmessage = Messages.toText(config.get().getNode("firstjoin", "format").getString());
                fjmessage = VariableUtil.replaceVariables(fjmessage, p);
                Sponge.getServer().getBroadcastChannel().send(fjmessage);
            }
        }

        //Join message
        if (!config.get().getNode("join", "enable").getBoolean()) {
            e.setMessageCancelled(true);
        }
        if (config.get().getNode("join", "enable-edit").getBoolean()) {
            Text jmessage = Messages.toText(config.get().getNode("join", "format").getString());
            jmessage = VariableUtil.replaceVariables(jmessage, p);
            e.setMessage(jmessage);
        }

        //Changed name message
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(ConnectionmessagesKeys.LASTNAME).isPresent()) {
            if (!p.getName().equalsIgnoreCase(up.get(ConnectionmessagesKeys.LASTNAME).get())) {
                Text jmessage = Messages.toText(config.get().getNode("changename", "format").getString());
                jmessage = TextUtil.replace(jmessage, "%oldname%", Text.of(up.get(ConnectionmessagesKeys.LASTNAME).get()));
                jmessage = VariableUtil.replaceVariables(jmessage, p);
                e.setMessage(jmessage);
            }
        }
        up.offer(ConnectionmessagesKeys.LASTNAME, p.getName());
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect e) {
        Player p = e.getTargetEntity();
        ModuleConfig config = Modules.CONNECTIONMESSAGES.get().getConfig().get();
        //Quit message
        if (!config.get().getNode("quit", "enable").getBoolean()) {
            e.setMessageCancelled(true);
        }
        if (config.get().getNode("quit", "enable-edit").getBoolean()) {
            Text jmessage = Messages.toText(config.get().getNode("quit", "format").getString());
            jmessage = VariableUtil.replaceVariables(jmessage, p);
            e.setMessage(jmessage);
        }
    }
}
