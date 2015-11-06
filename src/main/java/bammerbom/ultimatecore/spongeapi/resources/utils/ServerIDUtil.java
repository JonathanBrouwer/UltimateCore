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
package bammerbom.ultimatecore.spongeapi.resources.utils;

import bammerbom.ultimatecore.spongeapi.r;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class ServerIDUtil {

    static UUID uuid = null;

    public static void start() {
        if (uuid != null) {
            return;
        }
        try {
            File file = new File(r.getUC().getDataFolder() + File.separator + "Data", "UUID.id");
            if (!file.exists()) {
                file.createNewFile();
                UUID u = UUID.randomUUID();
                FileUtil.writeLargerTextFile(file, Arrays.asList(u.toString()));
                uuid = u;
            } else {
                String s = FileUtil.getLines(file).get(0);
                uuid = UUID.fromString(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static UUID getUUID() {
        if (uuid == null) {
            start();
        }
        return uuid;
    }
}
