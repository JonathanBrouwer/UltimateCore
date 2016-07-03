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

import bammerbom.ultimatecore.bukkit.api.UWorld.WorldFlag;
import bammerbom.ultimatecore.bukkit.resources.classes.MobType.Enemies;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.classes.MobType;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GlobalWorldListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        try {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                if (UC.getWorld(e.getEntity().getLocation().getWorld()).isFlagDenied(WorldFlag.PVP)) {
                    e.setCancelled(true);
                }
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: EntityDamageByEntityEvent");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        try {
            if (e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) || e.getSpawnReason().equals(SpawnReason.CUSTOM)) {
                return;
            }
            if (e.getEntity() instanceof Monster || (MobType.fromBukkitType(e.getEntityType()) != null && MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.ENEMY)) || e
                    .getEntityType().equals(EntityType.GHAST) || e.getEntityType().equals(EntityType.SLIME)) {
                if (UC.getWorld(e.getEntity().getLocation().getWorld()).isFlagDenied(WorldFlag.MONSTER)) {
                    e.setCancelled(true);
                }
            }
            if (e.getEntity() instanceof Animals || (MobType.fromBukkitType(e.getEntityType()) != null && MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.FRIENDLY)) ||
                    MobType.fromBukkitType(e.getEntityType()) != null && MobType.fromBukkitType(e.getEntityType()).type.equals(Enemies.NEUTRAL) || e.getEntityType().equals(EntityType
                    .SQUID)) {
                if (UC.getWorld(e.getEntity().getLocation().getWorld()).isFlagDenied(WorldFlag.ANIMAL)) {
                    e.setCancelled(true);
                }
            }
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to handle event: CreatureSpawnEvent");
        }
    }
}
