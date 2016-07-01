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
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdIp implements UltimateCommand {

    @Override
    public String getName() {
        return "ip";
    }

    @Override
    public String getPermission() {
        return "uc.ip";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Get the servers or a players ip.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            if (!r.perm(cs, "uc.ip.server", false) && !r.perm(cs, "uc.ip", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            if (Sponge.getServer().getBoundAddress().isPresent()) {
                r.sendMes(cs, "ipServer", "%IP", Sponge.getServer().getBoundAddress().get().getAddress().toString().split("/")[1].split(":")[0]
                        + ":" + Sponge.getServer().getBoundAddress().get().getPort());
            } else {
                r.sendMes(cs, "ipServer", "%IP", "n/a");
            }
        } else {
            if (!r.perm(cs, "uc.ip.player", false) && !r.perm(cs, "uc.ip", false)) {
                r.sendMes(cs, "noPermissions");
                return CommandResult.empty();
            }
            GameProfile p = r.searchGameProfile(args[0]).orElse(null);
            if (p == null || UC.getPlayer(p).getLastIp() == null || UC.getPlayer(p).getLastHostname() == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            r.sendMes(cs, "ipPlayer1", "%Player", r.getDisplayName(p), "%Hostname", UC.getPlayer(p).getLastHostname());
            r.sendMes(cs, "ipPlayer2", "%Player", r.getDisplayName(p), "%IP", UC.getPlayer(p).getLastIp());
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

}
