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
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDelhome implements UltimateCommand {

    @Override
    public String getName() {
        return "delhome";
    }

    @Override
    public String getPermission() {
        return "uc.delhome";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "delhomeUsage");
            return;
        } else {
            Player p = (Player) cs;
            if (r.perm(p, "uc.delhome", true, true) == false) {
                return;
            }
            if (args[0].contains(":")) {
                if (r.perm(p, "uc.delhome.others", true, true) == false) {
                    return;
                }
                OfflinePlayer t = r.searchOfflinePlayer(args[0].split(":")[0]);
                if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                List<String> homes = UC.getPlayer(t).getHomeNames();
                if (!homes.contains(args[0].toLowerCase().split(":")[1].toLowerCase())) {
                    r.sendMes(cs, "homeNotExist", "%Home", args[0]);
                    return;
                }
                UC.getPlayer(t).removeHome(args[0].toLowerCase().split(":")[1]);
                r.sendMes(cs, "delhomeMessage", "%Home", args[0]);
                return;
            }
            List<String> homes = UC.getPlayer(p).getHomeNames();
            if (!homes.contains(args[0].toLowerCase())) {
                r.sendMes(cs, "homeNotExist", "%Home", args[0]);
                return;
            }
            UC.getPlayer(p).removeHome(args[0].toLowerCase());
            r.sendMes(cs, "delhomeMessage", "%Home", args[0]);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
