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

import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import org.spongepowered.api.Game;

import javax.annotation.Nullable;
import java.util.Optional;

public class Key<C> {
    protected String identifier;
    //Defaul because default is a keyword in java
    protected C defaul;
    protected KeyProvider<C, ? extends Object> provider;

    /**
     * Create a new Key with the provided id and default value
     *
     * @param id  The identifier, must be unique
     * @param def The default value for this key, can be null
     */
    public Key(String id, @Nullable C def) {
        this.identifier = id;
        this.defaul = def;
        this.provider = null;
    }

    /**
     * Create a new Key with the provided id and default value
     *
     * @param id  The identifier, must be unique
     * @param def The default value for this key, can be null
     */
    public Key(String id, @Nullable KeyProvider<C, ? extends Object> def) {
        this.identifier = id;
        this.defaul = null;
        this.provider = def;
    }

    /**
     * Get the identifier of this key.
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the default value of this key.
     *
     * @return The default value
     */
    public Optional<C> getDefaultValue() {
        return Optional.ofNullable(defaul);
    }

    /**
     * Get the provider for default values, load and save actions
     */
    public <D> Optional<KeyProvider<C, D>> getProvider() {
        return Optional.ofNullable((KeyProvider<C, D>) provider);
    }

    /**
     * This is a {@link Key} which is associated with an User.
     *
     * @param <C>
     */
    public static class User<C> extends Key<C> {
        public User(String id, @Nullable C def) {
            super(id, def);
        }

        public User(String id, @Nullable KeyProvider<C, ? extends User> def) {
            super(id, def);
        }

        /**
         * This is a temporary Key which will be removed once the player logs out.
         *
         * @param <C> The type of data the key holds
         */
        public static class Online<C> extends User<C> {
            public Online(String id, @Nullable C def) {
                super(id, def);
                if (!UltimateUser.onlinekeys.contains(id)) {
                    UltimateUser.onlinekeys.add(id);
                }
            }

            public Online(String id, @Nullable KeyProvider<C, User> def) {
                super(id, def);
                if (!UltimateUser.onlinekeys.contains(id)) {
                    UltimateUser.onlinekeys.add(id);
                }
            }
        }
    }

    /**
     * This is a key which is not associated with an User, but instead is a key like a list of warps, or the global spawn.
     *
     * @param <C> The type of data the key holds
     */
    public static class Global<C> extends Key<C> {
        /**
         * Create a new Key with the provided id and default value
         *
         * @param id  The identifier, must be unique
         * @param def The default value for this key, can be null
         */
        public Global(String id, @Nullable C def) {
            super(id, def);
        }

        /**
         * Create a new Key with the provided id and the provider
         *
         * @param id  The identifier, must be unique
         * @param def The default value for this key, can be null
         */
        public Global(String id, @Nullable KeyProvider<C, Game> def) {
            super(id, def);
        }
    }

}
