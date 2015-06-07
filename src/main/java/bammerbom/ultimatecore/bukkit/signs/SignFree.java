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
package bammerbom.ultimatecore.bukkit.signs;

import bammerbom.ultimatecore.bukkit.UltimateSign;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignFree implements UltimateSign {

    @Override
    public String getName() {
        return "free";
    }

    @Override
    public String getPermission() {
        return "uc.sign.free";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.free", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        ItemStack item = ItemUtil.searchItem(sign.getLine(1));
        item.setAmount(item.getMaxStackSize());
        Inventory inv = Bukkit.createInventory(p, 36, ItemUtil.getName(item));
        for (int i = 0;
             i < 36;
             i++) {
            inv.addItem(item);
        }
        p.openInventory(inv);
    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.free.create", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        if (ItemUtil.searchItem(event.getLine(1)) == null) {
            r.sendMes(event.getPlayer(), "giveItemNotFound", "%Item", event.getLine(1));
            event.setLine(0, ChatColor.RED + "[Free]");
            return;
        }
        event.setLine(0, ChatColor.DARK_BLUE + "[Free]");
        r.sendMes(event.getPlayer(), "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.free.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getPlayer(), "signDestroyed");
    }
}
