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
package bammerbom.ultimatecore.sponge.modules.back.api;

import bammerbom.ultimatecore.sponge.api.config.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

public class BackKeys {
    public static Key.User<Transform<World>> BACK = new Key.User<>("back", new KeyProvider.User<Transform<World>>() {
        @Override
        public Transform<World> load(UltimateUser user) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            try {
                return node.getNode("back").getValue(TypeToken.of(Transform.class));
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to load back key for " + user.getIdentifier());
                return null;
            }
        }

        @Override
        public void save(UltimateUser user, Transform<World> data) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode("back").setValue(TypeToken.of(Transform.class), data);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save back key for " + user.getIdentifier());
            }
            config.save(node);
        }
    });
}
