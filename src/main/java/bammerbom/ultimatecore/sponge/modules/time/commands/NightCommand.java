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

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.time.TimeModule;
import bammerbom.ultimatecore.sponge.modules.time.api.TimePermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = TimeModule.class, aliases = {"night", "nighttime"})
public class NightCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return TimePermissions.UC_TIME_TIME_NIGHT;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TimePermissions.UC_TIME_TIME_NIGHT, TimePermissions.UC_TIME_TIME_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, TimePermissions.UC_TIME_TIME_BASE);
        checkPermission(sender, TimePermissions.UC_TIME_TIME_NIGHT);

        World world;
        if (sender instanceof Locatable) {
            world = ((Locatable) sender).getWorld();
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
}
