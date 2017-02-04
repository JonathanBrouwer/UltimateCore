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
package bammerbom.ultimatecore.sponge.modules.votifier.api;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.providers.GlobalKeyProvider;
import bammerbom.ultimatecore.sponge.api.data.providers.UserKeyProvider;
import com.google.common.reflect.TypeToken;
import com.vexsoftware.votifier.model.Vote;

import java.util.ArrayList;
import java.util.List;

public class VotifierKeys {
    public static Key.Global<List<Vote>> VOTES_CACHED = new Key.Global<>("votes-cached", new GlobalKeyProvider<>("votifier", "votes-cached", new TypeToken<List<Vote>>() {
    }, new ArrayList<>()));

    public static Key.User<Integer> VOTES_COUNT = new Key.User<>("votes-count", new UserKeyProvider<>("votes-count", TypeToken.of(Integer.class), 0));
}
