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
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

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
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            //repair
            if (!r.perm(cs, "uc.repair", false, true)) {
                return;
            }
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
            if (!r.perm(cs, "uc.repair.others", false, true)) {
                return;
            }
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
            r.sendMes(cs, "repairOtherSelfHand", "%Player", r.getDisplayName(p));
            r.sendMes(p, "repairOtherOtherHand", "%Player", r.getDisplayName(cs));
        } else if (args[0].equalsIgnoreCase("all")) {
            //repair all <Player>
            if (r.checkArgs(args, 1)) {
                if (!r.perm(cs, "uc.repair.others.all", false, true)) {
                    return;
                }
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
                r.sendMes(cs, "repairOtherSelfAll", "%Player", r.getDisplayName(p));
                r.sendMes(p, "repairOtherOtherAll", "%Player", r.getDisplayName(cs));
            } else {
                //repair all
                if (!r.perm(cs, "uc.repair.all", false, true)) {
                    return;
                }
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
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
