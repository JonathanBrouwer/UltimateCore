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
package bammerbom.ultimatecore.sponge.api.teleport.utils;

import org.spongepowered.api.data.property.AbstractProperty;
import org.spongepowered.api.data.property.block.PassableProperty;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class LocationUtil {

    public static Optional<Location<World>> getHighestLoc(Location<World> loc) {
        Optional<Integer> y = getHighestY(loc.getExtent(), loc.getX(), loc.getZ());
        return y.map(integer -> new Location<>(loc.getExtent(), loc.getX(), integer, loc.getZ()));
    }

    /**
     * Gets the highest y of the specified location, or Optional#empty if not possible.
     *
     * @param w The world
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The y coordinate of the highest solid block
     */
    public static Optional<Integer> getHighestY(World w, Integer x, Integer z) {
        return getHighestY(w, (double) x, (double) z);
    }

    /**
     * Gets the highest y of the specified location, or Optional#empty if not possible.
     *
     * @param w The world
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The y coordinate of the highest solid block
     */
    public static Optional<Integer> getHighestY(World w, Double x, Double z) {
        int y = w.getBlockMax().getY();
        while (isPassable(w, x, y, z)) {
            y = y - 1;
            if (y <= 0) {
                return Optional.empty();
            }
        }
        return Optional.of(y);
    }

    private static boolean isPassable(World w, Double x, int y, Double z) {
        Optional<PassableProperty> prop = new Location<>(w, x, y, z).getBlock().getProperty(PassableProperty.class);
        return prop.map(AbstractProperty::getValue).orElse(false);
    }
}
