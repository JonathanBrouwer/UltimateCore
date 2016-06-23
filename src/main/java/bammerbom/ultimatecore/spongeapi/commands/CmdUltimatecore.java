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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.bukkit.UltimateUpdater.UpdateType;
import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.UltimateCore;
import bammerbom.ultimatecore.spongeapi.UltimateUpdater;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UPlayer;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.api.IJails;
import com.earth2me.essentials.api.IWarps;
import com.earth2me.essentials.spawn.IEssentialsSpawn;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.command.ConsoleCommandSource;

import java.util.Arrays;
import java.util.List;

import static bammerbom.ultimatecore.bukkit.UltimateCore.file;

public class CmdUltimatecore implements UltimateCommand {

    @Override
    public String getName() {
        return "ultimatecore";
    }

    @Override
    public String getPermission() {
        return "uc.ultimatecore";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("uc");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (args.length == 0) {
            if (!r.perm(cs, "uc.ultimatecore", false, true)) {
                return;
            }
            r.sendMes(cs, "ultimatecoreMenu1");
            r.sendMes(cs, "ultimatecoreMenu2");
            r.sendMes(cs, "ultimatecoreMenu3");
            r.sendMes(cs, "ultimatecoreMenu4");
            r.sendMes(cs, "ultimatecoreMenu5");
            r.sendMes(cs, "ultimatecoreMenu6");
            r.sendMes(cs, "ultimatecoreMenu7");
            r.sendMes(cs, "ultimatecoreMenu8");
            r.sendMes(cs, "ultimatecoreMenu9");
            r.sendMes(cs, "ultimatecoreMenu10");
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!r.perm(cs, "uc.ultimatecore.reload", false, true)) {
                return;
            }
            Bukkit.getPluginManager().disablePlugin(r.getUC());
            System.gc();
            r.prestart();
            Bukkit.getPluginManager().enablePlugin(UltimateCore.getInstance());
            r.sendMes(cs, "ultimatecoreReload");
        } else if (args[0].equalsIgnoreCase("disable")) {
            if (!r.perm(cs, "uc.ultimatecore.disable", false, true)) {
                return;
            }
            Bukkit.getServer().getPluginManager().disablePlugin(r.getUC());
            r.sendMes(cs, "ultimatecoreDisable");
        } else if (args[0].equalsIgnoreCase("credits")) {
            r.sendMes(cs, "ultimatecoreCredits1");
            r.sendMes(cs, "ultimatecoreCredits2");
        } else if (args[0].equalsIgnoreCase("noreturn")) {
        } else if (args[0].equalsIgnoreCase("version")) {
            if (!r.perm(cs, "uc.ultimatecore.version", false, true)) {
                return;
            }
            cs.sendMessage(r.positive + "Your version of UltimateCore: " + r.neutral + r.getUC().getDescription().getVersion());
            cs.sendMessage(r.positive + "Newest version of UltimateCore: " + r.neutral + r.getUpdater().getLatestUpdate());
        } else if (args[0].equalsIgnoreCase("update")) {
            if (!r.perm(cs, "uc.ultimatecore.updater", false, true)) {
                return;
            }
            if (r.getCnfg().getBoolean("Updater.check")) {
                final Thread thr;
                thr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UltimateUpdater up = new UltimateUpdater(r.getUC(), 66979, file, UpdateType.DEFAULT, true);
                        r.getUpdater().waitForThread();
                        switch (up.getResult()) {
                            case DISABLED:
                            case FAIL_APIKEY:
                            case FAIL_BADID:
                            case FAIL_DBO:
                            case FAIL_DOWNLOAD:
                            case FAIL_NOVERSION:
                                r.sendMes(cs, "ultimatecoreUpdateFailed");
                                break;
                            case NO_UPDATE:
                                r.sendMes(cs, "ultimatecoreUpdateNotAvaiable");
                                break;
                            case SUCCESS:
                                if (!(cs instanceof ConsoleCommandSource)) {
                                    r.sendMes(cs, "ultimatecoreUpdateSucces");
                                }
                                break;
                            case UPDATE_AVAILABLE:
                                break;
                            default:
                                break;
                        }
                    }
                });
                thr.setName("UC Updater (Finishing thread)");
                thr.start();
            } else {
                r.sendMes(cs, "ultimatecoreUpdateDisabled");
            }
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (!r.perm(cs, "uc.ultimatecore.convert", false, true)) {
                return;
            }
            r.sendMes(cs, "ultimatecoreConvertStart");
            if (!Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
                r.sendMes(cs, "ultimatecoreConvertFailed");
                return;
            }
            IEssentials es = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
            try {
                r.log("Importing Jails...");
                IJails j = es.getJails();
                for (String s : j.getList()) {
                    UC.getServer().addJail(s, j.getJail(s));
                }
                r.log("Importing Warps...");
                IWarps w = es.getWarps();
                for (String s : w.getList()) {
                    UC.getServer().addWarp(s, w.getWarp(s));
                }
                try {
                    IEssentialsSpawn esp = (IEssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
                    UC.getServer().setSpawn(esp.getSpawn("default"), false, null, false);
                } catch (Exception ex) {
                }
                r.log("Importing Player Data...");
                for (org.bukkit.OfflinePlayer pl : r.getOfflinePlayers()) {
                    User u2 = es.getOfflineUser(pl.getName());
                    if (u2 == null) {
                        r.log("Failed to import player data of " + pl.getName());
                        continue;
                    }
                    UPlayer up = UC.getPlayer(pl);
                    for (String s : u2.getHomes()) {
                        up.addHome(s, u2.getHome(s));
                    }
                    if (u2.getJail() != null) {
                        up.jail(u2.getJail(), u2.getJailTimeout());
                    }
                    up.setNick(u2.getNickname());
                    up.setGod(u2.isGodModeEnabled());
                    up.setMuted(u2.getMuted(), u2.getMuteTimeout(), r.mes("muteDefaultReason"));
                    up.setSpy(u2.isSocialSpyEnabled());
                    up.setLastLocation(u2.getLastLocation());
                }
                r.sendMes(cs, "ultimatecoreConvertComplete");
            } catch (Exception ex) {
                ErrorLogger.log(ex, "Failed to convert from Essentials.");
            }
        } else {
            if (!r.perm(cs, "uc.ultimatecore", false, true)) {
                return;
            }
            r.sendMes(cs, "ultimatecoreMenu1");
            r.sendMes(cs, "ultimatecoreMenu2");
            r.sendMes(cs, "ultimatecoreMenu3");
            r.sendMes(cs, "ultimatecoreMenu4");
            r.sendMes(cs, "ultimatecoreMenu5");
            r.sendMes(cs, "ultimatecoreMenu6");
            r.sendMes(cs, "ultimatecoreMenu7");
            r.sendMes(cs, "ultimatecoreMenu8");
            r.sendMes(cs, "ultimatecoreMenu9");
            r.sendMes(cs, "ultimatecoreMenu10");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return Arrays.asList("reload", "credits", "disable", "version", "update", "convert");
    }
}
