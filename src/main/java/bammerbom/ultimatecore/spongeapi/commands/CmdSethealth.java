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

import java.util.Arrays;
import java.util.List;

public class CmdSethealth implements UltimateCommand {

    @Override
    public String getName() {
        return "sethealth";
    }

    @Override
    public String getPermission() {
        return "uc.sethealth";
    }

    @Override
    public String getUsage() {
        return "/<command> <Health> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set the amount of health of a player.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setlives");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.sethealth", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            p.offer(Keys.HEALTH, p.get(Keys.MAX_HEALTH).orElse(20.0));
            r.sendMes(cs, "sethealthMessage", "%Player", r.getDisplayName(p), "%Health", "20.0");
        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            if (r.isDouble(args[0])) {
                Double d = Double.parseDouble(args[0]);
                d = r.normalize(d, 0.0, 2048.0);
                Player p = (Player) cs;
                if (p.get(Keys.MAX_HEALTH).orElse(20.0) < d) {
                    p.offer(Keys.MAX_HEALTH, 20.0);
                }
                p.offer(Keys.HEALTH, 20.0);
                r.sendMes(cs, "sethealthMessage", "%Player", r.getDisplayName(p), "%Health", d);
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[0]);
            }
        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.sethealth.others", true)) {
                return CommandResult.empty();
            }
            if (r.isDouble(args[0])) {
                Double d = Double.parseDouble(args[0]);
                d = r.normalize(d, 0.0, 2048.0);
                Player t = r.searchPlayer(args[1]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
                if (t.get(Keys.MAX_HEALTH).orElse(20.0) < d) {
                    t.offer(Keys.MAX_HEALTH, 20.0);
                }
                t.offer(Keys.HEALTH, 20.0);
                r.sendMes(cs, "sethealthMessage", "%Player", t.getName(), "%Health", d);
                r.sendMes(t, "sethealthOthers", "%Player", r.getDisplayName(cs), "%Health", d);
            } else if (r.isDouble(args[1])) {
                Double d = Double.parseDouble(args[1]);
                d = r.normalize(d, 0.0, 999999.0);
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                if (t.get(Keys.MAX_HEALTH).orElse(20.0) < d) {
                    t.offer(Keys.MAX_HEALTH, 20.0);
                }
                t.offer(Keys.HEALTH, 20.0);
                r.sendMes(cs, "sethealthMessage", "%Player", t.getName(), "%Health", d);
                r.sendMes(t, "sethealthOthers", "%Player", r.getDisplayName(cs), "%Health", d);
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[0]);
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
