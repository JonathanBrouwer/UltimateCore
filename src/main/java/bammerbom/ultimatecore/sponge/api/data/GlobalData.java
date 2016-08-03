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
package bammerbom.ultimatecore.sponge.api.data;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.event.data.DataOfferEvent;
import bammerbom.ultimatecore.sponge.api.event.data.DataRetrieveEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;

import java.util.HashMap;
import java.util.Optional;

public class GlobalData {
    public static HashMap<String, Object> datas = new HashMap<>();

    /**
     * Get the value for the provided key, or the default value of the key when no value for the key was found in the map.
     * {@link Optional#empty()} is returned when the {@link Key} is not compatible or has no default value.
     *
     * @param key The key to search for
     * @param <C> The expected type of value to be returned
     * @return The value found, or {@link Optional#empty()} when no value was found.
     */
    public static <C> Optional<C> get(Key.Global<C> key) {
        Optional<C> rtrn;
        if (!datas.containsKey(key.getIdentifier())) {
            if (key.getProvider().isPresent()) {
                //Run the provider
                rtrn = Optional.of(key.getProvider().get().load(Sponge.getGame()));
            } else {
                rtrn = key.getDefaultValue();
            }
        } else {
            //Provider not available, get the default value
            rtrn = Optional.ofNullable((C) datas.get(key.getIdentifier()));
        }
        DataRetrieveEvent<C> event = new DataRetrieveEvent<>(key, rtrn.orElse(null), Cause.builder().owner(UltimateCore.get()).build());
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
    public static <C> Optional<C> getRaw(Key.Global<C> key) {
        Optional<C> rtrn;
        if (!datas.containsKey(key.getIdentifier())) {
            rtrn = Optional.empty();
        } else {
            rtrn = Optional.ofNullable((C) datas.get(key.getIdentifier()));
        }
        DataRetrieveEvent<C> event = new DataRetrieveEvent<>(key, rtrn.orElse(null), Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(event);
        return event.getValue();
    }

    /**
     * Set the value of a key to the specified value.
     *
     * @param key   The key to set the value of
     * @param value The value to set the value to
     * @param <C>   The type of value the key holds
     * @return Whether the value was accepted
     */
    public static <C> boolean offer(Key.Global<C> key, C value) {
        Cause cause = Cause.builder().owner(UltimateCore.get()).build();
        DataOfferEvent<C> event = new DataOfferEvent<>(key, (C) datas.get(key.getIdentifier()), value, cause);
        Sponge.getEventManager().post(event);
        if (event.isCancelled()) {
            return false;
        }
        value = event.getValue().orElse(null);
        //Save to config if needed
        if (key.getProvider().isPresent()) {
            key.getProvider().get().save(Sponge.getGame(), value);
        }
        //Save to map
        if (value == null) {
            datas.remove(key.getIdentifier());
        } else {
            datas.put(key.getIdentifier(), value);
        }
        return true;
    }
}
