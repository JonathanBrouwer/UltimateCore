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
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAfk implements UltimateCommand {

    @Override
    public String getName() {
        return "afk";
    }

    @Override
    public String getPermission() {
        return "uc.afk";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("away");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.afk", true, true)) {
                return;
            }
            if (!UC.getPlayer(p).isAfk()) {
                Bukkit.broadcastMessage(r.mes("afkAfk", "%Player", UC.getPlayer(p).getDisplayName()));
                UC.getPlayer(p).setAfk(true);
            } else {
                Bukkit.broadcastMessage(r.mes("afkUnafk", "%Player", UC.getPlayer(p).getDisplayName()));
                UC.getPlayer(p).setAfk(false);
            }
        } else {
            if (!r.perm(cs, "uc.afk.others", false, true)) {
                return;
            }
            if (r.searchPlayer(args[0]) != null) {
                Player t = r.searchPlayer(args[0]);
                if (!UC.getPlayer(t).isAfk()) {
                    Bukkit.broadcastMessage(r.mes("afkAfk", "%Player", UC.getPlayer(t).getDisplayName()));
                    UC.getPlayer(t).setAfk(true);
                } else {
                    Bukkit.broadcastMessage(r.mes("afkUnafk", "%Player", UC.getPlayer(t).getDisplayName()));
                    UC.getPlayer(t).setAfk(false);
                }
            } else {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
