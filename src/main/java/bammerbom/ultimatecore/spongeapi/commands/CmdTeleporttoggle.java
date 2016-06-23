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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdTeleporttoggle implements UltimateCommand {

    @Override
    public String getName() {
        return "teleporttoggle";
    }

    @Override
    public String getPermission() {
        return "uc.teleporttoggle";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tptoggle");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.teleporttoggle", true, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            Player p = (Player) cs;
            UC.getPlayer(p).setTeleportEnabled(!UC.getPlayer(p).hasTeleportEnabled());
            r.sendMes(cs, "teleporttoggleMessage", "%Enabled", UC.getPlayer(p).hasTeleportEnabled() ? r.mes("on") : r.mes("off"));
        } else {
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            if (!r.perm(cs, "uc.teleporttoggle.others", false, true)) {
                UC.getPlayer(t).setTeleportEnabled(!UC.getPlayer(t).hasTeleportEnabled());
                r.sendMes(cs, "teleporttoggleOthersSelf", "%Player", t.getName(), "%Enabled", UC.getPlayer(t).hasTeleportEnabled() ? r.mes("enabled") : r.mes("disabled"));
                r.sendMes(t, "teleporttoggleOthersOthers", "%Player", r.getDisplayName(cs), "%Enabled", UC.getPlayer(t).hasTeleportEnabled() ? r.mes("enabled") : r.mes("disabled"));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
