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
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdInventory implements UltimateCommand {

    public static void disable() {
        for (Player pl : UC.getServer().getInOnlineInventoryOnlinePlayers()) {
            UC.getPlayer(pl).setInOnlineInventory(false);
            pl.closeInventory();
        }
        for (Player pl : UC.getServer().getInOfflineInventoryOnlinePlayers()) {
            UC.getPlayer(pl).setInOfflineInventory(false);
            pl.closeInventory();
        }
    }

    @Override
    public String getName() {
        return "inventory";
    }

    @Override
    public String getPermission() {
        return "uc.inventory";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("inv", "invsee");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!(r.isPlayer(cs))) {
            return;
        }
        if (!r.perm(cs, "uc.inventory", false, true)) {
            return;
        }
        Player p = (Player) cs;
        if (r.checkArgs(args, 0)) {
            Player t = r.searchPlayer(args[0]);
            if (t != null) {
                p.openInventory(t.getInventory());
                UC.getPlayer(p).setInOnlineInventory(true);
            } else {
                OfflinePlayer t2 = r.searchOfflinePlayer(args[0]);
                if (t2 == null || (!t2.hasPlayedBefore() && !t2.isOnline())) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                if (UC.getPlayer(t2).getLastInventory() == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }

                p.openInventory(UC.getPlayer(t2).getLastInventory());
                UC.getPlayer(p).setInOfflineInventory(true);
            }
        } else {
            r.sendMes(cs, "inventoryUsage");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
