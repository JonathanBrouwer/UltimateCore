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
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class CmdTime implements UltimateCommand {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getPermission() {
        return "uc.time";
    }

    @Override
    public String getUsage() {
        return "/<command> day/night/ticks/disable/enable/add/query";
    }

    @Override
    public Text getDescription() {
        return Text.of("Change the time");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!(cs instanceof Player)) {
            Boolean perm = true;
            for (World w : Sponge.getServer().getWorlds()) {
                if (!r.checkArgs(args, 0)) {
                    r.sendMes(cs, "timeUsage");
                    return CommandResult.empty();
                } else {
                    if ("day".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.day", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        w.getProperties().setWorldTime(0);

                    } else if ("night".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.night", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        w.getProperties().setWorldTime(13000);

                    } else if ("disable".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.disabe", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        w.getProperties().setGameRule("doDaylightCycle", "false");
                    } else if ("enable".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.enable", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        w.getProperties().setGameRule("doDaylightCycle", "true");
                    } else if (r.isInt(args[0])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.ticks", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        Integer time = Integer.parseInt(args[0]);
                        w.getProperties().setWorldTime(time);
                    } else if (args[0].equalsIgnoreCase("add")) {
                        if (!r.checkArgs(args, 1)) {
                            r.sendMes(cs, "timeUsage");
                            return CommandResult.empty();
                        }
                        if (r.isInt(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.add", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            Integer time = Integer.parseInt(args[1]);
                            w.getProperties().setWorldTime(w.getProperties().getWorldTime() + time);
                        }
                    } else if (args[0].equalsIgnoreCase("query")) {

                    } else if (args[0].equalsIgnoreCase("set")) {
                        if (!r.checkArgs(args, 1)) {
                            r.sendMes(cs, "timeUsage");
                            return CommandResult.empty();
                        }
                        if ("day".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.day", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            w.getProperties().setWorldTime(1000);

                        } else if ("night".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.night", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            w.getProperties().setWorldTime(13000);

                        } else if ("disable".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.disabe", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            w.getProperties().setGameRule("doDaylightCycle", "false");
                        } else if ("enable".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.enable", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            w.getProperties().setGameRule("doDaylightCycle", "true");
                        } else if (r.isInt(args[1])) {
                            if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.ticks", false)) {
                                r.sendMes(cs, "noPermissions");
                                return CommandResult.empty();
                            }
                            Integer time = Integer.parseInt(args[1]);
                            w.getProperties().setWorldTime(time);
                        }
                    }
                }
            }

            if (!r.checkArgs(args, 0)) {
                r.sendMes(cs, "timeUsage");
            } else {
                if (!perm) {
                    return CommandResult.empty();
                }
                if ("day".equalsIgnoreCase(args[0])) {
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                } else if ("night".equalsIgnoreCase(args[0])) {
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                } else if ("disable".equalsIgnoreCase(args[0])) {
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                } else if ("enable".equalsIgnoreCase(args[0])) {
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                } else if (r.isInt(args[0])) {
                    r.sendMes(cs, "timeMessage", "%Time", args[0]);
                } else if (args[0].equalsIgnoreCase("query")) {
                    if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("daytime")) {
                        World w = (World) Sponge.getServer().getWorlds().toArray()[0];
                        if (r.checkArgs(args, 2)) {
                            w = Sponge.getServer().getWorld(args[2]).orElse(null);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return CommandResult.empty();
                            }
                        }
                        Long time = w.getProperties().getWorldTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeDaytime"), "%Value", time);
                    } else if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("gametime")) {
                        World w = (World) Sponge.getServer().getWorlds().toArray()[0];
                        if (r.checkArgs(args, 2)) {
                            w = Sponge.getServer().getWorld(args[2]).orElse(null);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return CommandResult.empty();
                            }
                        }
                        Long time = w.getProperties().getWorldTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeGametime"), "%Value", time);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (r.isInt(args[1])) {
                        r.sendMes(cs, "timeAdd", "%Time", args[1]);
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if ("day".equalsIgnoreCase(args[1])) {
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                    } else if ("night".equalsIgnoreCase(args[1])) {
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                    } else if ("disable".equalsIgnoreCase(args[1])) {
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                    } else if ("enable".equalsIgnoreCase(args[1])) {
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                    } else if (r.isInt(args[1])) {
                        r.sendMes(cs, "timeMessage", "%Time", args[1]);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else {
                    r.sendMes(cs, "timeUsage");
                }
            }
        } else {
            Player p = (Player) cs;
            World world = p.getWorld();
            if (!r.checkArgs(args, 0)) {
                r.sendMes(cs, "timeUsage");
            } else {
                if ("day".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.day", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setWorldTime(1000);
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                } else if ("night".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.night", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setWorldTime(13000);
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                } else if ("disable".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.disabe", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setGameRule("doDaylightCycle", "false");
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                } else if ("enable".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.enable", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    world.getProperties().setGameRule("doDaylightCycle", "true");
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                } else if (r.isInt(args[0])) {
                    if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.ticks", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    Integer time = Integer.parseInt(args[0]);
                    world.getProperties().setWorldTime(time);
                    r.sendMes(cs, "timeMessage", "%Time", args[0]);
                } else if (args[0].equalsIgnoreCase("query")) {
                    if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("daytime")) {
                        World w = p.getWorld();
                        if (r.checkArgs(args, 2)) {
                            w = Sponge.getServer().getWorld(args[2]).orElse(null);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return CommandResult.empty();
                            }
                        }
                        Long time = w.getProperties().getWorldTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeDaytime"), "%Value", time);
                    } else if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("gametime")) {
                        World w = p.getWorld();
                        if (r.checkArgs(args, 2)) {
                            w = Sponge.getServer().getWorld(args[2]).orElse(null);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return CommandResult.empty();
                            }
                        }
                        Long time = w.getProperties().getWorldTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeGametime"), "%Value", time);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (!r.checkArgs(args, 1)) {
                        r.sendMes(cs, "timeUsage");
                        return CommandResult.empty();
                    }
                    if (r.isInt(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.add", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        Integer time = Integer.parseInt(args[1]);
                        world.getProperties().setWorldTime(world.getProperties().getWorldTime() + time);
                        r.sendMes(cs, "timeAdd", "%Time", time);
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (!r.checkArgs(args, 1)) {
                        r.sendMes(cs, "timeUsage");
                        return CommandResult.empty();
                    }
                    if ("day".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.day", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setWorldTime(1000);
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                    } else if ("night".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.night", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setWorldTime(13000);
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                    } else if ("disable".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.disabe", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setGameRule("doDaylightCycle", "false");
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                    } else if ("enable".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.enable", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        world.getProperties().setGameRule("doDaylightCycle", "true");
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                    } else if (r.isInt(args[1])) {
                        if (!r.perm(cs, "uc.time", false) && !r.perm(cs, "uc.time.ticks", false)) {
                            r.sendMes(cs, "noPermissions");
                            return CommandResult.empty();
                        }
                        Integer time = Integer.parseInt(args[1]);
                        world.getProperties().setWorldTime(time);
                        r.sendMes(cs, "timeMessage", "%Time", time);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else {
                    r.sendMes(cs, "timeUsage");
                }
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
//    @Override
//    public List<String> getAliases() {
//        return Arrays.asList();
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {

//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        return Arrays.asList("day", "night", "ticks", "disable", "enable", "add", "query");
//    }
}
