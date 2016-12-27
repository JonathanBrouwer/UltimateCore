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
package bammerbom.ultimatecore.sponge.modules.chat.listeners;

import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.config.ModuleConfig;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ChatListener {
    @Listener(order = Order.FIRST)
    public void onChat(MessageChannelEvent.Chat e) {
        ModuleConfig config = Modules.CHAT.get().getConfig().get();
        CommentedConfigurationNode node = config.get();
        CommandSource p = e.getCause().first(CommandSource.class).orElse(null);
        if (p == null) return;

        //First load general header/body/footer
        String header = node.getNode("default").getNode("header").getString();
        String body = node.getNode("default").getNode("body").getString();
        String footer = node.getNode("default").getNode("footer").getString();

        //Check if the user is in any groups, if so replace the header/body/footer with that of the group
        List<Subject> subjects = p.getSubjectData().getParents(new HashSet<>());
        List<String> subjectnames = new ArrayList<>();
        for (Subject su : subjects) {
            subjectnames.add(su.getIdentifier());
        }
        Map<Object, ? extends CommentedConfigurationNode> children = node.getNode("groups").getChildrenMap();
        for (Object o : children.keySet()) {
            if (subjectnames.contains(o.toString())) {
                CommentedConfigurationNode subnode = children.get(o);
                header = subnode.getNode("header").getString(header);
                body = subnode.getNode("body").getString(body);
                footer = subnode.getNode("footer").getString(footer);
            }
        }

        //Replace stuff
        Text fheader = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(header), p), "%message%", e.getRawMessage());
        Text fbody = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(body), p), "%message%", e.getRawMessage());
        Text ffooter = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(footer), p), "%message%", e.getRawMessage());

        e.getFormatter().setHeader(fheader);
        e.getFormatter().setBody(fbody);
        e.getFormatter().setFooter(ffooter);
    }
}
