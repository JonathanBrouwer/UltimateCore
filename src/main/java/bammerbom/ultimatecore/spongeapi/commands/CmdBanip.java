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
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.FormatUtil;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.ban.BanType;
import org.spongepowered.api.util.ban.Bans;
import org.spongepowered.api.util.command.CommandSource;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CmdBanip implements UltimateCommand {

    @Override
    public String getName() {
        return "banip";
    }

    @Override
    public String getPermission() {
        return "uc.banip";
    }

    @Override
    public String getUsage() {
        return "/<command> <IP/Player> [Time] [Reason]";
    }

    @Override
    public String getDescription() {
        return "Bans the target ip.";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ipban");
    }

    @Override
    public void run(CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "banipUsage");
            return;
        }
        String ip;
        User t = null;
        if (FormatUtil.validIP(args[0])) {
            ip = args[0];
            for (Player p : r.getOnlinePlayers()) {
                if (ip.equalsIgnoreCase(UC.getPlayer(p).getLastIp())) {
                    t = p;
                }
            }
        } else {
            t = r.searchOfflinePlayer(args[0]);
            if (UC.getPlayer(t).getLastIp() != null) {
                ip = UC.getPlayer(t).getLastIp();
            } else {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
        }
        Long time = 0L;
        Text.Literal reason = r.mes("banipDefaultReason");
        if (!r.checkArgs(args, 1)) {
        } else if (DateUtil.parseDateDiff(args[1]) == -1) {
            reason = Texts.of(r.getFinalArg(args, 1));
        } else {
            time = DateUtil.parseDateDiff(args[1]);
            if (r.checkArgs(args, 2)) {
                reason = Texts.of(r.getFinalArg(args, 2));
            }
        }
        Text.Literal timen = Texts.of(DateUtil.format(time));
        if (time == 0) {
            timen = r.mes("banipForever");
        }
        if (!r.perm(cs, "uc.banip.time", false, false) && !r.perm(cs, "uc.banip", false, false) && time <= 0L) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        if (!r.perm(cs, "uc.banip.perm", false, false) && !r.perm(cs, "uc.banip", false, false) && !(time <= 0L)) {
            r.sendMes(cs, "noPermissions");
            return;
        }
        Text.Literal msg = r.mes("banipFormat", "%Time", timen, "%Reason", reason);
        if (t != null && t.isOnline()) {
            t.getPlayer().get().kick(msg);
        }
        Date date = time == 0 ? null : new Date(time + System.currentTimeMillis());
        BanService serv = Sponge.getGame().getServiceManager().provide(BanService.class).get();
        try {
            serv.ban(Bans.builder().type(BanType.IP_BAN).address(InetAddress.getByName(args[0])).source(cs).expirationDate(date).reason(msg).build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
            Sponge.getGame().getServer().getBroadcastSink()
                    .sendMessage(r.mes("banBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs)), "%Banned", ip, "%Time", timen, "%Reason", reason));
        } else {
            r.sendMes(cs, "banipBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs)), "%Banned", ip, "%Time", timen, "%Reason", reason);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String[] args, String label, String curs, Integer curn) {
        return null;
    }

}
