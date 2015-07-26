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

import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.ReflectionUtil.ReflectionObject;
import bammerbom.ultimatecore.bukkit.resources.utils.ReflectionUtil.ReflectionStatic;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabUtil {

    public static void sendTabTitle(Player p, String header, String footer) {
        try {

            if (header == null) {
                header = "";
            }
            if (footer == null) {
                footer = "";
            }

            Object hc = ReflectionUtil.executeStatic("a({1})", ReflectionStatic.fromNMS("ChatSerializer"), "{\"text\": \"" + header + "\"}").fetch();
            Object fc = ReflectionUtil.executeStatic("a({1})", ReflectionStatic.fromNMS("ChatSerializer"), "{\"text\": \"" + footer + "\"}").fetch();
            Object ob = ReflectionObject.fromNMS("PacketPlayOutPlayerListHeaderFooter", hc).fetch();

            Field field = ob.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(ob, fc);

            ReflectionUtil.execute("getHandle().playerConnection.sendPacket({1})", p, ob);

        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to set tab titles.");
        }

    }
}
