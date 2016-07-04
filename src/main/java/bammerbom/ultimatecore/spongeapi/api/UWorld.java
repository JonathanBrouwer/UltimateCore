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
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.world.World;

import java.io.File;

public class UWorld {

    World base;

    public UWorld(World w) {
        base = w;
    }

    public UWorld(String world) {
        try {
            if (world == null || !Sponge.getServer().getWorld(world).isPresent()) {
                throw new NullPointerException("World not found");
            }
            base = Sponge.getServer().getWorld(world).get();
        } catch (NullPointerException ex) {
            throw new NullPointerException("World not found");
        }
    }

    //Datafile
    public File getDataFile() {
        return new File(r.getUC().getDataFolder() + File.separator +
                "Data", "worlds.json");
    }

    public World getWorld() {
        return base;
    }

    //Register
    public void register() {
        JsonConfig conf = new JsonConfig(getDataFile());
        conf.set(base.getUniqueId() + ".env", base.getDimension().getType().getId());
        conf.set(base.getUniqueId() + ".gen", base.getWorldGenerator().getId());
        conf.set(base.getUniqueId() + ".type", base.getProperties().getGeneratorType().getId());
        //conf.set(base.getUniqueId() + ".gen", base.getGenerator().getClass());
        conf.save();
        base.getProperties().setEnabled(true);
    }

    public void unregister() {
        JsonConfig conf = new JsonConfig(getDataFile());
        conf.set(base.getUniqueId().toString(), null);
        conf.save();
        base.getProperties().setEnabled(false);
    }

    public void resetData() {
        String gen = new JsonConfig(getDataFile()).getString(base.getUniqueId() + ".gen");
        unregister();
        register(gen);
    }

    public boolean isFlagDenied(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        if (!conf.contains(getWorld().getUniqueId() + ".flags." + f.toString())) {
            return false;
        }
        return !conf.getBoolean(getWorld().getUniqueId() + ".flags." + f.toString());
    }

    public boolean isFlagAllowed(WorldFlag f) {
        return !isFlagDenied(f);
    }

    public void setFlagAllowed(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getUniqueId() + ".flags." + f.toString(), true);
        conf.save(file);
        if (f.equals(WorldFlag.PVP)) {
            getWorld().getProperties().setPVPEnabled(true);
        }
    }

    public void setFlagDenied(WorldFlag f) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getUniqueId() + ".flags." + f.toString(), false);
        conf.save(file);
        if (f.equals(WorldFlag.PVP)) {
            getWorld().getProperties().setPVPEnabled(false);
        }
    }

    public GameMode getDefaultGamemode() {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        String gm = conf.getString(getWorld().getUniqueId() + ".flags.gamemode");
        try {
            return Sponge.getRegistry().getType(CatalogTypes.GAME_MODE, gm).orElse(null);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setDefaultGamemode(GameMode gm) {
        File file = getDataFile();
        JsonConfig conf = new JsonConfig(file);
        conf.set(getWorld().getUniqueId() + ".flags.gamemode", gm.getId());
        conf.save(file);
        getWorld().getProperties().setGameMode(gm);
    }

    //World
    public enum WorldFlag {
        MONSTER,
        ANIMAL,
        PVP
    }

}
