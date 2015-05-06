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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdList implements UltimateCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "uc.list";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("players", "online", "who");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.list", true, true)) {
            return;
        }
        if (r.getVault() == null || r.getVault().getPermission() == null) {
            StringBuilder online = new StringBuilder();
            Player[] players = r.getOnlinePlayers();
            Integer i = 0;

            for (Player player : players) {
                if ((!(cs instanceof Player)) || (((Player) cs).canSee(player))) {
                    if (online.length() > 0) {
                        online.append(", ");
                    }
                    i++;
                    online.append(UC.getPlayer(player).getDisplayName());
                }
            }
            r.sendMes(cs, "listList", "%Online", i, "%Max", Bukkit.getMaxPlayers(), "%List", online.toString());
        } else {
            StringBuilder online = new StringBuilder();
            List<Player> plz = new ArrayList<>();
            plz.addAll(r.getOnlinePlayersL());
            Boolean first2 = true;
            Integer i = 0;
            for (String g : r.getVault().getPermission().getGroups()) {
                Boolean an = false;
                for (Player p : r.getOnlinePlayers()) {
                    if (r.getVault().getPermission().getPrimaryGroup(p) == null) {
                        continue;
                    }
                    if (r.getVault().getPermission().getPrimaryGroup(p).equalsIgnoreCase(g)) {
                        an = true;
                    }
                }
                if (an) {
                    String gn = g;
                    if (first2) {
                        first2 = false;
                        online.append(r.positive + StringUtil.firstUpperCase(gn) + ": ");
                    } else {
                        online.append("\n" + r.positive + StringUtil.firstUpperCase(gn) + ": ");
                    }
                    Boolean first = true;
                    Boolean any = false;
                    ArrayList<Player> remove = new ArrayList<>();
                    for (Player pl : plz) {
                        Player p = r.searchPlayer(cs.getName());
                        if (p == null || p.canSee(pl)) {
                            if (r.getVault().getPermission().getPrimaryGroup(pl) != null && r.getVault().getPermission().getPrimaryGroup(pl).equalsIgnoreCase(g)) {
                                if (!first) {
                                    online.append(", ");
                                }
                                online.append(r.neutral + UC.getPlayer(pl).getDisplayName());
                                i++;
                                any = true;
                                first = false;
                                remove.add(pl);
                            }
                        }
                    }
                    plz.removeAll(remove);
                    remove.clear();
                    if (any == false) {
                        online.append(r.neutral + "none");
                    }
                }
            }
            if (!plz.isEmpty()) {
                online.append("\n" + r.positive + "No group: ");
                for (Player pl : plz) {
                    online.append(r.neutral + pl.getName());
                }
            }
            r.sendMes(cs, "listList", "%Online", i, "%Max", Bukkit.getMaxPlayers(), "%List", online.toString());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
