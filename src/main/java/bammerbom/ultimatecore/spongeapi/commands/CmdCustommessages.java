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
import bammerbom.ultimatecore.spongeapi.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdCustommessages implements UltimateCommand {

    @Override
    public String getName() {
        return "custommessages";
    }

    @Override
    public String getPermission() {
        return "uc.custommessages";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("cm");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.custommessages", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "custommessagesUsage");
            return;
        }
        if (!new Config(UltimateFileLoader.Dcustommes).contains("Messages." + args[0])) {
            r.sendMes(cs, "custommessagesUsage");
            return;
        }
        String message = ChatColor.translateAlternateColorCodes('&', StringUtil.join("\n", new Config(UltimateFileLoader.Dcustommes).getStringList("Messages." + args[0])))
                .replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "");
        Integer count = 1;
        for (String s : r.getFinalArg(args, 1).split(" ")) {
            s = ChatColor.translateAlternateColorCodes('&', s).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("_", " ");
            if (s.isEmpty()) {
                continue;
            }
            message = message.replace("%var" + count, s);
            message = message.replace("%Var" + count, s);
            count++;
        }

        for (Player p : r.getOnlinePlayers()) {
            if (r.perm(p, "uc.custommessages.receive", true, false)) {
                p.sendMessage(message);
            }
        }

    }


    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}