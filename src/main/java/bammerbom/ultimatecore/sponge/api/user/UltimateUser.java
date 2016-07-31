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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.event.data.DataOfferEvent;
import bammerbom.ultimatecore.sponge.api.event.data.DataRetrieveEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UltimateUser {

    public HashMap<String, Object> datas = new HashMap<>();
    private UUID uuid; //TODO uuid or user?

    public UltimateUser(User user) {
        this.uuid = user.getUniqueId();
    }

    /**
     * Get the UUID of the user.
     *
     * @return The UUID
     */
    public UUID getIdentifier() {
        return uuid;
    }

    /**
     * Get the {@link User} this class is associated with.
     *
     * @return The user
     */
    public User getUser() {
        if (getPlayer().isPresent()) {
            return getPlayer().get();
        }
        return Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid).orElse(null);
    }

    /**
     * Get the {@link Player} this class is associated with, or {@link Optional#empty()} when the player this class is associated with is offline.
     *
     * @return The player
     */
    public Optional<Player> getPlayer() {
        return Sponge.getServer().getPlayer(uuid);
    }

    /**
     * Get the value for the provided key, or the default value of the key when no value for the key was found in the map.
     * {@link Optional#empty()} is returned when the {@link Key} is not compatible or has no default value.
     *
     * @param key The key to search for
     * @param <C> The expected type of value to be returned
     * @return The value found, or {@link Optional#empty()} when no value was found.
     */
    public <C> Optional<C> get(Key<C> key) {
        if (!isCompatible(key)) {
            return Optional.empty();
        }
        Optional<C> rtrn;
        if (!datas.containsKey(key.getIdentifier())) {
            rtrn = key.getDefaultValue();
        }else {
            rtrn = Optional.ofNullable((C) datas.get(key.getIdentifier()));
        }
        DataRetrieveEvent<C> event = new DataRetrieveEvent<C>(key, rtrn.orElse(null), Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(event);
        return event.getValue();
    }

    /**
     * Get the value for the provided key, or {@link Optional#empty()} when no value was found in the map.
     *
     * @param key The key to search for
     * @param <C> The expected type of value to be returned
     * @return The value found, or {@link Optional#empty()} when no value was found.
     */
    public <C> Optional<C> getRaw(Key<C> key) {
        if (!isCompatible(key)) {
            return Optional.empty();
        }
        Optional<C> rtrn;
        if (!datas.containsKey(key.getIdentifier())) {
            rtrn = Optional.empty();
        }else {
            rtrn = Optional.ofNullable((C) datas.get(key.getIdentifier()));
        }
        DataRetrieveEvent<C> event = new DataRetrieveEvent<C>(key, rtrn.orElse(null), Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(event);
        return event.getValue();
    }

    /**
     * Get whether a key is compatible with this user.
     * This is false when the key is an {@link Key.User.Online} and this user is offline.
     *
     * @param key The key to check for
     * @return Whether the key is compatible
     */
    public boolean isCompatible(Key key) {
        return !(key instanceof Key.User.Online) || getPlayer().isPresent();
    }

    /**
     * Set the value of a key to the specified value.
     *
     * @param key   The key to set the value of
     * @param value The value to set the value to
     * @param <C>   The type of value the key holds
     * @return Whether the value was accepted
     */
    public <C> boolean offer(Key<C> key, C value) {
        if (!isCompatible(key)) {
            return false;
        }
        DataOfferEvent<C> event = new DataOfferEvent<C>(key, (C) datas.get(key.getIdentifier()), value, Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(event);
        if(event.isCancelled()){
            return false;
        }
        value = event.getValue().orElse(null);
        if(value == null){
            datas.remove(key.getIdentifier());
        }else {
            datas.put(key.getIdentifier(), value);
        }
        return UltimateCore.get().getUserService().addToCache(this);
    }
}

