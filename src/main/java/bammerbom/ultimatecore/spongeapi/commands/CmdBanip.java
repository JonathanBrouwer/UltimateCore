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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;

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
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ipban");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "banipUsage");
            return CommandResult.empty();
        }
        String ip;
        GameProfile t = null;
        if (FormatUtil.validIP(args[0])) {
            ip = args[0];
            for (Player p : r.getOnlinePlayers()) {
                if (ip.equalsIgnoreCase(UC.getPlayer(p).getLastIp())) {
                    t = p.getProfile();
                }
            }
        } else {
            t = r.searchGameProfile(args[0]).orElse(null);
            if (UC.getPlayer(t).getLastIp() != null) {
                ip = UC.getPlayer(t).getLastIp();
            } else {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
        }
        Long time = 0L;
        Text reason = r.mes("banipDefaultReason");
        if (!r.checkArgs(args, 1)) {
        } else if (DateUtil.parseDateDiff(args[1]) == -1) {
            reason = Text.of(r.getFinalArg(args, 1));
        } else {
            time = DateUtil.parseDateDiff(args[1]);
            if (r.checkArgs(args, 2)) {
                reason = Text.of(r.getFinalArg(args, 2));
            }
        }
        String timen = DateUtil.format(time);
        if (time == 0) {
            timen = r.mes("banipForever").toPlain();
        } else {
            timen = "" + timen;
        }
        if (!r.perm(cs, "uc.banip.time", false) && !r.perm(cs, "uc.banip", false) && time <= 0L) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.banip.perm", false) && !r.perm(cs, "uc.banip", false) && !(time <= 0L)) {
            r.sendMes(cs, "noPermissions");
            return CommandResult.empty();
        }
        String msg = r.mes("banipFormat").toPlain().replace("%Time", timen).replace("%Reason", reason.toPlain());
        if (t != null && r.searchPlayer(t.getUniqueId()).isPresent()) {
            r.searchPlayer(t.getUniqueId()).get().kick(Text.of(msg));
        }
        Date date = time == 0 ? null : new Date(time + System.currentTimeMillis());
        BanService bans = Sponge.getServiceManager().provide(BanService.class).get();
        if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
            Sponge.getServer().getBroadcastChannel().send(r
                    .mes("banBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Banned", ip, "%Time", timen, "%Reason", reason));
        } else {
            r.sendMes(cs, "banipBroadcast", "%Banner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Banned", ip, "%Time", timen, "%Reason", reason);
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
