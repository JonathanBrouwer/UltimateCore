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
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdFreezelist implements UltimateCommand {

    @Override
    public String getName() {
        return "freezelist";
    }

    @Override
    public String getPermission() {
        return "uc.freezelist";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("freezes");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.perm(cs, "uc.freezelist", true, true) == false) {
            return;
        }
        List<OfflinePlayer> freezes = UC.getServer().getFrozenOfflinePlayers();
        if (freezes == null || freezes.isEmpty()) {
            r.sendMes(cs, "freezelistNoFreezesFound");
            return;
        }
        StringBuilder freezelist = new StringBuilder();
        Integer cur = 0;
        String result;
        for (int i = 0;
             i < freezes.size();
             i++) {
            freezelist.append(freezes.get(cur).getName() + ", ");
            cur++;

        }
        result = freezelist.substring(0, freezelist.length() - 2);
        r.sendMes(cs, "freezelistFreezes", "%Freezelist", result);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        List<String> freezes = new ArrayList<>();
        for (OfflinePlayer pl : UC.getServer().getFrozenOfflinePlayers()) {
            freezes.add(pl.getName());
        }
        return freezes;
    }
}
