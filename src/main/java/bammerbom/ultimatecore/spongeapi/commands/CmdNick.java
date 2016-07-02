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
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdNick implements UltimateCommand {

    @Override
    public String getName() {
        return "nick";
    }

    @Override
    public String getPermission() {
        return "uc.nick";
    }

    @Override
    public String getUsage() {
        return "/<command> <Name>/off [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set a player's nickname");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("nickname");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.nick", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "nickUsage");
            return CommandResult.empty();
        }
        Boolean o = false;
        if (r.checkArgs(args, 0) && args[0].equalsIgnoreCase("off")) {
            Player t;
            if (r.checkArgs(args, 1)) {
                o = true;
                t = r.searchPlayer(args[1]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
            } else {
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                t = (Player) cs;
            }
            if (o && !r.perm(cs, "uc.nick.others", true)) {
                return CommandResult.empty();
            }
            r.sendMes(cs, "nickMessage", "%Name", r.mes("nickOff"), "%Player", t.getName());
            if (o) {
                r.sendMes(t, "nickMessageOthers", "%Player", r.getDisplayName(cs), "%Name", r.mes("nickOff"));
            }
            t.offer(Keys.DISPLAY_NAME, Text.of(t.getName()));
            UC.getPlayer(t).setNick(null);
            return CommandResult.empty();
        }
        Player t;
        if (r.checkArgs(args, 1)) {
            o = true;
            t = r.searchPlayer(args[1]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return CommandResult.empty();
            }
        } else {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            t = (Player) cs;
        }
        if (o && !r.perm(cs, "uc.nick.others", true)) {
            return CommandResult.empty();
        }
        String name = args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", "");
        if (r.perm(cs, "uc.nick.colors", false)) {
            name = TextColorUtil.translateAlternate(name);
        }
        if (!TextColorUtil.strip(name).replaceAll("&y", "").replaceAll("_", "").replaceAll("[a-zA-Z0-9]", "").equalsIgnoreCase("")) {
            r.sendMes(cs, "nickNonAlpha");
            return CommandResult.empty();
        }
        for (Player pl : r.getOnlinePlayers()) {
            if (pl.equals(t)) {
                continue;
            }
            if (pl.getName().equalsIgnoreCase(TextColorUtil.strip(name).replaceAll("&y", "")) || UC.getPlayer(pl).getDisplayName().toPlain().equalsIgnoreCase(TextColorUtil.strip(name).replaceAll
                    ("&y", ""))) {
                r.sendMes(cs, "nickAlreadyInUse");
                return CommandResult.empty();
            }
        }
        name = TextColorUtil.translateAlternate(args[0].replaceAll("&k", "").replaceAll("%n", "").replaceAll("&l", ""));
        UC.getPlayer(t).setNick(name);
        r.sendMes(cs, "nickMessage", "%Name", name, "%Player", t.getName());
        if (o) {
            r.sendMes(t, "nickMessageOthers", "%Player", r.getDisplayName(cs), "%Name", name);
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
