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
import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdIp implements UltimateCommand {

    @Override
    public String getName() {
        return "ip";
    }

    @Override
    public String getPermission() {
        return "uc.ip";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.perm(cs, "uc.ip.server", false, false) && !r.perm(cs, "uc.ip", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            r.sendMes(cs, "ipServer", "%IP", Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort());
        } else {
            if (!r.perm(cs, "uc.ip.player", false, false) && !r.perm(cs, "uc.ip", false, false)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            OfflinePlayer p = r.searchOfflinePlayer(args[0]);
            if (p == null || UC.getPlayer(p).getLastIp() == null || UC.getPlayer(p).getLastHostname() == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            r.sendMes(cs, "ipPlayer1", "%Player", r.getDisplayName(p), "%Hostname", UC.getPlayer(p).getLastHostname());
            r.sendMes(cs, "ipPlayer2", "%Player", r.getDisplayName(p), "%IP", UC.getPlayer(p).getLastIp());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
