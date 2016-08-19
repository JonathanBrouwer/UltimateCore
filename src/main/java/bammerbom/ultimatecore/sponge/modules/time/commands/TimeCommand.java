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
package bammerbom.ultimatecore.sponge.modules.time.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.time.api.TimePermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class TimeCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.TIME.get();
    }

    @Override
    public String getIdentifier() {
        return "time";
    }

    @Override
    public Permission getPermission() {
        return TimePermissions.UC_TIME;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TimePermissions.UC_TIME);
    }

    @Override
    public Text getUsage() {
        return Messages.getFormatted("time.command.time.usage");
    }

    @Override
    public Text getShortDescription() {
        return Messages.getFormatted("time.command.time.shortdescription");
    }

    @Override
    public Text getLongDescription() {
        return Messages.getFormatted("time.command.time.longdescription");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("time", "settime");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        //time query: View the time
        if (args.length == 0 || args[0].equalsIgnoreCase("query")) {
            World world;
            if (sender instanceof Player) {
                world = ((Player) sender).getWorld();
            } else if (sender instanceof CommandBlockSource) {
                world = ((CommandBlockSource) sender).getWorld();
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            //Player wants daytime, days or gametime (/time query x)
            if (args.length >= 2) {
                Long result;
                //Parsing & Permission checks
                if (args[1].equalsIgnoreCase("daytime")) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_QUERY.get()) && !sender.hasPermission(TimePermissions
                            .UC_TIME_QUERY_DAYTIME.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    result = world.getProperties().getWorldTime() % 24000;
                    sender.sendMessage(Messages.getFormatted("time.command.time.query.daytime", "%daytime%", result));
                } else if (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("days")) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_QUERY.get()) && !sender.hasPermission(TimePermissions
                            .UC_TIME_QUERY_DAYS.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    result = world.getProperties().getWorldTime() / 24000;
                    sender.sendMessage(Messages.getFormatted("time.command.time.query.day", "%days%", result));
                } else if (args[1].equalsIgnoreCase("gametime")) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_QUERY.get()) && !sender.hasPermission(TimePermissions
                            .UC_TIME_QUERY_GAMETIME.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    result = world.getProperties().getWorldTime();
                    sender.sendMessage(Messages.getFormatted("time.command.time.query.gametime", "%gametime%", result));
                } else {
                    sender.sendMessage(getLongDescription());
                    return CommandResult.empty();
                }
                return CommandResult.builder().queryResult(result.intValue()).build();
            } else {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_QUERY.get()) && !sender.hasPermission(TimePermissions
                        .UC_TIME_QUERY_FORMATTED.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                //Player wants formatted time (/time query or /time)
                Long day = world.getProperties().getWorldTime() / 24000;
                Long daytime = world.getProperties().getWorldTime() % 24000;
                Long hours24 = Double.valueOf(Math.floor(daytime / 1000.0)).longValue();
                Long hours12 = Double.valueOf(Math.floor((daytime % 12000) / 1000.0)).longValue();
                Long minutes = Math.round((daytime % 1000) * 0.06); // / 1000 * 60
                boolean am = daytime < 12000;

                if (am) {
                    sender.sendMessage(Messages.getFormatted("time.command.time.query.formatted.am", "%day%", day, "%hours%", hours24, "%minutes%", minutes, "%hours12%", hours12));
                } else {
                    sender.sendMessage(Messages.getFormatted("time.command.time.query.formatted.pm", "%day%", day, "%hours%", hours24, "%minutes%", minutes, "%hours12%", hours12));
                }

                return CommandResult.success();
            }
        } else if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("day") || args[0].equalsIgnoreCase("night") || TimeUtil.isNumber(args[0])) {
            //time set/add/day/night/<ticks>
            World world;
            if (sender instanceof Player) {
                world = ((Player) sender).getWorld();
            } else if (sender instanceof CommandBlockSource) {
                world = ((CommandBlockSource) sender).getWorld();
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_ADD.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                if (args.length >= 2 && TimeUtil.isNumber(args[1])) {
                    Long ticks = Long.parseLong(args[1]);
                    world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
                    sender.sendMessage(Messages.getFormatted("time.command.time.set.add", "%ticks%", ticks));
                } else {
                    sender.sendMessage(getLongDescription());
                    return CommandResult.empty();
                }
            } else if (args[0].equalsIgnoreCase("day")) {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_DAY.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                Long ticks = 24000 - (world.getProperties().getWorldTime() % 24000);
                world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
                sender.sendMessage(Messages.getFormatted("time.command.time.set.day"));
            } else if (args[0].equalsIgnoreCase("night")) {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_NIGHT.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                //Players can enter their bed at 12541 ticks
                Long ticks = 12541 - (world.getProperties().getWorldTime() % 24000);
                world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
                sender.sendMessage(Messages.getFormatted("time.command.time.set.night"));
            } else if (TimeUtil.isNumber(args[0])) {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_TICKS.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                Long ticks = Long.parseLong(args[0]);
                world.getProperties().setWorldTime(ticks);
                sender.sendMessage(Messages.getFormatted("time.command.time.set.ticks", "%ticks%", ticks));
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length <= 1) {
                    sender.sendMessage(getLongDescription());
                    return CommandResult.empty();
                }
                if (args[1].equalsIgnoreCase("day")) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_DAY.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    Long ticks = 24000 - (world.getProperties().getWorldTime() % 24000);
                    world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
                    sender.sendMessage(Messages.getFormatted("time.command.time.set.day"));
                } else if (args[1].equalsIgnoreCase("night")) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_NIGHT.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    //Players can enter their bed at 12541 ticks
                    Long ticks = 12541 - (world.getProperties().getWorldTime() % 24000);
                    world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
                    sender.sendMessage(Messages.getFormatted("time.command.time.set.night"));
                } else if (TimeUtil.isNumber(args[1])) {
                    if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_TICKS.get())) {
                        sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                        return CommandResult.empty();
                    }
                    Long ticks = Long.parseLong(args[1]);
                    world.getProperties().setWorldTime(ticks);
                    sender.sendMessage(Messages.getFormatted("time.command.time.set.ticks", "%ticks%", ticks));
                } else {
                    sender.sendMessage(getLongDescription());
                    return CommandResult.empty();
                }
            } else {
                //No matching command found
                sender.sendMessage(getLongDescription());
                return CommandResult.empty();
            }
            return CommandResult.success();
        } else if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
            //time set/add/day/night/<ticks>
            World world;
            if (sender instanceof Player) {
                world = ((Player) sender).getWorld();
            } else if (sender instanceof CommandBlockSource) {
                world = ((CommandBlockSource) sender).getWorld();
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            if (args[0].equalsIgnoreCase("enable")) {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_ENABLE.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
            } else {
                if (!sender.hasPermission(TimePermissions.UC_TIME.get()) && !sender.hasPermission(TimePermissions.UC_TIME_DISABLE.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
            }
            world.getProperties().setGameRule("doDaylightCycle", args[0].equalsIgnoreCase("enable") ? "true" : "false");
            sender.sendMessage(Messages.getFormatted("time.command.time." + args[0].toLowerCase()));
            return CommandResult.success();
        }
        //No matching command found
        sender.sendMessage(getLongDescription());
        return CommandResult.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (curn == 1 && args[0].equalsIgnoreCase("query")) {
            return Arrays.asList("gametime", "daytime", "days");
        } else if (curn == 1 && args[0].equalsIgnoreCase("set")) {
            return Arrays.asList("day", "night");
        }
        return Arrays.asList("day", "night", "enable", "disable", "set", "add");
    }
}
