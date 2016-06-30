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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
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
        return "/<command> [-][XP][L] [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("View or set the experience of someone or yourself.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("xp", "exp");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.experience", false) && !r.perm(cs, "uc.experience.show", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            r.sendMes(cs, "experienceShow", "%Player", r.getDisplayName(cs), "%Experience", p.get(Keys.TOTAL_EXPERIENCE).orElse(0), "%Levels", p.get(Keys.EXPERIENCE_LEVEL).orElse(0));
        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            String rawxp = args[0];
            String xp = args[0].endsWith("L") ? rawxp : rawxp.replace("L", "").replace("l", "");
            if (!r.isInt(xp.replace("l", "").replace("L", ""))) {
                if (r.searchPlayer(args[0]) != null) {
                    Player p = r.searchPlayer(args[0]).orElse(null);
                    if (!r.perm(cs, "uc.experience", false) && !r.perm(cs, "uc.experience.show.others", false)) {
                        r.sendMes(cs, "noPermissions");
                        return CommandResult.empty();
                    }
                    r.sendMes(cs, "experienceShow", "%Player", r.getDisplayName(p), "%Experience", p.get(Keys.TOTAL_EXPERIENCE).orElse(0), "%Levels", p.get(Keys.EXPERIENCE_LEVEL).orElse(0));
                    return CommandResult.empty();
                } else {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
            }
            if (!r.perm(cs, "uc.experience", false) && !r.perm(cs, "uc.experience.set", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            Integer x = Integer.parseInt(xp.replace("L", "").replace("l", ""));
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (rawxp.endsWith("L") || rawxp.endsWith("l")) {
                if (p.get(Keys.EXPERIENCE_LEVEL).orElse(0) + x < 1) {
                    p.offer(Keys.TOTAL_EXPERIENCE, 0);
                } else {
                    p.offer(Keys.EXPERIENCE_LEVEL, p.get(Keys.EXPERIENCE_LEVEL).orElse(0) + x);
                }
                if (x >= 0) {
                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeLevels"), "%Player", r.getDisplayName(p));
                } else {
                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeLevels"), "%Player", r.getDisplayName(p));
                }
            } else {
                if (p.get(Keys.TOTAL_EXPERIENCE).orElse(0) + x < 1) {
                    p.offer(Keys.TOTAL_EXPERIENCE, 0);
                } else {
                    p.offer(Keys.TOTAL_EXPERIENCE, p.get(Keys.TOTAL_EXPERIENCE).orElse(0) + x);
                }
                if (x >= 0) {
                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeExperience"), "%Player", r.getDisplayName(p));
                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + x + "L");
                } else {
                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeExperience"), "%Player", r.getDisplayName(p));
                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + (x * -1) + "L");
                }
            }
        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.experience", false) && !r.perm(cs, "uc.experience.set.others", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            String rawxp = args[0];
            String xp = !args[0].endsWith("L") ? rawxp : rawxp.replaceAll("L", "").replaceAll("l", "");
            if (!r.isInt(xp)) {
                r.sendMes(cs, "numberFormat", "%Number", args[0]);
                return CommandResult.empty();
            }
            Integer x = Integer.parseInt(xp);
            Player t = r.searchPlayer(args[1]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return CommandResult.empty();
            }
            if (rawxp.endsWith("L") || rawxp.endsWith("l")) {
                if (t.get(Keys.EXPERIENCE_LEVEL).orElse(0) + x < 1) {
                    t.offer(Keys.TOTAL_EXPERIENCE, 0);
                } else {
                    t.offer(Keys.EXPERIENCE_LEVEL, (t.get(Keys.EXPERIENCE_LEVEL).orElse(0) + x));
                }
                if (x >= 0) {
                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeLevels"), "%Player", t.getName());
                } else {
                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeLevels"), "%Player", t.getName());
                }

            } else {
                if (x > Integer.MAX_VALUE) {
                    x = Integer.MAX_VALUE;
                }
                if (x < Integer.MIN_VALUE) {
                    x = Integer.MIN_VALUE;
                }

                if (t.get(Keys.TOTAL_EXPERIENCE).orElse(0) + x < 1) {
                    t.offer(Keys.TOTAL_EXPERIENCE, 0);
                } else {
                    t.offer(Keys.TOTAL_EXPERIENCE, t.get(Keys.TOTAL_EXPERIENCE).orElse(0) + x);
                }
                if (x >= 0) {
                    r.sendMes(cs, "experienceGive", "%Experience", x, "%Settype", r.mes("experienceSettypeExperience"), "%Player", t.getName());
                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + x + "L " + args[1]);
                } else {
                    r.sendMes(cs, "experienceTake", "%Experience", x * -1, "%Settype", r.mes("experienceSettypeExperience"), "%Player", t.getName());
                    r.sendMes(cs, "experienceTip", "%Command", "/xp " + (x * -1) + "L " + args[1]);
                }
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (curn == 1) {
            return null;
        }
        return new ArrayList<>();
    }

}
