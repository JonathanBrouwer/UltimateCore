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
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdTeleportdeny implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportdeny";
    }

    @Override
    public String getPermission() {
        return "uc.teleportdeny";
    }

    @Override
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Deny a teleport request.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpdeny", "tpno");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.teleportdeny", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (UC.getServer().getTeleportRequests().containsKey(p.getUniqueId()) || UC.getServer().getTeleportHereRequests().containsKey(p.getUniqueId())) {
            Player t1 = r.searchPlayer(UC.getServer().getTeleportRequests().get(p.getUniqueId())).orElse(null);
            Player t2 = r.searchPlayer(UC.getServer().getTeleportHereRequests().get(p.getUniqueId())).orElse(null);
            r.sendMes(cs, "teleportdenySender");
            if (t1 != null) {
                r.sendMes(t1, "teleportdenyTarget", "%Player", r.getDisplayName(p));
            }
            if (t2 != null) {
                r.sendMes(t2, "teleportdenyTarget", "%Player", r.getDisplayName(p));
            }
            UC.getServer().removeTeleportRequest(p.getUniqueId());
            UC.getServer().removeTeleportHereRequest(p.getUniqueId());
        } else {
            r.sendMes(p, "teleportaskNoRequests");
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
