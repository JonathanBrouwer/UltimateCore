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
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static List<String> getSoftDependedOnBy(String name) {
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
            List<String> depends = new ArrayList<>();
            depends.addAll(pdf.getDepend());
            depends.addAll(pdf.getSoftDepend());
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

    public static boolean unload(Plugin plugin) {
        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();

        SimpleCommandMap commandMap = null;

        List<Plugin> plugins = null;

        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;
        if (pluginManager != null) {
            pluginManager.disablePlugin(plugin);
            try {
                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map) lookupNamesField.get(pluginManager);
                try {
                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map) listenersField.get(pluginManager);
                } catch (Exception e) {
                    reloadlisteners = false;
                }
                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map) knownCommandsField.get(commandMap);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        pluginManager.disablePlugin(plugin);
        if ((plugins != null) && (plugins.contains(plugin))) {
            plugins.remove(plugin);
        }
        if ((names != null) && (names.containsKey(name))) {
            names.remove(name);
        }
        if ((listeners != null) && (reloadlisteners)) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                for (Iterator it = set.iterator(); it.hasNext(); ) {
                    RegisteredListener value = (RegisteredListener) it.next();
                    if (value.getPlugin() == plugin) {
                        it.remove();
                    }
                }
            }
        }
        Iterator<Map.Entry<String, Command>> it;
        if (commandMap != null) {
            for (it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = (Map.Entry) it.next();
                if ((entry.getValue() instanceof PluginCommand)) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }
        ClassLoader cl = plugin.getClass().getClassLoader();
        if ((cl instanceof URLClassLoader)) {
            try {
                ((URLClassLoader) cl).close();
            } catch (IOException ex) {
                Logger.getLogger(PluginUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.gc();

        return true;
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

    public static Plugin getPluginByName(String name) {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (name.equalsIgnoreCase(plugin.getName())) {
                return plugin;
            }
        }
        return null;
    }

    public static boolean isSameVersion(String version1, String version2) {
        if (version1.contains(version2)) return true;
        if (version2.contains(version1)) return true;
        for (String s : version1.split("[ ;-]")) {
            for (String s2 : version2.split("[ ;-]")) {
                String v1 = s.replace("v", "");
                String v2 = s.replace("v", "");
                if (v1.contains(".") && v2.contains(".")) {
                    if (v1.equalsIgnoreCase(v2)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
