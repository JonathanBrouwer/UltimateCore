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
package bammerbom.ultimatecore.bukkit.listeners;

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class AutosaveListener {

    public static void start() {
        if (!r.getCnfg().getBoolean("Autosave.Enabled")) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {

            @Override
            public void run() {
                if (r.getCnfg().getBoolean("Autosave.Message")) {
                    Bukkit.broadcastMessage(r.mes("autosaveStart"));
                }
                for (World w : Bukkit.getWorlds()) {
                    try {
                        w.save();
                    } catch (Exception ex) {
                    }
                }
                if (r.getCnfg().getBoolean("Autosave.Message")) {
                    Bukkit.broadcastMessage(r.mes("autosaveDone"));
                }

            }

        }, r.getCnfg().getInt("Autosave.Time") * 20, r.getCnfg().getInt("Autosave.Time") * 20);
    }
}
