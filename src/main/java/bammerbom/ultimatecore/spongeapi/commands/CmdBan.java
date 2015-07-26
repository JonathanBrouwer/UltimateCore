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
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UPlayer;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdBan implements UltimateCommand {

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getPermission() {
        return "uc.ban";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            r.sendMes(cs, "banUsage");
            return;
        }
        OfflinePlayer banp = r.searchOfflinePlayer(args[0]);
        if (banp == null || (!banp.hasPlayedBefore() && !banp.isOnline())) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        Long time = 0L;
        String reason = r.mes("banDefaultReason");
        if (r.checkArgs(args, 1) == false) {
        } else if (DateUtil.parseDateDiff(args[1]) == -1) {
            reason = r.getFinalArg(args, 1);
        } else {
            time = DateUtil.parseDateDiff(args[1]);
            if (r.checkArgs(args, 2) == true) {
                reason = r.getFinalArg(args, 2);
            }
        }
        String timen = DateUtil.format(time);
        if (time == 0) {
            timen = r.mes("banForever");
        } else {
            timen = "" + timen;
        }
        if (!r.perm(cs, "uc.ban.time", false, false) && !r.perm(cs, "uc.ban", false, false) && time <= 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (!r.perm(cs, "uc.ban.perm", false, false) && !r.perm(cs, "uc.ban", false, false) && !(time <= 0L)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        String msg = r.mes("banFormat").replace("%Time", timen).replace("%Reason", reason);
        if (banp.isOnline()) {
            banp.getPlayer().kickPlayer(msg);
        }
        UPlayer pl = UC.getPlayer(banp);
        pl.ban(time, reason, cs);
        if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
            Bukkit.broadcastMessage(r.mes("banBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Banned", r
                    .getDisplayName(banp), "%Time", timen, "%Reason", reason));
        } else {
            r.sendMes(cs, "banBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Banned", r
                    .getDisplayName(banp), "%Time", timen, "%Reason", reason);
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
