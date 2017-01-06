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
package bammerbom.ultimatecore.sponge.modules.jail.api;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

/**
 * The Jail class represents a jail.
 * So this is a physical place where a player CAN be jailed.
 */
public class Jail {
    String name;
    String description;
    Transform<World> location;

    public Jail(String name, String description, Transform<World> location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Transform<World> getLocation() {
        return location;
    }

    public static class JailSerializer implements TypeSerializer<Jail> {
        @Override
        public Jail deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            String name = node.getNode("name").getString();
            String description = node.getNode("description").getString();
            Transform<World> location = node.getNode("location").getValue(TypeToken.of(Transform.class));
            return new Jail(name, description, location);
        }

        @Override
        public void serialize(TypeToken<?> type, Jail jail, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("name").setValue(jail.getName());
            node.getNode("description").setValue(jail.getDescription());
            node.getNode("location").setValue(TypeToken.of(Transform.class), jail.getLocation());
        }
    }
}
