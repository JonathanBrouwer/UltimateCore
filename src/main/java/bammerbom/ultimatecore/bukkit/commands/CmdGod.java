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

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdGod implements UltimateCommand {

    @Override
    public String getName() {
        return "god";
    }

    @Override
    public String getPermission() {
        return "uc.god";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.god", false, true)) {
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (UC.getPlayer(p).isGod()) {
                UC.getPlayer(p).setGod(false);
                r.sendMes(cs, "godSelf", "%Status", r.mes("off"));
            } else {
                UC.getPlayer(p).setGod(true);
                r.sendMes(cs, "godSelf", "%Status", r.mes("on"));
            }
            return;
        }
        if (DateUtil.parseDateDiff(args[0], true) >= 1) {
            Long t = DateUtil.parseDateDiff(args[0], true);
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            UC.getPlayer(p).setGod(true, t);
            r.sendMes(cs, "godSelfT", "%Status", r.mes("on"), "%Time", DateUtil.format(t + System.currentTimeMillis()));
            return;
        }
        if (!r.perm(cs, "uc.god.others", false, true)) {
            return;
        }
        OfflinePlayer banp = r.searchOfflinePlayer(args[0]);
        if (banp != null && (banp.isOnline() || banp.hasPlayedBefore())) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        /*//Info
         if(r.checkArgs(args, 1) == false){
         UC.getPlayer(banp).setGod(!UC.getPlayer(banp).isGod());
         r.sendMes(cs, "godOthersSelfMessage", "%Player", banp.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")));
         if(banp.isOnline()) r.sendMes((Player) banp, "godOthersOtherMessage", "%Player", banp.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")));
         return;
         }*/
        Long time = 0L;
        if (r.checkArgs(args, 1) && DateUtil.parseDateDiff(args[1], true) >= 1) {
            time = DateUtil.parseDateDiff(args[1], true);
        }
        //Permcheck
        if (!r.perm(cs, "uc.god.time", false, false) && !r.perm(cs, "uc.god", false, false) && time == 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (!r.perm(cs, "uc.god.perm", false, false) && !r.perm(cs, "uc.god", false, false) && time != 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        UC.getPlayer(banp).setGod(!UC.getPlayer(banp).isGod(), time);
        if (time == 0L) {
            r.sendMes(cs, "godOthersSelfMessage", "%Player", banp.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")));
            if (banp.isOnline()) {
                r.sendMes((Player) banp, "godOthersOtherMessage", "%Player", banp.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")));
            }
        } else {
            r.sendMes(cs, "godOthersSelfMessageT", "%Player", banp.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")), "%Time", DateUtil.format(time + System.currentTimeMillis()));
            if (banp.isOnline()) {
                r.sendMes((Player) banp, "godOthersOtherMessageT", "%Player", cs.getName(), "%Status", (UC.getPlayer(banp).isGod() ? r.mes("on") : r.mes("off")), "%Time", DateUtil.format(time + System.currentTimeMillis()));
            }
        }
        return;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
