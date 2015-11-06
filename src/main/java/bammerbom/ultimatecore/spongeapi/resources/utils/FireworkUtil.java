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

import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * FireworkEffectPlayer v1.0
 * <p>
 * FireworkEffectPlayer provides a thread-safe and (reasonably) version independant way to
 * instantly explode a FireworkEffect at a given location. You are welcome to use, redistribute,
 * modify and destroy your own copies of this source with the following conditions:
 * <p>
 * 1. No warranty is given or implied. 2. All damage is your own responsibility. 3. You provide
 * credit publicly to the original source should you release the plugin.
 *
 * @author codename_B
 */
public class FireworkUtil {

    /*
     * Example use:
     *
     * public class FireWorkPlugin implements Listener {
     *
     * FireworkEffectPlayer fplayer = new FireworkEffectPlayer();
     *
     * @Listener
     * public void onPlayerLogin(PlayerLoginEvent event) {
     *   fplayer.playFirework(event.getPlayer().getWorld(), event.getPlayer.getLocation(), Util
     *   .getRandomFireworkEffect());
     * }
     *
     * }
     */
    // internal references, performance improvements
    private static Method world_getHandle = null;
    private static Method nms_world_broadcastEntityEffect = null;
    private static Method firework_getHandle = null;

    /**
     * Play a pretty firework at the location with the FireworkEffect when called
     *
     * @param loc
     * @param fe
     */
    public static void play(Location loc, FireworkEffect fe) {
        // Bukkity load (CraftFirework)
        Firework fw = loc.getWorld().spawn(loc, Firework.class);
        // the net.minecraft.server.World
        Object nms_world = null;
        Object nms_firework = null;
        /*
         * The reflection part, this gives us access to funky ways of messing around with things
         */
        if (world_getHandle == null) {
            // get the methods of the craftbukkit objects
            world_getHandle = getMethod(loc.getWorld().getClass(), "getHandle");
            firework_getHandle = getMethod(fw.getClass(), "getHandle");
        }
        try {
            // invoke with no arguments
            nms_world = world_getHandle.invoke(loc.getWorld(), (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ErrorLogger.log(ex, "Failed to play firework");
        }
        try {
            nms_firework = firework_getHandle.invoke(fw, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ErrorLogger.log(ex, "Failed to play firework");
        }
        // null checks are fast, so having this seperate is ok
        if (nms_world_broadcastEntityEffect == null) {
            // get the method of the nms_world
            nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
        }
        /*
         * Now we mess with the metadata, allowing nice clean spawning of a pretty firework (look, pretty lights!)
         */
        // metadata load
        FireworkMeta data = fw.getFireworkMeta();
        // clear existing
        data.clearEffects();
        // power of one
        data.setPower(1);
        // add the effect
        data.addEffect(fe);
        // set the meta
        fw.setFireworkMeta(data);
        try {
            /*
             * Finally, we broadcast the entity effect then kill our fireworks object
             */
            // invoke with arguments
            nms_world_broadcastEntityEffect.invoke(nms_world, nms_firework, (byte) 17);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            ErrorLogger.log(ex, "Failed to play firework");
        }
        // remove from the game
        fw.remove();
    }

    /**
     * Internal method, used as shorthand to grab our method in a nice friendly manner
     *
     * @param cl
     * @param method
     * @return Method (or null)
     */
    private static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

}
