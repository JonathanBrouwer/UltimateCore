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

import bammerbom.ultimatecore.bukkit.listeners.*;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.databases.ItemDatabase;
import bammerbom.ultimatecore.bukkit.resources.utils.BossbarUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.PerformanceUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.UuidUtil;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateCore extends JavaPlugin {

    public static UltimateCore instance;
    public static File file;

    public static UltimateCore getInstance() {
        return instance;
    }

    public static File getPluginFile() {
        return file;
    }

    @Override
    public void onEnable() {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            instance = this;
            file = getFile();
            UuidUtil.loadPlayers();
            UltimateFileLoader.Enable();
            r.EnableMES();
            UltimateFileLoader.addConfig();
            UltimateCommands.load();
            PerformanceUtil.getTps();
            BossbarUtil.enable();
            ItemDatabase.enable();
            //
            String c = Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
            Integer v = Integer.parseInt(c.replace(".", ""));
            if (v < 18) {
                Bukkit.getConsoleSender().sendMessage(" ");
                r.log(ChatColor.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                r.log(ChatColor.YELLOW + "Warning! Version " + c + " of craftbukkit is not supported!");
                r.log(ChatColor.YELLOW + "Use UltimateCore at your own risk!");
                r.log(ChatColor.DARK_RED + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                Bukkit.getConsoleSender().sendMessage(" ");
            }
            //
            r.runUpdater();
            r.runMetrics();
            //
            PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(new GlobalPlayerListener(), this);
            pm.registerEvents(new GlobalWorldListener(), this);
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
            //
            time = System.currentTimeMillis() - time;
            r.log(ChatColor.GREEN + "Enabled Ultimate Core! (" + time + "ms)");

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to enable UltimateCore");
        }
        UltimateWorldLoader.startWorldLoading();
        test();
    }

    @Override
    public void onDisable() {
        try {
            //
            Long time = System.currentTimeMillis();
            //
            r.removeUC();
            ItemDatabase.disable();
            BossbarUtil.disable();
            DynmapListener.stop();
            //
            HandlerList.unregisterAll(this);
            Bukkit.getServicesManager().unregisterAll(this);
            Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(this);
            Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
            Bukkit.getServer().getScheduler().cancelTasks(this);
            //
            time = System.currentTimeMillis() - time;
            r.log(ChatColor.GREEN + "Disabled Ultimate Core! (" + time + "ms)");
            //

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to disable UltimateCore");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            UltimateCommands.onCmd(sender, cmd, label, args);

        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to execute command: /" + label + " " + r.getFinalArg(args, 0));
        }
        return true;
    }

    public void test() {
    }

}
