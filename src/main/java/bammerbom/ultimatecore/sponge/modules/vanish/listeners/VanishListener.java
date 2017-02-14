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
package bammerbom.ultimatecore.sponge.modules.vanish.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.event.data.DataOfferEvent;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.vanish.api.VanishKeys;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class VanishListener {

    @Listener(order = Order.POST)
    public void onVanish(DataOfferEvent<Boolean> event, @First Player p) {
        if (event.getKey().equals(VanishKeys.VANISH)) {
            if (event.getValue().orElse(false)) {
                p.offer(Keys.VANISH, true);
                p.offer(Keys.VANISH_PREVENTS_TARGETING, true);
                p.offer(Keys.VANISH_IGNORES_COLLISION, true);
                p.offer(Keys.IS_SILENT, true);
            } else {
                p.offer(Keys.VANISH, false);
                p.offer(Keys.VANISH_PREVENTS_TARGETING, false);
                p.offer(Keys.VANISH_IGNORES_COLLISION, false);
                p.offer(Keys.IS_SILENT, false);
            }
        }
    }

    @Listener(order = Order.LAST)
    public void onJoin(ClientConnectionEvent.Join event) {
        Player p = event.getTargetEntity();
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (up.get(VanishKeys.VANISH).get()) {
            p.offer(Keys.VANISH, true);
            p.offer(Keys.VANISH_PREVENTS_TARGETING, true);
            p.offer(Keys.VANISH_IGNORES_COLLISION, true);
            p.offer(Keys.IS_SILENT, true);
            Messages.send(p, "vanish.onjoin");
        }
    }

    @Listener
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player p) {
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        if (!up.get(VanishKeys.VANISH).get()) {
            return;
        }
        ModuleConfig config = Modules.VANISH.get().getConfig().get();
        if (!config.get().getNode("events", "pickup").getBoolean()) {
            event.setCancelled(true);
        }
    }

}
