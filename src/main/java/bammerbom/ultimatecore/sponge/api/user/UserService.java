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
package bammerbom.ultimatecore.sponge.api.user;

import bammerbom.ultimatecore.sponge.api.data.Key;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Retrieve an UltimateUser by the user's uuid.
     *
     * @param uuid The uuid of the user to get
     * @return The user, or Optional.empty() if not found
     */
    Optional<UltimateUser> getUser(UUID uuid);

    /**
     * Retrieve an UltimateUser by the user's gameprofile.
     *
     * @param profile The gameprofile of the user to get
     * @return The UltimateUser, or Optional.empty() if not found
     */
    Optional<UltimateUser> getUser(GameProfile profile);

    /**
     * Retrieve an UltimateUser by the user's User instance.
     *
     * @param user The User instance of the user to get
     * @return The UltimateUser.
     */
    UltimateUser getUser(User user);

    /**
     * Retrieve a list of all online {@link UltimateUser}s.
     */
    List<UltimateUser> getOnlinePlayers();

    /**
     * Remove all users from the cache.
     * <p>
     * Warning: This will reset all {@link Key.User.Online}s!
     * (So afk players will no longer be afk, etc)</p>
     *
     * @return Whether the reset was successful
     */
    boolean clearcache();

    /**
     * Add the {@link UltimateUser} to the cache. This will overwrite other users with the same UUID.
     *
     * @param user The user to add to the cache
     * @return Whether the user was added to the cache successfully
     */
    boolean addToCache(UltimateUser user);

    /**
     * Remove the specified user from the cache.
     * <p>
     * Warning: This will reset all {@link Key.User.Online}s of the user!
     * (So afk player will no longer be afk, etc)</p>
     *
     * @param user The user's uuid to remove from the cache
     * @return Whether the reset was successful
     */
    boolean removeFromCache(UUID user);
}
