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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.RLocation;
import org.spongepowered.api.data.manipulator.mutable.entity.InvisibilityData;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

public class LocationUtil {

    static int delay2 = 0;

    static {
        if (r.getCnfg().getBoolean("Command.Teleport.EnableDelay")) {
            delay2 = r.getCnfg().getInt("Command.Teleport.Delay");
        }
    }

    public static Location getTarget(final Living entity) {
        BlockRayHit block = BlockRay.from(entity).blockLimit(300).end().orNull();
        if (block == null) {
            return null;
        }
        return block.getLocation();
    }

    // Not needed if using searchSafeLocation(loc)
    public static Location getRoundedDestination(final Location loc) {
        final Extent world = loc.getExtent();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5);
    }

    public static void teleport(final Player p, Location l, final boolean safe, boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
            r.getGame().getScheduler().createTaskBuilder().name("UC: Teleportation delay task #1").execute(new Runnable() {
                @Override
                public void run() {
                    r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
                }
            }).delayTicks(2L).submit(r.getUC());
            r.getGame().getScheduler().createTaskBuilder().name("UC: Teleportation delay task #2").execute(new Runnable() {

                @Override
                public void run() {
                    if (p.getLocation().getBlock().equals(loc.getBlock())) {
                        teleport(p, loc, safe, false);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }).delayTicks(20L * delay2).submit(r.getUC());

            return;
        }
        if (!safe) {
            p.setLocation(l);
            playEffect(p, l);
            return;
        }
        l = r.getGame().getTeleportHelper().getSafeLocation(l).isPresent() ? r.getGame().getTeleportHelper().getSafeLocation(l).get() : l;
        p.setLocation(getRoundedDestination(l));
        playEffect(p, l);
    }

    public static void teleport(Player p, Entity e, final boolean safe, boolean delay) {
        teleport(p, e.getLocation(), safe, delay);
    }

    public static void teleport(Player p, RLocation l, final boolean safe, boolean delay) {
        teleport(p, l.getLocation(), safe, delay);
    }

    public static void teleportUnsafe(Player p, Location loc, boolean delay) {
        teleport(p, loc, false, delay);
    }

    /**
     * Creates a teleport effect
     *
     * @param p   The player who is teleported
     * @param loc The location where the effect is shown
     */
    public static void playEffect(Player p, Location loc) {
        if (p != null && UC.getPlayer(p).isVanish()) {
            return;
        }
        for (Player pl : r.getOnlinePlayers()) {
            if (p != null && !p.get(InvisibilityData.class).get().invisibleToPlayerIds().contains(pl.getUniqueId())) {
                continue;
            }
            r.getRegistry().createParticleEffectBuilder(ParticleTypes.MOB_APPEARANCE).build();
            ((World) loc.getExtent()).playSound(SoundTypes.ENDERMAN_TELEPORT, loc.getPosition(), 10);
        }
    }

    public static RLocation convertStringToLocation(String s) {
        if (s == null) {
            return null;
        }
        if (s.contains(",")) {
            String[] split = s.split(",");
            return new RLocation(new Location(r.getGame().getServer().getWorld(split[0]).get(), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), Double
                    .parseDouble(split[5]), Double.parseDouble(split[4]));
        }
        String[] split = s.split("\\|");
        return new RLocation(new Location(r.getGame().getServer().getWorld(split[0]).get(), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), Double
                .parseDouble(split[5]), Double.parseDouble(split[4]));
    }

    public static String convertLocationToString(RLocation loc) {
        return ((World) loc.getLocation().getExtent()).getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
    }

    public static double getCoordinate(String input, double current) {
        boolean relative = input.startsWith("~");
        double result = relative ? current : 0.0D;
        if ((!relative) || (input.length() > 1)) {
            boolean exact = input.contains(".");
            if (relative) {
                input = input.substring(1);
            }
            double testResult = Double.parseDouble(input);
            if (testResult == -30000001.0D) {
                return -30000001.0D;
            }
            result += testResult;
            if ((!exact) && (!relative)) {
                result += 0.5D;
            }
        }
        if (result < -30000000) {
            result = -30000000.0D;
        }
        if (result > 30000000) {
            result = 30000000.0D;
        }
        return result;
    }

}
