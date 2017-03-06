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
package bammerbom.ultimatecore.sponge.api.data.key;

import com.google.common.reflect.TypeToken;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class KeyRegistry {
    private static KeyRegistry instance = new KeyRegistry();
    Set<GlobalKey> globalKeys = new HashSet<>();
    Set<UserKey> userKeys = new HashSet<>();

    public static KeyRegistry get() {
        return instance;
    }

    public Set<GlobalKey> getGlobalKeys() {
        return this.globalKeys;
    }

    public Set<UserKey> getUserKeys() {
        return this.userKeys;
    }

    public <C> GlobalKey<C> createGlobalKey(String module, String id, String storage, TypeToken<C> token) {
        return createGlobalKey(module, id, storage, token, null);
    }

    public <C> UserKey<C> createUserKey(String module, String id, TypeToken<C> token) {
        return createUserKey(module, id, token, null);
    }

    public <C> GlobalKey<C> createGlobalKey(String module, String id, String storage, TypeToken<C> token, @Nullable C def) {
        GlobalKey<C> key = new GlobalKey<>(module + ":" + id, def, storage, token);
        this.globalKeys.add(key);
        return key;
    }

    public <C> UserKey<C> createUserKey(String module, String id, TypeToken<C> token, @Nullable C def) {
        UserKey<C> key = new UserKey<>(module + ":" + id, def, token);
        this.userKeys.add(key);
        return key;
    }
}
