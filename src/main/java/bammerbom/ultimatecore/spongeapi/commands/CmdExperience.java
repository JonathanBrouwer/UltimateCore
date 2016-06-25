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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdExperience implements UltimateCommand {

    @Override
    public String getName() {
        return "experience";
    }

    @Override
    public String getPermission() {
        return "uc.experience";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
//    @Override
//    public List<String> getAliases() {
//        return Arrays.asList("xp", "exp");
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.checkArgs(args, 0)) {
//            if (!r.isPlayer(cs)) {
//                return CommandResult.empty();
//            }
//            Player p = (Player) cs;
//            if (!r.perm(cs, "uc.experience", false, false) && !r.perm(cs, "uc.experience.show", false, false)) {
//                r.sendMes(cs, "noPermissions");
//                return CommandResult.empty();
//            }
//            r.sendMes(cs, "experienceShow", "%Player", r.getDisplayName(cs), "%Experience", XpUtil.getExp(p), "%Levels", p.getLevel());
//        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
//            String rawxp = args[0];
//            String xp = args[0].endsWith("L") ? rawxp : rawxp.replace("L", "").replace("l", "");
//            if (!r.isInt(xp.replace("l", "").replace("L", ""))) {
//                if (r.searchPlayer(args[0]) != null) {
//                    Player p = r.searchPlayer(args[0]);
//                    if (!r.perm(cs, "uc.experience", false, false) && !r.perm(cs, "uc.experience.show.others", false, false)) {
//                        r.sendMes(cs, "noPermissions");
//                        return CommandResult.empty();
//                    }
//                    r.sendMes(cs, "experienceShow", "%Player", r.getDisplayName(p), "%Experience", XpUtil.getExp(p), "%Levels", p.getLevel());
//                    return CommandResult.empty();
//                } else {
//                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
//                    return CommandResult.empty();
//                }
//            }
//            if (!r.perm(cs, "uc.experience", false, false) && !r.perm(cs, "uc.experience.set", false, false)) {
//                r.sendMes(cs, "noPermissions");
//                return CommandResult.empty();
//            }
//            Integer x = Integer.parseInt(xp.replace("L", "").replace("l", ""));
//            if (!r.isPlayer(cs)) {
//                return CommandResult.empty();
//            }
//            Player p = (Player) cs;
//            if (rawxp.endsWith("L") || rawxp.endsWith("l")) {
//                if (p.getLevel() + x < 1) {
//                    XpUtil.setTotalExp(p, 0);
//                } else {
//                    p.setLevel(p.getLevel() + x);
//                }
//                if (x >= 0) {
//                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeLevels"), "%Player", r.getDisplayName(p));
//                } else {
//                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeLevels"), "%Player", r.getDisplayName(p));
//                }
//            } else {
//                if (XpUtil.getExp(p) + x < 1) {
//                    XpUtil.setTotalExp(p, 0);
//                } else {
//                    XpUtil.setTotalExp(p, XpUtil.getExp(p) + x);
//                }
//                if (x >= 0) {
//                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeExperience"), "%Player", r.getDisplayName(p));
//                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + x + "L");
//                } else {
//                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeExperience"), "%Player", r.getDisplayName(p));
//                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + (x * -1) + "L");
//                }
//            }
//        } else if (r.checkArgs(args, 1)) {
//            if (!r.perm(cs, "uc.experience", false, false) && !r.perm(cs, "uc.experience.set.others", false, false)) {
//                r.sendMes(cs, "noPermissions");
//                return CommandResult.empty();
//            }
//            String rawxp = args[0];
//            String xp = !args[0].endsWith("L") ? rawxp : rawxp.replaceAll("L", "").replaceAll("l", "");
//            if (!r.isInt(xp)) {
//                r.sendMes(cs, "numberFormat", "%Number", args[0]);
//                return CommandResult.empty();
//            }
//            Integer x = Integer.parseInt(xp);
//            Player t = r.searchPlayer(args[1]);
//            if (t == null) {
//                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
//                return CommandResult.empty();
//            }
//            if (rawxp.endsWith("L") || rawxp.endsWith("l")) {
//                if (t.getLevel() + x < 1) {
//                    XpUtil.setTotalExp(t, 0);
//                } else {
//                    t.setLevel(t.getLevel() + x);
//                }
//                if (x >= 0) {
//                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeLevels"), "%Player", t.getName());
//                } else {
//                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeLevels"), "%Player", t.getName());
//                }
//
//            } else {
//                if (x > Integer.MAX_VALUE) {
//                    x = Integer.MAX_VALUE;
//                }
//                if (x < Integer.MIN_VALUE) {
//                    x = Integer.MIN_VALUE;
//                }
//
//                if (XpUtil.getExp(t) + x < 1) {
//                    XpUtil.setTotalExp(t, 0);
//                } else {
//                    XpUtil.setTotalExp(t, XpUtil.getExp(t) + x);
//                }
//                if (x >= 0) {
//                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeExperience"), "%Player", t.getName());
//                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + x + "L " + args[1]);
//                } else {
//                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeExperience"), "%Player", t.getName());
//                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + (x * -1) + "L " + args[1]);
//                }
//            }
//        }
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        if (curn == 1) {
//            return null;
//        }
//        return new ArrayList<>();
//    }
}
