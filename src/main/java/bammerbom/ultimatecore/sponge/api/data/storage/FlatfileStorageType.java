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
package bammerbom.ultimatecore.sponge.api.data.storage;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.data.holder.GlobalHolder;
import bammerbom.ultimatecore.sponge.api.data.holder.UserHolder;
import bammerbom.ultimatecore.sponge.api.data.key.GlobalKey;
import bammerbom.ultimatecore.sponge.api.data.key.UserKey;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class FlatfileStorageType implements StorageType {
    @Override
    public <C> C loadGlobal(GlobalHolder holder, GlobalKey<C> key) {
        GlobalDataFile config = new GlobalDataFile(key.getStorageLocation());
        CommentedConfigurationNode node = config.get();
        try {
            return node.getNode(key.getIdentifier()).getValue(key.getToken(), key.getDefault().orElse(null));
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to load " + key.getIdentifier() + " global key");
            return key.getDefault().orElse(null);
        }

    }

    @Override
    public <C> C loadUser(UserHolder holder, UserKey<C> key) {
        PlayerDataFile config = new PlayerDataFile(holder.getUser());
        CommentedConfigurationNode node = config.get();
        try {
            return node.getNode(key.getIdentifier()).getValue(key.getToken(), key.getDefault().orElse(null));
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to load " + key.getIdentifier() + " user key for " + holder.getUser());
            return key.getDefault().orElse(null);
        }
    }

    @Override
    public boolean saveGlobal(GlobalHolder holder) {
        //TODO optimization
        holder.getCachedKeys().forEach((key, value) -> {
            GlobalDataFile config = new GlobalDataFile(key.getStorageLocation());
            CommentedConfigurationNode node = config.get();
            try {
                node.getNode(key.getIdentifier()).setValue(null);
                node.getNode(key.getIdentifier()).setValue(key.getToken(), value);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save " + key.getIdentifier() + " global key");
            }
            config.save(node);
        });
        return true;
    }

    @Override
    public boolean saveUser(UserHolder holder) {
        PlayerDataFile config = new PlayerDataFile(holder.getUser());
        CommentedConfigurationNode node = config.get();
        holder.getCachedKeys().forEach((key, value) -> {
            try {
                node.getNode(key.getIdentifier()).setValue(null);
                node.getNode(key.getIdentifier()).setValue(key.getToken(), value);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to save " + key + " user key for " + holder.getUser());
            }
        });
        config.save(node);
        return true;
    }
}
