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
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.UuidUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

public class CmdNames implements UltimateCommand {

    @Override
    public String getName() {
        return "names";
    }

    @Override
    public String getPermission() {
        return "uc.names";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.names", false, true)) {
            return;
        }
        if (!r.getCnfg().getBoolean("Mojang")) {
            r.sendMes(cs, "accountstatusDisabled");
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "namesUsage");
            return;
        }
        OfflinePlayer p = r.searchOfflinePlayer(args[0]);
        if (p == null || p.getUniqueId() == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        Map<Long, String> names = null;
        try {
            names = UuidUtil.getNameHistory(p.getUniqueId());
        } catch (Exception e) {
            r.sendMes(cs, "namesFailed");
            return;
        }
        r.sendMes(cs, "namesMessage", "%Player", r.getDisplayName(p));
        for (Long date : names.keySet()) {
            String name = names.get(date);
            final String time;
            if (date == -1) {
                time = r.mes("namesFirst");
            } else {
                time = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.US).format(new Date(date));
            }
            r.sendMes(cs, "namesMessage2", "%Date", time, "%Name", name);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
