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
package bammerbom.ultimatecore.sponge.modules.home.api;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

public class Home {
    String name;
    Transform<World> location;

    public Home(String name, Transform<World> location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Transform<World> getLocation() {
        return location;
    }

    public static class HomeSerializer implements TypeSerializer<Home> {
        @Override
        public Home deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            String name = node.getNode("name").getString();
            Transform<World> location = node.getNode("location").getValue(TypeToken.of(Transform.class));
            return new Home(name, location);
        }

        @Override
        public void serialize(TypeToken<?> type, Home home, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("name").setValue(home.getName());
            node.getNode("location").setValue(TypeToken.of(Transform.class), home.getLocation());
        }
    }
}
