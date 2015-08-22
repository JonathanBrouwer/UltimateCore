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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!(cs instanceof Player)) {
            Boolean perm = true;
            for (World w : Bukkit.getWorlds()) {
                if (r.checkArgs(args, 0) == false) {
                    r.sendMes(cs, "timeUsage");
                    return;
                } else {
                    if ("day".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.day", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        w.setFullTime(0);

                    } else if ("night".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.night", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        w.setFullTime(13000);

                    } else if ("disable".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.disabe", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        w.setGameRuleValue("doDaylightCycle", "false");
                    } else if ("enable".equalsIgnoreCase(args[0])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.enable", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        w.setGameRuleValue("doDaylightCycle", "true");
                    } else if (r.isInt(args[0])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.ticks", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        Integer time = Integer.parseInt(args[0]);
                        w.setFullTime(time);
                    } else if (args[0].equalsIgnoreCase("add")) {
                        if (!r.checkArgs(args, 1)) {
                            r.sendMes(cs, "timeUsage");
                            return;
                        }
                        if (r.isInt(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.add", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            Integer time = Integer.parseInt(args[1]);
                            w.setFullTime(w.getTime() + time);
                        }
                    } else if (args[0].equalsIgnoreCase("query")) {

                    } else if (args[0].equalsIgnoreCase("set")) {
                        if (!r.checkArgs(args, 1)) {
                            r.sendMes(cs, "timeUsage");
                            return;
                        }
                        if ("day".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.day", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            w.setFullTime(1000);

                        } else if ("night".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.night", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            w.setFullTime(13000);

                        } else if ("disable".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.disabe", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            w.setGameRuleValue("doDaylightCycle", "false");
                        } else if ("enable".equalsIgnoreCase(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.enable", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            w.setGameRuleValue("doDaylightCycle", "true");
                        } else if (r.isInt(args[1])) {
                            if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.ticks", false, false)) {
                                r.sendMes(cs, "noPermissions");
                                return;
                            }
                            Integer time = Integer.parseInt(args[1]);
                            w.setFullTime(time);
                        }
                    }
                }
            }

            if (r.checkArgs(args, 0) == false) {
                r.sendMes(cs, "timeUsage");
            } else {
                if (perm == false) {
                    return;
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
                        World w = Bukkit.getWorlds().get(0);
                        if (r.checkArgs(args, 2)) {
                            w = Bukkit.getWorld(args[2]);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return;
                            }
                        }
                        Long time = w.getTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeDaytime"), "%Value", time);
                    } else if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("gametime")) {
                        World w = Bukkit.getWorlds().get(0);
                        if (r.checkArgs(args, 2)) {
                            w = Bukkit.getWorld(args[2]);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return;
                            }
                        }
                        Long time = w.getFullTime();
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
            if (r.checkArgs(args, 0) == false) {
                r.sendMes(cs, "timeUsage");
            } else {
                if ("day".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.day", false, false)) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setFullTime(1000);
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                } else if ("night".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.night", false, false)) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setFullTime(13000);
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                } else if ("disable".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.disabe", false, false)) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setGameRuleValue("doDaylightCycle", "false");
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                } else if ("enable".equalsIgnoreCase(args[0])) {
                    if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.enable", false, false)) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    world.setGameRuleValue("doDaylightCycle", "true");
                    r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                } else if (r.isInt(args[0])) {
                    if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.ticks", false, false)) {
                        r.sendMes(cs, "noPermissions");
                        return;
                    }
                    Integer time = Integer.parseInt(args[0]);
                    world.setFullTime(time);
                    r.sendMes(cs, "timeMessage", "%Time", args[0]);
                } else if (args[0].equalsIgnoreCase("query")) {
                    if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("daytime")) {
                        World w = p.getWorld();
                        if (r.checkArgs(args, 2)) {
                            w = Bukkit.getWorld(args[2]);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return;
                            }
                        }
                        Long time = w.getTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeDaytime"), "%Value", time);
                    } else if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("gametime")) {
                        World w = p.getWorld();
                        if (r.checkArgs(args, 2)) {
                            w = Bukkit.getWorld(args[2]);
                            if (w == null) {
                                r.sendMes(cs, "worldNotFound", "%World", args[2]);
                                return;
                            }
                        }
                        Long time = w.getFullTime();
                        r.sendMes(cs, "timeQuery", "%Type", r.mes("timeGametime"), "%Value", time);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (!r.checkArgs(args, 1)) {
                        r.sendMes(cs, "timeUsage");
                        return;
                    }
                    if (r.isInt(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.add", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        Integer time = Integer.parseInt(args[1]);
                        world.setFullTime(world.getTime() + time);
                        r.sendMes(cs, "timeAdd", "%Time", time);
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (!r.checkArgs(args, 1)) {
                        r.sendMes(cs, "timeUsage");
                        return;
                    }
                    if ("day".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.day", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setFullTime(1000);
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeDay"));
                    } else if ("night".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.night", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setFullTime(13000);
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("timeNight"));
                    } else if ("disable".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.disabe", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setGameRuleValue("doDaylightCycle", "false");
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("disabled"));
                    } else if ("enable".equalsIgnoreCase(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.enable", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        world.setGameRuleValue("doDaylightCycle", "true");
                        r.sendMes(cs, "timeMessage", "%Time", r.mes("enabled"));
                    } else if (r.isInt(args[1])) {
                        if (!r.perm(cs, "uc.time", false, false) && !r.perm(cs, "uc.time.ticks", false, false)) {
                            r.sendMes(cs, "noPermissions");
                            return;
                        }
                        Integer time = Integer.parseInt(args[1]);
                        world.setFullTime(time);
                        r.sendMes(cs, "timeMessage", "%Time", time);
                    } else {
                        r.sendMes(cs, "timeUsage");
                    }
                } else {
                    r.sendMes(cs, "timeUsage");
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return Arrays.asList("day", "night", "ticks", "disable", "enable", "add", "query");
    }
}
