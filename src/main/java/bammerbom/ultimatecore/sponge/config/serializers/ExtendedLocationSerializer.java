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
package bammerbom.ultimatecore.sponge.config.serializers;

import bammerbom.ultimatecore.sponge.utils.ExtendedLocation;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class ExtendedLocationSerializer implements TypeSerializer<ExtendedLocation> {
    @Override
    public ExtendedLocation deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
        World world = Sponge.getServer().getWorld(node.getNode("world").getValue(TypeToken.of(UUID.class))).orElse(null);
        if (world == null) throw new ObjectMappingException("World could not be found.");
        double locx = node.getNode("locx").getDouble();
        double locy = node.getNode("locy").getDouble();
        double locz = node.getNode("locz").getDouble();
        Location<World> loc = world.getLocation(locx, locy, locz);
        double rotx = node.getNode("rotx").getDouble();
        double roty = node.getNode("roty").getDouble();
        double rotz = node.getNode("rotz").getDouble();
        Vector3d rot = new Vector3d(rotx, roty, rotz);
        return new ExtendedLocation(loc, rot);
    }

    @Override
    public void serialize(TypeToken<?> type, ExtendedLocation loc, ConfigurationNode node) throws ObjectMappingException {
        node.getNode("world").setValue(TypeToken.of(UUID.class), loc.getWorld().getUniqueId());
        node.getNode("locx").setValue(loc.getLocationX());
        node.getNode("locy").setValue(loc.getLocationY());
        node.getNode("locz").setValue(loc.getLocationZ());
        node.getNode("rotx").setValue(loc.getRotationX());
        node.getNode("roty").setValue(loc.getRotationY());
        node.getNode("rotz").setValue(loc.getRotationZ());
    }
}
