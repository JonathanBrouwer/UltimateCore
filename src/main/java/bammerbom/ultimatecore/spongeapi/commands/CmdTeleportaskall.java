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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CmdTeleportaskall implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportaskall";
    }

    @Override
    public String getPermission() {
        return "uc.teleportaskall";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpaall");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.teleportaskall", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            final Player p = (Player) cs;
            final List<UUID> targets = new ArrayList<>();
            r.sendMes(cs, "teleportaskallSend");
            for (final Player t : Bukkit.getOnlinePlayers()) {
                if (UC.getPlayer(t).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                    r.sendMes(cs, "teleportDisabled");
                    return;
                }
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
                r.sendMes(cs, "teleportaskhereTarget1", "%Player", p.getName());
                r.sendMes(cs, "teleportaskTarget2");
                r.sendMes(cs, "teleportaskTarget3");
                targets.add(t.getUniqueId());
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                @Override
                public void run() {
                    for (UUID t : targets) {
                        if (UC.getServer().getTeleportHereRequests().containsKey(t) && UC.getServer().getTeleportHereRequests().get(t).equals(p.getUniqueId())) {
                            UC.getServer().removeTeleportHereRequest(t);
                        }
                    }
                }
            }, r.getCnfg().getInt("Command.Teleport.TpaCancel") * 20L);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
