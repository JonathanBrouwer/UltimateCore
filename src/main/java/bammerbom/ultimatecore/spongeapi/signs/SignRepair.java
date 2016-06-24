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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.EmptyInventory;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

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
        if (!r.perm(p, "uc.sign.repair", false) && !r.perm(p, "uc.sign", false)) {
            r.sendMes(p, "noPermissions");
            return;
        }
        Boolean all = sign.lines().get(1).toPlain().equalsIgnoreCase("all") || sign.lines().get(1).toPlain().equalsIgnoreCase("*");
        if (all) {
            Inventory inv = p.getInventory().next();
            while (!(stack instanceof EmptyInventory)) {
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
    public void onCreate(ChangeSignEvent event, Player p) {
        if (!r.perm(p, "uc.sign.repair.create", true)) {
            event.setCancelled(true);
            event.getTargetTile().getLocation().setBlockType(BlockTypes.AIR);
            Optional<Entity> eno = event.getTargetTile().getLocation().getExtent().createEntity(EntityTypes.ITEM, event.getTargetTile().getLocation().getPosition());
            if (eno.isPresent()) {
                Item en = (Item) eno.get();
                en.offer(Keys.REPRESENTED_ITEM, ItemStack.builder().itemType(ItemTypes.SIGN).build().createSnapshot());
                p.getWorld().spawnEntity(en, Cause.builder().named(NamedCause.simulated(p)).build());
            }
            return;
        }
        event.getText().set(Keys.SIGN_LINES, event.getText().lines().set(0, Text.of(TextColors.DARK_BLUE + "[Repair]")).get());
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(ChangeBlockEvent.Break event, Player p) {
        if (!r.perm(p, "uc.sign.repair.destroy", true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(p, "signDestroyed");
    }
}
