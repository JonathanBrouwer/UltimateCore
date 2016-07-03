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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdRealname implements UltimateCommand {

    @Override
    public String getName() {
        return "realname";
    }

    @Override
    public String getPermission() {
        return "uc.realname";
    }

    @Override
    public String getUsage() {
        return "/<command> <Nick>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Get the real name of a certain nick.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.realname", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "realnameUsage");
            return CommandResult.empty();
        }
        GameProfile t = null;
        //Search online
        String lowerName = args[0].toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : r.getOnlinePlayers()) {
            if (UC.getPlayer(player).getNick() == null) {
                continue;
            }
            String s = TextColorUtil.strip(UC.getPlayer(player).getNick().toPlain()).toLowerCase();
            if (s.startsWith(lowerName)) {
                int curDelta = s.length() - lowerName.length();
                if (curDelta < delta) {
                    t = player.getProfile();
                    delta = curDelta;
                }
                if (curDelta == 0) {
                    break;
                }
            }
        }
        if (t == null) {
            for (GameProfile player : r.getGameProfiles()) {
                if (UC.getPlayer(player).getNick() == null) {
                    continue;
                }
                String s = TextColorUtil.strip(UC.getPlayer(player).getNick().toPlain()).toLowerCase();
                if (s.toLowerCase().startsWith(lowerName)) {
                    int curDelta = s.length() - lowerName.length();
                    if (curDelta < delta) {
                        t = player;
                        delta = curDelta;
                    }
                    if (curDelta == 0) {
                        break;
                    }
                }
            }
        }
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        r.sendMes(cs, "realnameMessage", "%Nick", UC.getPlayer(t).getNick(), "%Name", t.getName());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
