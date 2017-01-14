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
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.weather.Weathers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SunCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.WEATHER.get();
    }

    @Override
    public String getIdentifier() {
        return "sun";
    }

    @Override
    public Permission getPermission() {
        return WeatherPermissions.UC_WEATHER_WEATHER_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WeatherPermissions.UC_WEATHER_WEATHER_BASE, WeatherPermissions.UC_WEATHER_WEATHER_SUN);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sun", "clearweather");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(WeatherPermissions.UC_WEATHER_WEATHER_BASE.get()) && !sender.hasPermission(WeatherPermissions.UC_WEATHER_WEATHER_SUN.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        World world;
        if (args.length >= 1) {
            if (Sponge.getServer().getWorld(args[0]).isPresent()) {
                world = Sponge.getServer().getWorld(args[0]).get();
            } else {
                sender.sendMessage(Messages.getFormatted(sender, "core.worldnotfound", "%world%", args[0]));
                return CommandResult.empty();
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
                return CommandResult.empty();
            }
            Player player = (Player) sender;
            world = player.getWorld();
        }
        world.setWeather(Weathers.CLEAR);
        sender.sendMessage(Messages.getFormatted(sender, "weather.command.weather.success", "%weather%", "sun", "%world%", world.getName()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Sponge.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new));
        } else {
            return new ArrayList<>();
        }
    }
}
