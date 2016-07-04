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

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.UltimateCore;
import bammerbom.ultimatecore.spongeapi.UltimateUpdater;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

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
    public String getUsage() {
        return "/<command> reload/credits/disable/version/update";
    }

    @Override
    public Text getDescription() {
        return Text.of("The ultimatecore menu command.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("uc");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (args.length == 0) {
            if (!r.perm(cs, "uc.ultimatecore", true)) {
                return CommandResult.empty();
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
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!r.perm(cs, "uc.ultimatecore.reload", true)) {
                return CommandResult.empty();
            }
            //TODO wait for api
            Sponge.getPluginManager().getPlugin("ultimatecore").get().disablePlugin(r.getUC());
            System.gc();
            Bukkit.getPluginManager().enablePlugin(UltimateCore.getInstance());
            r.sendMes(cs, "ultimatecoreReload");
        } else if (args[0].equalsIgnoreCase("disable")) {
            if (!r.perm(cs, "uc.ultimatecore.disable", true)) {
                return CommandResult.empty();
            }
            //TODO wait for api
            Bukkit.getServer().getPluginManager().disablePlugin(r.getUC());
            r.sendMes(cs, "ultimatecoreDisable");
        } else if (args[0].equalsIgnoreCase("credits")) {
            r.sendMes(cs, "ultimatecoreCredits1");
            r.sendMes(cs, "ultimatecoreCredits2");
        } else if (args[0].equalsIgnoreCase("noreturn")) {
        } else if (args[0].equalsIgnoreCase("version")) {
            if (!r.perm(cs, "uc.ultimatecore.version", true)) {
                return CommandResult.empty();
            }
            cs.sendMessage(Text.of(r.positive + "Your version of UltimateCore: " + r.neutral + Sponge.getPluginManager().fromInstance(r.getUC()).get().getVersion().get()));
            cs.sendMessage(Text.of(r.positive + "Newest version of UltimateCore: " + r.neutral + r.getUpdater().getLatestUpdate()));
            //TODO custom messages?
        } else if (args[0].equalsIgnoreCase("update")) {
            if (!r.perm(cs, "uc.ultimatecore.updater", true)) {
                return CommandResult.empty();
            }
            if (r.getCnfg().getBoolean("Updater.check")) {
                final Thread thr;
                thr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UltimateUpdater up = new UltimateUpdater(66979, Sponge.getPluginManager().getPlugin("ultimatecore").get().getSource().get().toFile(), UltimateUpdater.UpdateType
                                .DEFAULT, true);
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
                                if (!(cs instanceof ConsoleSource)) {
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
        } else {
            if (!r.perm(cs, "uc.ultimatecore", true)) {
                return CommandResult.empty();
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
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return Arrays.asList("reload", "credits", "disable", "version", "update", "convert");
    }
}
