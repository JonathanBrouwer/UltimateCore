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
package bammerbom.ultimatecore.sponge.modules.kit.api;

import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;

public class Kit {
    String name;
    Text description;
    List<ItemStackSnapshot> items;
    List<String> commands;
    Long cooldown;

    public Kit(String id, Text description, List<ItemStackSnapshot> items, List<String> commands, Long cooldown) {
        this.name = id;
        this.description = description;
        this.items = items;
        this.commands = commands;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public void setName(String id) {
        this.name = id;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public List<ItemStackSnapshot> getItems() {
        return items;
    }

    public void setItems(List<ItemStackSnapshot> items) {
        this.items = items;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    public Long getCooldown() {
        return cooldown;
    }

    public void setCooldown(Long cooldown) {
        this.cooldown = cooldown;
    }

    public static class KitSerializer implements TypeSerializer<Kit> {

        @Override
        public Kit deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            String id = node.getNode("id").getString();
            Text desc = Messages.toText(node.getNode("description").getString());
            List<ItemStackSnapshot> items = node.getNode("items").getList(TypeToken.of(ItemStackSnapshot.class));
            List<String> cmds = node.getNode("commands").getList(TypeToken.of(String.class));
            Long cooldown = TimeUtil.parse(node.getNode("cooldown").getString());
            return new Kit(id, desc, items, cmds, cooldown);
        }

        @Override
        public void serialize(TypeToken<?> type, Kit kit, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("id").setValue(kit.getName());
            node.getNode("description").setValue(TextSerializers.JSON.serialize(kit.getDescription()));
            node.getNode("items").setValue(new TypeToken<List<ItemStackSnapshot>>() {
            }, kit.getItems());
            node.getNode("commands").setValue(kit.getCommands());
            node.getNode("cooldown").setValue(kit.getCooldown());
        }
    }
}
