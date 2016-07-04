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
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdTeleportaccept implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportaccept";
    }

    @Override
    public String getPermission() {
        return "uc.teleportaccept";
    }

    @Override
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Accept a teleport request.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpaccept", "tpyes");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.teleportaccept", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (UC.getServer().getTeleportHereRequests().containsKey(p.getUniqueId())) {
            Player t = r.searchPlayer(UC.getServer().getTeleportHereRequests().get(p.getUniqueId())).orElse(null);
            if (t == null) {
                r.sendMes(p, "teleportaskNoRequests");
            } else {
                LocationUtil.teleport(p, t, Cause.builder().build(), true, true);
                r.sendMes(cs, "teleportaskhereAcceptSender", "%Player", t.getName());
                r.sendMes(t, "teleportaskhereAcceptTarget", "%Player", r.getDisplayName(p));
                UC.getServer().removeTeleportHereRequest(t.getUniqueId());
            }
        } else if (!UC.getServer().getTeleportRequests().containsKey(p.getUniqueId())) {
            r.sendMes(p, "teleportaskNoRequests");
        } else {
            Player t = r.searchPlayer(UC.getServer().getTeleportRequests().get(p.getUniqueId())).orElse(null);
            if (t == null) {
                r.sendMes(p, "teleportaskNoRequests");
            } else {
                LocationUtil.teleport(t, p, Cause.builder().build(), true, true);
                r.sendMes(cs, "teleportaskAcceptSender", "%Player", t.getName());
                r.sendMes(t, "teleportaskAcceptTarget", "%Player", r.getDisplayName(p));
                UC.getServer().removeTeleportRequest(p.getUniqueId());
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
