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

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PluginUtil {

    public static List<String> getDependedOnBy(String name) {
        final List<String> dependedOnBy = new ArrayList<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            if (pl == null) {
                continue;
            }
            if (!pl.isEnabled()) {
                continue;
            }
            PluginDescriptionFile pdf = pl.getDescription();
            if (pdf == null) {
                continue;
            }
            List<String> depends = pdf.getDepend();
            if (depends == null) {
                continue;
            }
            for (String depend : depends) {
                if (name.equalsIgnoreCase(depend)) {
                    dependedOnBy.add(pl.getName());
                }
            }
        }
        return dependedOnBy;
    }

    public static void unregisterAllPluginCommands(String pluginName) {
        try {
            Object result;
            {
                Class<?> clazz = Bukkit.getPluginManager().getClass();
                Field objectField = clazz.getDeclaredField("commandMap");
                final boolean wasAccessible = objectField.isAccessible();
                objectField.setAccessible(true);
                result = objectField.get(Bukkit.getPluginManager());
                objectField.setAccessible(wasAccessible);
            }
            SimpleCommandMap commandMap = (SimpleCommandMap) result;
            Object map;
            {
                Class<?> clazz = commandMap.getClass();
                Field objectField = clazz.getDeclaredField("knownCommands");
                final boolean wasAccessible = objectField.isAccessible();
                objectField.setAccessible(true);
                map = objectField.get(commandMap);
                objectField.setAccessible(wasAccessible);
            }
            @SuppressWarnings("unchecked") HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
            final List<Command> commands = new ArrayList<>(commandMap.getCommands());
            for (Command c : commands) {
                if (!(c instanceof PluginCommand)) {
                    continue;
                }
                final PluginCommand pc = (PluginCommand) c;
                if (!pc.getPlugin().getName().equals(pluginName)) {
                    continue;
                }
                knownCommands.remove(pc.getName());
                for (String alias : pc.getAliases()) {
                    if (knownCommands.containsKey(alias)) {
                        final Command ac = knownCommands.get(alias);
                        if (!(ac instanceof PluginCommand)) {
                            continue;
                        }
                        final PluginCommand apc = (PluginCommand) ac;
                        if (!apc.getPlugin().getName().equals(pluginName)) {
                            continue;
                        }
                        knownCommands.remove(alias);
                    }
                }
            }
        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to unregister plugin commands.");
        }
    }

    @SuppressWarnings("unchecked")
    public static void removePluginFromList(Plugin p) {
        try {
            final List<Plugin> plugins;
            {
                Class<?> clazz = Bukkit.getPluginManager().getClass();
                Field objectField = clazz.getDeclaredField("plugins");
                final boolean wasAccessible = objectField.isAccessible();
                objectField.setAccessible(true);
                plugins = (List<Plugin>) objectField.get(Bukkit.getPluginManager());
                objectField.setAccessible(wasAccessible);
            }
            plugins.remove(p);
        } catch (Exception e) {
            ErrorLogger.log(e, "Failed to unregister plugin.");
        }
    }

    public static String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(r.neutral);
                pluginList.append(", ");
            }

            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            pluginList.append(plugin.getDescription().getName());
        }

        return r.positive + "(" + r.neutral + plugins.length + r.positive + "): " + r.neutral + pluginList.toString();
    }

    public static void decompress(String fileName, String destinationFolder) {
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        try {
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(fileName);
            Enumeration<? extends ZipEntry> e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = e.nextElement();
                if (entry.isDirectory()) {
                    new File(destinationFolder + File.separator + entry.getName()).mkdir();
                    continue;
                }
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[2048];
                File f = new File(destinationFolder + File.separator + entry.getName());
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(destinationFolder + File.separator + entry.getName());
                dest = new BufferedOutputStream(fos, 2048);
                while ((count = is.read(data, 0, 2048)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                is.close();
            }
            zipfile.close();
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to decompress zip file.");
        } finally {
            try {
                if (dest != null) {
                    dest.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static List<File> listFiles(File f) {
        List<File> fs = new ArrayList<>();
        if (!f.isDirectory()) {
            return fs;
        }
        File[] listed = f.listFiles();
        if (listed == null) {
            return fs;
        }
        for (File in : listed) {
            if (in.isDirectory()) {
                fs.addAll(listFiles(in));
                continue;
            }
            fs.add(in);
        }
        return fs;
    }

    public static boolean deleteDirectory(File f) {
        boolean success = true;
        if (!f.isDirectory()) {
            return false;
        }
        File[] files = f.listFiles();
        if (files == null) {
            return false;
        }
        for (File delete : files) {
            if (delete.isDirectory()) {
                boolean recur = deleteDirectory(delete);
                if (success) {
                    success = recur;
                }
                continue;
            }
            if (!delete.delete()) {
                r.log("Failed to delete " + f.getAbsolutePath());
                success = false;
            }
        }
        if (success) {
            success = f.delete();
        }
        return success;
    }

}
