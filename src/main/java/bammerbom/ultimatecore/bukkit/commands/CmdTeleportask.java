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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CmdTeleportask implements UltimateCommand {

    @Override
    public String getName() {
        return "teleportask";
    }

    @Override
    public String getPermission() {
        return "uc.teleportask";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tpa", "call", "tpask");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            //TODO menu
            r.sendMes(cs, "teleportaskUsage");
            return;
        }
        if (!r.perm(cs, "uc.teleportask", true, true)) {
            return;
        }
        final Player p = (Player) cs;
        final Player t = r.searchPlayer(args[0]);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        if (UC.getPlayer(t).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false, false)) {
            r.sendMes(cs, "teleportDisabled", "%Player", t.getName());
            return;
        }
        r.sendMes(cs, "teleportaskSend", "%Player", t.getName());
        UC.getServer().addTeleportRequest(t.getUniqueId(), p.getUniqueId());
        if (UC.getServer().getTeleportHereRequests().containsKey(t.getUniqueId())) {
            UC.getServer().getTeleportHereRequests().remove(t.getUniqueId());
        }
        ArrayList<UUID> remove = new ArrayList<>();
        for (UUID u : UC.getServer().getTeleportHereRequests().keySet()) {
            if (UC.getServer().getTeleportHereRequests().get(u).equals(t.getUniqueId())) {
                remove.add(u);
            }
        }
        for (UUID u : remove) {
            UC.getServer().removeTeleportHereRequest(u);
        }
        r.sendMes(t, "teleportaskTarget1", "%Player", r.getDisplayName(p));
        r.sendMes(t, "teleportaskTarget2");
        r.sendMes(t, "teleportaskTarget3");
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                if (UC.getServer().getTeleportRequests().containsKey(t.getUniqueId()) && UC.getServer().getTeleportRequests().get(t.getUniqueId()).equals(p.getUniqueId())) {
                    UC.getServer().removeTeleportRequest(t.getUniqueId());
                }
            }
        }, r.getCnfg().getInt("Command.Teleport.TpaCancel") * 20L);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
