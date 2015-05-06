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
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;

public class CmdSpawn implements UltimateCommand {

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getPermission() {
        return "uc.spawn";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.spawn", true, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (UC.getServer().getSpawn() == null) {
                LocationUtil.teleport(p, p.getWorld().getSpawnLocation(), TeleportCause.COMMAND, false, true);
            } else {
                LocationUtil.teleport(p, UC.getServer().getSpawn(), TeleportCause.COMMAND, false, true);
            }
            r.sendMes(cs, "spawnMessage");
        } else {
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            if (UC.getServer().getSpawn() == null) {
                LocationUtil.teleport(t, t.getWorld().getSpawnLocation(), TeleportCause.COMMAND, false, false);
            } else {
                LocationUtil.teleport(t, UC.getServer().getSpawn(), TeleportCause.COMMAND, false, false);
            }
            r.sendMes(cs, "spawnMessageOtherSelf", "%Player", UC.getPlayer(t).getDisplayName());
            r.sendMes(t, "spawnMessageOtherOthers", "%Player", cs.getName());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
