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
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class LocationUtil {

    static int delay2 = 0;

    static {
        if (r.getCnfg().getBoolean("Command.Teleport.EnableDelay")) {
            delay2 = r.getCnfg().getInt("Command.Teleport.Delay");
        }
    }

    public static Optional<Location> getTarget(final Living entity) {
        Optional<BlockRayHit<World>> hit = BlockRay.from(entity).build().end();
        if (!hit.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(hit.get().getLocation());
    }

    public static void teleport(final Player p, Location l, final Cause c, final boolean safe, boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false)) {
            final Location loc = p.getLocation();
            TeleportHelper helper = Sponge.getGame().getTeleportHelper();
            if (safe) {
                l = helper.getSafeLocation(l).orElse(l);
            }
            final Location to = l;
            Sponge.getScheduler().createTaskBuilder().delayTicks(2L).name("Teleport delay starting message delay").execute(new Runnable() {
                @Override
                public void run() {
                    r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
                }
            }).submit(r.getUC());
            Sponge.getScheduler().createTaskBuilder().delayTicks(20L * delay2).name("Teleport delay").execute(new Runnable() {
                @Override
                public void run() {
                    if (p.getLocation().equals(loc)) {
                        teleport(p, to, c, safe, false);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }).submit(r.getUC());

            return;
        }
        if (p.getVehicle().isPresent()) {
            p.setVehicle(null);
        }
        p.setLocation(l);
        playEffect(p, l);
        return;
    }

    public static void teleport(Player p, Entity e, final Cause c, final boolean safe, boolean delay) {
        teleport(p, e.getLocation(), c, safe, delay);
    }

    public static void teleportUnsafe(Player p, Location loc, Cause c, boolean delay) {
        teleport(p, loc, c, false, delay);
    }

    /**
     * Creates a teleport effect
     *
     * @param p   The player who is teleported
     * @param loc The location where the effect is shown
     */
    public static void playEffect(Player p, Location<World> loc) {
        if (p != null && UC.getPlayer(p).isVanish()) {
            return;
        }
        for (Player pl : Sponge.getServer().getOnlinePlayers()) {
            if (p != null && !pl.canSee(p)) {
                continue;
            }
            pl.spawnParticles(ParticleEffect.builder().type(ParticleTypes.MOB_APPEARANCE).build(), loc.getPosition()); //TODO is this ENDER_SIGNAL?
            pl.playSound(SoundTypes.ENTITY_ENDERMEN_TELEPORT, loc.getPosition(), 1);
        }
    }

    /**
     * Converts a serialized string to a location
     *
     * @param s The string
     * @return An Object[] with a Location and a Vector3d for the rotation.
     */
    public static Object[] convertStringToLocation(String s) {
        if (s == null) {
            return null;
        }
        String[] split = s.contains(",") ? s.split(",") : s.split("\\|");
        Location loc = new Location(Sponge.getServer().getWorld(UUID.fromString(split[0])).get(), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
        Vector3d rot = new Vector3d(Double.parseDouble(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]));
        return new Object[]{loc, rot};
    }

    public static String convertLocationToString(Location<World> loc, Vector3d rot) {
        return loc.getExtent().getUniqueId() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + rot.getX() + "|" + rot.getY() + "|" + rot.getZ();
    }

    public static Double getCoordinate(String input, double current) {
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

    public static Double getDistance(Location l1, Location l2) {
        if (l1.getExtent() != l2.getExtent()) {
            return null;
        }
        return Math.sqrt(Math.pow(l1.getX() - l2.getX(), 2) + Math.pow(l1.getY() - l2.getY(), 2) + Math.pow(l1.getZ() - l2.getZ(), 2));
    }

    public static Optional<Integer> getHighestBlockYAt(World w, int x, int z) {
        int y = 256; //TODO max hight?
        while (y >= 0) {
            if (!w.getLocation(x, y, z).getBlockType().equals(BlockTypes.AIR)) {
                return Optional.of(y);
            }
            y--;
        }
        return Optional.empty();
    }
}
