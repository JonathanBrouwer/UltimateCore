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

import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.r;
import java.io.File;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.world.World;

public class UWorld {

    World base;

    public UWorld(World w) {
        base = w;
    }

    public UWorld(String world) {
        try {
            if (!r.getGame().getServer().isPresent()) {
                throw new NullPointerException("World not found");
            }
            if (!r.getGame().getServer().isPresent()) {
                throw new NullPointerException("World not found");
            }
            if (!r.getGame().getServer().get().getWorld(world).isPresent()) {
                throw new NullPointerException("World not found");
            }
            base = r.getGame().getServer().get().getWorld(world).get();
        } catch (NullPointerException ex) {
            throw new NullPointerException("World not found");
        }
    }

    //Firework
    /*public void playFirework(Location l, FireworkEffect ef) {
     FireworkUtil.play(l, ef);
     }*/  //TODO
    //Datafile
    public File getDataFile() {
        return new File(r.getUC().getDataFolder() + File.separator + "Data", "worlds.yml");
    }

    public World getWorld() {
        return base;
    }

    //Register
    public void register() {
        Config conf = new Config(getDataFile());
        conf.set(base.getName() + ".env", base.getDimension().getType().getName());
        conf.save();
    }

    public void unregister() {
        Config conf = new Config(getDataFile());
        conf.set(base.getName(), null);
        conf.save();
    }

    public void resetData() {
        unregister();
        register();
    }

    public boolean isFlagDenied(WorldFlag f) {
        File file = getDataFile();
        Config conf = new Config(file);
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
        Config conf = new Config(file);
        conf.set(getWorld().getName() + ".flags." + f.toString(), true);
        conf.save(file);
    }

    public void setFlagDenied(WorldFlag f) {
        File file = getDataFile();
        Config conf = new Config(file);
        conf.set(getWorld().getName() + ".flags." + f.toString(), false);
        conf.save(file);
    }

    public GameMode getDefaultGamemode() {
        File file = getDataFile();
        Config conf = new Config(file);
        String gm = conf.getString(getWorld().getName() + ".flags.gamemode");
        if (gm.equalsIgnoreCase("survival")) {
            return GameModes.SURVIVAL;
        }
        if (gm.equalsIgnoreCase("creative")) {
            return GameModes.CREATIVE;
        }
        if (gm.equalsIgnoreCase("adventure")) {
            return GameModes.ADVENTURE;
        }
        if (gm.equalsIgnoreCase("spectator")) {
            return GameModes.SPECTATOR;
        }
        return null;
    }

    public void setDefaultGamemode(GameMode gm) {
        File file = getDataFile();
        Config conf = new Config(file);
        String gms = "";
        if (gm.equals(GameModes.SURVIVAL)) {
            gms = "survival";
        }
        if (gm.equals(GameModes.CREATIVE)) {
            gms = "creative";
        }
        if (gm.equals(GameModes.ADVENTURE)) {
            gms = "adventure";
        }
        if (gm.equals(GameModes.SPECTATOR)) {
            gms = "spectator";
        }
        conf.set(getWorld().getName() + ".flags.gamemode", gms);
        conf.save(file);
    }

    //World
    public static enum WorldFlag {

        MONSTER,
        ANIMAL,
        PVP
    }

}
