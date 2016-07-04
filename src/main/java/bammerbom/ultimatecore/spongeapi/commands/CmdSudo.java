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

public class CmdSudo implements UltimateCommand {

    @Override
    public String getName() {
        return "sudo";
    }

    @Override
    public String getPermission() {
        return "uc.sudo";
    }

    @Override
    public String getUsage() {
        return "/<command> <Player> <Command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Force a player to run a command.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.sudo", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "sudoUsage");
            return CommandResult.empty();
        }
        Player t = r.searchPlayer(args[0]).orElse(null);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        if (r.perm(t, "uc.sudo.exempt", false) && !r.perm(cs, "uc.sudo.exempt.override", false)) {
            r.sendMes(cs, "sudoExempt");
            return CommandResult.empty();
        }
        final String[] arguments = new String[args.length - 1];
        if (arguments.length > 0) {
            System.arraycopy(args, 1, arguments, 0, args.length - 1);
        }
        final String command = r.getFinalArg(arguments, 0);
        r.sendMes(cs, "sudoMessage", "%Player", t.getName(), "%Command", command);
        try {
            t.getMessageChannel().send(Text.of("/" + command));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
