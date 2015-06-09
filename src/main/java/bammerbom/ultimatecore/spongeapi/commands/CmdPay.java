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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdPay implements UltimateCommand {

    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getPermission() {
        return "uc.pay";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.pay", true, true)) {
            return;
        }
        if (r.getVault() == null) {
            r.sendMes(cs, "moneyNoVault");
            return;
        }
        if (r.getVault().getEconomy() == null) {
            r.sendMes(cs, "moneyNoEconomy");
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        //pay player amount
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "payUsage");
            return;
        }
        Player t = r.searchPlayer(args[0]);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        if (!r.isDouble(args[1])) {
            r.sendMes(cs, "numberFormat", "%Number", args[1]);
            return;
        }
        Double d = Double.parseDouble(args[1]);
        d = r.normalize(d, 0.1, r.getVault().getEconomy().getBalance(p));
        r.getVault().getEconomy().withdrawPlayer(p, d);
        r.getVault().getEconomy().depositPlayer(t, d);
        r.sendMes(cs, "payMessage", "%Player", UC.getPlayer(t).getDisplayName(), "%Amount", r.getVault().getEconomy().format(d));
        r.sendMes(t, "payTarget", "%Player", UC.getPlayer(p).getDisplayName(), "%Amount", r.getVault().getEconomy().format(d));
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
