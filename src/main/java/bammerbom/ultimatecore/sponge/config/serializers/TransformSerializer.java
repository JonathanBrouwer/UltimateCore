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

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class TransformSerializer implements TypeSerializer<Transform> {
    @Override
    public Transform<World> deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
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
        double scax = node.getNode("scax").getDouble();
        double scay = node.getNode("scay").getDouble();
        double scaz = node.getNode("scaz").getDouble();
        Vector3d sca = new Vector3d(scax, scay, scaz);
        return new Transform<>(loc, rot, sca);
    }

    @Override
    public void serialize(TypeToken<?> type, Transform loc, ConfigurationNode node) throws ObjectMappingException {
        node.getNode("world").setValue(TypeToken.of(UUID.class), loc.getExtent().getUniqueId());
        node.getNode("locx").setValue(loc.getLocation().getX());
        node.getNode("locy").setValue(loc.getLocation().getY());
        node.getNode("locz").setValue(loc.getLocation().getZ());
        node.getNode("rotx").setValue(loc.getRotation().getX());
        node.getNode("roty").setValue(loc.getRotation().getY());
        node.getNode("rotz").setValue(loc.getRotation().getZ());
        node.getNode("scax").setValue(loc.getScale().getX());
        node.getNode("scay").setValue(loc.getScale().getY());
        node.getNode("scaz").setValue(loc.getScale().getZ());
    }
}
