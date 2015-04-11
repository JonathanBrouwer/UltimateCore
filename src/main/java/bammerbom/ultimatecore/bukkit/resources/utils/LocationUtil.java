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
import static bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil.isBlockDamaging;
import java.util.*;
import org.bukkit.*;
import static org.bukkit.Material.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

public class LocationUtil {

    // The player can stand inside these materials
    public static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    private static final Set<Material> TRANSPARENT_MATERIALS = new HashSet<>();

    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;
    static int delay2 = 0;

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
        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);
        TRANSPARENT_MATERIALS.add(Material.WATER);
        TRANSPARENT_MATERIALS.add(Material.STATIONARY_WATER);
    }

    static {
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(
                pos, new Comparator<Vector3D>() {
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

    public static ItemStack convertBlockToItem(final Block block) {
        final ItemStack is = new ItemStack(block.getType(), 1, (short) 0, block.getData());
        switch (is.getType()) {
            case WOODEN_DOOR:
                is.setType(Material.WOOD_DOOR);
                is.setDurability((short) 0);
                break;
            case IRON_DOOR_BLOCK:
                is.setType(Material.IRON_DOOR);
                is.setDurability((short) 0);
                break;
            case SIGN_POST:
            case WALL_SIGN:
                is.setType(Material.SIGN);
                is.setDurability((short) 0);
                break;
            case CROPS:
                is.setType(Material.SEEDS);
                is.setDurability((short) 0);
                break;
            case CAKE_BLOCK:
                is.setType(Material.CAKE);
                is.setDurability((short) 0);
                break;
            case BED_BLOCK:
                is.setType(Material.BED);
                is.setDurability((short) 0);
                break;
            case REDSTONE_WIRE:
                is.setType(Material.REDSTONE);
                is.setDurability((short) 0);
                break;
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
                is.setType(Material.REDSTONE_TORCH_ON);
                is.setDurability((short) 0);
                break;
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
                is.setType(Material.DIODE);
                is.setDurability((short) 0);
                break;
            case DOUBLE_STEP:
                is.setType(Material.STEP);
                break;
            case TORCH:
            case RAILS:
            case LADDER:
            case WOOD_STAIRS:
            case COBBLESTONE_STAIRS:
            case LEVER:
            case STONE_BUTTON:
            case FURNACE:
            case DISPENSER:
            case PUMPKIN:
            case JACK_O_LANTERN:
            case WOOD_PLATE:
            case STONE_PLATE:
            case PISTON_STICKY_BASE:
            case PISTON_BASE:
            case IRON_FENCE:
            case THIN_GLASS:
            case TRAP_DOOR:
            case FENCE:
            case FENCE_GATE:
            case NETHER_FENCE:
                is.setDurability((short) 0);
                break;
            case FIRE:
                return null;
            case PUMPKIN_STEM:
                is.setType(Material.PUMPKIN_SEEDS);
                break;
            case MELON_STEM:
                is.setType(Material.MELON_SEEDS);
                break;
        }
        return is;
    }

    public static Location getTarget(final LivingEntity entity) {
        final Block block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
        if (block == null) {
            return null;
        }
        return block.getLocation();
    }

    static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        if (y > world.getMaxHeight()) {
            return true;
        }
        return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    public static boolean isBlockUnsafeForUser(final Player user, final World world, final int x, final int y, final int z) {
        if (world.equals(user.getWorld())
                && (user.getGameMode() == GameMode.CREATIVE || UC.getPlayer(user).isGod())
                && user.getAllowFlight()) {
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
        final Block below = world.getBlockAt(x, y - 1, z);
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

    // Not needed if using searchSafeLocation(loc)
    public static Location getRoundedDestination(final Location loc) {
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static Location searchSafeLocation(final Player user, final Location loc) {
        if (loc.getWorld().equals(user.getWorld())
                && (user.getGameMode() == GameMode.CREATIVE || UC.getPlayer(user).isGod())
                && user.getAllowFlight()) {
            if (shouldFly(loc)) {
                user.setFlying(true);
            }
            return getRoundedDestination(loc);
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

    public static void teleport(final Player p, Location l, final TeleportCause c, final boolean safe, boolean delay) {
        if (delay && delay2 > 0 && !r.perm(p, "uc.teleport.bypasstimer", false, false)) {
            final Location loc = p.getLocation().getBlock().getLocation();
            l = searchSafeLocation(l) != null ? searchSafeLocation(l) : l;
            final Location to = l;
            r.sendMes(p, "teleportDelayStarting", "%Time", delay2);
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
            p.teleport(LocationUtil.getRoundedDestination(l), c);
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
            p.teleport(LocationUtil.getRoundedDestination(l), c);
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

    public static Location convertStringToLocation(String s) {
        if (s == null) {
            return null;
        }
        if (s.contains(",")) {
            String[] split = s.split(",");
            return new Location(
                    Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));
        }
        String[] split = s.split("\\|");
        return new Location(
                Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));
    }

    public static String convertLocationToString(Location loc) {
        return loc.getWorld().getName() + "|" + loc.getX() + "|" + loc.getY() + "|" + loc.getZ() + "|" + loc.getPitch() + "|" + loc.getYaw();
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

    public static Location getAbsoluteTarget(LivingEntity entity) {
        Block block = entity.getTargetBlock((Set<Material>) null, 300);
        if (block == null) {
            return null;
        }
        return block.getLocation();
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
