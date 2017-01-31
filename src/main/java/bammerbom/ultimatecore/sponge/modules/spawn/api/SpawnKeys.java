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
package bammerbom.ultimatecore.sponge.modules.spawn.api;

import bammerbom.ultimatecore.sponge.api.config.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.HashMap;

public class SpawnKeys {
    public static Key.Global<Transform<World>> FIRST_SPAWN = new Key.Global<>("firstspawn", new KeyProvider.Global<Transform<World>>() {
        @Override
        public Transform<World> load(Game game) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("first").getValue(new TypeToken<Transform<World>>() {
                });
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load first spawn.");
                return null;
            }
        }

        @Override
        public void save(Game game, Transform<World> data) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("first").setValue(new TypeToken<Transform<World>>() {
                }, data);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save first spawn.");
            }
            config.save(node);
        }
    });
    public static Key.Global<Transform<World>> GLOBAL_SPAWN = new Key.Global<>("globalspawn", new KeyProvider.Global<Transform<World>>() {
        @Override
        public Transform<World> load(Game game) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("global").getValue(new TypeToken<Transform<World>>() {
                });
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load global spawn.");
                return null;
            }
        }

        @Override
        public void save(Game game, Transform<World> data) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("global").setValue(new TypeToken<Transform<World>>() {
                }, data);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save global spawn.");
            }
            config.save(node);
        }
    });
    public static Key.Global<HashMap<String, Transform<World>>> GROUP_SPAWNS = new Key.Global<>("groupspawns", new KeyProvider.Global<HashMap<String, Transform<World>>>() {
        @Override
        public HashMap<String, Transform<World>> load(Game game) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("groups").getValue(new TypeToken<HashMap<String, Transform<World>>>() {
                }, new HashMap<>());
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load group spawns.");
                return null;
            }
        }

        @Override
        public void save(Game game, HashMap<String, Transform<World>> data) {
            GlobalDataFile config = new GlobalDataFile("spawns");
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("groups").setValue(new TypeToken<HashMap<String, Transform<World>>>() {
                }, data);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save group spawns.");
            }
            config.save(node);
        }
    });
}
