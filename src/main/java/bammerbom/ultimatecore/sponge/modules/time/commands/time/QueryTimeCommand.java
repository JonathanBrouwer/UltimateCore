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
package bammerbom.ultimatecore.sponge.modules.time.commands.time;

import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.ChoicesArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.WorldArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.modules.time.TimeModule;
import bammerbom.ultimatecore.sponge.modules.time.api.TimePermissions;
import bammerbom.ultimatecore.sponge.modules.time.commands.TimeCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.HashMap;

@CommandParentInfo(parent = TimeCommand.class)
@CommandInfo(module = TimeModule.class, aliases = {"query", "request"})
public class QueryTimeCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        HashMap<String, String> types = new HashMap<>();
        types.put("daytime", "daytime");
        types.put("day", "days");
        types.put("days", "days");
        types.put("gametime", "gametime");
        return new CommandElement[]{
                Arguments.builder(new ChoicesArgument(Text.of("type"), types)).onlyOne().optional().build(), Arguments.builder(new WorldArgument(Text.of("world"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        World world;
        if (!args.hasAny("world")) {
            checkIfPlayer(sender);
            world = ((Player) sender).getWorld();
        } else {
            world = args.<World>getOne("world").get();
        }

        if (args.hasAny("type")) {
            String type = args.<String>getOne("type").get();
            if (type.equalsIgnoreCase("daytime")) {
                checkPermission(sender, TimePermissions.UC_TIME_TIME_QUERY_DAYTIME);
                Long result = world.getProperties().getWorldTime() % 24000;
                Messages.send(sender, "time.command.time.query.daytime", "%daytime%", result);
                return CommandResult.builder().queryResult(result.intValue()).build();
            } else if (type.equalsIgnoreCase("days")) {
                checkPermission(sender, TimePermissions.UC_TIME_TIME_QUERY_DAYS);
                Long result = world.getProperties().getWorldTime() / 24000;
                Messages.send(sender, "time.command.time.query.day", "%days%", result);
                return CommandResult.builder().queryResult(result.intValue()).build();
            } else if (type.equalsIgnoreCase("gametime")) {
                checkPermission(sender, TimePermissions.UC_TIME_TIME_QUERY_GAMETIME);
                Long result = world.getProperties().getWorldTime();
                Messages.send(sender, "time.command.time.query.gametime", "%gametime%", result);
                return CommandResult.builder().queryResult(result.intValue()).build();
            }
            throw new ErrorMessageException(getUsage(sender));
        } else {
            checkPermission(sender, TimePermissions.UC_TIME_TIME_QUERY_FORMATTED);
            //Player wants formatted time (/time query or /time)
            Long day = world.getProperties().getWorldTime() / 24000;
            Long daytime = world.getProperties().getWorldTime() % 24000;
            Long hours24 = Double.valueOf(Math.floor(daytime / 1000.0)).longValue();
            Long hours12 = Double.valueOf(Math.floor((daytime % 12000) / 1000.0)).longValue();
            Long minutes = Math.round((daytime % 1000) * 0.06); // / 1000 * 60
            boolean am = daytime < 12000;

            if (am) {
                Messages.send(sender, "time.command.time.query.formatted.am", "%day%", day, "%hours%", hours24, "%minutes%", minutes, "%hours12%", hours12);
            } else {
                Messages.send(sender, "time.command.time.query.formatted.pm", "%day%", day, "%hours%", hours24, "%minutes%", minutes, "%hours12%", hours12);
            }
            return CommandResult.success();
        }
    }
}