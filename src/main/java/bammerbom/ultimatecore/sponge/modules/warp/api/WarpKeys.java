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
import bammerbom.ultimatecore.sponge.config.datafiles.DataFile;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpKeys {
    public static Key.Global<List<Warp>> WARPS = new Key.Global<>("warps", new KeyProvider<List<Warp>, Game>() {
        @Override
        public List<Warp> load(Game arg) {
            CommentedConfigurationNode node = DataFile.get("warps");
            List<Warp> warps = new ArrayList<>();
            for (CommentedConfigurationNode wnode : node.getChildrenMap().values()) {
                try {
                    warps.add(wnode.getValue(TypeToken.of(Warp.class)));
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
            return warps;
        }

        @Override
        public void save(Game arg, List<Warp> data) {
            ConfigurationLoader<CommentedConfigurationNode> loader = DataFile.getLoader("warps");
            CommentedConfigurationNode node = DataFile.get("warps");
            //Remove all warps from node
            node.getChildrenMap().keySet().forEach(node::removeChild);
            //Set new warps to node
            for (Warp warp : data) {
                try {
                    node.getNode(warp.getName()).setValue(TypeToken.of(Warp.class), warp);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
            try {
                loader.save(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
}
