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
package bammerbom.ultimatecore.sponge.utils;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.GeneralConfig;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import org.json.JSONArray;
import org.json.JSONObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Stats {
    static boolean started = false;

    public static void start() {
        if (started) {
            return;
        }
        started = true;
        if (!GeneralConfig.get().getNode("stats", "enabled").getBoolean()) {
            return;
        }
        Sponge.getScheduler().createTaskBuilder().name("UC stats task").delay(20, TimeUnit.SECONDS).interval(30, TimeUnit.MINUTES).execute(Stats::send).submit(UltimateCore.get());
    }

    public static HashMap<String, Object> collect() {
        final HashMap<String, Object> data = new HashMap<>();
        data.put("serverid", ServerID.getUUID());
        data.put("statsversion", 1);
        data.put("apitype", Sponge.getPlatform().getApi().getName().toLowerCase());
        data.put("apiversion", Sponge.getPlatform().getApi().getVersion().orElse("Not Available"));
        data.put("implname", Sponge.getPlatform().getImplementation().getName().toLowerCase());
        data.put("implversion", Sponge.getPlatform().getImplementation().getVersion().orElse("Not Available"));
        data.put("servertype", Sponge.getPlatform().getType().isServer() ? "server" : "client");
        data.put("mcversion", Sponge.getPlatform().getMinecraftVersion().getName());
        data.put("ucversion", Sponge.getPluginManager().fromInstance(UltimateCore.get()).get().getVersion().orElse("Not Available"));
        data.put("playersonline", Sponge.getServer().getOnlinePlayers().size());
        data.put("worldsloaded", Sponge.getServer().getWorlds().size());
        data.put("osname", System.getProperty("os.name"));
        data.put("osarch", System.getProperty("os.arch").contains("64") ? 64 : 32);
        data.put("osversion", System.getProperty("os.version"));
        data.put("cores", Runtime.getRuntime().availableProcessors());
        data.put("maxram", Runtime.getRuntime().maxMemory());
        data.put("freeram", Runtime.getRuntime().freeMemory());
        data.put("onlinemode", Sponge.getServer().getOnlineMode());
        data.put("javaversion", System.getProperty("java.version"));
        //Modules
        JSONArray modulesarray = new JSONArray();
        for (Module mod : UltimateCore.get().getModuleService().getRegisteredModules()) {
            if (mod.getIdentifier().equalsIgnoreCase("default")) continue;
            modulesarray.put(mod.getIdentifier());
        }
        JSONObject modules = new JSONObject();
        try {
            modules.put("modules", modulesarray);
        } catch (Exception ex) {
        }
        data.put("modules", modules.toString());
        //
        data.put("language", GeneralConfig.get().getNode("language", "language").getString("EN_US"));
        //Plugins
        JSONArray pluginsarray = new JSONArray();
        for (PluginContainer plugin : Sponge.getPluginManager().getPlugins()) {
            try {
                JSONObject pluginobject = new JSONObject();
                pluginobject.put("id", plugin.getId());
                pluginobject.put("name", plugin.getName());
                pluginobject.put("version", plugin.getVersion().orElse("Not Available"));
                pluginsarray.put(pluginobject);
            } catch (Exception ex) {
            }
        }
        JSONObject plugins = new JSONObject();
        try {
            plugins.put("plugins", pluginsarray);
        } catch (Exception ex) {
        }
        data.put("plugins", plugins.toString());
        //Permissions plugin
        Optional<ProviderRegistration<PermissionService>> permplugin = Sponge.getServiceManager().getRegistration(PermissionService.class);
        if (permplugin.isPresent()) {
            try {
                JSONObject pluginobject = new JSONObject();
                pluginobject.put("id", permplugin.get().getPlugin().getId());
                pluginobject.put("name", permplugin.get().getPlugin().getName());
                pluginobject.put("version", permplugin.get().getPlugin().getVersion().orElse("Not Available"));
                data.put("permissionsplugin", pluginobject);
            } catch (Exception ex) {
            }
        } else {
            data.put("permissionsplugin", "None");
        }
        //Economy plugin
        Optional<ProviderRegistration<EconomyService>> economyplugin = Sponge.getServiceManager().getRegistration(EconomyService.class);
        if (economyplugin.isPresent()) {
            try {
                JSONObject pluginobject = new JSONObject();
                pluginobject.put("id", economyplugin.get().getPlugin().getId());
                pluginobject.put("name", economyplugin.get().getPlugin().getName());
                pluginobject.put("version", economyplugin.get().getPlugin().getVersion().orElse("Not Available"));
                data.put("economyplugin", pluginobject);
            } catch (Exception ex) {
            }
        } else {
            data.put("economyplugin", "None");
        }
        //Return
        return data;
    }

    public static void send() {
        //Sync
        final HashMap<String, Object> data = collect();
        //Async
        Sponge.getScheduler().createTaskBuilder().name("UC async stats task").delayTicks(1L).async().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    data.put("country", getCountryCode());
                    Webb webb = Webb.create();
                    Response<String> response = webb.post("http://ultimatecore.org/postrequest/statistics.php").params(data).asString();
                    Messages.log(Messages.getFormatted("core.stats.sent", "%status%", response.getStatusLine()));
                    //TODO add file or not?
                    File file = new File(UltimateCore.get().getDataFolder().toFile(), "stats.txt");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }

                    //Get lines
                    List<String> lines = new ArrayList<>();
                    for (String key : data.keySet()) {
                        lines.add(key + " = " + data.get(key));
                    }

                    FileUtil.writeLines(file, lines);
                } catch (Exception e) {
                    Messages.log(Messages.getFormatted("core.stats.failed", "%message%", e.getMessage()));
                }
            }
        }).submit(UltimateCore.get());
    }

    public static String getCountryCode() {
        // http://freegeoip.net/json/
        try {
            Webb webb = Webb.create();
            return webb.get("http://freegeoip.net/json/").asJsonObject().getBody().getString("country_code");
        } catch (Exception ex) {
            return "unknown";
        }
    }
}
