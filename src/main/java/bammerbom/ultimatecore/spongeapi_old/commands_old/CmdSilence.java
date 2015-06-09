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
package bammerbom.ultimatecore.spongeapi_old.commands_old;

import bammerbom.ultimatecore.spongeapi_old.api.UC;
import bammerbom.ultimatecore.spongeapi_old.r;
import bammerbom.ultimatecore.spongeapi_old.resources.utils.DateUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdSilence implements UltimateCommand {

    @Override
    public String getName() {
        return "silence";
    }

    @Override
    public String getPermission() {
        return "uc.silence";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("disablechat");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.silence", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            UC.getServer().setSilenced(true);
            r.sendMes(cs, "silenceMessage");
        } else {
            Long time = null;
            if (DateUtil.parseDateDiff(args[0]) != -1) {
                time = DateUtil.parseDateDiff(args[0]);
            }
            UC.getServer().setSilenced(true, time);
            r.sendMes(cs, "silenceMessageTime", "%Time", DateUtil.format(time));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
