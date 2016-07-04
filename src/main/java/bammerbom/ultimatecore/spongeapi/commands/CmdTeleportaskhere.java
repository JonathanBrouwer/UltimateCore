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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CmdTeleportaskhere implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportaskhere";
    }

    @Override
    public String getPermission() {
        return "uc.teleportaskhere";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Ask someone to teleport to you.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpahere");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.teleportaskhere", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            //TODO menu
            r.sendMes(cs, "teleportaskhereUsage");
            return CommandResult.empty();
        }
        final Player p = (Player) cs;
        final Player t = r.searchPlayer(args[0]).orElse(null);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        if (UC.getPlayer(t).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false)) {
            r.sendMes(cs, "teleportDisabled", "%Player", t.getName());
            return CommandResult.empty();
        }
        r.sendMes(cs, "teleportaskSend", "%Player", t.getName());
        UC.getServer().addTeleportHereRequest(t.getUniqueId(), p.getUniqueId());
        if (UC.getServer().getTeleportRequests().containsKey(t.getUniqueId())) {
            UC.getServer().getTeleportRequests().remove(t.getUniqueId());
        }
        ArrayList<UUID> remove = new ArrayList<>();
        for (UUID u : UC.getServer().getTeleportRequests().keySet()) {
            if (UC.getServer().getTeleportRequests().get(u).equals(t.getUniqueId())) {
                remove.add(u);
            }
        }
        for (UUID u : remove) {
            UC.getServer().removeTeleportRequest(u);
        }
        r.sendMes(t, "teleportaskhereTarget1", "%Player", r.getDisplayName(p));
        r.sendMes(t, "teleportaskTarget2");
        r.sendMes(t, "teleportaskTarget3");
        Sponge.getScheduler().createTaskBuilder().name("TPA here cancel delay task").delayTicks(r.getCnfg().getInt("Command.Teleport.TpaCancel") * 20L).execute(new Runnable() {
            @Override
            public void run() {
                if (UC.getServer().getTeleportHereRequests().containsKey(t.getUniqueId()) && UC.getServer().getTeleportHereRequests().get(t.getUniqueId()).equals(p.getUniqueId())) {
                    UC.getServer().removeTeleportHereRequest(t.getUniqueId());
                }
            }
        }).submit(r.getUC());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
