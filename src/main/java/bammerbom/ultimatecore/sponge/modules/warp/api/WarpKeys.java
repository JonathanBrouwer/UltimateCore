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
package bammerbom.ultimatecore.sponge.modules.warp.api;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.config.datafiles.WorldDataFile;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WarpKeys {
    public static Key.Global<List<Warp>> WARPS = new Key.Global<>("warps", new KeyProvider.Global<List<Warp>>() {
        @Override
        public List<Warp> load(Game arg) {
            List<Warp> warps = new ArrayList<>();
            for (World world : Sponge.getServer().getWorlds()) {
                WorldDataFile loader = new WorldDataFile(world.getUniqueId());
                CommentedConfigurationNode node = loader.get();
                for (CommentedConfigurationNode wnode : node.getNode("warps").getChildrenMap().values()) {
                    try {
                        warps.add(wnode.getValue(TypeToken.of(Warp.class)));
                    } catch (ObjectMappingException e) {
                        Messages.log(Messages.getFormatted("warp.command.warp.invalidwarp", "%warp%", wnode.getNode("name").getString()));
                    }
                }
            }
            return warps;
        }

        @Override
        public void save(Game arg, List<Warp> data) {
            //Sort warps by world
            HashMap<UUID, List<Warp>> worldWarps = new HashMap<>();
            for (Warp warp : data) {
                List<Warp> warps = worldWarps.containsKey(warp.getLocation().getExtent().getUniqueId()) ? worldWarps.get(warp.getLocation().getExtent().getUniqueId()) : new ArrayList<>();
                warps.add(warp);
                worldWarps.put(warp.getLocation().getExtent().getUniqueId(), warps);
            }

            for (UUID uuid : worldWarps.keySet()) {
                WorldDataFile loader = new WorldDataFile(uuid);
                CommentedConfigurationNode node = loader.get();
                node.getNode("warps").getChildrenMap().keySet().forEach(node.getNode("warps")::removeChild);
                List<Warp> warps = worldWarps.get(uuid);
                for (Warp warp : warps) {
                    try {
                        node.getNode("warps", warp.getName()).setValue(TypeToken.of(Warp.class), warp);
                    } catch (ObjectMappingException e) {
                        ErrorLogger.log(e, "Failed to load warp. (Invalid world?)");
                    }
                }
                loader.save(node);
            }

        }
    });
}
