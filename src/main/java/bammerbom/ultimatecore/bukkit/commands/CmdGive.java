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
import bammerbom.ultimatecore.bukkit.resources.utils.InventoryUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CmdGive implements UltimateCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getPermission() {
        return "uc.give";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.give", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "giveUsage");
            return;
        }
        Player target = r.searchPlayer(args[0]);
        if (target == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        ItemStack item;
        try {
            item = new ItemStack(ItemUtil.searchItem(args[1]));
        } catch (Exception e) {
            r.sendMes(cs, "giveItemNotFound", "%Item", args[1]);
            return;
        }
        if (item == null || item.getType() == null || item.getType().equals(Material.AIR)) {
            r.sendMes(cs, "giveItemNotFound", "%Item", args[1]);
            return;
        }
        if (InventoryUtil.isFullInventory(target.getInventory())) {
            r.sendMes(cs, "giveInventoryFull", "%Item", args[1]);
            return;
        }
        Integer amount = item.getMaxStackSize();
        if (r.checkArgs(args, 2)) {
            if (!r.isInt(args[2])) {
                r.sendMes(cs, "numberFormat", "%Number", args[2]);
                return;
            }
            amount = Integer.parseInt(args[2]);
        }
        item.setAmount(amount);
        if (r.checkArgs(args, 3)) {
            if (r.isInt(args[3])) {
                item.setDurability(Short.parseShort(args[3]));
            }
            MetaItemStack meta = new MetaItemStack(item);
            int metaStart = r.isInt(args[3]) ? 4 : 3;

            if (args.length > metaStart) {
                try {
                    String s = r.getFinalArg(args, metaStart);
                    if (s.startsWith("\\{")) {
                        item = Bukkit.getUnsafe().modifyItemStack(item, s);
                    } else {
                        try {
                            meta.parseStringMeta(cs, r.perm(cs, "uc.give.unsafe", false, false), args, metaStart);
                        } catch (IllegalArgumentException ex) {
                            if (ex.getMessage() != null && (ex.getMessage().contains("Enchantment level is either too" + " low or too high") || ex.getMessage().contains("Specified enchantment cannot be " + "applied"))) {
                                r.sendMes(cs, "enchantUnsafe");
                            }
                            return;
                        }
                        item = meta.getItemStack();
                    }
                } catch (Exception e) {
                    r.sendMes(cs, "giveMetadataFailed");
                    return;
                }
            }
        }
        InventoryUtil.addItem(target.getInventory(), item);
        r.sendMes(cs, "giveMessage", "%Item", ItemUtil.getName(item), "%Amount", amount, "%Player", target.getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
