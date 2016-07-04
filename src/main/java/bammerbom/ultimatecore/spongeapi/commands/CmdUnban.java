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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.ban.Ban;

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
    public String getUsage() {
        return "/<command> <Player>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Unbans the target player.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pardon");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.unban", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "unbanUsage");
            return CommandResult.empty();
        }
        if (FormatUtil.validIP(args[0])) {
            boolean banned = false;
            BanService service = Sponge.getServiceManager().provide(BanService.class).get();
            for (Ban.Ip ip : service.getIpBans()) {
                if (ip.getAddress().getAddress().toString().split("/")[1].split(":")[0].equalsIgnoreCase(args[0])) {
                    service.removeBan(ip);
                    banned = true;
                }
            }
            if (banned) {
                if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
                    Sponge.getServer().getBroadcastChannel().send(Text.of(r.mes("unbanBroadcast").toPlain().replace("%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r
                            .getDisplayName(cs).toLowerCase())).replace("%Unbanned", args[0])));
                } else {
                    r.sendMes(cs, "unbanBroadcast", "%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Unbanned", args[0]);
                }
            } else {
                r.sendMes(cs, "unbanNotBanned", "%Player", args[0]);
                return CommandResult.empty();
            }
            return CommandResult.success();
        }
        GameProfile banp = r.searchGameProfile(args[0]).orElse(null);
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
        if (r.getCnfg().getBoolean("Command.BanBroadcast")) {
            Sponge.getServer().getBroadcastChannel().send(r.mes("unbanBroadcast", "%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()),
                    "%Unbanned", r.getDisplayName(banp)));
        } else {
            r.sendMes(cs, "unbanBroadcast", "%Unbanner", ((cs instanceof Player) ? r.getDisplayName(cs) : r.getDisplayName(cs).toLowerCase()), "%Unbanned", r.getDisplayName(banp));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (!r.perm(cs, "uc.unban", true)) {
            return new ArrayList<>();
        }
        ArrayList<String> str = new ArrayList<>();
        for (GameProfile pl : UC.getServer().getBannedGameProfiles()) {
            str.add(pl.getName().orElse(""));
        }
        return str;
    }
}
