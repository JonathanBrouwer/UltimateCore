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
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdRealname implements UltimateCommand {

    @Override
    public String getName() {
        return "realname";
    }

    @Override
    public String getPermission() {
        return "uc.realname";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.realname", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "realnameUsage");
            return;
        }
        OfflinePlayer t = null;
        //Search online
        String lowerName = args[0].toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : r.getOnlinePlayers()) {
            if (UC.getPlayer(player).getNick() == null) {
                continue;
            }
            String s = ChatColor.stripColor(UC.getPlayer(player).getNick()).toLowerCase();
            if (s.startsWith(lowerName)) {
                int curDelta = s.length() - lowerName.length();
                if (curDelta < delta) {
                    t = player;
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }
        if (t == null) {
            for (OfflinePlayer player : r.getOfflinePlayers()) {
                if (UC.getPlayer(player).getNick() == null) {
                    continue;
                }
                String s = ChatColor.stripColor(UC.getPlayer(player).getNick()).toLowerCase();
                if (s.toLowerCase().startsWith(lowerName)) {
                    int curDelta = s.length() - lowerName.length();
                    if (curDelta < delta) {
                        t = player;
                        delta = curDelta;
                    }
                    if (curDelta == 0) {
                        break;
                    }
                }
            }
        }
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        r.sendMes(cs, "realnameMessage", "%Nick", UC.getPlayer(t).getNick(), "%Name", t.getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
