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
package bammerbom.ultimatecore.spongeapi.commands_old;

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;

public class CmdWarp implements UltimateCommand {

    @Override
    public String getName() {
        return "warp";
    }

    @Override
    public String getPermission() {
        return "uc.warp";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("warps", "warplist");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            if (r.perm(cs, "uc.warplist", true, true) == false) {
                return;
            }
            List<String> warps = UC.getServer().getWarpNames();
            if (warps == null || warps.isEmpty()) {
                r.sendMes(cs, "warpNoWarpsFound");
                return;
            }
            StringBuilder warplist = new StringBuilder();
            Integer cur = 0;
            String result;
            for (int i = 0; i < warps.size(); i++) {
                warplist.append(warps.get(cur) + ", ");
                cur++;

            }
            result = warplist.substring(0, warplist.length() - 2);
            r.sendMes(cs, "warpWarps", "%Warps", result);
        } else {
            if (!(r.isPlayer(cs))) {
                return;
            }
            //Exist
            Player p = (Player) cs;
            if (r.perm(p, "uc.warp", true, false) == false && r.perm(p, "uc.warp." + args[0], true, false) == false) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            if (UC.getServer().getWarp(args[0]) == null) {
                r.sendMes(cs, "warpNotExist", "%Warp", args[0]);
                return;
            }

            //Teleport
            Location loc = UC.getServer().getWarp(args[0]);
            LocationUtil.teleportUnsafe(p, loc, TeleportCause.COMMAND, true);
            r.sendMes(cs, "warpMessage", "%Warp", args[0]);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return UC.getServer().getWarpNames();
    }
}
