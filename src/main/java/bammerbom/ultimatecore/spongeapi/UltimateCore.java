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

import bammerbom.ultimatecore.spongeapi.api.UEconomy;
import bammerbom.ultimatecore.spongeapi.api.UServer;
import bammerbom.ultimatecore.spongeapi.commands.CmdHeal;
import bammerbom.ultimatecore.spongeapi.commands.CmdRules;
import bammerbom.ultimatecore.spongeapi.listeners.*;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.classes.MetaItemStack;
import bammerbom.ultimatecore.spongeapi.resources.databases.ItemDatabase;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.PerformanceUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.UuidUtil;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.event.state.ServerStartingEvent;
import org.spongepowered.api.event.state.ServerStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.text.format.TextColors;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Plugin(id = "UltimateCore", name = "UltimateCore", version = "2.1.6")
public class UltimateCore {

    public static UltimateCore instance;
    @Inject
    @ConfigDir(sharedRoot = false)
    public static File cfile;
    public static File file;
    public static Game game;
    @Inject
    public static Logger logger;
    public static String version;

    static {
        r.prestart();
        try {
            r.log("Prestarted Succesfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static UltimateCore getInstance() {
        return instance;
    }

    public static File getPluginFile() {
        return file;
    }

    public File getDataFolder() {
        return file;
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException localIOException) {
        }
        return null;
    }

    public void saveResource(String resourcePath, boolean replace) {
        if ((resourcePath == null) || (resourcePath.equals(""))) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + this.file);
        }
        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        try {
            if ((!outFile.exists()) || (replace)) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                this.logger.info("Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            this.logger.info("Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    @Subscribe
    public void onEnable(InitializationEvent ev) {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            instance = this;
            file = cfile.getParentFile();
            game = ev.getGame();
            version = ev.getGame().getPluginManager().getPlugin("UltimateCore").get().getVersion();
            UltimateFileLoader.Enable();
            r.enableMES();
            UltimateFileLoader.addConfig();
            r.setColors();
            UuidUtil.loadPlayers();
            UltimateCommands.load();
            UltimateSigns.start();
            PerformanceUtil.getTps();
            ItemDatabase.enable();
            //
            UEconomy.start();
            r.start();
            UServer.start();
            CmdHeal.start();
            CmdRules.start();
            MetaItemStack.start();
            ItemUtil.start();
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
            UltimateConverter.convert();
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
            game.getSyncScheduler().runRepeatingTaskAfter(this, new UltimateTick(), 20L, 20L);
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Enabled Ultimate Core! (" + time + "ms)");

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to enable UltimateCore");
        }
        test();
    }

    @Subscribe
    public void onWorldLoad(ServerStartingEvent e) {
        UltimateWorldLoader.startWorldLoading();
    }

    @Subscribe
    public void onDisable(ServerStoppingEvent e) {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            r.removeUC();
            ItemDatabase.disable();
            DynmapListener.stop();
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Disabled Ultimate Core! (" + time + "ms)");
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to disable UltimateCore");
        }
    }

    public void test() {

    }

}
