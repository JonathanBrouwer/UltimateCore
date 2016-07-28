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

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.profile.GameProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Retrieve an UCUser by the user's uuid.
     *
     * @param uuid The uuid of the user to get
     * @return The user, or Optional.empty() if not found
     */
    Optional<UCUser> getUser(UUID uuid);

    /**
     * Retrieve an UCUser by the user's gameprofile.
     *
     * @param profile The gameprofile of the user to get
     * @return The UCUser, or Optional.empty() if not found
     */
    Optional<UCUser> getUser(GameProfile profile);

    /**
     * Retrieve an UCUser by the user's User instance.
     *
     * @param user The User instance of the user to get
     * @return The UCUser, or Optional.empty() if not found
     */
    UCUser getUser(User user);

    /**
     * Retrieve a list of all online UltimatePlayers
     */
    List<UCPlayer> getOnlinePlayers();

    //TODO is UCPlayer really needed? Shit should work with just UCUser too.

    /**
     * Retrieve an UCPlayer by the player's uuid.
     *
     * @param uuid The uuid of the player to get
     * @return The player, or Optional.empty() if not found
     */
    Optional<UCPlayer> getPlayer(UUID uuid);

    /**
     * Retrieve an UCPlayer by the player's gameprofile.
     *
     * @param profile The gameprofile of the player to get
     * @return The UCPlayer, or Optional.empty() if not found
     */
    Optional<UCPlayer> getPlayer(GameProfile profile);

    /**
     * Retrieve an UCPlayer by the player's Player instance.
     *
     * @param player The Player instance of the player to get
     * @return The UCPlayer, or Optional.empty() if not found
     */
    UCPlayer getPlayer(Player player);
}
