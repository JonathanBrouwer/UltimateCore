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
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdTeleportall implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportall";
    }

    @Override
    public String getPermission() {
        return "uc.teleportall";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpall");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.teleportall", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!UC.getPlayer(pl).hasTeleportEnabled() && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                    r.sendMes(cs, "teleportDisabled", "%Player", pl.getName());
                    continue;
                }
                LocationUtil.teleport(pl, p.getLocation(), TeleportCause.COMMAND, true, false);
            }
            LocationUtil.playEffect(null, p.getLocation());
            r.sendMes(cs, "teleportallSelf");
        } else {
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!UC.getPlayer(pl).hasTeleportEnabled() && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                    r.sendMes(cs, "teleportDisabled", "%Player", pl.getName());
                    continue;
                }
                LocationUtil.teleport(pl, t.getLocation(), TeleportCause.COMMAND, true, false);
            }
            LocationUtil.playEffect(null, t.getLocation());
            r.sendMes(cs, "teleportallOthers", "%Player", t.getName());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
