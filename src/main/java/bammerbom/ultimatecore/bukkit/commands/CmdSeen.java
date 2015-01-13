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

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.api.UCplayer;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.DateUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdSeen implements UltimateCommand {

    @Override
    public String getName() {
        return "seen";
    }

    @Override
    public String getPermission() {
        return "uc.seen";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.seen", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "seenUsage");
            return;
        }
        OfflinePlayer p = r.searchOfflinePlayer(args[0]);
        if (p == null || (!p.hasPlayedBefore() && !p.isOnline())) {
            r.sendMes(cs, "seenNotFound");
            return;
        }
        UCplayer pl = UC.getPlayer(p);
        r.sendMes(cs, "seenMessage", "%Player", p.getName(), "%Status", (p.isOnline() ? r.mes("seenOnline") : r.mes("seenOffline")), "%Time", DateUtil.formatDateDiff(pl.getLastConnectMillis()));
        //Last location
        if (p.getPlayer() != null && p.getPlayer().getLocation() != null) {
            String loc = p.getPlayer().getWorld().getName() + " " + p.getPlayer().getLocation().getBlockX() + " " + p.getPlayer().getLocation().getBlockY() + " " + p.getPlayer().getLocation().getBlockZ();
            r.sendMes(cs, "seenLocation", "%Location", loc);
        }
        //Ban
        r.sendMes(cs, "seenBanned", "%Banned", pl.isBanned() ? r.mes("yes") : r.mes("no"));
        if (pl.isBanned()) {
            r.sendMes(cs, "seenBantime", "%Bantime", pl.getBanTimeLeft() >= 0 ? DateUtil.format(pl.getBanTimeLeft()) : r.mes("banForever"));
            r.sendMes(cs, "seenBanreason", "%Reason", pl.getBanReason());
        }
        //Mute
        r.sendMes(cs, "seenMuted", "%Muted", pl.isMuted() ? r.mes("yes") : r.mes("no"));
        if (pl.isMuted()) {
            r.sendMes(cs, "seenMutetime", "%Mutetime", pl.getMuteTimeLeft() >= 0 ? DateUtil.format(pl.getMuteTimeLeft()) : r.mes("banForever"));
        }
        //Deaf
        r.sendMes(cs, "seenDeaf", "%Deaf", pl.isDeaf() ? r.mes("yes") : r.mes("no"));
        if (pl.isDeaf()) {
            r.sendMes(cs, "seenDeaftime", "%Deaftime", pl.getDeafTimeLeft() >= 0 ? DateUtil.format(pl.getDeafTimeLeft()) : r.mes("banForever"));
        }
        //Jailed
        r.sendMes(cs, "seenJailed", "%Jailed", pl.isJailed() ? r.mes("yes") : r.mes("no"));
        if (pl.isJailed()) {
            r.sendMes(cs, "seenJailtime", "%Jailtime", pl.getJailTimeLeft() >= 0 ? DateUtil.format(pl.getJailTimeLeft()) : r.mes("banForever"));
        }
        //Frozen
        r.sendMes(cs, "seenFrozen", "%Frozen", pl.isFrozen() ? r.mes("yes") : r.mes("no"));
        if (pl.isFrozen()) {
            r.sendMes(cs, "seenFrozentime", "%Frozentime", pl.getFrozenTimeLeft() >= 0 ? DateUtil.format(pl.getFrozenTimeLeft()) : r.mes("banForever"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
