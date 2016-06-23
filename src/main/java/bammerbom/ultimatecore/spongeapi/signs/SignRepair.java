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
package bammerbom.ultimatecore.spongeapi.signs;

import bammerbom.ultimatecore.spongeapi.UltimateSign;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class SignRepair implements UltimateSign {

    @Override
    public String getName() {
        return "repair";
    }

    @Override
    public String getPermission() {
        return "uc.sign.repair";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.repair", true, false) && !r.perm(p, "uc.sign", true, false)) {
            r.sendMes(p, "noPermissions");
            return;
        }
        Boolean all = sign.getLine(1).equalsIgnoreCase("all") || sign.getLine(1).equalsIgnoreCase("*");
        if (all) {
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
            r.sendMes(p, "repairSelfAll");
        } else {
            ItemStack stack = p.getItemInHand();
            if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
                r.sendMes(p, "repairNoItemInHand");
                return;
            }
            if (!ItemUtil.isRepairable(stack)) {
                r.sendMes(p, "repairNotRepairable");
                return;
            }
            stack.setDurability((short) 0);
            r.sendMes(p, "repairSelfHand");
        }
    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(p, "uc.sign.repair", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        event.setLine(0, TextColors.DARK_BLUE + "[Repair]");
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(p, "uc.sign.repair.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(p, "signDestroyed");
    }
}
