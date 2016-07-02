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

public class CmdMute implements UltimateCommand {

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getPermission() {
        return "uc.mute";
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
//        return Arrays.asList();
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.checkArgs(args, 0)) {
//            r.sendMes(cs, "muteUsage");
//            return CommandResult.empty();
//        }
//        OfflinePlayer banp = r.searchGameProfile(args[0]);
//        if (banp == null || (!banp.hasPlayedBefore() && !banp.isOnline())) {
//            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
//            return CommandResult.empty();
//        }
//        if (UC.getPlayer(banp).isMuted()) {
//            r.sendMes(cs, "muteAlreadyMuted", "%Player", r.getDisplayName(banp));
//            return CommandResult.empty();
//        }
//        Long time = 0L;
//        String reason = r.mes("muteDefaultReason");
//        //Info
//        if (!r.checkArgs(args, 1)) {
//        } else if (DateUtil.parseDateDiff(args[1]) != -1) {
//            time = DateUtil.parseDateDiff(args[1]);
//            if (r.checkArgs(args, 2)) {
//                reason = r.getFinalArg(args, 2);
//            }
//        } else if (DateUtil.parseDateDiff(args[1]) == -1) {
//            reason = r.getFinalArg(args, 1);
//        }
//        //Permcheck
//        if (!r.perm(cs, "uc.mute.time", false, false) && !r.perm(cs, "uc.mute", false, false) && time == 0L) {
//            r.sendMes(cs, "noPermissions");
//            return CommandResult.empty();
//        }
//        if (!r.perm(cs, "uc.mute.perm", false, false) && !r.perm(cs, "uc.mute", false, false) && time != 0L) {
//            r.sendMes(cs, "noPermissions");
//            return CommandResult.empty();
//        }
//        UC.getPlayer(banp).setMuted(true, time, reason);
//        r.sendMes(cs, "muteMessage", "%Player", r.getDisplayName(banp));
//        if (banp.isOnline()) {
//            Player banp2 = (Player) banp;
//            r.sendMes(banp2, "muteTarget");
//            if (time > 0L) {
//                r.sendMes(banp2, "muteTime", "%Time", DateUtil.format(UC.getPlayer(banp).getMuteTimeLeft()));
//            }
//            r.sendMes(banp2, "muteReason", "%Reason", reason);
//        }
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        return null;
//    }
}
