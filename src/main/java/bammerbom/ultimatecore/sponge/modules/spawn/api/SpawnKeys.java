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
package bammerbom.ultimatecore.sponge.modules.spawn.api;

import bammerbom.ultimatecore.sponge.api.data_old.Key;
import bammerbom.ultimatecore.sponge.api.data_old.providers.GlobalKeyProvider;
import bammerbom.ultimatecore.sponge.api.teleport.serializabletransform.SerializableTransform;
import com.google.common.reflect.TypeToken;

import java.util.HashMap;

public class SpawnKeys {
    public static Key.Global<SerializableTransform> FIRST_SPAWN = new Key.Global<>("firstspawn", new GlobalKeyProvider<>("spawns", "first", TypeToken.of(SerializableTransform.class)));

    public static Key.Global<SerializableTransform> GLOBAL_SPAWN = new Key.Global<>("globalspawn", new GlobalKeyProvider<>("spawns", "global", TypeToken.of(SerializableTransform.class)));

    public static Key.Global<HashMap<String, SerializableTransform>> GROUP_SPAWNS = new Key.Global<>("groupspawns", new GlobalKeyProvider<>("spawns", "groups", new TypeToken<HashMap<String, SerializableTransform>>() {
    }, new HashMap<>()));
}
