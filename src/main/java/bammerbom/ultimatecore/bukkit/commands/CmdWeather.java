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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.listeners.WeatherListener;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdWeather implements UltimateCommand {

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getPermission() {
        return "uc.weather";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sun", "rain", "thunder", "storm");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (label.equalsIgnoreCase("sun")) {
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Bukkit.getWorlds()) {
                    world.setStorm(false);
                    world.setThundering(false);
                    if (r.checkArgs(args, 1) && r.isInt(args[0])) {
                        world.setWeatherDuration(20 * Integer.parseInt(args[0]));
                    }
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherSun"));
            } else {
                Player p = (Player) cs;
                p.getWorld().setStorm(false);
                p.getWorld().setThundering(false);
                if (r.checkArgs(args, 0) && r.isInt(args[0])) {
                    p.getWorld().setWeatherDuration(20 * Integer.parseInt(args[0]));
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherSun"));
            }
            WeatherListener.setEnabled(true);
            return;
        }
        if (label.equalsIgnoreCase("rain")) {
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Bukkit.getWorlds()) {
                    world.setStorm(true);
                    world.setThundering(false);
                    if (r.checkArgs(args, 1) && r.isInt(args[0])) {
                        world.setWeatherDuration(20 * Integer.parseInt(args[0]));
                    }
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherRain"));
            } else {
                Player p = (Player) cs;
                p.getWorld().setStorm(true);
                p.getWorld().setThundering(false);
                if (r.checkArgs(args, 0) && r.isInt(args[0])) {
                    p.getWorld().setWeatherDuration(20 * Integer.parseInt(args[0]));
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherRain"));
            }
            WeatherListener.setEnabled(true);
            return;
        }
        if (label.equalsIgnoreCase("thunder") || label.equalsIgnoreCase("storm")) {
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Bukkit.getWorlds()) {
                    world.setStorm(true);
                    world.setThundering(true);
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherThunder"));
            } else {
                Player p = (Player) cs;
                p.getWorld().setStorm(true);
                p.getWorld().setThundering(true);
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherThunder"));
            }
            WeatherListener.setEnabled(true);
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            r.sendMes(cs, "weatherUsage");
        } else {
            Integer weather = 0;
            if (!(cs instanceof Player)) {
                WeatherListener.setEnabled(false);
                for (World world : Bukkit.getWorlds()) {
                    if ("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])) {
                        if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.sun", false, false) == false) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setStorm(false);
                        world.setThundering(false);
                        weather = 1;
                    } else if ("rain".equalsIgnoreCase(args[0])) {
                        if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.rain", false, false) == false) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setStorm(true);
                        world.setThundering(false);
                        weather = 2;
                    } else if ("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0]) ||
                            "thunderstorm".equalsIgnoreCase(args[0])) {
                        if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.storm", false, false) == false) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setStorm(true);
                        world.setThundering(true);
                        weather = 3;
                    }
                    if (r.checkArgs(args, 1) && r.isInt(args[1])) {
                        world.setWeatherDuration(20 * Integer.parseInt(args[1]));
                    }
                }
                WeatherListener.setEnabled(true);
            } else {
                WeatherListener.setEnabled(false);
                World world = ((Entity) cs).getWorld();
                if ("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])) {
                    if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.sun", false, false) == false) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setStorm(false);
                    world.setThundering(false);
                    weather = 1;
                } else if ("rain".equalsIgnoreCase(args[0])) {
                    if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.rain", false, false) == false) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setStorm(true);
                    world.setThundering(false);
                    weather = 2;
                } else if ("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0]) || "thunderstorm".equalsIgnoreCase(args[0])) {
                    if (r.perm(cs, "uc.weather", false, false) == false && r.perm(cs, "uc.weather.storm", false, false) == false) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setStorm(true);
                    world.setThundering(true);
                    weather = 3;
                }
                if (r.checkArgs(args, 1) && r.isInt(args[1])) {
                    world.setWeatherDuration(20 * Integer.parseInt(args[1]));
                }
                WeatherListener.setEnabled(true);
            }
            if (weather == 1) {
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherSun"));
            } else if (weather == 2) {
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherRain"));
            } else if (weather == 3) {
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherThunder"));
            } else {
                r.sendMes(cs, "weatherUsage");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return Arrays.asList("sun", "rain", "thunder");
    }
}
