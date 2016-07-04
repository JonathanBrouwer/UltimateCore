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
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdSpeed implements UltimateCommand {

    public static Double getSpeed(Double f, Boolean fly) {
        double userSpeed;
        userSpeed = f;
        if (userSpeed > 10.0) {
            userSpeed = 10.0;
        } else if (userSpeed < 1.0E-004) {
            userSpeed = 1.0E-004;
        }

        double defaultSpeed = fly ? 0.1 : 0.2;
        double maxSpeed = 1.0;
        if (userSpeed < 1.0) {
            return defaultSpeed * userSpeed;
        }
        double ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
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
    public String getUsage() {
        return "/<command> <0-10> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set your fly and walk speed.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.speed", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "speedUsage");
            return CommandResult.empty();
        }
        if (!r.isFloat(args[0])) {
            if (r.checkArgs(args, 1) && r.isFloat(args[1])) {
                run(cs, label, new String[]{args[1], args[0]});
                return CommandResult.empty();
            }
            r.sendMes(cs, "speedUsage");
            return CommandResult.empty();
        }
        Double d = Double.parseDouble(args[0]);
        if (d > 10 || d < 0) {
            r.sendMes(cs, "speedUsage");
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 1)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            p.offer(Keys.FLYING_SPEED, getSpeed(d, true));
            p.offer(Keys.WALKING_SPEED, getSpeed(d, false));
            r.sendMes(cs, "speedSelf", "%Speed", args[0]);
        } else {
            Player t = r.searchPlayer(args[1]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return CommandResult.empty();
            }
            if (!r.perm(cs, "uc.speed.others", true)) {
                return CommandResult.empty();
            }
            t.offer(Keys.FLYING_SPEED, getSpeed(d, true));
            t.offer(Keys.WALKING_SPEED, getSpeed(d, false));
            r.sendMes(cs, "speedOtherSelf", "%Player", t.getName(), "%Speed", args[0]);
            r.sendMes(t, "speedOtherOthers", "%Speed", args[0]);
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (curn == 1) {
            return null;
        }
        return new ArrayList<>();
    }
}
