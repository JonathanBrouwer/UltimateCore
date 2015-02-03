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
import bammerbom.ultimatecore.bukkit.r;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHomelist implements UltimateCommand {

    @Override
    public String getName() {
        return "homelist";
    }

    @Override
    public String getPermission() {
        return "uc.home";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("homes");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!(r.isPlayer(cs))) {
            return;
        }
        if (!r.perm(cs, "uc.home", true, true)) {
            return;
        }
        Player p = (Player) cs;
        ArrayList<String> homes = UC.getPlayer(p).getHomeNames();
        String a = "";
        Integer b = 0;
        try {
            Integer amount = homes.toArray().length;
            for (int i = 0; i < amount; i++) {
                a = a + homes.get(b) + ", ";
                b++;

            }
            a = a.substring(0, a.length() - 2);
        } catch (IndexOutOfBoundsException ex) {
        }
        if (a.equalsIgnoreCase("") || a.equalsIgnoreCase(null)) {
            r.sendMes(cs, "homeNoHomesFound");
            return;
        } else {
            r.sendMes(cs, "homeList", "%Homes", a);
            return;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (!r.isPlayer(cs)) {
            return new ArrayList<>();
        }
        return UC.getPlayer((Player) cs).getHomeNames();
    }
}
