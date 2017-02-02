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
package bammerbom.ultimatecore.sponge.modules.votifier.api;

import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.stream.Collectors;

public class VotifierSchemeSerializer implements TypeSerializer<VotifierScheme> {
    @Override
    public VotifierScheme deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
        boolean enabled = node.getNode("enable").getBoolean();
        double chance = node.getNode("chance").getDouble();
        String perm = node.getNode("permission").getString();
        List<String> commands = node.getNode("commands").getList(TypeToken.of(String.class));
        List<Text> messages = node.getNode("broadcast").getList(TypeToken.of(String.class)).stream().map(Messages::toText).collect(Collectors.toList());
        return new VotifierScheme(enabled, chance, perm, commands, messages);
    }

    @Override
    public void serialize(TypeToken<?> type, VotifierScheme scheme, ConfigurationNode node) throws ObjectMappingException {
        node.getNode("enable", scheme.isEnabled());
        node.getNode("chance", scheme.getChance());
        node.getNode("permission", scheme.getPermission());
        node.getNode("commands", scheme.getCommands());
        node.getNode("broadcast", scheme.getMessages().stream().map(TextSerializers.JSON::serialize).collect(Collectors.toList()));
    }
}
