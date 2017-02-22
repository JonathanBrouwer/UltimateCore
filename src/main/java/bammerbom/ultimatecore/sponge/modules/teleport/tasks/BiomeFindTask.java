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
package bammerbom.ultimatecore.sponge.modules.teleport.tasks;

import bammerbom.ultimatecore.sponge.api.teleport.utils.LocationUtil;
import bammerbom.ultimatecore.sponge.utils.RandomUtil;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.MutableBiomeVolume;

import java.util.Optional;

public class BiomeFindTask {
    static Integer maxDistance = 100000;
    static Integer maxTries = 2000;

    public static Optional<Location<World>> findBiome(Player p, BiomeType type) {
        int tries = 0;
        TeleportHelper tph = Sponge.getGame().getTeleportHelper();

        while (tries < maxTries) {
            tries++;

            //Try to find suitable location
            Integer x = RandomUtil.nextInt(-maxDistance, maxDistance);
            Integer z = RandomUtil.nextInt(-maxDistance, maxDistance);

            //Check if biome matches
            MutableBiomeVolume volume = p.getWorld().getBiomeView(new Vector3i(x, 0, z), new Vector3i(x, 0, z)).getBiomeCopy();
            p.getWorld().getWorldGenerator().getBiomeGenerator().generateBiomes(volume);
            BiomeType biome = volume.getBiome(new Vector3i(x, 0, z));
            if (!biome.equals(type)) continue;

            //Get y + get safe location
            Integer y = LocationUtil.getHighestY(p.getWorld(), x, z).orElse(null);
            if (y == null) continue;
            Location<World> loc = tph.getSafeLocation(new Location<>(p.getWorld(), x, y, z)).orElse(null);
            if (loc == null) continue;

            return Optional.of(loc);
        }

        return Optional.empty();
    }
}
