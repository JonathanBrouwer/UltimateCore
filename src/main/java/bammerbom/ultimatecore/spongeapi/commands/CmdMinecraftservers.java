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
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.MinecraftServerUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class CmdMinecraftservers implements UltimateCommand {

    @Override
    public String getName() {
        return "minecraftservers";
    }

    @Override
    public String getPermission() {
        return "uc.minecraftservers";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("View the status of the minecraft services.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mcservers");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.minecraftservers", true)) {
            return CommandResult.empty();
        }
        if (!r.getCnfg().getBoolean("Metrics")) {
            r.sendMes(cs, "minecraftserversDisabled");
            return CommandResult.empty();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MinecraftServerUtil.runcheck();

                String os = "";
                for (MinecraftServerUtil.MinecraftServer str : MinecraftServerUtil.online) {
                    if (!os.equals("")) {
                        os = os + ", " + TextColors.GREEN + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + TextColors.GREEN + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServerUtil.MinecraftServer str : MinecraftServerUtil.problems) {
                    if (!os.equals("")) {
                        os = os + ", " + TextColors.GOLD + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + TextColors.GOLD + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServerUtil.MinecraftServer str : MinecraftServerUtil.offline) {
                    if (!os.equals("")) {
                        os = os + ", " + TextColors.DARK_RED + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + TextColors.DARK_RED + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                for (MinecraftServerUtil.MinecraftServer str : MinecraftServerUtil.unknown) {
                    if (!os.equals("")) {
                        os = os + ", " + TextColors.GRAY + str.toString().toLowerCase() + r.positive + "";
                    } else {
                        os = os + TextColors.GRAY + str.toString().toLowerCase() + r.positive + "";
                    }
                }
                r.sendMes(cs, "minecraftserversMessage", "%Servers", TextColors.RESET + os);

            }
        });
        thread.setName("UltimateCore: Server Check Thread");
        thread.start();
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}


