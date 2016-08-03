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
package bammerbom.ultimatecore.sponge.utils;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class ExtendedLocation {

    /**
     * Only used for testing, shouldn't be used anywhere else
     */
    public static ExtendedLocation ZERO = new ExtendedLocation(Sponge.getServer().getWorlds().iterator().next(), 0, 0, 0, 0, 0, 0);

    Location<World> location;
    Vector3d rotation;

    public ExtendedLocation(Location<World> loc, Vector3d rot) {
        location = loc;
        rotation = rot;
    }

    public ExtendedLocation(World w, double lx, double ly, double lz, double rx, double ry, double rz) {
        this(new Location<>(w, lx, ly, lz), new Vector3d(rx, ry, rz));
    }

    public Location<World> getLocation() {
        return location;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    public World getWorld() {
        return location.getExtent();
    }

    public double getLocationX() {
        return location.getX();
    }

    public double getLocationY() {
        return location.getY();
    }

    public double getLocationZ() {
        return location.getZ();
    }

    public double getRotationX() {
        return rotation.getX();
    }

    public double getRotationY() {
        return rotation.getY();
    }

    public double getRotationZ() {
        return rotation.getZ();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExtendedLocation)) {
            return false;
        }
        ExtendedLocation eloc = (ExtendedLocation) object;
        if (!(eloc.getLocation().getExtent().getUniqueId() == location.getExtent().getUniqueId())) {
            return false;
        }
        if (!(eloc.getLocation().getX() == location.getX())) {
            return false;
        }
        if (!(eloc.getLocation().getY() == location.getY())) {
            return false;
        }
        if (!(eloc.getLocation().getZ() == location.getZ())) {
            return false;
        }
        if (!(eloc.getRotation().getX() == rotation.getX())) {
            return false;
        }
        if (!(eloc.getRotation().getY() == rotation.getY())) {
            return false;
        }
        if (!(eloc.getRotation().getZ() == rotation.getZ())) {
            return false;
        }
        return true;
    }

}
