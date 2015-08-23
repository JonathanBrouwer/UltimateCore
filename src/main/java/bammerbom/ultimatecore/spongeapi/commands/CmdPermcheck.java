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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdPermcheck implements UltimateCommand {

    @Override
    public String getName() {
        return "permcheck";
    }

    @Override
    public String getPermission() {
        return "uc.permcheck";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("permissioncheck");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.permcheck", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "permcheckUsage");
        } else if (!r.checkArgs(args, 1)) {
            if (r.perm(cs, args[0], false, false)) {
                r.sendMes(cs, "permcheckMessageTrue", "%Player", r.getDisplayName(cs), "%Permission", args[0]);
            } else {
                r.sendMes(cs, "permcheckMessageFalse", "%Player", r.getDisplayName(cs), "%Permission", args[0]);
            }
        } else {
            if (!r.perm(cs, "uc.permcheck.others", false, true)) {
                return;
            }
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            if (r.perm(t, args[1], false, false)) {
                r.sendMes(cs, "permcheckMessageTrue", "%Player", t.getName(), "%Permission", args[1]);
            } else {
                r.sendMes(cs, "permcheckMessageFalse", "%Player", t.getName(), "%Permission", args[1]);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
