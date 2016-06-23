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
import bammerbom.ultimatecore.spongeapi.resources.utils.*;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.util.logging.Logger;

@Plugin(id = "ultimatecore", name = "UltimateCore", version = "${project.version}")
public class UltimateCore {

    @ConfigDir(sharedRoot = false)
    public static File file;
    private static UltimateCore instance = null;
    @Inject
    private Logger logger;

    public static UltimateCore getInstance() {
        return instance;
    }

    @Listener
    public void onLoad(GamePreInitializationEvent ev) {
        instance = this;
        try {
            r.log("Prestarted Succesfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Listener
    public void onEnable(GameInitializationEvent ev) {
        try {
            //
            Long time = System.currentTimeMillis();
            //UTIL STARTUP
            UltimateFileLoader.Enable();
            ServerIDUtil.start();
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
            //UTIL STARTUP END
            String c = Sponge.getPlatform().getMinecraftVersion().getName();  //Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
            Integer v = Integer.parseInt(c.split("\\.")[1]);
            if (v < 10) {
                Sponge.getServer().getConsole().sendMessage(Text.of(""));
                r.log(TextColors.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                r.log(TextColors.YELLOW + "Warning! Version " + c + " of Minecraft is not supported!");
                r.log(TextColors.YELLOW + "Use UltimateCore at your own risk!");
                r.log(TextColors.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                Sponge.getServer().getConsole().sendMessage(Text.of(""));
            }
            //
            r.runUpdater();
            r.runMetrics();
            //LISTENER STARTUP
            EventManager pm = Sponge.getEventManager();
            GlobalPlayerListener.start();
            pm.registerListeners(this, new GlobalWorldListener());
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
            MinecraftServerListener.start();
            //LISTENER STARTUP END
            Sponge.getScheduler().createTaskBuilder().delayTicks(40).intervalTicks(40).name("UltimateCore Tick").execute(new UltimateTick()).submit(this);
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Enabled UltimateCore! (" + time + "ms)");

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to enable UltimateCore");
        }
        UltimateWorldLoader.startWorldLoading();
        test();
    }

    @Listener
    public void onDisable(GameStoppingEvent ev) {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            r.removeUC();
            ItemDatabase.disable();
            DynmapListener.stop();
            BossbarUtil.stop();
            //
            time = System.currentTimeMillis() - time;
            r.log(TextColors.GREEN + "Disabled UltimateCore! (" + time + "ms)");
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