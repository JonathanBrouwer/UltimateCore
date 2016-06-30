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
import org.bukkit.util.NumberConversions;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.property.block.PassableProperty;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class LocationUtil {

    // The player can stand inside these materials
    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;
    static int delay2 = 0;

    static {
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -RADIUS;
             x <= RADIUS;
             x++) {
            for (int y = -RADIUS;
                 y <= RADIUS;
                 y++) {
                for (int z = -RADIUS;
                     z <= RADIUS;
                     z++) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(pos, new Comparator<Vector3D>() {
            @Override
            public int compare(Vector3D a, Vector3D b) {
                return (a.x * a.x + a.y * a.y + a.z * a.z) - (b.x * b.x + b.y * b.y + b.z * b.z);
            }
        });
        VOLUME = pos.toArray(new Vector3D[0]);
    }

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

    static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        return world.getLocation(x, y - 1, z).getBlockType().getProperty(PassableProperty.class).get().getValue();
    }

    public static boolean isBlockUnsafeForUser(final Player user, final World world, final int x, final int y, final int z) {
        if (world == null || world == null) {
            return false;
        }
        if (world.equals(user.getWorld()) && (user.get(Keys.GAME_MODE).orElse(GameModes.NOT_SET) == GameModes.CREATIVE || UC.getPlayer(user).isGod()) && user.get(Keys.CAN_FLY).orElse
                (false)) {
            return false;
        }

        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(final World world, final int x, final int y, final int z) {
        final Location below = world.getBlockAt(x, y - 1, z);
        if (below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA) {
            return true;
        }
        if (below.getType() == Material.FIRE) {
            return true;
        }
        if (below.getType() == Material.BED_BLOCK) {
            return true;
        }
        return (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType())) || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType()));
    }

    public static Location searchSafeLocation(final Player user, final Location loc) {
        if (loc.getWorld().equals(user.getWorld()) && (user.getGameMode() == GameMode.CREATIVE || UC.getPlayer(user).isGod()) && user.getAllowFlight()) {
            if (shouldFly(loc)) {
                user.setFlying(true);
            }
            return loc;
        }
        return searchSafeLocation(loc);
    }

    public static Location searchSafeLocation(final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }
        Location org = loc;
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y -= 1;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
            z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y += 1;
            if (y >= world.getMaxHeight()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) {
                    return org;
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static boolean shouldFly(Location loc) {
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        final int z = loc.getBlockZ();
        int count = 0;
        while (LocationUtil.isBlockUnsafe(world, x, y, z) && y > -1) {
            y--;
            count++;
            if (count > 2) {
                return true;
            }
        }

        return y < 0;
    }

    public static void teleport(final Player p, Location l, final Cause c, final boolean safe, boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = p.getLocation().getBlock().getLocation();
            l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
            final Location to = l;
            Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                @Override
                public void run() {
                    r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
                }
            }, 2L);
            Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {

                @Override
                public void run() {
                    if (p.getLocation().getBlock().getLocation().equals(loc)) {
                        teleport(p, to, c, safe, false);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }, (20L * delay2));

            return;
        }
        if (!safe) {
            if (p.isInsideVehicle()) {
                p.leaveVehicle();
            }
            p.teleport(l, c);
            playEffect(p, l);
            return;
        }

        if (LocationUtil.isBlockUnsafeForUser(p, l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
            if (p.isInsideVehicle()) {
                p.leaveVehicle();
            }
            p.teleport(LocationUtil.searchSafeLocation(p, l), c);
            playEffect(p, l);
        } else {
            if (p.isInsideVehicle()) {
                p.leaveVehicle();
            }
            p.teleport(l, c);
            playEffect(p, l);
        }
    }

    public static void teleport(Player p, Entity e, final TeleportCause c, final boolean safe, boolean delay) {
        teleport(p, e.getLocation(), c, safe, delay);
    }

    public static void teleportUnsafe(Player p, Location loc, TeleportCause c, boolean delay) {
        teleport(p, loc, c, false, delay);
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
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (p != null && !pl.canSee(p)) {
                continue;
            }
            pl.playEffect(loc, Effect.ENDER_SIGNAL, 10);
            pl.playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
        }
    }

    public static Location convertStringToLocation(String s) {
        if (s == null) {
            return null;
        }
        if (s.contains(",")) {
            String[] split = s.split(",");
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float
                    .parseFloat(split[4]));
        }
        String[] split = s.split("\\|");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float
                .parseFloat(split[4]));
    }

    public static String convertLocationToString(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
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

    public static Location getAbsoluteTarget(LivingEntity entity) {
        Block block = entity.getTargetBlock((Set<Material>) null, 300);
        if (block == null) {
            return null;
        }
        return block.getLocation();

    }

    public static Double getDistance(Location l1, Location l2) {
        if (l1.getExtent() != l2.getExtent()) {
            return null;
        }
        return Math.sqrt(NumberConversions.square(l1.getX() - l2.getX()) + NumberConversions.square(l1.getY() - l2.getY()) + NumberConversions.square(l1.getZ() - l2.getZ()));
    }

    public static class Vector3D {

        public int x;
        public int y;
        public int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
