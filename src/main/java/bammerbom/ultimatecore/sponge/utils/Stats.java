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
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import com.goebl.david.Response;
import com.goebl.david.Webb;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionService;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Stats {
    static boolean started = false;

    public static void start() {
        if (started) {
            return;
        }
        started = true;
        if (!UltimateCore.get().getGeneralConfig().get().getNode("stats", "enabled").getBoolean()) {
            return;
        }
        Sponge.getScheduler().createTaskBuilder().name("UC stats task").delay(15, TimeUnit.MINUTES).interval(30, TimeUnit.MINUTES).execute(Stats::send).submit(UltimateCore.get());
    }

    public static HashMap<String, Object> collect() {
        final HashMap<String, Object> data = new HashMap<>();
        data.put("serverid", ServerID.getUUID());
        data.put("statsversion", 1);
        data.put("apitype", Sponge.getPlatform().getContainer(Platform.Component.API).getName().toLowerCase());
        data.put("apiversion", Sponge.getPlatform().getContainer(Platform.Component.API).getVersion().orElse("Not Available"));
        data.put("implname", Sponge.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getName().toLowerCase());
        data.put("implversion", Sponge.getPlatform().getContainer(Platform.Component.IMPLEMENTATION).getVersion().orElse("Not Available"));
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
        data.put("modules", StringUtil.join(", ", UltimateCore.get().getModuleService().getModules().stream().map(mod -> mod.getIdentifier()).collect(Collectors.toList())));
        data.put("language", UltimateCore.get().getGeneralConfig().get().getNode("language", "language").getString("EN_US"));
        //Plugins
        StringBuilder pluginbuilder = new StringBuilder();
        for (PluginContainer plugin : Sponge.getPluginManager().getPlugins()) {
            pluginbuilder.append("\n" + plugin.getId() + " / " + plugin.getName() + " / " + plugin.getVersion().orElse("Not Available"));
        }
        data.put("plugins", pluginbuilder.toString());
        //Permissions plugin
        Optional<ProviderRegistration<PermissionService>> permplugin = Sponge.getServiceManager().getRegistration(PermissionService.class);
        if (permplugin.isPresent()) {
            data.put("permissionsplugin", permplugin.get().getPlugin().getId() + " / " + permplugin.get().getPlugin().getName() + " / " + permplugin.get().getPlugin().getVersion().orElse("Not Available"));
        } else {
            data.put("permissionsplugin", "None");
        }
        //Economy plugin
        Optional<ProviderRegistration<EconomyService>> economyplugin = Sponge.getServiceManager().getRegistration(EconomyService.class);
        if (economyplugin.isPresent()) {
            data.put("economyplugin", economyplugin.get().getPlugin().getId() + " / " + economyplugin.get().getPlugin().getName() + " / " + economyplugin.get().getPlugin().getVersion().orElse("Not Available"));
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
//                    File file = new File(UltimateCore.get().getDataFolder().toFile(), "stats.txt");
//                    if (!file.exists()) {
//                        file.getParentFile().mkdirs();
//                        file.createNewFile();
//                    }
//
//                    //Get lines
//                    List<String> lines = new ArrayList<>();
//                    for (String key : data.keySet()) {
//                        lines.add(key + " = " + data.get(key));
//                    }
//
//                    FileUtil.writeLines(file, lines);
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
