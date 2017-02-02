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
package bammerbom.ultimatecore.sponge.api.variable.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;

import java.util.Optional;

public class ArgumentUtil {
    public static boolean isInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isLong(String arg) {
        try {
            Long.parseLong(arg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isDouble(String arg) {
        try {
            Double.parseDouble(arg);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isBoolean(String arg) {
        return arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false");
    }

    public static Optional<Key> getKeyById(String id) {
        for (Key key : Sponge.getRegistry().getAllOf(Key.class)) {
            if (key.getId().equalsIgnoreCase(id) || key.getId().equalsIgnoreCase("sponge:" + id)) {
                return Optional.of(key);
            }
        }
        return Optional.empty();
    }
}
