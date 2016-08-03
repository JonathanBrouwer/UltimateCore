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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.weather.api.WeatherPermissions;
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weather;
import org.spongepowered.api.world.weather.Weathers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.WEATHER.get();
    }

    @Override
    public String getIdentifier() {
        return "weather";
    }

    @Override
    public Permission getPermission() {
        return WeatherPermissions.UC_WEATHER;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WeatherPermissions.UC_WEATHER, WeatherPermissions.UC_WEATHER_SUN, WeatherPermissions.UC_WEATHER_RAIN, WeatherPermissions.UC_WEATHER_THUNDER);
    }

    @Override
    public Text getUsage() {
        return CMGenerator.usage(this, Messages.getFormatted("weather.command.weather.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("weather.command.weather.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return Messages.getFormatted("weather.command.weather.longdescription");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("weather", "downfall");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        Weather weathertype;
        switch (args[0].toLowerCase()) {
            case "sun":
            case "clear":
                if (!sender.hasPermission(WeatherPermissions.UC_WEATHER.get()) && !sender.hasPermission(WeatherPermissions.UC_WEATHER_SUN.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                weathertype = Weathers.CLEAR;
                break;
            case "rain":
            case "snow":
            case "downfall":
                if (!sender.hasPermission(WeatherPermissions.UC_WEATHER.get()) && !sender.hasPermission(WeatherPermissions.UC_WEATHER_RAIN.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                weathertype = Weathers.RAIN;
                break;
            case "thunder":
            case "thunderstorm":
            case "storm":
                if (!sender.hasPermission(WeatherPermissions.UC_WEATHER.get()) && !sender.hasPermission(WeatherPermissions.UC_WEATHER_THUNDER.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                weathertype = Weathers.THUNDER_STORM;
                break;
            default:
                if (!sender.hasPermission(WeatherPermissions.UC_WEATHER.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                sender.sendMessage(Messages.getFormatted("weather.command.weather.invalidweathertype", "%weather%", args[0]));
                return CommandResult.empty();
        }
        World world;
        if (args.length >= 2) {
            if (Sponge.getServer().getWorld(args[1]).isPresent()) {
                world = Sponge.getServer().getWorld(args[1]).get();
            } else {
                sender.sendMessage(Messages.getFormatted("core.worldnotfound", "%world%", args[1]));
                return CommandResult.empty();
            }
        } else {
            if (!(sender instanceof Player) && args.length != 2) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player player = (Player) sender;
            world = player.getWorld();
        }
        world.setWeather(weathertype);
        sender.sendMessage(Messages.getFormatted("weather.command.weather.success", "%weather%", args[0].toLowerCase(), "%world%", world.getName()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("sun", "clear", "rain", "snow", "downfall", "thunder", "thunderstorm", "storm");
        } else if (curn == 1) {
            return Sponge.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new));
        } else {
            return new ArrayList<>();
        }
    }
}
