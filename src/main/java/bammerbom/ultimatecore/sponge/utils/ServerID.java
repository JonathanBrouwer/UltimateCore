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
package bammerbom.ultimatecore.sponge.utils;

import bammerbom.ultimatecore.sponge.UltimateCore;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class ServerID {
    static UUID uuid;

    public static void start() {
        try {
            File file = new File(UltimateCore.getInstance().getDataFolder().toUri().getPath() + "/data", "serverid.id");
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
                UUID u = UUID.randomUUID();
                FileUtil.writeLines(file, Arrays.asList("This UUID is used by UltimateCore to identify your server while sending errors, stats, etc.", u.toString()));
                uuid = u;
            } else {
                String s = FileUtil.readLines(file).get(1);
                uuid = UUID.fromString(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static UUID getUUID() {
        return uuid;
    }
}
