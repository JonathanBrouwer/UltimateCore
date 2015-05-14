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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdSpeed implements UltimateCommand {

    public static Float getSpeed(Float f, Boolean fly) {
        float userSpeed;
        userSpeed = f;
        if (userSpeed > 10.0F) {
            userSpeed = 10.0F;
        } else if (userSpeed < 1.0E-004F) {
            userSpeed = 1.0E-004F;
        }

        float defaultSpeed = fly ? 0.1F : 0.2F;
        float maxSpeed = 1.0F;
        if (userSpeed < 1.0F) {
            return defaultSpeed * userSpeed;
        }
        float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
        return ratio + defaultSpeed;
    }

    @Override
    public String getName() {
        return "speed";
    }

    @Override
    public String getPermission() {
        return "uc.speed";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.speed", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "speedUsage");
            return;
        }
        if (!r.isFloat(args[0])) {
            if (r.checkArgs(args, 1) && r.isFloat(args[1])) {
                run(cs, label, new String[]{args[1], args[0]});
                return;
            }
            r.sendMes(cs, "speedUsage");
            return;
        }
        Float d = Float.parseFloat(args[0]);
        if (d > 10 || d < 0) {
            r.sendMes(cs, "speedUsage");
            return;
        }
        if (r.checkArgs(args, 1) == false) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            p.setFlySpeed(getSpeed(d, true));
            p.setWalkSpeed(getSpeed(d, false));
            r.sendMes(cs, "speedSelf", "%Speed", args[0]);
        } else {
            Player t = r.searchPlayer(args[1]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return;
            }
            if (!r.perm(cs, "uc.speed.others", false, true)) {
                return;
            }
            t.setFlySpeed(getSpeed(d, true));
            t.setWalkSpeed(getSpeed(d, false));
            r.sendMes(cs, "speedOtherSelf", "%Player", t.getName(), "%Speed", args[0]);
            r.sendMes(t, "speedOtherOthers", "%Speed", args[0]);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 1) {
            return null;
        }
        return new ArrayList<>();
    }
}
