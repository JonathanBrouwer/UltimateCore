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
import com.google.common.base.Optional;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.event.entity.player.PlayerBreakBlockEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

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
        if (!r.perm(p, "uc.sign.repair", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        Boolean all = ((Text.Literal) sign.getData().get().lines().get(1)).getContent().equalsIgnoreCase("all") || ((Text.Literal) sign.getData().get().lines().get(1)).getContent()
                .equalsIgnoreCase("*");
        if (all) {
            for (Inventory inv : p.getInventory()) {
                Optional<ItemStack> stackOptional = inv.peek();
                if (!stackOptional.isPresent()) {
                    continue;
                }
                ItemStack stack = stackOptional.get();
                if (!stack.supports(Keys.ITEM_DURABILITY)) {
                    continue;
                }
                stack.offer(Keys.ITEM_DURABILITY, 0);
            }
            r.sendMes(p, "repairSelfAll");
        } else {
            Optional<ItemStack> stack = p.getItemInHand();
            if (!stack.isPresent()) {
                r.sendMes(p, "repairNoItemInHand");
                return;
            }
            if (!stack.get().supports(Keys.ITEM_DURABILITY)) {
                r.sendMes(p, "repairNotRepairable");
                return;
            }
            stack.get().offer(Keys.ITEM_DURABILITY, 0);
            r.sendMes(p, "repairSelfHand");
        }
    }

    @Override
    public void onCreate(SignChangeEvent event, Player p) {
        if (!r.perm(p, "uc.sign.repair", false, true)) {
            event.setCancelled(true);
            event.getTile().getLocation().digBlock();
            return;
        }
        event.setNewData(event.getNewData().set(Keys.SIGN_LINES, event.getNewData().lines().set(0, Texts.of(TextColors.DARK_BLUE + "[Repair]")).get()));
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(PlayerBreakBlockEvent event) {
        if (!r.perm(event.getUser(), "uc.sign.repair.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getUser(), "signDestroyed");
    }
}
