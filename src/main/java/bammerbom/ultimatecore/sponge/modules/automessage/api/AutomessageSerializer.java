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
package bammerbom.ultimatecore.sponge.modules.automessage.api;

import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.language.utils.TextUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.stream.Collectors;

public class AutomessageSerializer implements TypeSerializer<Automessage> {
    @Override
    public Automessage deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
        Automessage am = new Automessage();

        am.enable = node.getNode("enable").getBoolean(false);
        am.time = node.getNode("time").getInt(120);
        am.random = node.getNode("random").getString("randomorder");
        am.messages = ImmutableList.copyOf(node.getNode("messages").getList(TypeToken.of(String.class)).stream().map(Messages::toText).map(Text::trim).map(text -> TextUtil.replace(text, "\\n", Text.of("\n"))).collect(Collectors.toList()));

        am.chat = node.getNode("chat-enabled").getBoolean(false);

        am.actionbar = node.getNode("actionbar-enabled").getBoolean(false);
        am.actionbar_stay = node.getNode("actionbar-stay").getInt(10);

        am.bossbar = node.getNode("bossbar-enabled").getBoolean(false);
        am.bossbar_stay = node.getNode("bossbar-stay").getInt(10);
        am.bossbar_decrease = node.getNode("bossbar-decrease").getBoolean(true);
        am.bossbar_color = Sponge.getRegistry().getType(BossBarColor.class, node.getNode("bossbar-color").getString("GREEN")).orElse(BossBarColors.GREEN);
        am.bossbar_style = Sponge.getRegistry().getType(BossBarOverlay.class, node.getNode("bossbar-style").getString("PROGRESS")).orElse(BossBarOverlays.PROGRESS);

        am.title = node.getNode("title-enabled").getBoolean(false);
        am.title_fadein = node.getNode("title-fadein").getInt(1);
        am.title_stay = node.getNode("title-fadestay").getInt(8);
        am.title_fadeout = node.getNode("title-fadeout").getInt(1);

        return am;
    }

    @Override
    public void serialize(TypeToken<?> type, Automessage am, ConfigurationNode node) throws ObjectMappingException {
        Messages.log(Text.of(TextColors.RED, "Something tried to serialize an Automessage, which should never happen!"));
    }
}
