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
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TitleUtil {

    public static void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        new bammerbom.ultimatecore.spongeapi.resources.utils.Title(title, subtitle, fadeIn, stay, fadeOut).send(p);
    }

    public static void sendActionBar(Player player, String message) {
        String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        try {
            Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object p = c1.cast(player);
            Object ppoc = null;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if ((nmsver.equalsIgnoreCase("v1_8_R1")) || (!nmsver.startsWith("v1_8_"))) {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", new Class[]{String.class});
                Object cbc = c3.cast(m3.invoke(c2, new Object[]{"{\"text\": \"" + message + "\"}"}));
                ppoc = c4.getConstructor(new Class[]{c3, Byte.TYPE}).newInstance(new Object[]{cbc, Byte.valueOf("2")});
            } else {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class[]{String.class}).newInstance(new Object[]{message});
                ppoc = c4.getConstructor(new Class[]{c3, Byte.TYPE}).newInstance(new Object[]{o, Byte.valueOf("2")});
            }
            Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
            Object h = m1.invoke(p, new Object[0]);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[]{c5});
            m5.invoke(pc, new Object[]{ppoc});
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to send action bar.");
        }

    }
}

class Title {
    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap();
    private Class<?> packetTitle;
    private Class<?> packetActions;
    private Class<?> nmsChatSerializer;
    private String title = "";
    private ChatColor titleColor = ChatColor.WHITE;
    private String subtitle = "";
    private ChatColor subtitleColor = ChatColor.WHITE;
    private int fadeInTime = -1;
    private int stayTime = -1;
    private int fadeOutTime = -1;
    private boolean ticks = false;

    public Title(String title) {
        this.title = title;
        loadClasses();
    }

    public Title(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        loadClasses();
    }

    public Title(bammerbom.ultimatecore.spongeapi.resources.utils.Title title) {
        this.title = title.title;
        this.subtitle = title.subtitle;
        this.titleColor = title.titleColor;
        this.subtitleColor = title.subtitleColor;
        this.fadeInTime = title.fadeInTime;
        this.fadeOutTime = title.fadeOutTime;
        this.stayTime = title.stayTime;
        this.ticks = title.ticks;
        loadClasses();
    }

    public Title(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
        loadClasses();
    }

    private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length) {
            return false;
        }
        for (int i = 0;
             i < a.length;
             i++) {
            if ((!a[i].equals(o[i])) && (!a[i].isAssignableFrom(o[i]))) {
                return false;
            }
        }
        return true;
    }

    private void loadClasses() {
        this.packetTitle = getClass("org.spigotmc.ProtocolInjector$PacketTitle");
        this.packetActions = getClass("org.spigotmc.ProtocolInjector$PacketTitle$Action");
        this.nmsChatSerializer = getNMSClass("ChatSerializer");
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setTitleColor(ChatColor color) {
        this.titleColor = color;
    }

    public void setSubtitleColor(ChatColor color) {
        this.subtitleColor = color;
    }

    public void setFadeInTime(int time) {
        this.fadeInTime = time;
    }

    public void setFadeOutTime(int time) {
        this.fadeOutTime = time;
    }

    public void setStayTime(int time) {
        this.stayTime = time;
    }

    public void setTimingsToTicks() {
        this.ticks = true;
    }

    public void setTimingsToSeconds() {
        this.ticks = false;
    }

    public void send(Player player) {
        if ((getProtocolVersion(player) >= 47) && (isSpigot()) && (this.packetTitle != null)) {
            resetTitle(player);
            try {
                Object handle = getHandle(player);

                Object connection = getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = this.packetActions.getEnumConstants();
                Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);


                Object packet = this.packetTitle.getConstructor(new Class[]{this.packetActions, Integer.TYPE, Integer.TYPE, Integer.TYPE})
                        .newInstance(new Object[]{actions[2], Integer.valueOf(this.fadeInTime * (this.ticks ? 1 : 20)), Integer.valueOf(this.stayTime * (this.ticks ? 1 : 20)), Integer
                                .valueOf(this.fadeOutTime * (this.ticks ? 1 : 20))});
                if ((this.fadeInTime != -1) && (this.fadeOutTime != -1) && (this.stayTime != -1)) {
                    sendPacket.invoke(connection, new Object[]{packet});
                }
                Object serialized = getMethod(this.nmsChatSerializer, "a", new Class[]{String.class}).invoke(null, new Object[]{"{text:\"" +


                        ChatColor.translateAlternateColorCodes('&', this.title) + "\",color:" + this.titleColor

                        .name().toLowerCase() + "}"});

                packet = this.packetTitle.getConstructor(new Class[]{this.packetActions, getNMSClass("IChatBaseComponent")}).newInstance(new Object[]{actions[0], serialized});

                sendPacket.invoke(connection, new Object[]{packet});
                if (this.subtitle != "") {
                    serialized = getMethod(this.nmsChatSerializer, "a", new Class[]{String.class}).invoke(null, new Object[]{"{text:\"" +


                            ChatColor.translateAlternateColorCodes('&', this.subtitle) + "\",color:" + this.subtitleColor


                            .name().toLowerCase() + "}"});

                    packet = this.packetTitle.getConstructor(new Class[]{this.packetActions, getNMSClass("IChatBaseComponent")}).newInstance(new Object[]{actions[1], serialized});

                    sendPacket.invoke(connection, new Object[]{packet});
                }
            } catch (Exception e) {
                ErrorLogger.log(e, "Failed to send title.");
            }
        }
    }

    public void broadcast() {
        for (Player p : r.getOnlinePlayers()) {
            send(p);
        }
    }

    public void clearTitle(Player player) {
        if ((getProtocolVersion(player) >= 47) && (isSpigot())) {
            try {
                Object handle = getHandle(player);

                Object connection = getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = this.packetActions.getEnumConstants();
                Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);


                Object packet = this.packetTitle.getConstructor(new Class[]{this.packetActions}).newInstance(new Object[]{actions[3]});
                sendPacket.invoke(connection, new Object[]{packet});
            } catch (Exception e) {
                ErrorLogger.log(e, "Failed to clear title.");
            }
        }
    }

    public void resetTitle(Player player) {
        if ((getProtocolVersion(player) >= 47) && (isSpigot())) {
            try {
                Object handle = getHandle(player);

                Object connection = getField(handle.getClass(), "playerConnection").get(handle);
                Object[] actions = this.packetActions.getEnumConstants();
                Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);


                Object packet = this.packetTitle.getConstructor(new Class[]{this.packetActions}).newInstance(new Object[]{actions[4]});
                sendPacket.invoke(connection, new Object[]{packet});
            } catch (Exception e) {
                ErrorLogger.log(e, "Failed to reset title.");
            }
        }
    }

    private int getProtocolVersion(Player player) {
        int version = 0;
        try {
            Object handle = getHandle(player);

            Object connection = getField(handle.getClass(), "playerConnection").get(handle);
            Object networkManager = getValue("networkManager", connection);
            return ((Integer) getMethod("getVersion", networkManager.getClass(), new Class[0]).invoke(networkManager, new Object[0])).intValue();
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to get protocol version.");
        }
        return version;
    }

    private boolean isSpigot() {
        return Bukkit.getVersion().contains("Spigot");
    }

    private Class<?> getClass(String namespace) {
        try {
            return Class.forName(namespace);
        } catch (Exception e) {
        }
        return null;
    }

    private Field getField(String name, Class<?> clazz) throws Exception {
        return clazz.getDeclaredField(name);
    }

    private Object getValue(String name, Object obj) throws Exception {
        Field f = getField(name, obj.getClass());
        f.setAccessible(true);
        return f.get(obj);
    }

    private Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? (Class) CORRESPONDING_TYPES.get(clazz) : clazz;
    }

    private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class[a];
        for (int i = 0;
             i < a;
             i++) {
            types[i] = getPrimitiveType(classes[i]);
        }
        return types;
    }

    private Object getHandle(Object obj) {
        try {
            return getMethod("getHandle", obj.getClass(), new Class[0]).invoke(obj, new Object[0]);
        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to get handle.");
        }
        return null;
    }

    private Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if ((m.getName().equals(name)) && (equalsTypeArray(types, t))) {
                return m;
            }
        }
        return null;
    }

    private String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }

    private Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to get NMS class." + className);
        }
        return clazz;
    }

    private Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to get field.");
        }
        return null;
    }

    private Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods()) {
            if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
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
