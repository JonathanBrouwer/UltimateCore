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
package bammerbom.ultimatecore.sponge.api.config;

import bammerbom.ultimatecore.sponge.api.config.datafiles.DataFile;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.asset.Asset;

import java.util.ArrayList;
import java.util.Map;

public class ConfigCompleter {
    /**
     * This method completes a datafile.
     * Any fields that are in the asset provided but not in the datafile will be set in the datafile.
     *
     * @param file  The file to complete
     * @param asset The asset with the default values
     * @return Whether anything was added to the file
     */
    public static boolean complete(DataFile file, Asset asset) {
        try {
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setURL(asset.getUrl()).build();
            CommentedConfigurationNode assetnode = loader.load();
            CommentedConfigurationNode sourcenode = file.get();

//            if (complete(assetnode, sourcenode)) {
//                file.save(assetnode);
//                return true;
//            }
            return false;
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Config completion failed for " + file + " / " + asset);
            return false;
        }
    }

    private static boolean complete(CommentedConfigurationNode assetnode, CommentedConfigurationNode sourcenode) {
        boolean changed = false;
        //Children of sourcenode && assetnode
        Map<Object, ? extends CommentedConfigurationNode> sourcenodes = sourcenode.getChildrenMap();
        Map<Object, ? extends CommentedConfigurationNode> assetnodes = assetnode.getChildrenMap();

        //For each child of assetnode
        for (Object name : new ArrayList<>(assetnode.getChildrenMap().keySet())) {
            //The current child of assetnode which is being tested
            CommentedConfigurationNode node = assetnode.getNode(name);
            //If it doesnt contain the current key
            if (!sourcenodes.containsKey(name)) {
                CommentedConfigurationNode newnode = sourcenode.getNode(name);
                newnode.setValue(node.getValue());
                newnode.setComment(node.getComment().orElse(null));
                changed = true;
            }
        }
        return changed;
    }
}
