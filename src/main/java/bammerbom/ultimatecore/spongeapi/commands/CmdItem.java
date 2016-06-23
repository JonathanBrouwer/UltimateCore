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
import bammerbom.ultimatecore.spongeapi.resources.classes.MetaItemStack;
import bammerbom.ultimatecore.spongeapi.resources.utils.InventoryUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CmdItem implements UltimateCommand {

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public String getPermission() {
        return "uc.item";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("i");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.item", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "itemUsage");
            return;
        }
        ItemStack item;
        try {
            item = new ItemStack(ItemUtil.searchItem(args[0]));
        } catch (Exception e) {
            r.sendMes(cs, "itemItemNotFound", "%Item", args[0]);
            return;
        }
        if (item == null || item.getType() == null || item.getType().equals(Material.AIR)) {
            r.sendMes(cs, "itemItemNotFound", "%Item", args[0]);
            return;
        }
        if (InventoryUtil.isFullInventory(p.getInventory())) {
            r.sendMes(cs, "itemInventoryFull");
            return;
        }
        Integer amount = item.getMaxStackSize();
        if (r.checkArgs(args, 1)) {
            if (!r.isInt(args[1])) {
                r.sendMes(cs, "numberFormat", "%Number", args[1]);
                return;
            }
            amount = Integer.parseInt(args[1]);
        }
        item.setAmount(amount);
        if (r.checkArgs(args, 2)) {
            if (r.isInt(args[2])) {
                item.setDurability(Short.parseShort(args[2]));
            }
            MetaItemStack meta = new MetaItemStack(item);
            int metaStart = r.isInt(args[2]) ? 3 : 2;

            if (args.length > metaStart) {
                try {
                    String s = r.getFinalArg(args, metaStart);
                    if (s.startsWith("\\{")) {
                        item = Bukkit.getUnsafe().modifyItemStack(item, s);
                    } else {
                        try {
                            meta.parseStringMeta(cs, r.perm(cs, "uc.item.unsafe", false, false), args, metaStart);
                            item = meta.getItemStack();
                        } catch (IllegalArgumentException ex) {
                            if (ex.getMessage() != null && ex.getMessage().contains("Enchantment level is either too " + "low or too high")) {
                                r.sendMes(cs, "enchantUnsafe");
                                return;
                            } else {
                                r.sendMes(cs, "itemMetadataFailed");
                            }
                            return;
                        }

                    }
                } catch (Exception e) {
                    r.sendMes(cs, "itemMetadataFailed");
                    return;
                }
            }
        }
        InventoryUtil.addItem(p.getInventory(), item);
        r.sendMes(cs, "itemMessage", "%Item", ItemUtil.getName(item), "%Amount", amount, "%Player", r.getDisplayName(p));
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
