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
package bammerbom.ultimatecore.sponge.modules.votifier.api;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import com.google.common.reflect.TypeToken;
import com.vexsoftware.votifier.model.Vote;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;

import java.util.ArrayList;
import java.util.List;

public class VotifierKeys {
    public static Key.Global<List<Vote>> VOTES_CACHED = new Key.Global<>("votes-cached", new KeyProvider.Global<List<Vote>>() {
        @Override
        public List<Vote> load(Game game) {
            GlobalDataFile config = new GlobalDataFile("votifier");
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("votes-cached").getValue(new TypeToken<List<Vote>>() {
                }, new ArrayList<>());
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load global spawn.");
                return null;
            }
        }

        @Override
        public void save(Game game, List<Vote> data) {
            GlobalDataFile config = new GlobalDataFile("votifier");
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("votes-cached").setValue(new TypeToken<List<Vote>>() {
                }, data);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save global spawn.");
            }
            config.save(node);
        }
    });

    public static Key.User<Integer> VOTES_COUNT = new Key.User<>("votes-count", new KeyProvider.User<Integer>() {
        @Override
        public Integer load(UltimateUser user) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            return node.getNode("votes-count").getInt(0);
        }

        @Override
        public void save(UltimateUser user, Integer data) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            node.getNode("votes-count").setValue(data);
            config.save(node);
        }
    });
}
