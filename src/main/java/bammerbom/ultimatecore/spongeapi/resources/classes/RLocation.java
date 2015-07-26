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
package bammerbom.ultimatecore.spongeapi.resources.classes;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

public class RLocation {

    Location location;
    Vector3d rot;

    public RLocation(Extent ex, Vector3d pos, Vector3d rota) {
        location = new Location(ex, pos);
        rot = rota;
    }

    public RLocation(Location loc, Vector3d rota) {
        location = loc;
        rot = rota;
    }

    public RLocation(Location loc, Double yaw, Double pitch) {
        this(loc, new Vector3d(yaw, pitch, 0.0));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location loc) {
        location = loc;
    }

    public Vector3d getRotation() {
        return rot;
    }

    public void setRotation(Vector3d rota) {
        rot = rota;
    }

    public Double getYaw() {
        return rot.getX();
    }

    public Double getPitch() {
        return rot.getY();
    }

    public Double getX() {
        return location.getX();
    }

    public Double getY() {
        return location.getY();
    }

    public Double getZ() {
        return location.getZ();
    }

    public void teleport(Entity en) {
        en.setLocation(location);
        en.setRotation(rot);
    }

}