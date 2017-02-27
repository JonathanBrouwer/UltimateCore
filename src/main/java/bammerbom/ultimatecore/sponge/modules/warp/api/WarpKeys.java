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
import bammerbom.ultimatecore.sponge.api.data.providers.GlobalKeyProvider;
import com.google.common.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WarpKeys {
    public static Key.Global<List<Warp>> WARPS = new Key.Global<>("warps", new GlobalKeyProvider<>("warps", "warps", new TypeToken<List<Warp>>() {
    }, new ArrayList<>()));


            /*new KeyProvider.Global<List<Warp>>() {
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
            for (World world : Sponge.getServer().getWorlds()) {
                WorldDataFile loader = new WorldDataFile(world.getUniqueId());
                CommentedConfigurationNode node = loader.get();
                node.getNode("warps").setValue(null);
                List<Warp> warps = worldWarps.containsKey(world.getUniqueId()) ? worldWarps.get(world.getUniqueId()) : new ArrayList<>();
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
    });*/
}
