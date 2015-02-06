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
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;

public class PerformanceUtil {

    private static float tps = 20F;
    private static long lastPoll = System.currentTimeMillis() - 3000L;

    static {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                Float timeSpent = ((now - lastPoll) * 1.0F) / 1000.0F;
                float tpsn = (timeSpent * 20.0F);
                if (tpsn > 20.0F) {
                    tpsn = 20.0F;
                }
                tps = (Math.round(tpsn * 10.0F) / 10.0F);
                lastPoll = now;
            }
        }, 20, 20);
    }

    public static float getTps() {
        return tps;
    }

    public static float maxRam() {
        return Runtime.getRuntime().maxMemory() / 1048576;
    }

    public static float totalRam() {
        return Runtime.getRuntime().totalMemory() / 1048576;
    }

    public static float freeRam() {
        return Runtime.getRuntime().freeMemory() / 1048576;
    }

    public static float usedRam() {
        return maxRam() - freeRam();
    }

    public static float percentageUsed() {
        return Math.round((((usedRam() * 1.0) / (maxRam() * 1.0)) * 100.0) * 10) / 10F;
    }

    public static float percentageFree() {
        return Math.round((((freeRam() * 1.0) / (maxRam() * 1.0)) * 100.0) * 10) / 10F;
    }
}
