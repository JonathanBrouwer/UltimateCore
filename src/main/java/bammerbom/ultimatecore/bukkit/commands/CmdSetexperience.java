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

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.XpUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdSetexperience implements UltimateCommand {

    @Override
    public String getName() {
        return "setexperience";
    }

    @Override
    public String getPermission() {
        return "uc.setexperience";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setxp", "setexp");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "setexperienceUsage");
            return;
        }
        if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.setexperience", false, true)) {
                return;
            }
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            String amount = args[0];
            Integer x;
            if (r.isInt(amount.replace("l", "").replace("L", ""))) {
                x = Integer.parseInt(amount.replace("l", "").replace("L", ""));
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[0].replace("l", "").replace("L", ""));
                return;
            }
            r.normalize(x, 0, 999999);
            if (amount.endsWith("l") || amount.endsWith("L")) {
                p.setLevel(x);
                r.sendMes(cs, "experienceSet", "%Settype", r.mes("experienceSettypeLevels"), "%Player", p.getName(), "%Experience", x);
            } else {
                XpUtil.setTotalExp(p, x);
                r.sendMes(cs, "experienceSet", "%Settype", r.mes("experienceSettypeExperience"), "%Player", p.getName(), "%Experience", x);
                r.sendMes(cs, "experienceTip", "%Command", "/setexperience " + x + "L");
            }

        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.setexperience.others", false, true)) {
                return;
            }
            Player p = r.searchPlayer(args[1]);
            if (p == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return;
            }
            String amount = args[0];
            Integer x;
            if (r.isInt(amount.replace("l", "").replace("L", ""))) {
                x = Integer.parseInt(amount.replace("l", "").replace("L", ""));
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[0].replace("l", "").replace("L", ""));
                return;
            }
            r.normalize(x, 0, 999999);
            if (amount.endsWith("l") || amount.endsWith("L")) {
                p.setLevel(x);
                r.sendMes(cs, "experienceSet", "%Settype", r.mes("experienceSettypeLevels"), "%Player", p.getName(), "%Experience", x);
            } else {
                XpUtil.setTotalExp(p, x);
                r.sendMes(cs, "experienceSet", "%Settype", r.mes("experienceSettypeExperience"), "%Player", p.getName(), "%Experience", x);
                r.sendMes(cs, "experienceTip", "%Command", "/setexperience " + x + "L " + p.getName());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 1) {
            return null;
        }
        return new ArrayList<>();
    }
}
