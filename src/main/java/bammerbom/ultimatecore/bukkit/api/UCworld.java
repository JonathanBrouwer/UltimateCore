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
package bammerbom.ultimatecore.bukkit.api;

import bammerbom.ultimatecore.bukkit.resources.utils.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;

public class UCworld {

    World base;

    public UCworld(World w) {
        base = w;
    }

    public UCworld(String world) {
        try {
            if (world == null || Bukkit.getWorld(world) == null) {
                throw new NullPointerException("World not found");
            }
            base = Bukkit.getWorld(world);
        } catch (NullPointerException ex) {
            throw new NullPointerException("World not found");
        }
    }

    //Firework
    public void playFirework(Location l, FireworkEffect ef) {
        FireworkUtil.play(l, ef);
    }

    //Datafile
    public File getDataFile() {
        return new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + File.separator + "Data", "worlds.yml");
    }

}
