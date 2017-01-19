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
package bammerbom.ultimatecore.sponge.modules.weather.commands;

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.WorldArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.weather.WeatherModule;
import bammerbom.ultimatecore.sponge.modules.weather.api.WeatherPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = WeatherModule.class, aliases = {"thunder", "thunderstorm", "storm"})
public class ThunderCommand implements SmartCommand {
    @Override
    public Permission getPermission() {
        return WeatherPermissions.UC_WEATHER_WEATHER_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WeatherPermissions.UC_WEATHER_WEATHER_BASE, WeatherPermissions.UC_WEATHER_WEATHER_THUNDER);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new WorldArgument(Text.of("world"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, WeatherPermissions.UC_WEATHER_WEATHER_SUN);
        World world;
        if (sender instanceof Locatable) {
            world = ((Locatable) sender).getWorld();
        } else {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        world.setWeather(Weathers.THUNDER_STORM);
        sender.sendMessage(Messages.getFormatted(sender, "weather.command.weather.success", "%weather%", "thunder", "%world%", world.getName()));
        return CommandResult.success();
    }
}