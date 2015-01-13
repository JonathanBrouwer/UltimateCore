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
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdFly implements UltimateCommand {

    @Override
    public String getName() {
        return "fly";
    }

    @Override
    public String getPermission() {
        return "uc.fly";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            if (!(r.isPlayer(cs))) {
                return;
            }
            if (!r.perm(cs, "uc.fly", false, true)) {
                return;
            }
            Player p = (Player) cs;
            if (p.getAllowFlight() == true) {
                p.setAllowFlight(false);
                r.sendMes(cs, "flySelf", "%Status", r.mes("flyOff"));
            } else {
                p.setAllowFlight(true);
                p.setFlySpeed(0.1F);
                r.sendMes(cs, "flySelf", "%Status", r.mes("flyOn"));
            }
        } else {
            if (!r.perm(cs, "uc.fly.others", false, true)) {
                return;
            }
            Player target = r.searchPlayer(args[0]);
            if (target != null) {
                if (target.getAllowFlight() == true) {
                    target.setAllowFlight(false);
                    r.sendMes(target, "flyOthersOther", "%Status", r.mes("flyOff"), "%Player", cs.getName());
                    r.sendMes(cs, "flyOthersSelf", "%Status", r.mes("flyOff"), "%Player", target.getName());
                } else {
                    target.setAllowFlight(true);
                    r.sendMes(target, "flyOthersOther", "%Status", r.mes("flyOn"), "%Player", cs.getName());
                    r.sendMes(cs, "flyOthersSelf", "%Status", r.mes("flyOn"), "%Player", target.getName());
                }
            } else {
                r.sendMes(cs, "PlayerNotFound", "%Player", args[0]);
            }

        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
