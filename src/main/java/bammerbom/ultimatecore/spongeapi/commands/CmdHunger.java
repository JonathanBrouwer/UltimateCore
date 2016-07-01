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

public class CmdHunger implements UltimateCommand {

    @Override
    public String getName() {
        return "hunger";
    }

    @Override
    public String getPermission() {
        return "uc.hunger";
    }

    @Override
    public String getUsage() {
        return "/<command> <Amount> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Remove hunger bars from a player.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.hunger", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            r.sendMes(cs, "hungerUsage");
        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            if (r.isInt(args[0])) {
                Integer d = Integer.parseInt(args[0]);
                Player p = (Player) cs;
                p.offer(Keys.FOOD_LEVEL, r.normalize(p.get(Keys.FOOD_LEVEL).orElse(20) - d, 0, 20));
                r.sendMes(cs, "hungerSelf", "%Player", r.getDisplayName(p), "%Hunger", args[0]);
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[0]);
            }
        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.hunger.others", true)) {
                return CommandResult.empty();
            }
            if (r.isInt(args[0])) {
                Integer d = Integer.parseInt(args[0]);
                Player t = r.searchPlayer(args[1]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
                t.offer(Keys.FOOD_LEVEL, r.normalize(t.get(Keys.FOOD_LEVEL).orElse(20) - d, 0, 20));
                r.sendMes(cs, "hungerOthers", "%Player", t.getName(), "%Hunger", args[0]);
            } else if (r.isInt(args[1])) {
                Integer d = Integer.parseInt(args[1]);
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                t.offer(Keys.FOOD_LEVEL, r.normalize(t.get(Keys.FOOD_LEVEL).orElse(20) - d, 0, 20));
                r.sendMes(cs, "hungerOthers", "%Player", t.getName(), "%Hunger", args[1]);
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
