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
package bammerbom.ultimatecore.sponge.modules.spawn.utils;

import bammerbom.ultimatecore.sponge.api.data_old.GlobalData;
import bammerbom.ultimatecore.sponge.api.teleport.serializabletransform.SerializableTransform;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class SpawnUtil {
    public static SerializableTransform getSpawnLocation(Player p) {
        //Global spawn
        SerializableTransform loc = GlobalData.get(SpawnKeys.GLOBAL_SPAWN).orElse(null);

        //Check group spawn
        HashMap<String, SerializableTransform> spawns = GlobalData.get(SpawnKeys.GROUP_SPAWNS).get();
        String group = SpawnPermissions.UC_SPAWN_GROUPSPAWN.getFor(p);
        if (group != null && spawns.containsKey(group.toLowerCase())) {
            loc = spawns.get(group);
        }

        //World spawn
        if (loc == null) {
            loc = new SerializableTransform(new Transform<>(p.getWorld().getSpawnLocation()));
        }

        return loc;
    }
}
