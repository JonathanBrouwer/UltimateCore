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
package bammerbom.ultimatecore.sponge.modules.home.api;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;

public class HomeKeys {
    public static Key.User<List<Home>> HOMES = new Key.User<>("homes", new KeyProvider.User<List<Home>>() {
        @Override
        public List<Home> load(UltimateUser user) {
            List<Home> homes = new ArrayList<>();
            PlayerDataFile loader = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = loader.get();
            for (CommentedConfigurationNode wnode : node.getNode("homes").getChildrenMap().values()) {
                try {
                    homes.add(wnode.getValue(TypeToken.of(Home.class)));
                } catch (ObjectMappingException e) {
                    //World is not loaded, ignore home
                    //Messages.log(Messages.getFormatted("home.command.home.invalidhome", "%home%", wnode.getNode("name").getString()));
                }
            }
            return homes;
        }

        @Override
        public void save(UltimateUser user, List<Home> homes) {
            List<Home> currenthomes = load(user);
            PlayerDataFile loader = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = loader.get();
            currenthomes.forEach(home -> node.getNode("homes").removeChild(home.getName()));
            for (Home home : homes) {
                try {
                    node.getNode("homes", home.getName()).setValue(TypeToken.of(Home.class), home);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
            loader.save(node);
        }
    });
}
