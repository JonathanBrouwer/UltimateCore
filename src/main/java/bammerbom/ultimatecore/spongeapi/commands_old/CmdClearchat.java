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

import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdClearchat implements UltimateCommand {

    @Override
    public String getName() {
        return "clearchat";
    }

    @Override
    public String getPermission() {
        return "uc.clearchat";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cleanchat");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            if (!r.perm(cs, "uc.clearchat", false, true) && !r.perm(cs, "uc.clearchat.everyone", false, true)) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            for (Player p : r.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    p.sendMessage("");
                }
            }
        } else {
            Player pl = r.searchPlayer(args[0]);
            if (pl == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            if (!r.perm(cs, "uc.clearchat", false, true) && !r.perm(cs, "uc.clearchat.player.others", false, true) && !(pl.equals(cs) && r.perm(cs, "uc.clearchat.player", true, true))) {
                r.sendMes(cs, "noPermissions");
                return;
            }
            for (int i = 0; i < 100; i++) {
                pl.sendMessage("");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
