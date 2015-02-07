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
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import java.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class LocationUtil {

    private static final Vector3D[] VOLUME;
    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    private static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<>();
    static Integer delay2 = 0;

    static {
        HOLLOW_MATERIALS.add(Material.AIR);
        HOLLOW_MATERIALS.add(Material.SAPLING);
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(Material.LONG_GRASS);
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(Material.RED_ROSE);
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(Material.SEEDS);
        HOLLOW_MATERIALS.add(Material.SIGN_POST);
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR);
        HOLLOW_MATERIALS.add(Material.LADDER);
        HOLLOW_MATERIALS.add(Material.RAILS);
        HOLLOW_MATERIALS.add(Material.WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.LEVER);
        HOLLOW_MATERIALS.add(Material.STONE_PLATE);
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK);
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON);
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
        HOLLOW_MATERIALS.add(Material.SNOW);
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(Material.MELON_STEM);
        HOLLOW_MATERIALS.add(Material.VINE);
        HOLLOW_MATERIALS.add(Material.FENCE_GATE);
        HOLLOW_MATERIALS.add(Material.WATER_LILY);
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS);
        HOLLOW_MATERIALS.add(Material.CARPET);
        for (Material mat : HOLLOW_MATERIALS) {
            TRANSPARENT_MATERIALS.add(mat);
        }
        TRANSPARENT_MATERIALS.add(Material.WATER);
        TRANSPARENT_MATERIALS.add(Material.STATIONARY_WATER);
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(pos, new Comparator<Vector3D>() {
            @Override
            public int compare(Vector3D a, Vector3D b) {
                return a.x * a.x + a.y * a.y + a.z * a.z - (b.x * b.x + b.y * b.y + b.z * b.z);
            }
        });
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    static {
        if (r.getCnfg().getBoolean("Command.Teleport.EnableDelay")) {
            delay2 = r.getCnfg().getInt("Command.Teleport.Delay");
        }
    }

    public static Location convertStringToLocation(String s) {
        if (s == null) {
            return null;
        }
        if (s.contains(",")) {
            String[] split = s.split(",");
            return new Location(
                    Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
        String[] split = s.split("\\|");
        return new Location(
                Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String convertLocationToString(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
    }

    public static Location searchSafeLocation(Location loc) {
        if ((loc == null) || (loc.getWorld() == null)) {
            return null;
        }
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y--;
            if (y < 0) {
                y = origY;
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
                y = origY + 3;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
            z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y++;
            if (y >= world.getMaxHeight()) {
                x++;
            }
        }

        while (isBlockUnsafe(world, x, y, z)) {
            y--;
            if (y <= 1) {
                x++;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) {
                    return null;
                }
            }
        }
        return new Location(world, x + 0.5D, y, z + 0.5D, loc.getYaw(), loc.getPitch());
    }

    @SuppressWarnings("deprecation")
    public static Location getTarget(LivingEntity entity) {
        HashSet<Byte> TMb = new HashSet<>();
        for (Material mat : TRANSPARENT_MATERIALS) {
            TMb.add((byte) mat.getId());
        }
        Block block = entity.getTargetBlock(TMb, 300);
        if (block == null) {
            return null;
        }
        return block.getLocation();
    }

    @SuppressWarnings("deprecation")
    public static Location getAbsoluteTarget(LivingEntity entity) {
        Block block = entity.getTargetBlock(null, 300);
        if (block == null) {
            return null;
        }
        return block.getLocation();
    }

    public static void teleport(Player p, Entity l, TeleportCause c, Boolean delay) {
        teleport(p, l.getLocation(), c, delay);
    }

    public static void teleportUnsafe(Player p, Entity l, TeleportCause c, Boolean delay) {
        teleportUnsafe(p, l.getLocation(), c, delay);
    }

    public static void teleport(final Player p, Location l, final TeleportCause c, Boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = p.getLocation().getBlock().getLocation();
            l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
            final Location to = l;
            r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
            Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {

                @Override
                public void run() {
                    if (p.getLocation().getBlock().getLocation().equals(loc)) {
                        if (p.isInsideVehicle()) {
                            p.leaveVehicle();
                        }
                        p.teleport(to, c);
                        playEffect(p, to);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }, (20L * delay2));

        } else {
            l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
            if (p.isInsideVehicle()) {
                p.leaveVehicle();
            }
            p.teleport(l, c);
        }
    }

    public static void teleportUnsafe(final Player p, Location l, final TeleportCause c, Boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = p.getLocation().getBlock().getLocation();
            final Location to = l;
            r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
            Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {

                @Override
                public void run() {
                    if (p.getLocation().getBlock().getLocation().equals(loc)) {
                        if (p.isInsideVehicle()) {
                            p.leaveVehicle();
                        }
                        p.teleport(to, c);
                        playEffect(p, to);
                        r.sendMes(p, "teleportDelaySucces");
                    } else {
                        r.sendMes(p, "teleportDelayFailedMove");
                    }
                }
            }, (20L * delay2));

        } else {
            if (p.isInsideVehicle()) {
                p.leaveVehicle();
            }
            p.teleport(l, c);
        }
    }

    //
    private static boolean isBlockAboveAir(World world, int x, int y, int z) {
        if (y > world.getMaxHeight()) {
            return true;
        }
        return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    private static boolean isBlockUnsafe(World world, int x, int y, int z) {
        if (isBlockDamaging(world, x, y, z)) {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    private static boolean isBlockDamaging(World world, int x, int y, int z) {
        Block below = world.getBlockAt(x, y - 1, z);
        if ((below.getType() == Material.LAVA) || (below.getType() == Material.STATIONARY_LAVA)) {
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

    /**
     * Creates a teleport effect
     *
     * @param p The player who is teleported
     * @param loc The location where the effect is shown
     */
    public static void playEffect(Player p, Location loc) {
        if (UC.getPlayer(p).isVanish()) {
            return;
        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (p != null && !pl.canSee(p)) {
                continue;
            }
            pl.playEffect(loc, Effect.ENDER_SIGNAL, 10);
            pl.playSound(loc, Sound.ENDERMAN_TELEPORT, 1, 1);
        }
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

    private static class Vector3D {

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
