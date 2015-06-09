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
package bammerbom.ultimatecore.spongeapi_old;

import bammerbom.ultimatecore.spongeapi_old.configuration.Config;
import bammerbom.ultimatecore.spongeapi_old.configuration.ConfigSection;
import bammerbom.ultimatecore.spongeapi_old.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi_old.resources.classes.ErrorLogger;
import org.spongepowered.api.entity.player.User;

import java.io.*;
import java.util.UUID;

public class UltimateFileLoader {

    public static File messages;
    public static File datamap;

    public static File conf;
    public static File LANGf;
    public static File ENf;

    public static File Dglobal;
    public static File Dspawns;
    public static File Dwarps;
    public static File Dworlds;
    public static File Djails;
    public static File Dkits;
    public static File Deconomy;
    //DIS public static File DFuuid;

    public static void Enable() {
        UltimateCore plugin = UltimateCore.getInstance();
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        conf = new File(plugin.getDataFolder() + File.separator + "config.yml");
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
        Dglobal = new File(plugin.getDataFolder() + File.separator + "Data", "global.json");
        Dspawns = new File(plugin.getDataFolder() + File.separator + "Data", "spawns.json");
        Dwarps = new File(plugin.getDataFolder() + File.separator + "Data", "warps.json");
        Dworlds = new File(plugin.getDataFolder() + File.separator + "Data", "worlds.json");
        Djails = new File(plugin.getDataFolder() + File.separator + "Data", "jails.json");
        Dkits = new File(plugin.getDataFolder(), "kits.yml");
        Deconomy = new File(plugin.getDataFolder() + File.separator + "Data", "economy.json");
        //DIS DFuuid = new File(plugin.getDataFolder() + File.separator + "Data", "uuids.json");
        //
        try {
            if (!Dglobal.exists()) {
                Dglobal.createNewFile();
            }
            if (!Dspawns.exists()) {
                Dspawns.createNewFile();
            }
            if (!Dwarps.exists()) {
                Dwarps.createNewFile();
            }
            if (!Dworlds.exists()) {
                Dworlds.createNewFile();
            }
            if (!Djails.exists()) {
                Djails.createNewFile();
            }
            if (!Dkits.exists()) {
                plugin.saveResource("kits.yml", true);
            }
            if (!Deconomy.exists()) {
                Deconomy.createNewFile();
            }
            /*DIS if (!DFuuid.exists()) {
             DFuuid.createNewFile();
             }*/
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to create Data files.");
        }
        //
        if (!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "EN.properties").exists()) {
            plugin.saveResource("Messages" + File.separator + "EN.properties", true);
        }
        if (!new File(plugin.getDataFolder() + File.separator + "Messages" + File.separator + "NL.properties").exists()) {
            plugin.saveResource("Messages" + File.separator + "NL.properties", true);
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
        for (User pl : r.getOfflinePlayers()) {
            try {
                getPlayerFile(pl);
            } catch (Exception ex) {
                ErrorLogger.log(ex, "Failed to load playerfile: " + pl.getName() + " - " + pl.getUniqueId());
            }
        }
        //

    }

    public static File getPlayerFile(final User p) {
        UUID id = p.getUniqueId();
        final File file = new File(r.getUC().getDataFolder() + File.separator + "Players" + File.separator + id.toString() + ".json");
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

    public static JsonConfig getPlayerConfig(User p) {
        File file = getPlayerFile(p);
        JsonConfig config = new JsonConfig(file);
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
                copy(r.getUC().getResource("config.yml"), out);
                out.close();
            } catch (IOException ex) {
                ErrorLogger.log(ex, "Failed to complete config.yml");
            } catch (Exception e) {
                r.log("WARNING: Failed to complete config.yml");
                r.log("Restart your server to fix this problem.");
            }
            Config confL = new Config(tempFile);
            Config confS = r.getCnfg();
            Boolean changed = false;
            for (String s : confL.getKeys(true)) {
                if (!confS.contains(s) && !(confL.get(s) instanceof ConfigSection)) {
                    confS.set(s, confL.get(s));
                    changed = true;
                }
            }
            if (changed) {
                for (String str : confL.getHeaders().keySet()) {
                    confS.setHeader(str, confL.getHeaders().get(str));
                }
                confS.save();
            }
            if (tempFile != null) {
                tempFile.delete();
            }
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
