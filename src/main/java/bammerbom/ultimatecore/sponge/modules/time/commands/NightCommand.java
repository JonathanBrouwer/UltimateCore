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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NightCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.TIME.get();
    }

    @Override
    public String getIdentifier() {
        return "night";
    }

    @Override
    public Permission getPermission() {
        return TimePermissions.UC_TIME_TIME_NIGHT;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TimePermissions.UC_TIME_TIME_NIGHT, TimePermissions.UC_TIME_TIME_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("night", "nighttime");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(TimePermissions.UC_TIME_TIME_BASE.get()) && !sender.hasPermission(TimePermissions.UC_TIME_TIME_NIGHT.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        World world;
        if (sender instanceof Player) {
            world = ((Player) sender).getWorld();
        } else if (sender instanceof CommandBlockSource) {
            world = ((CommandBlockSource) sender).getWorld();
        } else {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        //Players can enter their bed at 12541 ticks
        Long ticks = 12541 - (world.getProperties().getWorldTime() % 24000);
        world.getProperties().setWorldTime(world.getProperties().getWorldTime() + ticks);
        sender.sendMessage(Messages.getFormatted(sender, "time.command.time.set.night"));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return Collections.emptyList();
    }
}
