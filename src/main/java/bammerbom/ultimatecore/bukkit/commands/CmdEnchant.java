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

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.MetaItemStack;
import bammerbom.ultimatecore.bukkit.resources.databases.EnchantmentDatabase;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdEnchant implements UltimateCommand {

    @Override
    public String getName() {
        return "enchant";
    }

    @Override
    public String getPermission() {
        return "uc.enchant";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("enchantment");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.enchant", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "enchantUsage");
            return;
        }
        Player p = (Player) cs;
        Enchantment ench = EnchantmentDatabase.getByName(args[0]);
        if (ench == null) {
            r.sendMes(cs, "enchantNotFound", "%Enchant", args[0]);
            return;
        }
        ItemStack stack = p.getItemInHand();
        if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
            r.sendMes(cs, "enchantNoItemInHand");
            return;
        }
        String name = ench.getName().replace("_", "").toLowerCase();
        Integer level = 1;
        if (r.checkArgs(args, 1) && r.isInt(args[1])) {
            level = Integer.parseInt(args[1]);
        }
        if (level < 0) {
            level = 0;
        }
        if (level == 0) {
            stack.removeEnchantment(ench);
            r.sendMes(cs, "enchantMessage", "%Enchant", name, "%Level", level, "%Item", ItemUtil.getName(stack).toLowerCase());
        } else {
            try {
                MetaItemStack stack2 = new MetaItemStack(stack);
                stack2.addEnchantment(cs, r.perm(cs, "uc.enchant.unsafe", false, false), ench, level);
                p.setItemInHand(stack2.getItemStack());
                r.sendMes(cs, "enchantMessage", "%Enchant", name, "%Level", level, "%Item", ItemUtil.getName(stack).toLowerCase());
            } catch (IllegalArgumentException ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("Enchantment level is either too low or too " + "high")) {
                    r.sendMes(cs, "enchantUnsafe");
                }
            }
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            List<String> sts = new ArrayList<>();
            for (Enchantment enc : Enchantment.values()) {
                sts.add(enc.getName().toLowerCase().replaceAll("_", ""));
            }
            return sts;
        }
        return new ArrayList<>();
    }
}
