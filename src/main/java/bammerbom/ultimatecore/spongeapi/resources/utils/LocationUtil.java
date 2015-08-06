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
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.entity.FlyingData;
import org.spongepowered.api.data.manipulator.entity.InvisibilityData;
import org.spongepowered.api.data.manipulator.entity.PassengerData;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.*;

public class LocationUtil {

    // The player can stand inside these materials
    public static final Set<BlockType> HOLLOW_MATERIALS = new HashSet<>();
    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;
    private static final Set<BlockType> TRANSPARENT_MATERIALS = new HashSet<>();
    static int delay2 = 0;

    static {
        HOLLOW_MATERIALS.add(BlockTypes.AIR);
        HOLLOW_MATERIALS.add(BlockTypes.SAPLING);
        HOLLOW_MATERIALS.add(BlockTypes.GOLDEN_RAIL);
        HOLLOW_MATERIALS.add(BlockTypes.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(BlockTypes.TALLGRASS);
        HOLLOW_MATERIALS.add(BlockTypes.DEADBUSH);
        HOLLOW_MATERIALS.add(BlockTypes.YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(BlockTypes.RED_FLOWER);
        HOLLOW_MATERIALS.add(BlockTypes.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(BlockTypes.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(BlockTypes.TORCH);
        HOLLOW_MATERIALS.add(BlockTypes.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(BlockTypes.WHEAT);
        HOLLOW_MATERIALS.add(BlockTypes.CARROTS);
        HOLLOW_MATERIALS.add(BlockTypes.POTATOES);
        HOLLOW_MATERIALS.add(BlockTypes.STANDING_SIGN);
        HOLLOW_MATERIALS.add(BlockTypes.WOODEN_DOOR);
        HOLLOW_MATERIALS.add(BlockTypes.LADDER);
        HOLLOW_MATERIALS.add(BlockTypes.RAIL);
        HOLLOW_MATERIALS.add(BlockTypes.WALL_SIGN);
        HOLLOW_MATERIALS.add(BlockTypes.LEVER);
        HOLLOW_MATERIALS.add(BlockTypes.STONE_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(BlockTypes.IRON_DOOR);
        HOLLOW_MATERIALS.add(BlockTypes.WOODEN_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(BlockTypes.REDSTONE_TORCH);
        HOLLOW_MATERIALS.add(BlockTypes.UNLIT_REDSTONE_TORCH);
        HOLLOW_MATERIALS.add(BlockTypes.STONE_BUTTON);
        HOLLOW_MATERIALS.add(BlockTypes.SNOW);
        HOLLOW_MATERIALS.add(BlockTypes.REEDS);
        HOLLOW_MATERIALS.add(BlockTypes.POWERED_REPEATER);
        HOLLOW_MATERIALS.add(BlockTypes.UNPOWERED_REPEATER);
        HOLLOW_MATERIALS.add(BlockTypes.POWERED_COMPARATOR);
        HOLLOW_MATERIALS.add(BlockTypes.UNPOWERED_COMPARATOR);
        HOLLOW_MATERIALS.add(BlockTypes.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(BlockTypes.MELON_STEM);
        HOLLOW_MATERIALS.add(BlockTypes.VINE);
        HOLLOW_MATERIALS.add(BlockTypes.FENCE_GATE);
        HOLLOW_MATERIALS.add(BlockTypes.WATERLILY);
        HOLLOW_MATERIALS.add(BlockTypes.NETHER_WART);
        HOLLOW_MATERIALS.add(BlockTypes.CARPET);
        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);
        TRANSPARENT_MATERIALS.add(BlockTypes.WATER);
        TRANSPARENT_MATERIALS.add(BlockTypes.FLOWING_WATER);
    }

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

    public static Location getTarget(final Living entity) {
        BlockRayHit block = BlockRay.from(entity).blockLimit(300).end().orNull();
        if (block == null) {
            return null;
        }
        return block.getLocation();
    }

    static boolean isBlockAboveAir(final Extent world, final int x, final int y, final int z) {
        if (y > world.getBlockMax().getY()) {
            return true;
        }
        return HOLLOW_MATERIALS.contains(new Location(world, x, y - 1, z).getBlock());
    }

    public static boolean isBlockUnsafeForUser(final Player user, final Extent world, final int x, final int y, final int z) {
        if (world.equals(user.getWorld()) && user.getGameModeData().getGameMode() == GameModes.CREATIVE || user.getGameModeData().getGameMode() == GameModes.SPECTATOR || UC.getPlayer(user).isGod())&&
        user.mayFly()){
            return false;
        }

        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockUnsafe(final Extent world, final int x, final int y, final int z) {
        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(final Extent world, final int x, final int y, final int z) {
        Location loc = new Location(world, x, y, z);
        Location below = new Location(world, x, y - 1, z);
        if (below.getBlock() == BlockTypes.LAVA || below.getBlock() == BlockTypes.FLOWING_LAVA) {
            return true;
        }
        if (below.getBlock() == BlockTypes.FIRE) {
            return true;
        }
        if (below.getBlock() == BlockTypes.BED) {
            return true;
        }
        return (!HOLLOW_MATERIALS.contains(loc.getBlock())) || (!HOLLOW_MATERIALS.contains(new Location(world, x, y + 1, z).getBlockType()));
    }

    // Not needed if using searchSafeLocation(loc)
    public static Location getRoundedDestination(final Location loc) {
        final Extent world = loc.getExtent();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5);
    }

    public static Location searchSafeLocation(final Player user, final Location loc) {
        if (loc.getExtent().equals(user.getWorld()) && (user.getGameModeData().getGameMode() == GameModes.CREATIVE || UC.getPlayer(user).isGod()) && user.getAllowFlight()) {
            if (shouldFly(loc)) {
                user.getOrCreate(FlyingData.class);
            }
            return getRoundedDestination(loc);
        }
        return searchSafeLocation(loc);
    }

    public static Location searchSafeLocation(final Location loc) {
        if (loc == null || loc.getExtent() == null) {
            return null;
        }
        Location org = loc;
        final Extent world = loc.getExtent();
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
            if (y >= world.getBlockMax().getY()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = getHighestBlockYAt(world, x, z);
                if (x - 48 > loc.getBlockX()) {
                    return org;
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5);
    }

    public static Integer getHighestBlockYAt(Extent world, Integer x, Integer z) {
        int current = world.getBlockMax().getY();
        while (new Location(world, x, current, z).getBlockType().isGaseous()) {
            current--;
        }
        return current;
    }

    public static boolean shouldFly(Location loc) {
        final Extent world = loc.getExtent();
        final int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        final int z = loc.getBlockZ();
        int count = 0;
        while (isBlockUnsafe(world, x, y, z) && y > -1) {
            y--;
            count++;
            if (count > 2) {
                return true;
            }
        }

        return y < 0;
    }

    public static void teleport(final Player p, Location l, final TeleportCause c, final boolean safe, boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
            l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
            final Location to = l;
            r.getGame().getScheduler().getTaskBuilder().execute(new Runnable() {
                @Override
                public void run() {
                    r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
                }
            }).delay(2L).submit(r.getUC());
            r.getGame().getScheduler().getTaskBuilder().execute(new Runnable() {

                @Override
                public void run() {
                    if (p.getLocation().getBlock().equals(loc)) {
                        teleport(p, to, c, safe, false);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }).delay(20L * delay2).submit(r.getUC());

            return;
        }
        if (p.getData(PassengerData.class).isPresent()) {
            p.remove(PassengerData.class);
        }
        if (!safe) {
            p.setLocation(getRoundedDestination(l), c);
            playEffect(p, l);
            return;
        }

        if (bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil.isBlockUnsafeForUser(p, l.getExtent(), l.getBlockX(), l.getBlockY(), l.getBlockZ())) {
            p.setLocation(searchSafeLocation(p, l), c);
            playEffect(p, l);
        } else {
            p.setLocation(getRoundedDestination(l), c);
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
        for (Player pl : r.getOnlinePlayers()) {
            if (p != null && !p.getOrCreate(InvisibilityData.class).get().isInvisibleTo(pl)) {
                continue;
            }
            r.getRegistry().getParticleEffectBuilder(ParticleTypes.MOB_APPEARANCE).build();
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
