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
package bammerbom.ultimatecore.sponge.modules.inventory.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.inventory.api.InventoryKeys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.filter.type.Exclude;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

public class InventoryListener {
    @Listener
    @Exclude({ClickInventoryEvent.Close.class, ClickInventoryEvent.Open.class})
    public void onInteract(ClickInventoryEvent event, @Root Player p) {
        //Get target inventory owner
        Inventory inv = event.getTargetInventory();
        if (!(inv instanceof CarriedInventory)) return;
        CarriedInventory cinv = (CarriedInventory) inv;
        if (!cinv.getCarrier().isPresent() || !(cinv.getCarrier().get() instanceof User)) return;
        User t = (User) cinv.getCarrier().get();

        //Check if player is in invsee & Cancel event if player doesn't have permission
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(InventoryKeys.INVSEE_TARGET).isPresent() && up.get(InventoryKeys.INVSEE_TARGET).get().equals(t.getUniqueId())) {
            if (!p.hasPermission("uc.inventory.invsee.modify")) {
                event.getCursorTransaction().setValid(false);
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onCloseInventory(InteractInventoryEvent.Close event, @Root Player p) {
        //Player closed invsee
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        up.offer(InventoryKeys.INVSEE_TARGET, null);
    }
}
