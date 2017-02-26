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

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.language.utils.TextUtil;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.chat.api.ChatPermissions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.serializer.TextSerializers;

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
        String group = ChatPermissions.UC_CHAT_GROUP.getFor(p);
        if (group != null && !node.getNode("groups", group).isVirtual()) {
            CommentedConfigurationNode subnode = node.getNode("groups", group);
            header = subnode.getNode("header").getString(header);
            body = subnode.getNode("body").getString(body);
            footer = subnode.getNode("footer").getString(footer);
        }

        //TODO use text instead of string
        String rawmessage = e.getRawMessage().toPlain();
        for (TextColor color : Sponge.getRegistry().getAllOf(CatalogTypes.TEXT_COLOR)) {
            if (!p.hasPermission("uc.chat.color." + color.getId().toLowerCase())) {
                continue;
            }
            Character ch = TextUtil.getColorChar(color);
            rawmessage = rawmessage.replaceAll("&" + ch, "\u00A7" + ch);
            //rawmessage = TextUtil.replace(rawmessage, );
            //rawmessage = TextUtil.replace(rawmessage, "&" + , Text.of(color, "a"));
        }
        for (TextStyle.Base style : Sponge.getRegistry().getAllOf(TextStyle.Base.class)) {
            if (!p.hasPermission("uc.chat.style." + style.getId().toLowerCase())) {
                continue;
            }
            Character ch = TextUtil.getStyleChar(style);
            rawmessage = rawmessage.replaceAll("&" + ch, "\u00A7" + ch);
            //rawmessage = TextUtil.replace(rawmessage, "&", Text.of("§"));
        }
        Text message = TextSerializers.LEGACY_FORMATTING_CODE.deserialize(rawmessage);

        //Replace stuff
        Text fheader = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(header), p), "%message%", message);
        Text fbody = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(body), p), "%message%", message);
        Text ffooter = TextUtil.replace(VariableUtil.replaceVariables(Messages.toText(footer), p), "%message%", message);

        e.getFormatter().setHeader(fheader);
        e.getFormatter().setBody(fbody);
        e.getFormatter().setFooter(ffooter);
    }
}
