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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdPermcheck implements UltimateCommand {

    @Override
    public String getName() {
        return "permcheck";
    }

    @Override
    public String getPermission() {
        return "uc.permcheck";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player] <Permission>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Check if a certain player has a permission");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("permissioncheck");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.permcheck", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "permcheckUsage");
            return CommandResult.empty();
        } else if (!r.checkArgs(args, 1)) {
            if (r.perm(cs, args[0], false)) {
                r.sendMes(cs, "permcheckMessageTrue", "%Player", r.getDisplayName(cs), "%Permission", args[0]);
            } else {
                r.sendMes(cs, "permcheckMessageFalse", "%Player", r.getDisplayName(cs), "%Permission", args[0]);
            }
        } else {
            if (!r.perm(cs, "uc.permcheck.others", true)) {
                return CommandResult.empty();
            }
            Player t = r.searchPlayer(args[0]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            if (r.perm(t, args[1], false)) {
                r.sendMes(cs, "permcheckMessageTrue", "%Player", t.getName(), "%Permission", args[1]);
            } else {
                r.sendMes(cs, "permcheckMessageFalse", "%Player", t.getName(), "%Permission", args[1]);
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
