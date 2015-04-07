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

import bammerbom.ultimatecore.bukkit.r;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMoney implements UltimateCommand {

    @Override
    public String getName() {
        return "money";
    }

    @Override
    public String getPermission() {
        return "uc.money";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("balance");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.getVault() == null) {
            cs.sendMessage("No Vault");
            return;
        }
        if (r.getVault().getEconomy() == null) {
            cs.sendMessage("No Economy");
            return;
        }
        //money
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            if (!r.perm(cs, "uc.money", true, false) && !r.perm(cs, "uc.money.status", true, false)) {
                return;
            }
            r.sendMes(cs, "moneyStatusSelf", "%Balance", r.getVault().getEconomy().getBalance((Player) cs));
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!r.perm(cs, "uc.money", true, false) && !r.perm(cs, "uc.money.set", true, false)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                //TODO usage

            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                r.getVault().getEconomy().withdrawPlayer((Player) cs, r.getVault().getEconomy().getBalance((Player) cs));
                r.getVault().getEconomy().depositPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneySetSelf", "%Balance", args[1]);
            } else if (!r.checkArgs(args, 2)) {
                //TODO usage

            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                Player t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                r.getVault().getEconomy().withdrawPlayer(t, r.getVault().getEconomy().getBalance(t));
                r.getVault().getEconomy().depositPlayer(t, Double.parseDouble(args[2]));
                r.sendMes(cs, "moneySetOthers", "%Balance", args[2], "%Player", t.getName());
            } else {
                //TODO usage

            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!r.perm(cs, "uc.money", true, false) && !r.perm(cs, "uc.money.add", true, false)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                //TODO usage

            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                r.getVault().getEconomy().depositPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyAddSelf", "%Balance", args[1]);
            } else if (!r.checkArgs(args, 2)) {
                //TODO usage

            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                Player t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                r.getVault().getEconomy().depositPlayer(t, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyAddOthers", "%Balance", args[2], "%Player", t.getName());
            } else {
                //TODO usage

            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!r.perm(cs, "uc.money", true, false) && !r.perm(cs, "uc.money.remove", true, false)) {
                return;
            }
            if (!r.checkArgs(args, 1)) {
                //TODO usage

            } else if (r.isDouble(args[1])) {
                if (!r.isPlayer(cs)) {
                    return;
                }
                r.getVault().getEconomy().withdrawPlayer((Player) cs, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyRemoveSelf", "%Balance", args[1]);
            } else if (!r.checkArgs(args, 2)) {
                //TODO usage

            } else if (!r.isDouble(args[1]) && r.isDouble(args[2])) {
                Player t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                r.getVault().getEconomy().withdrawPlayer(t, Double.parseDouble(args[1]));
                r.sendMes(cs, "moneyRemoveOthers", "%Balance", args[2], "%Player", t.getName());
            } else {
                //TODO usage

            }
        } else {
            OfflinePlayer t = r.searchOfflinePlayer(args[0]);
            if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            r.sendMes(cs, "moneyStatusOthers", "%Player", t.getName(), "%Balance", r.getVault().getEconomy().getBalance((Player) cs));
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

}
