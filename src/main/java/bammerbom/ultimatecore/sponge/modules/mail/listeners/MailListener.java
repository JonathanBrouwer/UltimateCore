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
package bammerbom.ultimatecore.sponge.modules.mail.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.modules.mail.api.MailKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.action.TextActions;

public class MailListener {
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        UltimateUser up = UltimateCore.get().getUserService().getUser(event.getTargetEntity());
        int unreadCount = up.get(MailKeys.UNREAD_MAIL).get();
        if (unreadCount == 0) return;
        event.getTargetEntity().sendMessage(Messages.getFormatted(event.getTargetEntity(), "mail.command.mail.newmail", "%count%", unreadCount).toBuilder().onHover(TextActions.showText(Messages.getFormatted(event.getTargetEntity(), "mail.command.mail.newmail.hover"))).onClick(TextActions.runCommand("/mail read")).build());
    }
}
