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

import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.FileUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONValue;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UltimateConverter {

    public static void convert() {
        //2.0 convert
        /*if (r.getCnfg().contains("Tp.TpaCancelDelay")) {
         try {
         r.log(ChatColor.DARK_RED + "-----------------------------------------------");
         r.log("WARNING!!!");
         r.log("UltimateCore is converting to 2.x ...");
         r.log("Everything except HOMES and WARPS is lost!");
         r.log("UltimateCore will make a backup for you: ");
         r.log(ChatColor.DARK_RED + "-----------------------------------------------");
         Thread.sleep(10000L);
         r.log("Creating backup...");
         FileUtil.copy(r.getUC().getDataFolder(), new File(r.getUC().getDataFolder().getParentFile(), "UltimateCore
         (Backup from 1.x)"));
         r.log("Converting...");
         HashMap<UUID, HashMap<String, Location>> homes = new HashMap<>();
         for (OfflinePlayer pl : r.getOfflinePlayers()) {
         Config conf = new Config(new File(UC.getPlayer(pl).getPlayerFile(), pl.getUniqueId() + ".yml"));
         HashMap<String, Location> hom = new HashMap<>();
         if (conf.contains("homes")) {
         for (String str : conf.getConfigurationSection("homes").getKeys(false)) {
         hom.put(str, LocationUtil.convertStringToLocation(conf.getString("homes." + str)));
         }
         homes.put(pl.getUniqueId(), hom);
         }
         }
         Config conf = new Config(new File(r.getUC().getDataFolder() + File.separator + "Data", "warps.yml"));
         HashMap<String, Location> warps = new HashMap<>();
         if (conf.contains("warps")) {
         for (String str : conf.getConfigurationSection("warps").getKeys(false)) {
         warps.put(str, LocationUtil.convertStringToLocation(conf.getString("warps." + str)));
         }
         }
         FileUtils.deleteDirectory(r.getUC().getDataFolder());
         UltimateFileLoader.Enable();
         UC.getServer().setWarps(warps);
         for (UUID u : homes.keySet()) {
         UC.getPlayer(u).setHomes(homes.get(u));
         }
         r.log("Converting complete!");
         r.log(ChatColor.DARK_RED + "-----------------------------------------------");
         } catch (InterruptedException | IOException ex) {
         ErrorLogger.log(ex, "Failed to convert from 1.x");
         }
         }*/
        //JSON convert
        if (new File(r.getUC().getDataFolder() + File.separator + "Data", "kits.yml").exists()) {
            try {
                r.log(ChatColor.DARK_RED + "-----------------------------------------------");
                r.log("WARNING!!!");
                r.log("UltimateCore is converting to json data...");
                r.log("No data is lost!");
                r.log("UltimateCore will make a backup for you.");
                r.log(ChatColor.DARK_RED + "-----------------------------------------------");
                Thread.sleep(10000L);
                r.log("Creating backup...");
                FileUtil.copy(r.getUC().getDataFolder(), new File(r.getUC().getDataFolder().getParentFile(), "UltimateCore (Backup from yml data)"));
                r.log("Converting...");
                //Move kits.yml
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "kits.yml");
                    File newf = new File(r.getUC().getDataFolder(), "kits.yml");
                    FileUtil.copy(oldf, newf);
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Move kits.yml");
                }
                //Economy.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "economy.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "economy.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }

                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert economy.yml");
                }
                //Global.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "global.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "global.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }
                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert global.yml");
                }
                //Jails.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "jails.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "jails.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }
                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert jails.yml");
                }
                //Spawns.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "spawns.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "spawns.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }
                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert spawns.yml");
                }
                //Warps.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "warps.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "warps.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }
                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert warps.yml");
                }
                //Worlds.json
                try {
                    File oldf = new File(r.getUC().getDataFolder() + File.separator + "Data", "worlds.yml");
                    File newf = new File(r.getUC().getDataFolder() + File.separator + "Data", "worlds.json");
                    if (newf.exists() && oldf.exists()) {
                        newf.delete();
                    }

                    Yaml yaml = new Yaml();
                    InputStream ios = new FileInputStream(oldf);
                    Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                    ios.close();

                    map = convert(map);

                    String json = JSONValue.toJSONString(map);
                    if (json.equalsIgnoreCase("null")) {
                        json = "{}";
                    }
                    FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                    oldf.delete();
                } catch (Exception ex) {
                    r.log("FAILED: Convert worlds.yml");
                }
                //Player files
                for (File oldf : new File(r.getUC().getDataFolder() + File.separator + "Players").listFiles()) {
                    if (FilenameUtils.getExtension(oldf.getAbsolutePath()).equalsIgnoreCase("yml")) {
                        String name = FilenameUtils.getBaseName(oldf.getAbsolutePath());
                        File newf = new File(oldf.getParentFile(), name + ".json");
                        if (newf.exists() && oldf.exists()) {
                            newf.delete();
                        }

                        Yaml yaml = new Yaml();
                        InputStream ios = new FileInputStream(oldf);
                        Map<String, Object> map = (Map<String, Object>) yaml.load(ios);
                        ios.close();

                        map = convert(map);

                        String json = JSONValue.toJSONString(map);
                        if (json.equalsIgnoreCase("null")) {
                            json = "{}";
                        }
                        FileUtil.writeLargerTextFile(newf, Arrays.asList(json));
                        oldf.delete();
                    }
                }

                //
                r.log("Converting complete!");
                r.log(ChatColor.DARK_RED + "-----------------------------------------------");
                r.log("Server restart needed to complete convertion...");
                r.log(ChatColor.GREEN + "Stopping server...");
                Bukkit.getServer().shutdown();
            } catch (InterruptedException | IOException ex) {
                ErrorLogger.log(ex, "Failed to convert from yaml data");
            }
        }
    }

    public static Map<String, Object> convert(Map<String, Object> input) {
        if (StringUtil.nullOrEmpty(input)) {
            return input;
        }
        return convert(input, "");
    }

    public static Map<String, Object> convert(Map<String, Object> input, String key) {
        Map<String, Object> res = new HashMap<>();

        for (Entry<String, Object> e : input.entrySet()) {
            String newKey = key == "" ? e.getKey() : (key + "." + e.getKey());

            if (e.getValue() instanceof Map) {
                res.putAll(convert((Map) e.getValue(), newKey));  // recursive call
            } else {
                res.put(newKey, e.getValue());
            }
        }
        return res;
    }
}
