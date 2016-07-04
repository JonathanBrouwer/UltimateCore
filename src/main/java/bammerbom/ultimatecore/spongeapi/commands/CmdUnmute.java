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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdUnmute implements UltimateCommand {

    @Override
    public String getName() {
        return "unmute";
    }

    @Override
    public String getPermission() {
        return "uc.unmute";
    }

    @Override
    public String getUsage() {
        return "/<command> <Player>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Unmute someone.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.unmute", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "unmuteUsage");
            return CommandResult.empty();
        }
        GameProfile banp = r.searchGameProfile(args[0]).orElse(null);
        if (banp == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        if (!UC.getPlayer(banp).isMuted()) {
            r.sendMes(cs, "unmuteNotMuted", "%Player", r.getDisplayName(banp));
            return CommandResult.empty();
        }
        UC.getPlayer(banp).setMuted(false);
        r.sendMes(cs, "unmuteMessage", "%Player", r.getDisplayName(banp));
        if (r.searchPlayer(banp.getUniqueId()).isPresent()) {
            Player banp2 = r.searchPlayer(banp.getUniqueId()).get();
            r.sendMes(banp2, "unmuteTarget");
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (!r.perm(cs, "uc.unmute", true)) {
            return new ArrayList<>();
        }
        ArrayList<String> str = new ArrayList<>();
        for (GameProfile pl : UC.getServer().getMutedGameProfiles()) {
            str.add(pl.getName().orElse(""));
        }
        return str;
    }
}
