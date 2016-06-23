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
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class SignDisposal implements UltimateSign {

    @Override
    public String getName() {
        return "disposal";
    }

    @Override
    public String getPermission() {
        return "uc.sign.disposal";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.disposal", false) && !r.perm(p, "uc.sign", false)) {
            r.sendMes(p, "noPermissions");
            return;
        }
        //(p, 36, r.mes("signDisposalTitle")
    }

    @Override
    public void onCreate(ChangeSignEvent event, Player p) {
        if (!r.perm(p, "uc.sign.disposal.create", true)) {
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
        List<Text> lines = event.getTargetTile().get(Keys.SIGN_LINES).get();
        lines.set(0, Text.of(TextColors.DARK_BLUE + "[Disposal]"));
        event.getTargetTile().offer(Keys.SIGN_LINES, lines);
        r.sendMes(p, "signCreated");
    }

    @Override
    public void onDestroy(ChangeBlockEvent.Break event, Player p) {
        if (!r.perm(p, "uc.sign.disposal.destroy", true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(p, "signDestroyed");
    }
//    @Override
//    public void onClick(Player p, Sign sign) {
//        if (!r.perm(p, "uc.sign.disposal", true, false) && !r.perm(p, "uc.sign", true, false)) {
//            r.sendMes(p, "noPermissions");
//            return;
//        }
//        p.openInventory(Bukkit.createInventory(p, 36, r.mes("signDisposalTitle")));
//    }
//
//    @Override
//    public void onCreate(SignChangeEvent event) {
//        if (!r.perm(p, "uc.sign.disposal.create", false, true)) {
//            event.setCancelled(true);
//            event.getBlock().breakNaturally();
//            return;
//        }
//        event.setLine(0, TextColors.DARK_BLUE + "[Disposal]");
//        r.sendMes(p, "signCreated");
//    }
//
//    @Override
//    public void onDestroy(BlockBreakEvent event) {
//        if (!r.perm(p, "uc.sign.disposal.destroy", false, true)) {
//            event.setCancelled(true);
//            return;
//        }
//        r.sendMes(p, "signDestroyed");
//    }
}
