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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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

    public static void send() {
        //Sync
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
        data.put("osarch", System.getProperty("os.arch").indexOf("64") == -1 ? 32 : 64);
        data.put("osversion", System.getProperty("os.version"));
        data.put("cores", Runtime.getRuntime().availableProcessors());
        data.put("maxram", Runtime.getRuntime().maxMemory());
        data.put("freeram", Runtime.getRuntime().freeMemory());
        data.put("onlinemode", Sponge.getServer().getOnlineMode());
        data.put("javaversion", System.getProperty("java.version"));
        StringBuilder modules = new StringBuilder();
        for (Module mod : UltimateCore.get().getModuleService().getRegisteredModules()) {
            modules.append(mod.getIdentifier()).append(",");
        }
        data.put("modules", modules.toString().isEmpty() ? "" : modules.substring(0, modules.length() - 1));
        data.put("language", "EN_US"); //TODO get language
        StringBuilder plugins = new StringBuilder();
        for (PluginContainer plugin : Sponge.getPluginManager().getPlugins()) {
            plugins.append(plugin.getId()).append("|").append(plugin.getVersion()).append(",");
        }
        data.put("plugins", plugins.toString().isEmpty() ? "" : plugins.substring(0, plugins.length() - 1));
        Optional<ProviderRegistration<PermissionService>> permplugin = Sponge.getServiceManager().getRegistration(PermissionService.class);
        data.put("permissionsplugin", permplugin.isPresent() ? (permplugin.get().getPlugin().getName() + "|" + permplugin.get().getPlugin().getVersion().orElse("")) : "Not Available");
        Optional<ProviderRegistration<EconomyService>> economyplugin = Sponge.getServiceManager().getRegistration(EconomyService.class);
        data.put("economyplugin", economyplugin.isPresent() ? (economyplugin.get().getPlugin().getName() + "|" + economyplugin.get().getPlugin().getVersion().orElse("")) : "Not Available");
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
                    File file = new File(UltimateCore.get().getDataFolder().toFile(), "stats.html");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    FileUtil.writeLines(file, Arrays.asList(response.getBody()));
                } catch (IOException e) {
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
