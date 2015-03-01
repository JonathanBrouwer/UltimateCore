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

import bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject;
import bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class TitleUtil {

    public static void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            ReflectionObject titleJSON = ReflectionUtil.executeStatic("a({1})", ReflectionStatic.fromNMS("ChatSerializer"), "{'text': '" + title + "'}");
            ReflectionObject subtitleJSON = ReflectionUtil.executeStatic("a({1})", ReflectionStatic.fromNMS("ChatSerializer"), "{'text': '" + subtitle + "'}");

            Object titlePacket;
            {//Title
                Class<?> clazz = Class.forName(ReflectionUtil.NMS_PATH + ".EnumTitleAction");
                Method nameMethod = clazz.getMethod("name");
                Object value = null;
                Object[] enums = clazz.getEnumConstants();
                for (Object o : enums) {
                    if (nameMethod.invoke(o).equals("TITLE")) {
                        value = o;
                        break;
                    }
                }
                titlePacket = Class.forName(ReflectionUtil.NMS_PATH + ".PacketPlayOutTitle").getConstructor(clazz, ReflectionStatic.fromNMS("IChatBaseComponent"), int.class, int.class, int.class).newInstance(value, titleJSON.fetch(), fadeIn, stay, fadeOut);
            }

            Object subtitlePacket;
            {//Subtitle
                Class<?> clazz = Class.forName(ReflectionUtil.NMS_PATH + ".EnumTitleAction");
                Method nameMethod = clazz.getMethod("name");
                Object value = null;
                Object[] enums = clazz.getEnumConstants();
                for (Object o : enums) {
                    if (nameMethod.invoke(o).equals("SUBTITLE")) {
                        value = o;
                        break;
                    }
                }
                subtitlePacket = Class.forName(ReflectionUtil.NMS_PATH + ".PacketPlayOutTitle").getConstructor(clazz, ReflectionStatic.fromNMS("IChatBaseComponent")).newInstance(value, subtitleJSON.fetch());
            }

            ReflectionUtil.execute("getHandle().playerConnection.sendPacket({1})", p, titlePacket);
            ReflectionUtil.execute("getHandle().playerConnection.sendPacket({1})", p, subtitlePacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendActionBar(Player player, String message) {
        try {
            Class<?> a = ReflectionStatic.fromNMS("ChatSerializer");
            ReflectionObject b = ReflectionUtil.executeStatic("a({1})", a, "{\"text\": \"" + message + "\"}");
            ReflectionObject c = ReflectionObject.fromNMS("PacketPlayOutChat", b.fetch(), (byte) 2);
            ReflectionUtil.execute("getHandle().playerConnection.sendPacket({1})", player, c.fetch());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
