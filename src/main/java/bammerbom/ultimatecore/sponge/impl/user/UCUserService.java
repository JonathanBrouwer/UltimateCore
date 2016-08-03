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
package bammerbom.ultimatecore.sponge.impl.user;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.user.UserService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UCUserService implements UserService {

    private List<UltimateUser> users = new ArrayList<>();

    /**
     * Retrieve an {@link UltimateUser} by the user's uuid.
     *
     * @param uuid The uuid of the user to get
     * @return The user, or Optional.empty() if not found
     */
    @Override
    public Optional<UltimateUser> getUser(UUID uuid) {
        Optional<User> user = Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(getUser(user.get()));
    }

    /**
     * Retrieve an {@link UltimateUser} by the user's gameprofile.
     *
     * @param profile The gameprofile of the user to get
     * @return The UltimateUser, or Optional.empty() if not found
     */
    @Override
    public Optional<UltimateUser> getUser(GameProfile profile) {
        return getUser(profile.getUniqueId());
    }

    /**
     * Retrieve an {@link UltimateUser} by the user's User instance.
     *
     * @param user The User instance of the user to get
     * @return The UltimateUser, or Optional.empty() if not found
     */
    @Override
    public UltimateUser getUser(User user) {
        for (UltimateUser use : users) {
            if (use.getIdentifier().equals(user.getUniqueId())) {
                return use;
            }
        }
        UltimateUser ucuser = new UltimateUser(user);
        users.add(ucuser);
        return ucuser;
    }

    /**
     * Retrieve a list of all online {@link UltimateUser}s
     */
    @Override
    public List<UltimateUser> getOnlinePlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(this::getUser).collect(Collectors.toList());
    }

    /**
     * Remove all users from the cache.
     * <p>
     * Warning: This will reset all {@link Key.User.Online}s!
     * (So afk players will no longer be afk, etc)</p>
     *
     * @return Whether the reset was successful
     */
    @Override
    public boolean clearcache() {
        users.clear();
        return true;
    }

    /**
     * Add the {@link UltimateUser} to the cache. This will overwrite other users with the same UUID.
     *
     * @param user The user to add to the cache
     * @return Whether the user was added to the cache successfully
     */
    @Override
    public boolean addToCache(UltimateUser user) {
        if (removeFromCache(user.getIdentifier())) {
            return users.add(user);
        } else {
            return false;
        }
    }

    /**
     * Remove the specified user from the cache.
     * <p>
     * Warning: This will reset all {@link Key.User.Online}s of the user!
     * (So afk player will no longer be afk, etc)</p>
     *
     * @param uuid The user's uuid to remove from the cache
     * @return Whether the reset was successful
     */
    @Override
    public boolean removeFromCache(UUID uuid) {
        //A list in case for something went wrong and there are multiple instances of a player in the cache.
        List<UltimateUser> rusers = new ArrayList<>();
        users.stream().filter(user -> user.getIdentifier().equals(uuid)).forEach(user -> {
            rusers.add(user);
        });
        return !rusers.isEmpty() && users.removeAll(rusers);
    }
}
