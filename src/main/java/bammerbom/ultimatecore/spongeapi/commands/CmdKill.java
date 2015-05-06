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

import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;
import java.util.List;

public class CmdKill implements UltimateCommand {

    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public String getPermission() {
        return "uc.kill";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("suicide");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            if (!r.isPlayer(cs)) {
                return;
            }
            if (!r.perm(cs, "uc.kill", true, true)) {
                return;
            }
            Player p = (Player) cs;
            p.setLastDamageCause(new EntityDamageEvent(p, DamageCause.SUICIDE, Double.MAX_VALUE));
            p.setHealth(0.0);
        } else {
            if (!r.perm(cs, "uc.kill.others", false, true)) {
                return;
            }
            Player target = r.searchPlayer(args[0]);
            if (target == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            } else {
                r.sendMes(target, "killTarget", "%Player", cs.getName());
                r.sendMes(cs, "killKiller", "%Player", target.getName());

                target.setLastDamageCause(new EntityDamageEvent(target, DamageCause.CUSTOM, Double.MAX_VALUE));
                target.setHealth(0.0);
            }

        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
