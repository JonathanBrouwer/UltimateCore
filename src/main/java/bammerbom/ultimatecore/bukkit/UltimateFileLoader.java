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
package bammerbom.ultimatecore.bukkit;

import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.StreamUtil;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;

public class UltimateFileLoader {

    public static File messages;
    public static File datamap;

    public static File conf;
    public static File LANGf;
    public static File ENf;

    public static File DFspawns;
    public static File DFwarps;
    public static File DFworlds;
    public static File DFreports;
    public static File DFjails;

    public static void Enable() {
        Plugin plugin = r.getUC();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        conf = new File(plugin.getDataFolder() + "\\config.yml");
        if (!conf.exists()) {
            plugin.saveResource("config.yml", true);
        }

        messages = new File(plugin.getDataFolder(), "Messages");
        datamap = new File(plugin.getDataFolder(), "Data");
        if (!datamap.exists()) {
            datamap.mkdir();
        }
        if (!messages.exists()) {
            messages.mkdir();
        }
        //
        DFspawns = new File(plugin.getDataFolder() + File.separator + "Data", "spawns.yml");
        DFwarps = new File(plugin.getDataFolder() + File.separator + "Data", "warps.yml");
        DFworlds = new File(plugin.getDataFolder() + File.separator + "Data", "worlds.yml");
        DFreports = new File(plugin.getDataFolder() + File.separator + "Data", "reports.yml");
        DFjails = new File(plugin.getDataFolder() + File.separator + "Data", "jails.yml");
        //
        try {
            if (!DFspawns.exists()) {
                DFspawns.createNewFile();
            }
            if (!DFwarps.exists()) {
                DFwarps.createNewFile();
            }
            if (!DFworlds.exists()) {
                DFworlds.createNewFile();
            }
            //if(!DFreports.exists()) DFspawns.createNewFile();
            if (!DFjails.exists()) {
                DFjails.createNewFile();
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to create Data files.");
        }
        //
        if (!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "EN.properties").exists()) {
            plugin.saveResource("Messages" + File.separator + "EN.properties", true);
        }
        //
        File file = new File(plugin.getDataFolder() + File.separator + "Messages", r.getCnfg().getString("Language") + ".properties");
        if (file.exists()) {
            LANGf = file;
        } else {
            LANGf = new File(plugin.getDataFolder() + File.separator + "Messages", "EN.properties");
        }
        ENf = new File(plugin.getDataFolder() + File.separator + "Messages", "EN.properties");
        //
        for (OfflinePlayer pl : r.getOfflinePlayers()) {
            try {
                getPlayerFile(pl);
            } catch (Exception ex) {
                ErrorLogger.log(ex, "Failed to load playerfile: " + pl.getName() + " - " + pl.getUniqueId());
            }
        }
        //

    }

    public static File getPlayerFile(final OfflinePlayer p) {
        UUID id = p.getUniqueId();
        final File file = new File(r.getUC().getDataFolder() + File.separator + "Players" + File.separator + id.toString() + ".yml");
        File directory = new File(r.getUC().getDataFolder() + File.separator + "Players");
        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                ErrorLogger.log(e, "Failed to load playerfile: " + p.getName() + " - " + p.getUniqueId());
            }
        }
        return file;

    }

    public static Config getPlayerConfig(OfflinePlayer p) {
        File file = getPlayerFile(p);
        Config config = new Config(file);
        return config;
    }

    public static void addConfig() {
        //CONFIG
        {
            File tempFile;
            try {
                tempFile = File.createTempFile("temp_config", ".yml");
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to create temp config.yml");
                return;
            }
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                tempFile.deleteOnExit();
                copy(Bukkit.getPluginManager().getPlugin("UltimateCore").getResource("config.yml"), out);
                out.close();
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to complete config.yml");
            } catch (Exception e) {
                r.log("WARNING: Failed to complete config.yml");
                r.log("Restart your server to fix this problem.");
            }
            Config confL = new Config(tempFile);
            Config confS = r.getCnfg();
            for (String s : confL.getKeys(true)) {
                if (!confS.contains(s) && !(confL.get(s) instanceof MemorySection)) {
                    confS.set(s, confL.get(s));
                }
            }
            confS.save();
            if (tempFile != null) {
                tempFile.delete();
            }
        }
        //LANG
        try {
            File tempFile;
            try {
                tempFile = File.createTempFile("temp_EN", ".properties");
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to create temp language file.");
                return;
            }
            try (FileOutputStream out = new FileOutputStream(tempFile);
                    InputStream in = r.getUC().getResource("Messages/EN.properties")) {
                tempFile.deleteOnExit();
                copy(in, out);
                in.close();
                out.close();
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to complete language file.");
            }
            ResourceBundle tempR;
            try {
                tempR = new PropertyResourceBundle(new FileInputStream(tempFile));
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to create ResourceBundle.");
                return;
            }
            Enumeration<String> keys = tempR.getKeys();
            //EN
            Properties propsEN = new Properties();
            Properties propsCU = new Properties();
            FileInputStream strEN = new FileInputStream(ENf);
            FileInputStream strCU = new FileInputStream(LANGf);
            propsEN.load(strEN);
            propsCU.load(strCU);
            strEN.close();
            strCU.close();
            Boolean rl = false;
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                //
                if (!r.en.containsKey(key)) {
                    rl = true;
                    propsEN.put(key, tempR.getString(key));
                }
                if (!r.cu.containsKey(key)) {
                    rl = true;
                    propsCU.put(key, tempR.getString(key));
                }
            }
            if (rl) {
                FileOutputStream ENo = StreamUtil.createOutputStream(ENf);
                FileOutputStream CUo = StreamUtil.createOutputStream(LANGf);
                propsEN.store(ENo, "UltimateCore messages - EN");
                propsCU.store(CUo, "UltimateCore messages - " + (FilenameUtils.removeExtension(LANGf.getName())));
                ENo.close();
                CUo.close();
            }
            if (tempFile != null) {
                tempFile.delete();
            }
            if (rl) {
                InputStream inA = new FileInputStream(UltimateFileLoader.ENf);
                InputStream inB = new FileInputStream(UltimateFileLoader.LANGf);
                r.en = new PropertyResourceBundle(inA);
                r.cu = new PropertyResourceBundle(inB);
                inA.close();
                inB.close();
            }
        } catch (IOException | SecurityException ex) {
            ErrorLogger.log(ex, "Failed to complete message files.");
        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[4096]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        try {
            long count = 0L;
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (Exception e) {
            r.log("WARNING: Failed to complete config.yml");
            r.log("Restart your server to fix this problem.");
            return -1L;
        }
    }

    public static void resetFile(File file) {
        Integer i = 1;
        File parent = new File(file.getParent());
        try {
            File ren = new File(parent.getCanonicalPath(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "_OLD" + i + ".yml");
            while (ren.exists()) {
                i++;
                ren = new File(parent.getCanonicalPath(), file.getName().substring(0, file.getName().lastIndexOf('.')) + "_OLD" + i + ".yml");
            }
            file.renameTo(ren);
        } catch (IOException ex) {
            ErrorLogger.log(ex, "Failed to reset file.");
        }
    }
}
