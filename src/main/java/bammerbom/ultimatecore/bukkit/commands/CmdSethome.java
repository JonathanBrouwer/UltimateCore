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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CmdSethome implements UltimateCommand {

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getPermission() {
        return "uc.selhome";
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
        Player p = (Player) cs;
        if (!r.perm(p, "uc.sethome", true, true)) {
            return;
        }
        if (r.checkArgs(args, 0) && args[0].contains(":")) {
            if (!r.perm(p, "uc.sethome.others", true, true)) {
                return;
            }
            OfflinePlayer t = r.searchOfflinePlayer(args[0].split(":")[0]);
            if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0].split(":")[0]);
                return;
            }
            List<String> homes = UC.getPlayer(t).getHomeNames();
            if (homes.contains(args[0])) {
                r.sendMes(cs, "sethomeMoved", "%Home", args[0]);
            } else {
                r.sendMes(cs, "sethomeSet", "%Home", args[0]);
            }
            if (!homes.contains(args[0].toLowerCase().split(":")[1])) {
                homes.add(args[0].toLowerCase().split(":")[1]);
            }
            UC.getPlayer(t).addHome(args[0].toLowerCase().split(":")[1], p.getLocation());
            return;
        }
        Set<String> multihomes = r.getCnfg().getConfigurationSection("Command.HomeLimits").getKeys(false);
        Integer limit = 1;
        if (multihomes != null) {
            for (String s : multihomes) {
                if (r.perm(cs, "uc.sethome." + s.toLowerCase(), false, false)) {
                    if (limit < r.getCnfg().getInt("Command.HomeLimits." + s)) {
                        limit = r.getCnfg().getInt("Command.HomeLimits." + s);
                    }
                }
            }
        }
        if (r.perm(cs, "uc.sethome.unlimited", false, false)) {
            limit = 999999;
        }
        List<String> homes = UC.getPlayer(p).getHomeNames();
        String name = r.checkArgs(args, 0) ? args[0] : "home";
        if (homes.size() >= limit && !homes.contains(name.toLowerCase())) {
            r.sendMes(cs, "sethomeMax", "%Limit", limit);
            return;
        }
        if (homes.contains(name)) {
            r.sendMes(cs, "sethomeMoved", "%Home", name);
        } else {
            r.sendMes(cs, "sethomeSet", "%Home", name);
        }
        if (!homes.contains(name.toLowerCase())) {
            homes.add(name.toLowerCase());
        }
        UC.getPlayer(p).addHome(name.toLowerCase(), p.getLocation());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
