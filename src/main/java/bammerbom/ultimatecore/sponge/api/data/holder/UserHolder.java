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
package bammerbom.ultimatecore.sponge.api.data.holder;

import bammerbom.ultimatecore.sponge.api.data.key.Key;
import bammerbom.ultimatecore.sponge.api.data.key.UserKey;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UserHolder implements Holder {
    HashMap<UserKey, Object> map = new HashMap<>();
    UUID user;

    public UserHolder(UUID user) {
        this.user = user;
    }

    public UUID getUser() {
        return this.user;
    }

    @Override
    public HashMap<UserKey, Object> getCachedKeys() {
        return this.map;
    }

    @Override
    public <C> Optional<C> get(Key<C> key) {
        return (Optional<C>) Optional.ofNullable(this.map.get(key));
    }

    @Override
    public <C> boolean offer(Key<C> key, C value) {
        if (!(key instanceof UserKey)) throw new IllegalStateException();
        this.map.put((UserKey) key, value);
        return true;
    }
}
