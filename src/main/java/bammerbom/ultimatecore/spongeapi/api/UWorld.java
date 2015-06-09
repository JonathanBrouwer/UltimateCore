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
package bammerbom.ultimatecore.spongeapi.api;

import bammerbom.ultimatecore.spongeapi.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi.resources.utils.FireworkUtil;
import org.bukkit.*;

import java.io.File;

public class UWorld {

    World base;

    public UWorld(World w) {
        base = w;
    }

    public UWorld(String world) {
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
        return new File(Bukkit.getPluginManager().getPlugin("UltimateCore").getDataFolder() + File.separator +
                "Data", "worlds.json");
    }

    public World getWorld() {
        return base;
    }

    //Register
    public void register(String gen) {
        JsonConfig conf = new JsonConfig(getDataFile());
        conf.set(base.getName() + ".env", base.getEnvironment().name());
        conf.set(base.getName() + ".gen", gen);
        conf.set(base.getName() + ".type", base.getWorldType().toString());
        //conf.set(base.getName() + ".gen", base.getGenerator().getClass());
        conf.save();
    }

    public void unregister() {
        JsonConfig conf = new JsonConfig(getDataFile());
        conf.set(base.getName(), null);
        conf.save();
    }

    public void resetData() {
        String gen = new JsonConfig(getDataFile()).getString(base.getName() + ".gen");
        unregister();
        register(gen.isEmpty() ? null : gen);
    }

    public boolean isFlagDenied(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        if (!conf.contains(getWorld().getName() + ".flags." + f.toString())) {
            return false;
        }
        return !conf.getBoolean(getWorld().getName() + ".flags." + f.toString());
    }

    public boolean isFlagAllowed(WorldFlag f) {
        return !isFlagDenied(f);
    }

    public void setFlagAllowed(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getName() + ".flags." + f.toString(), true);
        conf.save(file);
        if (f.equals(WorldFlag.ANIMAL)) {
            getWorld().setAnimalSpawnLimit(15);
        }
        if (f.equals(WorldFlag.MONSTER)) {
            getWorld().setMonsterSpawnLimit(70);
        }
        if (f.equals(WorldFlag.PVP)) {
            getWorld().setPVP(true);
        }
    }

    public void setFlagDenied(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getName() + ".flags." + f.toString(), false);
        conf.save(file);
        if (f.equals(WorldFlag.ANIMAL)) {
            getWorld().setAnimalSpawnLimit(0);
        }
        if (f.equals(WorldFlag.MONSTER)) {
            getWorld().setMonsterSpawnLimit(0);
        }
        if (f.equals(WorldFlag.PVP)) {
            getWorld().setPVP(false);
        }
    }

    public GameMode getDefaultGamemode() {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        String gm = conf.getString(getWorld().getName() + ".flags.gamemode");
        try {
            return GameMode.valueOf(gm);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setDefaultGamemode(GameMode gm) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getName() + ".flags.gamemode", gm.name());
        conf.save(file);
    }

    //World
    public enum WorldFlag {
        MONSTER,
        ANIMAL,
        PVP
    }

}
