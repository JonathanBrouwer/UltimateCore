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
package bammerbom.ultimatecore.sponge.modules.commandtimer.api;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data_old.Key;
import bammerbom.ultimatecore.sponge.api.data_old.providers.UserKeyProvider;
import com.google.common.reflect.TypeToken;

import java.util.HashMap;

public class CommandtimerKeys {
    public static Key.Global<HashMap<Command, Long>> COOLDOWNS = new Key.Global<>("cooldowns", new HashMap<>());
    public static Key.Global<HashMap<Command, Long>> WARMUPS = new Key.Global<>("warmups", new HashMap<>());
    public static Key.User<HashMap<String, Long>> USER_LASTEXECUTED = new Key.User<>("user_lastexecuted", new UserKeyProvider<>("cooldowns", new TypeToken<HashMap<String, Long>>() {
    }, new HashMap<>()));
    public static Key.User<HashMap<String, Warmup>> USER_WARMUPS = new Key.User<>("user_warmups", new HashMap<>());
    public static Key.Global<HashMap<String, Runnable>> CURRENT_WARMUPS = new Key.Global<>("current_warmups", new HashMap<>());
}
