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
import bammerbom.ultimatecore.spongeapi.api.UPlayer;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.FormatUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdUnban implements UltimateCommand {

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getPermission() {
        return "uc.unban";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pardon");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.unban", false, true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "unbanUsage");
            return CommandResult.empty();
        }
        if (FormatUtil.validIP(args[0])) {
            BanList list = Bukkit.getBanList(BanList.Type.IP);
            if (list.isBanned(args[0])) {
                list.pardon(args[0]);
                for (OfflinePlayer p : UC.getServer().getBannedOfflinePlayers()) {
                    if (UC.getPlayer(p).getLastIp() != null && UC.getPlayer(p).getLastIp().equalsIgnoreCase(args[0])) {
                        UC.getPlayer(p).unban();
                    }
                }
                if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
                    Bukkit.broadcastMessage(r.mes("unbanBroadcast").replace("%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase())).replace
                            ("%Unbanned", args[0]));
                }
            }
            return CommandResult.empty();
        }
        OfflinePlayer banp = r.searchGameProfile(args[0]);
        if (banp == null || banp.getUniqueId() == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        UPlayer pl = UC.getPlayer(banp);
        if (!pl.isBanned()) {
            r.sendMes(cs, "unbanNotBanned", "%Player", r.getDisplayName(banp));
            return CommandResult.empty();
        }
        pl.unban();
        //Check ip
        BanList list = Bukkit.getBanList(BanList.Type.IP);
        if (pl.getLastIp() != null && list.isBanned(pl.getLastIp())) {
            list.pardon(pl.getLastIp());
        }
        //
        if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
            Bukkit.broadcastMessage(r.mes("unbanBroadcast", "%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Unbanned", r
                    .getDisplayName(banp)));
        } else {
            r.sendMes(cs, "unbanBroadcast", "%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Unbanned", r.getDisplayName(banp));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (!r.perm(cs, "uc.unban", false, true)) {
            return new ArrayList<>();
        }
        ArrayList<String> str = new ArrayList<>();
        for (OfflinePlayer pl : UC.getServer().getBannedOfflinePlayers()) {
            str.add(pl.getName());
        }
        return str;
    }
}
