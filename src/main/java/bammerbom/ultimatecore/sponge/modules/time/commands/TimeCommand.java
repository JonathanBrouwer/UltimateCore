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

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandChildrenInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.BoundedIntegerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.time.TimeModule;
import bammerbom.ultimatecore.sponge.modules.time.api.TimePermissions;
import bammerbom.ultimatecore.sponge.modules.time.commands.time.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

@CommandChildrenInfo(children = {AddTimeCommand.class, DayTimeCommand.class, DisableTimeCommand.class, EnableTimeCommand.class, NightTimeCommand.class, QueryTimeCommand.class, SetTimeCommand.class})
@CommandInfo(module = TimeModule.class, aliases = "time")
@CommandPermissions(level = PermissionLevel.MOD)
public class TimeCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new BoundedIntegerArgument(Text.of("time"), 0, null)).optionalWeak().onlyOne().build()};
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        if (args.hasAny("time")) {
            checkPermission(sender, TimePermissions.UC_TIME_TIME_TICKS);

            World world;
            if (!args.hasAny("world")) {
                checkIfPlayer(sender);
                world = ((Player) sender).getWorld();
            } else {
                world = args.<World>getOne("world").get();
            }

            Integer ticks = args.<Integer>getOne("time").get();
            world.getProperties().setWorldTime(ticks);
            Messages.send(sender, "time.command.time.set.ticks", "%ticks%", ticks);
            return CommandResult.success();
        }
        throw new ErrorMessageException(getUsage(sender));
    }
}
