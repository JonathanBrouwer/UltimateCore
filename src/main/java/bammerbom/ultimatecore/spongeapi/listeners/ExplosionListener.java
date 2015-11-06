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
package bammerbom.ultimatecore.spongeapi.listeners;

import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;

public class ExplosionListener {

    Boolean creeper = r.getCnfg().getBoolean("Explode.Creeper");
    Boolean tnt = r.getCnfg().getBoolean("Explode.TNT");
    Boolean ghast = r.getCnfg().getBoolean("Explode.Ghast");
    Boolean enderdragon = r.getCnfg().getBoolean("Explode.Enderdragon");
    Boolean wither = r.getCnfg().getBoolean("Explode.Wither");
    Boolean lightning = r.getCnfg().getBoolean("Explode.Lightning");

    public static void start() {
        r.getGame().getEventManager().registerListeners(r.getUC(), new ExplosionListener());
    }

    @Listener(order = Order.LATE)
    public void explosionListener(EntityExplodeEvent e) {
        try {
            if (e.getEntityType() != null) {
                if (creeper && e.getEntityType().equals(EntityType.CREEPER)) {
                    e.setYield(0.0F);
                    e.setCancelled(true);
                }
                if (tnt && (e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT))) {
                    e.setYield(0.0F);
                    e.setCancelled(true);
                }
                if (ghast && (e.getEntityType().equals(EntityType.GHAST) || e.getEntity() instanceof Fireball || e.getEntity() instanceof LargeFireball)) {
                    e.setYield(0.0F);
                    e.setCancelled(true);
                }
                if (enderdragon && (e.getEntityType().equals(EntityType.ENDER_DRAGON) || e.getEntityType().equals(EntityType.ENDER_CRYSTAL))) {
                    e.setCancelled(true);
                }
                if (wither && (e.getEntityType().equals(EntityType.WITHER) || e.getEntityType().equals(EntityType.WITHER_SKULL))) {
                    e.setYield(0.0F);
                    e.setCancelled(true);
                }
                if (lightning && e.getEntityType().equals(EntityType.LIGHTNING)) {
                    e.setYield(0.0F);
                    e.setCancelled(true);
                }
            }
        } catch (Exception exc) {
        }
    }

}
