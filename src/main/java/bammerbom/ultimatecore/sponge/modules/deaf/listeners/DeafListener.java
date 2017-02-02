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
package bammerbom.ultimatecore.sponge.modules.deaf.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.deaf.api.Deaf;
import bammerbom.ultimatecore.sponge.modules.deaf.api.DeafKeys;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;

import java.util.ArrayList;

public class DeafListener {
    @Listener
    public void onChat(MessageChannelEvent.Chat event) {
        //Receiver
        MutableMessageChannel channel = event.getChannel().orElse(event.getOriginalChannel()).asMutable();
        for (MessageReceiver receiver : new ArrayList<>(channel.getMembers())) {
            if (!(receiver instanceof Player)) {
                continue;
            }
            Player p = (Player) receiver;
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            if (up.get(DeafKeys.DEAF).isPresent()) {
                channel.removeMember(receiver);
            }
        }
        event.setChannel(channel);

        //Sender
        Player p = event.getCause().first(Player.class).orElse(null);
        if (p == null) return;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(DeafKeys.DEAF).isPresent()) {
            Deaf deaf = up.get(DeafKeys.DEAF).get();
            event.setCancelled(true);
            p.sendMessage(Messages.getFormatted(p, "deaf.deafed", "%time%", (deaf.getEndtime() == -1L ? Messages.getFormatted("core.time.ever") : Text.of(TimeUtil.formatDateDiff(deaf.getEndtime()))), "%reason%", deaf.getReason()));
        }
    }
}
