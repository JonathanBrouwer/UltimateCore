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
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdRepair implements UltimateCommand {

    @Override
    public String getName() {
        return "repair";
    }

    @Override
    public String getPermission() {
        return "uc.repair";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fix");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.repair", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            //repair
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            ItemStack stack = p.getItemInHand();
            if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
                r.sendMes(cs, "repairNoItemInHand");
                return;
            }
            if (!ItemUtil.isRepairable(stack)) {
                r.sendMes(cs, "repairNotRepairable");
                return;
            }
            stack.setDurability((short) 0);
            r.sendMes(cs, "repairSelfHand");
        } else if (!args[0].equalsIgnoreCase("all")) {
            //repair <Player>
            Player p = r.searchPlayer(args[0]);
            if (p == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return;
            }
            ItemStack stack = p.getItemInHand();
            if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
                r.sendMes(cs, "repairNoItemInHand");
                return;
            }
            if (!ItemUtil.isRepairable(stack)) {
                r.sendMes(cs, "repairNotRepairable");
                return;
            }
            stack.setDurability((short) 0);
            r.sendMes(cs, "repairOtherSelfHand", "%Player", p.getName());
            r.sendMes(p, "repairOtherOtherHand", "%Player", cs.getName());
        } else if (args[0].equalsIgnoreCase("all")) {
            //repair all <Player>
            if (!r.perm(cs, "uc.repair.all", false, true)) {
                return;
            }
            if (r.checkArgs(args, 1)) {
                Player p = r.searchPlayer(args[1]);
                if (p == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                for (ItemStack stack : p.getInventory().getContents()) {
                    if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
                        continue;
                    }
                    if (!ItemUtil.isRepairable(stack)) {
                        continue;
                    }
                    stack.setDurability((short) 0);
                }
                for (ItemStack stack : p.getInventory().getArmorContents()) {
                    if (stack == null) {
                        continue;
                    }
                    if (!ItemUtil.isRepairable(stack)) {
                        continue;
                    }
                    stack.setDurability((short) 0);
                }
                r.sendMes(cs, "repairOtherSelfAll", "%Player", p.getName());
                r.sendMes(p, "repairOtherOtherAll", "%Player", cs.getName());
            } else {
                //repair all
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                for (ItemStack stack : p.getInventory().getContents()) {
                    if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
                        continue;
                    }
                    if (!ItemUtil.isRepairable(stack)) {
                        continue;
                    }
                    stack.setDurability((short) 0);
                }
                for (ItemStack stack : p.getInventory().getArmorContents()) {
                    if (stack == null) {
                        continue;
                    }
                    if (!ItemUtil.isRepairable(stack)) {
                        continue;
                    }
                    stack.setDurability((short) 0);
                }
                r.sendMes(cs, "repairSelfAll");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
