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
import bammerbom.ultimatecore.bukkit.api.UPlayer;
import bammerbom.ultimatecore.bukkit.r;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReply implements UltimateCommand {

    static String format = ChatColor.translateAlternateColorCodes('&',
            r.getCnfg().getString("Chat.MsgFormat"));
    static String formatSpy = ChatColor.translateAlternateColorCodes('&',
            r.getCnfg().getString("Chat.MsgFormatSpy"));

    @Override
    public String getName() {
        return "reply";
    }

    @Override
    public String getPermission() {
        return "uc.reply";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("r");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.reply", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "replyUsage");
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        Player pl = UC.getPlayer(p).getReply();
        if (pl == null) {
            r.sendMes(cs, "replyNotFound");
            return;
        }
        UC.getPlayer(pl).setReply((Player) cs);
        //Spy
        for (Player ps : r.getOnlinePlayers()) {
            UPlayer up = UC.getPlayer(ps);
            if (up.isSpy()) {
                ps.sendMessage(formatSpy
                        .replace("@1", r.positive + "")
                        .replace("@2", r.neutral + "")
                        .replace("@3", r.negative + "")
                        .replace("%Player1", cs.getName())
                        .replace("%Player2", pl.getName())
                        .replace("%Message", r.getFinalArg(args, 0)));
            }
        }
        cs.sendMessage(format
                .replace("@1", r.positive + "")
                .replace("@2", r.neutral + "")
                .replace("@3", r.negative + "")
                .replace("%Player1", r.mes("me"))
                .replace("%Player2", pl.getName())
                .replace("%Message", r.getFinalArg(args, 0)));
        pl.sendMessage(format
                .replace("@1", r.positive + "")
                .replace("@2", r.neutral + "")
                .replace("@3", r.negative + "")
                .replace("%Player1", pl.getName())
                .replace("%Player2", r.mes("me"))
                .replace("%Message", r.getFinalArg(args, 0)));
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
