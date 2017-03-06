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
package bammerbom.ultimatecore.sponge.api.data_old.providers;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class UserKeyProvider<E> implements KeyProvider.User<E> {
    private String key;
    private TypeToken<E> token;
    private E def = null;

    public UserKeyProvider(String key, TypeToken<E> token) {
        this.key = key;
        this.token = token;
    }

    public UserKeyProvider(String key, TypeToken<E> token, E def) {
        this.key = key;
        this.token = token;
        this.def = def;
    }

    @Override
    public E load(UltimateUser user) {
        PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
        CommentedConfigurationNode node = config.get();
        try {
            return node.getNode(this.key).getValue(this.token, this.def);
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to save " + this.key + " key for " + user.getIdentifier());
            return this.def;
        }
    }

    @Override
    public void save(UltimateUser user, E data) {
        PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
        CommentedConfigurationNode node = config.get();
        try {
            node.getNode(this.key).setValue(null);
            node.getNode(this.key).setValue(this.token, data);
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to save " + this.key + " key for " + user.getIdentifier());
        }
        config.save(node);
    }
}
