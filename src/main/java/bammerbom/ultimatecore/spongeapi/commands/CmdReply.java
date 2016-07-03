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
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdReply implements UltimateCommand {

    static String format = TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.MsgFormat"));
    static String formatSpy = TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.MsgFormatSpy"));

    @Override
    public String getName() {
        return "reply";
    }

    @Override
    public String getPermission() {
        return "uc.reply";
    }

    @Override
    public String getUsage() {
        return "/<command> <Message>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Reply to a private message.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("r");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.reply", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "replyUsage");
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (UC.getPlayer((Player) cs).isMuted()) {
            if (UC.getPlayer((Player) cs).getMuteTime() == 0 || UC.getPlayer((Player) cs).getMuteTime() == -1) {
                r.sendMes(cs, "muteChat");
            } else {
                r.sendMes(cs, "muteChatTime", "%Time", DateUtil.format(UC.getPlayer((Player) cs).getMuteTimeLeft()));
            }
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        Player pl = UC.getPlayer(p).getReply().orElse(null);
        if (pl == null) {
            r.sendMes(cs, "replyNotFound");
            return CommandResult.empty();
        }
        UC.getPlayer(pl).setReply((Player) cs);
        String message = r.perm(cs, "uc.coloredchat", false) ? TextColorUtil.translateAlternate(r.getFinalArg(args, 0)) : r.getFinalArg(args, 0);
        //Spy
        for (Player ps : r.getOnlinePlayers()) {
            UPlayer up = UC.getPlayer(ps);
            if (up.isSpy()) {
                ps.sendMessage(Text.of(formatSpy.replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("%Player1", r.getDisplayName(cs))
                        .replace("%Player2", pl.getName()).replace("%Message", message)));
            }
        }
        cs.sendMessage(Text.of(format.replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("%Player1", r.mes("me").toPlain()).replace
                ("%Player2", pl.getName()).replace("%Message", message)));
        pl.sendMessage(Text.of(format.replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("%Player1", r.getDisplayName(cs)).replace
                ("%Player2", r.mes("me").toPlain()).replace("%Message", message)));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
