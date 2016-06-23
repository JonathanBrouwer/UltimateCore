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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdAfk implements UltimateCommand {

    @Override
    public String getName() {
        return "afk";
    }

    @Override
    public String getPermission() {
        return "uc.afk";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player]/[Reason]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Marks you as away-from-keyboard.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("away");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!(r.checkArgs(args, 0) && r.searchPlayer(args[0]) != null)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.afk", true)) {
                return CommandResult.empty();
            }
            String message = r.checkArgs(args, 0) ? r.getFinalArg(args, 0) : null;
            if (!UC.getPlayer(p).isAfk()) {
                if (message != null) {
                    Sponge.getServer().getBroadcastChannel().send(r.mes("afkAfkReason", "%Player", UC.getPlayer(p).getDisplayName(), "%Message", message));
                } else {
                    Sponge.getServer().getBroadcastChannel().send(r.mes("afkAfk", "%Player", UC.getPlayer(p).getDisplayName()));
                }
                UC.getPlayer(p).setAfk(true);
                UC.getPlayer(p).setAfkMessage(message);
            } else {
                Sponge.getServer().getBroadcastChannel().send(r.mes("afkUnafk", "%Player", UC.getPlayer(p).getDisplayName()));
                UC.getPlayer(p).setAfk(false);
                UC.getPlayer(p).setAfkMessage(null);
            }
            return CommandResult.success();
        } else {
            if (!r.perm(cs, "uc.afk.others", true)) {
                return CommandResult.empty();
            }
            if (r.searchPlayer(args[0]).isPresent()) {
                Player t = r.searchPlayer(args[0]).get();
                String message = r.checkArgs(args, 1) ? r.getFinalArg(args, 1) : null;
                if (!UC.getPlayer(t).isAfk()) {
                    if (message != null) {
                        Sponge.getServer().getBroadcastChannel().send(r.mes("afkAfkReason", "%Player", UC.getPlayer(t).getDisplayName(), "%Message", message));
                    } else {
                        Sponge.getServer().getBroadcastChannel().send(r.mes("afkAfk", "%Player", UC.getPlayer(t).getDisplayName()));
                    }
                    UC.getPlayer(t).setAfk(true);
                    UC.getPlayer(t).setAfkMessage(message);
                } else {
                    Sponge.getServer().getBroadcastChannel().send(r.mes("afkUnafk", "%Player", UC.getPlayer(t).getDisplayName()));
                    UC.getPlayer(t).setAfk(false);
                    UC.getPlayer(t).setAfkMessage(null);
                }
            } else {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            }
            return CommandResult.success();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
