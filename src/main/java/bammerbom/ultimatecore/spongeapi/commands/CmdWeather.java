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
import bammerbom.ultimatecore.spongeapi.listeners.WeatherListener;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

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
    public String getUsage() {
        return "/<command> clear/rain/thunder [Time]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Change the weather.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (label.equalsIgnoreCase("sun")) {
            if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.sun", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Sponge.getServer().getWorlds()) {
                    world.getProperties().setRaining(false);
                    world.getProperties().setThundering(false);
                    if (r.checkArgs(args, 1) && r.isInt(args[0])) {
                        world.getProperties().setRainTime(20 * Integer.parseInt(args[0]));
                    }
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherSun"));
            } else {
                Player p = (Player) cs;
                p.getWorld().getProperties().setRaining(false);
                p.getWorld().getProperties().setThundering(false);
                if (r.checkArgs(args, 0) && r.isInt(args[0])) {
                    p.getWorld().getProperties().setRainTime(20 * Integer.parseInt(args[0]));
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherSun"));
            }
            WeatherListener.setEnabled(true);
            return CommandResult.empty();
        }
        if (label.equalsIgnoreCase("rain")) {
            if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.rain", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Sponge.getServer().getWorlds()) {
                    world.getProperties().setRaining(true);
                    world.getProperties().setThundering(false);
                    if (r.checkArgs(args, 1) && r.isInt(args[0])) {
                        world.getProperties().setRainTime(20 * Integer.parseInt(args[0]));
                    }
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherRain"));
            } else {
                Player p = (Player) cs;
                World world = p.getWorld();
                world.getProperties().setRaining(true);
                world.getProperties().setThundering(false);
                if (r.checkArgs(args, 0) && r.isInt(args[0])) {
                    world.getProperties().setRainTime(20 * Integer.parseInt(args[0]));
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherRain"));
            }
            WeatherListener.setEnabled(true);
            return CommandResult.empty();
        }
        if (label.equalsIgnoreCase("thunder") || label.equalsIgnoreCase("storm")) {
            if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.storm", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            WeatherListener.setEnabled(false);
            if (!(cs instanceof Player)) {
                for (World world : Sponge.getServer().getWorlds()) {
                    world.getProperties().setRaining(true);
                    world.getProperties().setThundering(true);
                }
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherThunder"));
            } else {
                Player p = (Player) cs;
                World world = p.getWorld();
                world.getProperties().setRaining(true);
                world.getProperties().setThundering(true);
                r.sendMes(cs, "weatherSet", "%Weather", r.mes("weatherThunder"));
            }
            WeatherListener.setEnabled(true);
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "weatherUsage");
        } else {
            Integer weather = 0;
            if (!(cs instanceof Player)) {
                WeatherListener.setEnabled(false);
                for (World world : Sponge.getServer().getWorlds()) {
                    if ("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.sun", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setRaining(false);
                        world.getProperties().setThundering(false);
                        weather = 1;
                    } else if ("rain".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.rain", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setRaining(true);
                        world.getProperties().setThundering(false);
                        weather = 2;
                    } else if ("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0]) ||
                            "thunderstorm".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.storm", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setRaining(true);
                        world.getProperties().setThundering(true);
                        weather = 3;
                    }
                    if (r.checkArgs(args, 1) && r.isInt(args[1])) {
                        world.getProperties().setRainTime(20 * Integer.parseInt(args[0]));
                    }
                }
                WeatherListener.setEnabled(true);
            } else {
                WeatherListener.setEnabled(false);
                World world = ((Entity) cs).getWorld();
                if ("sun".equalsIgnoreCase(args[0]) || "clear".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.sun", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setRaining(false);
                    world.getProperties().setThundering(false);
                    weather = 1;
                } else if ("rain".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.rain", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setRaining(true);
                    world.getProperties().setThundering(false);
                    weather = 2;
                } else if ("storm".equalsIgnoreCase(args[0]) || "thunder".equalsIgnoreCase(args[0]) || "thunderstorm".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.weather", false) && !r.perm(cs, "uc.weather.storm", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setRaining(true);
                    world.getProperties().setThundering(true);
                    weather = 3;
                }
                if (r.checkArgs(args, 1) && r.isInt(args[1])) {
                    world.getProperties().setRainTime(20 * Integer.parseInt(args[0]));
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
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return Arrays.asList("sun", "rain", "thunder");
    }
}
