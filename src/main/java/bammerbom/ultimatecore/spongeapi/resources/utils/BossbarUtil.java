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

import bammerbom.ultimatecore.spongeapi.r;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class BossbarUtil implements Listener {

    static boolean enabled = false;
    private static HashMap<UUID, bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon> players = new HashMap<>();
    private static HashMap<UUID, Integer> timers = new HashMap<>();

    public static void enable() {
        if (enabled == true) {
            return;
        }
        enabled = true;
        Bukkit.getServer().getPluginManager().registerEvents(new bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil(), r.getUC());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                for (UUID uuid : players.keySet()) {
                    Player p = Bukkit.getPlayer(uuid);
                    bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(p, players.get(uuid).getTeleportPacket(getDragonLocation(p.getLocation())));
                }
            }
        }, 0L, 5L);
    }

    public static void disable() {
        for (Player player : r.getOnlinePlayers()) {
            removeBar(player);
        }
        players.clear();
        for (Iterator<Integer> i = timers.values().iterator();
             i.hasNext(); ) {
            int timerID = i.next().intValue();
            Bukkit.getScheduler().cancelTask(timerID);
        }
        timers.clear();
    }

    private static String cleanMessage(String message) {
        if (message.length() > 64) {
            message = message.substring(0, 63);
        }
        return message;
    }

    private static void cancelTimer(Player player) {
        Integer timerID = timers.remove(player.getUniqueId());
        if (timerID != null) {
            Bukkit.getScheduler().cancelTask(timerID.intValue());
        }
    }

    private static void sendDragon(bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon, Player player) {
        bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
        bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player.getLocation())));
    }

    private static bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon getDragon(Player player, String message) {
        if (hasBar(player)) {
            return players.get(player.getUniqueId());
        }
        return addDragon(player, cleanMessage(message));
    }

    private static bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon addDragon(Player player, String message) {
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = bammerbom.ultimatecore.spongeapi.resources.utils.Util.newDragon(message, getDragonLocation(player.getLocation()));
        bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, dragon.getSpawnPacket());
        players.put(player.getUniqueId(), dragon);
        return dragon;
    }

    private static bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon addDragon(Player player, Location loc, String message) {
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = bammerbom.ultimatecore.spongeapi.resources.utils.Util.newDragon(message, getDragonLocation(loc));
        bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, dragon.getSpawnPacket());
        players.put(player.getUniqueId(), dragon);
        return dragon;
    }

    private static Location getDragonLocation(Location loc) {
        loc.subtract(0.0D, 300.0D, 0.0D);
        return loc;
    }

    @SuppressWarnings("unused")
    private static BlockFace getDirection(Location loc) {
        float dir = Math.round(loc.getYaw() / 90.0F);
        if ((dir == -4.0F) || (dir == 0.0F) || (dir == 4.0F)) {
            return BlockFace.SOUTH;
        }
        if ((dir == -1.0F) || (dir == 3.0F)) {
            return BlockFace.EAST;
        }
        if ((dir == -2.0F) || (dir == 2.0F)) {
            return BlockFace.NORTH;
        }
        if ((dir == -3.0F) || (dir == 1.0F)) {
            return BlockFace.WEST;
        }
        return null;
    }

    //Api
    public static void setMessage(String message) {
        for (Player player : r.getOnlinePlayers()) {
            setMessage(player, message);
        }
    }

    public static void setMessage(Player player, String message) {
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = dragon.getMaxHealth();
        cancelTimer(player);
        sendDragon(dragon, player);
    }

    public static void setMessage(String message, float percent) {
        for (Player player : r.getOnlinePlayers()) {
            setMessage(player, message, percent);
        }
    }

    public static void setMessage(Player player, String message, float percent) {
        Validate.isTrue((0.0F <= percent) && (percent <= 100.0F), "Percent must be between 0F and 100F, but was: ", percent);
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = (percent / 100.0F * dragon.getMaxHealth());
        cancelTimer(player);
        sendDragon(dragon, player);
    }

    public static void setMessage(String message, int seconds) {
        for (Player player : r.getOnlinePlayers()) {
            setMessage(player, message, seconds);
        }
    }

    public static void setMessage(final Player player, String message, int seconds) {
        Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = getDragon(player, message);
        dragon.name = cleanMessage(message);
        dragon.health = dragon.getMaxHealth();
        final float dragonHealthMinus = dragon.getMaxHealth() / seconds;
        cancelTimer(player);

        timers.put(player.getUniqueId(), Integer.valueOf(Bukkit.getScheduler().runTaskTimer(r.getUC(), new Runnable() {
            @Override
            public void run() {
                bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon drag = getDragon(player, "");
                drag.health -= dragonHealthMinus;

                if (drag.health <= 1.0F) {
                    removeBar(player);
                    cancelTimer(player);
                } else {
                    sendDragon(drag, player);
                }
            }
        }, 20L, 20L).getTaskId()));

        sendDragon(dragon, player);
    }

    public static boolean hasBar(Player player) {
        return players.get(player.getUniqueId()) != null;
    }

    public static void removeBar(Player player) {
        if (!hasBar(player)) {
            return;
        }
        bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

        players.remove(player.getUniqueId());

        cancelTimer(player);
    }

    public static void setHealth(Player player, float percent) {
        if (!hasBar(player)) {
            return;
        }
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = getDragon(player, "");
        dragon.health = (percent / 100.0F * dragon.getMaxHealth());

        cancelTimer(player);

        if (percent == 0.0F) {
            removeBar(player);
        } else {
            sendDragon(dragon, player);
        }
    }

    public static float getHealth(Player player) {
        if (!hasBar(player)) {
            return -1.0F;
        }
        return getDragon(player, "").health;
    }

    public static String getMessage(Player player) {
        if (!hasBar(player)) {
            return "";
        }
        return getDragon(player, "").name;
    }

    //Events
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLoggout(PlayerQuitEvent event) {
        removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        handleTeleport(event.getPlayer(), event.getTo().clone());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerRespawnEvent event) {
        handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
    }

    //Methods
    private void handleTeleport(final Player player, final Location loc) {
        if (!hasBar(player)) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(r.getUC(), new Runnable() {
            @Override
            public void run() {
                if (!hasBar(player)) {
                    return;
                }
                bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon oldDragon = getDragon(player, "");
                float health = oldDragon.health;
                String message = oldDragon.name;
                bammerbom.ultimatecore.spongeapi.resources.utils.Util.sendPacket(player, getDragon(player, "").getDestroyPacket());
                players.remove(player.getUniqueId());
                bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon dragon = addDragon(player, loc, message);
                dragon.health = health;
                sendDragon(dragon, player);
            }
        }, 2L);
    }
}

class Util {

    public static String version = (Bukkit.getServer() != null ? Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] : "UNKNOWN") + ".";

    public static bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon newDragon(String message, Location loc) {
        bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon fakeDragon = null;
        try {
            fakeDragon = bammerbom.ultimatecore.spongeapi.resources.utils.FakeDragon.class.getConstructor(new Class[]{String.class, Location.class}).newInstance(new Object[]{message, loc});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return fakeDragon;
    }

    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getHandle(p);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, new Object[]{packet});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getCraftClass(String ClassName) {
        String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static Object getHandle(World world) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(world.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(world, new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    public static Object getHandle(Entity entity) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity, new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    public static Field getField(Class<?> cl, String field_name) {
        try {
            return cl.getDeclaredField(field_name);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
        for (Method m : cl.getMethods()) {
            if ((m.getName().equals(method)) && (ClassListEqual(args, m.getParameterTypes()))) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method, Integer args) {
        for (Method m : cl.getMethods()) {
            if ((m.getName().equals(method)) && (args.equals(m.getParameterTypes().length))) {
                return m;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;

        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0;
             i < l1.length;
             i++) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }

        return equal;
    }
}

class FakeDragon {

    public float health = 0.0F;
    public String name;
    private float maxHealth = 200.0F;
    private int x;
    private int y;
    private int z;
    private int pitch = 0;
    private int yaw = 0;
    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;
    private boolean visible = false;
    private Object world;
    private Object dragon;
    private int id;

    public FakeDragon(String name, Location loc, int percent) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = (loc.getBlockY() + 200);
        this.z = loc.getBlockZ();
        this.health = (percent / 100.0F * this.maxHealth);
        this.world = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getHandle(loc.getWorld());
    }

    public FakeDragon(String name, Location loc) {
        this.name = name;
        this.x = loc.getBlockX();
        this.y = (loc.getBlockY() + 200);
        this.z = loc.getBlockZ();
        this.world = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getHandle(loc.getWorld());
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(float max) {
        this.maxHealth = max;
    }

    public void setHealth(int percent) {
        this.health = (percent / 100.0F * this.maxHealth);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getPitch() {
        return this.pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getYaw() {
        return this.yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public byte getXvel() {
        return this.xvel;
    }

    public void setXvel(byte xvel) {
        this.xvel = xvel;
    }

    public byte getYvel() {
        return this.yvel;
    }

    public void setYvel(byte yvel) {
        this.yvel = yvel;
    }

    public byte getZvel() {
        return this.zvel;
    }

    public void setZvel(byte zvel) {
        this.zvel = zvel;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Object getWorld() {
        return this.world;
    }

    public void setWorld(Object world) {
        this.world = world;
    }

    public Object getSpawnPacket() {
        Class<?> Entity = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("Entity");
        Class<?> EntityLiving = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("EntityLiving");
        Class<?> EntityEnderDragon = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("EntityEnderDragon");
        Object packet = null;
        try {
            this.dragon = EntityEnderDragon.getConstructor(new Class[]{bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("World")}).newInstance(new Object[]{getWorld()});

            Method setLocation = bammerbom.ultimatecore.spongeapi.resources.utils.Util
                    .getMethod(EntityEnderDragon, "setLocation", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE});
            setLocation.invoke(this.dragon, new Object[]{Integer.valueOf(getX()), Integer.valueOf(getY()), Integer.valueOf(getZ()), Integer.valueOf(getPitch()), Integer.valueOf(getYaw())});

            Method setInvisible = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getMethod(EntityEnderDragon, "setInvisible", new Class[]{Boolean.TYPE});
            setInvisible.invoke(this.dragon, new Object[]{Boolean.valueOf(isVisible())});

            Method setCustomName = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getMethod(EntityEnderDragon, "setCustomName", new Class[]{String.class});
            setCustomName.invoke(this.dragon, new Object[]{this.name});

            Method setHealth = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getMethod(EntityEnderDragon, "setHealth", new Class[]{Float.TYPE});
            setHealth.invoke(this.dragon, new Object[]{Float.valueOf(this.health)});

            Field motX = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getField(Entity, "motX");
            motX.set(this.dragon, Byte.valueOf(getXvel()));

            Field motY = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getField(Entity, "motY");
            motY.set(this.dragon, Byte.valueOf(getYvel()));

            Field motZ = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getField(Entity, "motZ");
            motZ.set(this.dragon, Byte.valueOf(getZvel()));

            Method getId = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getMethod(EntityEnderDragon, "getId", new Class[0]);
            this.id = ((Number) getId.invoke(this.dragon, new Object[0])).intValue();

            Class<?> PacketPlayOutSpawnEntityLiving = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("PacketPlayOutSpawnEntityLiving");

            packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class[]{EntityLiving}).newInstance(new Object[]{this.dragon});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getDestroyPacket() {
        Class<?> PacketPlayOutEntityDestroy = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("PacketPlayOutEntityDestroy");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityDestroy.newInstance();
            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, new int[]{this.id});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getMetaPacket(Object watcher) {
        Class<?> DataWatcher = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("DataWatcher");

        Class<?> PacketPlayOutEntityMetadata = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("PacketPlayOutEntityMetadata");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityMetadata.getConstructor(new Class[]{Integer.TYPE, DataWatcher, Boolean.TYPE})
                    .newInstance(new Object[]{Integer.valueOf(this.id), watcher, Boolean.valueOf(true)});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getTeleportPacket(Location loc) {
        Class<?> PacketPlayOutEntityTeleport = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("PacketPlayOutEntityTeleport");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityTeleport.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE, Boolean.TYPE})
                    .newInstance(new Object[]{Integer.valueOf(this.id), Integer.valueOf(loc.getBlockX() * 32), Integer.valueOf(loc.getBlockY() * 32), Integer.valueOf(loc.getBlockZ() * 32), Byte
                            .valueOf((byte) ((int) loc.getYaw() * 256 / 360)), Byte.valueOf((byte) ((int) loc.getPitch() * 256 / 360)), Boolean.valueOf(false)});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public Object getWatcher() {
        Class<?> Entity = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("Entity");
        Class<?> DataWatcher = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getCraftClass("DataWatcher");

        Object watcher = null;
        try {
            watcher = DataWatcher.getConstructor(new Class[]{Entity}).newInstance(new Object[]{this.dragon});
            Method a = bammerbom.ultimatecore.spongeapi.resources.utils.Util.getMethod(DataWatcher, "a", new Class[]{Integer.TYPE, Object.class});

            a.invoke(watcher, new Object[]{Integer.valueOf(0), Byte.valueOf(isVisible() ? (byte) 0 : (byte) 32)});
            a.invoke(watcher, new Object[]{Integer.valueOf(6), Float.valueOf(this.health)});
            a.invoke(watcher, new Object[]{Integer.valueOf(7), Integer.valueOf(0)});
            a.invoke(watcher, new Object[]{Integer.valueOf(8), Byte.valueOf((byte) 0)});
            a.invoke(watcher, new Object[]{Integer.valueOf(10), this.name});
            a.invoke(watcher, new Object[]{Integer.valueOf(11), Byte.valueOf((byte) 1)});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return watcher;
    }
}
