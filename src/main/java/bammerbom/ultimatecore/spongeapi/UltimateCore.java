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

import bammerbom.ultimatecore.spongeapi.listeners.*;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.databases.ItemDatabase;
import bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.PerformanceUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.UuidUtil;
import com.google.inject.Inject;
import java.io.File;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.event.Subscribe;

@Plugin(id = "UltimateCore", name = "UltimateCore", version = "2.0.10")
public class UltimateCore {

    public static UltimateCore instance;
    @Inject
    @ConfigDir(sharedRoot = false)
    public static File file;
    @Inject
    public static Logger logger;
    public static Game game;

    public static UltimateCore getInstance() {
        return instance;
    }

    @Subscribe
    public void onEnable(InitializationEvent ev) {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            instance = this;
            game = ev.getGame();
            UltimateFileLoader.Enable();
            r.enableMES();
            UltimateFileLoader.addConfig();
            UuidUtil.loadPlayers();
            UltimateCommands.load();
            PerformanceUtil.getTps();
            BossbarUtil.enable();
            ItemDatabase.enable();
            //
            if (!ev.getGame().getMinecraftVersion().getName().startsWith("1.8")) {
                logger.info(" ");
                r.log(TextColors.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                r.log(TextColors.YELLOW + "Warning! Version " + ev.getGame().getMinecraftVersion().getName() + " of spongeapi is not supported!");
                r.log(TextColors.YELLOW + "Use UltimateCore at your own risk!");
                r.log(TextColors.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                logger.info(" ");
            }
            //
            r.runUpdater();
            r.runMetrics();
            //
            EventManager em = ev.getGame().getEventManager();
            GlobalPlayerListener.start();
            em.register(this, new GlobalWorldListener());
            AfkListener.start();
            AutomessageListener.start();
            AutosaveListener.start();
            BloodListener.start();
            ChatListener.start();
            DeathmessagesListener.start();
            DynmapListener.start();
            ExplosionListener.start();
            JoinLeaveListener.start();
            MotdListener.start();
            PluginStealListener.start();
            RespawnListener.start();
            SignListener.start();
            TabListener.start();
            TreeListener.start();
            UnknownCommandListener.start();
            WeatherListener.start();
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Enabled Ultimate Core! (" + time + "ms)");

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to enable UltimateCore");
        }
        test();
    }

    @Subscribe
    public void loadWorlds(ServerStartingEvent ev) {
        UltimateWorldLoader.startWorldLoading();
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent ev) {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            r.removeUC();
            ItemDatabase.disable();
            BossbarUtil.disable();
            DynmapListener.stop();
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Disabled Ultimate Core! (" + time + "ms)");
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to disable UltimateCore");
        }
    }

    public File getDataFolder() {
        return file;
    }

    public void test() {
    }

}
