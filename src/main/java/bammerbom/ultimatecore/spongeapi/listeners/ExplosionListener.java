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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.explosion.Explosion;

import java.util.Optional;

public class ExplosionListener {

    Boolean creeper = r.getCnfg().getBoolean("Explode.Creeper");
    Boolean tnt = r.getCnfg().getBoolean("Explode.TNT");
    Boolean ghast = r.getCnfg().getBoolean("Explode.Ghast");
    Boolean enderdragon = r.getCnfg().getBoolean("Explode.Enderdragon");
    Boolean wither = r.getCnfg().getBoolean("Explode.Wither");
    Boolean lightning = r.getCnfg().getBoolean("Explode.Lightning");

    public static void start() {
        Sponge.getEventManager().registerListeners(r.getUC(), new ExplosionListener());
    }

    @Listener(order = Order.DEFAULT)
    public void explosionListener(ExplosionEvent.Pre e) {
        Optional<Entity> en = e.getCause().first(Entity.class);
        if (!en.isPresent()) {
            return;
        }
        EntityType et = en.get().getType();
        try {
            if (et != null) {
                if (creeper && et.equals(EntityTypes.CREEPER)) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
                if (tnt && (et.equals(EntityTypes.TNT_MINECART) || et.equals(EntityTypes.PRIMED_TNT))) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
                if (ghast && (et.equals(EntityTypes.GHAST) || et.equals(EntityTypes.FIREBALL))) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
                if (enderdragon && (et.equals(EntityTypes.ENDER_CRYSTAL) || et.equals(EntityTypes.ENDER_DRAGON) || et.equals(EntityTypes.DRAGON_FIREBALL))) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
                if (wither && (et.equals(EntityTypes.WITHER) || et.equals(EntityTypes.WITHER_SKULL))) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
                if (lightning && (et.equals(EntityTypes.LIGHTNING) || et.equals(EntityTypes.WEATHER))) {
                    e.setExplosion(Explosion.builder().from(e.getExplosion()).shouldBreakBlocks(false).canCauseFire(false).build());
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
