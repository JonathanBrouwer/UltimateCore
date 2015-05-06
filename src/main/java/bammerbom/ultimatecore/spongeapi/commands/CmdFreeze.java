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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdFreeze implements UltimateCommand {

    @Override
    public String getName() {
        return "freeze";
    }

    @Override
    public String getPermission() {
        return "uc.freeze";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            r.sendMes(cs, "freezeUsage");
            return;
        }
        OfflinePlayer t = r.searchOfflinePlayer(args[0]);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        Long time = 0L;
        //Info
        if (r.checkArgs(args, 1) == false) {
        } else if (DateUtil.parseDateDiff(args[1]) != -1) {
            time = DateUtil.parseDateDiff(args[1]);
        }
        //Permcheck
        if (!r.perm(cs, "uc.freeze.time", false, false) && !r.perm(cs, "uc.freeze", false, false) && time == 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (!r.perm(cs, "uc.freeze.perm", false, false) && !r.perm(cs, "uc.freeze", false, false) && time != 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        UC.getPlayer(t).setFrozen(true, time);
        if (time == 0L) {
            r.sendMes(cs, "freezeMessage", "%Player", t.getName());
        } else {
            r.sendMes(cs, "freezeMessageTime", "%Player", t.getName(), "%Time", DateUtil.format(time));
        }
        if (t.isOnline()) {
            Player banp2 = (Player) t;
            r.sendMes(banp2, "freezeTarget");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
