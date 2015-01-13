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

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdNick implements UltimateCommand {

    @Override
    public String getName() {
        return "nick";
    }

    @Override
    public String getPermission() {
        return "uc.nick";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.nick", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "nickUsage");
            return;
        }
        Boolean o = false;
        if (r.checkArgs(args, 0) && args[0].equalsIgnoreCase("off")) {
            Player t;
            if (r.checkArgs(args, 1)) {
                o = true;
                t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "PlayerNotFound", "%Player", args[1]);
                    return;
                }
            } else {
                if (!r.isPlayer(cs)) {
                    return;
                }
                t = (Player) cs;
            }
            if (o && !r.perm(cs, "uc.nick.others", false, true)) {
                return;
            }
            r.sendMes(cs, "nickMessage", "%Name", r.mes("nickOff"), "%Player", t.getName());
            if (o) {
                r.sendMes(t, "nickMessageOthers", "%Player", cs.getName(), "%Name", r.mes("nickOff"));
            }
            UC.getPlayer(t).setNick(null);
            return;
        }
        Player t;
        if (r.checkArgs(args, 1)) {
            o = true;
            t = r.searchPlayer(args[1]);
            if (t == null) {
                r.sendMes(cs, "PlayerNotFound", "%Player", args[1]);
                return;
            }
        } else {
            if (!r.isPlayer(cs)) {
                return;
            }
            t = (Player) cs;
        }
        if (o && !r.perm(cs, "uc.nick.others", false, true)) {
            return;
        }
        String name = args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", "");
        if (r.perm(cs, "uc.nick.colors", false, false)) {
            name = ChatColor.translateAlternateColorCodes('&', name);
        }
        if (!ChatColor.stripColor(name.replaceAll(" ", "").replaceAll("�", "").replaceAll("&y", "").replaceAll("_", "").replaceAll("[a-zA-Z0-9]", "")).equalsIgnoreCase("")) {
            r.sendMes(cs, "nickNonAlpha");
            return;
        }
        name = ChatColor.translateAlternateColorCodes('&', args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", ""));
        UC.getPlayer(t).setNick(name);
        r.sendMes(cs, "nickMessage", "%Name", name, "%Player", t.getName());
        if (o) {
            r.sendMes(t, "nickMessageOthers", "%Player", cs.getName(), "%Name", name);
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
