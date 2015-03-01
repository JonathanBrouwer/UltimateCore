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
package bammerbom.ultimatecore.spongeapi;

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.utils.FileUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public class UltimateConverter {

    public static void convert() {
        if (r.getCnfg().contains("Tp.TpaCancelDelay")) {
            try {
                r.log(ChatColor.DARK_RED + "-----------------------------------------------");
                r.log("WARNING!!!");
                r.log("UltimateCore is converting to 2.x ...");
                r.log("Everything except HOMES and WARPS is lost!");
                r.log("UltimateCore will make a backup for you: ");
                r.log(ChatColor.DARK_RED + "-----------------------------------------------");
                Thread.sleep(10000L);
                r.log("Creating backup...");
                FileUtil.copy(r.getUC().getDataFolder(), new File(r.getUC().getDataFolder().getParentFile(), "UltimateCore (Backup from 1.x)"));
                r.log("Converting...");
                HashMap<UUID, HashMap<String, Location>> homes = new HashMap<>();
                for (OfflinePlayer pl : r.getOfflinePlayers()) {
                    Config conf = UC.getPlayer(pl).getPlayerConfig();
                    HashMap<String, Location> hom = new HashMap<>();
                    if (conf.contains("homes")) {
                        for (String str : conf.getConfigurationSection("homes").getKeys(false)) {
                            hom.put(str, LocationUtil.convertStringToLocation(conf.getString("homes." + str)));
                        }
                        homes.put(pl.getUniqueId(), hom);
                    }
                }
                Config conf = new Config(UltimateFileLoader.DFwarps);
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
        }
    }
}
