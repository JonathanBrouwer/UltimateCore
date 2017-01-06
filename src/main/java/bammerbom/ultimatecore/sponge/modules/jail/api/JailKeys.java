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
package bammerbom.ultimatecore.sponge.modules.jail.api;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.config.datafiles.PlayerDataFile;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;

import java.util.ArrayList;
import java.util.List;

public class JailKeys {
    public static Key.Global<List<Jail>> JAILS = new Key.Global<>("jail", new KeyProvider.Global<List<Jail>>() {
        @Override
        public List<Jail> load(Game game) {
            GlobalDataFile config = new GlobalDataFile("jails");
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("jails").getValue(new TypeToken<List<Jail>>() {
                }, new ArrayList<>());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void save(Game game, List<Jail> data) {
            GlobalDataFile config = new GlobalDataFile("jails");
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("jails").setValue(new TypeToken<List<Jail>>() {
                }, data);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
            config.save(node);
        }
    });

    public static Key.User<JailData> JAIL = new Key.User<>("jail", new KeyProvider.User<JailData>() {
        @Override
        public JailData load(UltimateUser user) {
            PlayerDataFile loader = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = loader.get();
            try {
                return node.getNode("jail").getValue(TypeToken.of(JailData.class));
            } catch (ObjectMappingException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void save(UltimateUser user, JailData data) {
            PlayerDataFile loader = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = loader.get();
            try {
                node.getNode("jail").setValue(TypeToken.of(JailData.class), data);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
            loader.save(node);
        }
    });
}
