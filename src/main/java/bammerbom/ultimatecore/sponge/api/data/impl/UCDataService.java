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
package bammerbom.ultimatecore.sponge.api.data.impl;

import bammerbom.ultimatecore.sponge.api.data.DataService;
import bammerbom.ultimatecore.sponge.api.data.holder.GlobalHolder;
import bammerbom.ultimatecore.sponge.api.data.holder.UserHolder;
import bammerbom.ultimatecore.sponge.api.data.storage.StorageType;
import bammerbom.ultimatecore.sponge.api.data.storage.StorageTypeManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UCDataService implements DataService {

    GlobalHolder global = new GlobalHolder();
    HashMap<UUID, UserHolder> users = new HashMap<>();

    @Override
    public GlobalHolder getGlobalHolder() {
        return this.global;
    }

    @Override
    public Optional<UserHolder> getUser(UUID uuid) {
        UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
        return service.get(uuid).map(this::getUserHolder);
    }

    @Override
    public Optional<UserHolder> getUser(GameProfile profile) {
        UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
        return service.get(profile).map(this::getUserHolder);
    }

    @Override
    public UserHolder getUserHolder(User user) {
        return this.users.getOrDefault(user.getUniqueId(), new UserHolder(user.getUniqueId()));
    }

    @Override
    public HashMap<UUID, UserHolder> getUserMap() {
        return this.users;
    }

    @Override
    public boolean clearCache() {
        save();
        this.global = new GlobalHolder();
        this.users = new HashMap<>();
        return true;
    }

    @Override
    public boolean removeFromCache(UUID uuid) {
        this.users.remove(uuid);
        return true;
    }

    @Override
    public boolean save() {
        StorageType type = StorageTypeManager.getStorageType();
        type.saveGlobal(this.global);
        this.users.values().forEach(type::saveUser);
        return true;
    }
}
