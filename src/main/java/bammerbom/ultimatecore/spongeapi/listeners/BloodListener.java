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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;

public class BloodListener implements Listener {

    ArrayList<Integer> iCD = new ArrayList<>();

    public static void start() {
        if (r.getCnfg().getBoolean("Blood.Enabled")) {
            Bukkit.getPluginManager().registerEvents(new BloodListener(), r.getUC());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void bleed(final EntityDamageEvent e) {
        if (e.isCancelled() || e.getEntity().isDead()) {
            return;
        }
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (!r.getCnfg().getBoolean("Blood.Enabled")) {
            return;
        }
        if (r.getOnlinePlayersL().isEmpty()) {
            return;
        }
        if (e.getEntity() instanceof Player || !r.getCnfg().getBoolean("Blood.PlayersOnly")) {
            if (e.getEntity() instanceof Player) {
                final Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isGod()) {
                    return;
                }
                if (iCD.contains(p.getEntityId())) {
                    return;
                }
                if (((HumanEntity) e.getEntity()).getGameMode().equals(GameMode.CREATIVE) || ((HumanEntity) e.getEntity()).getGameMode().equals(GameMode.SPECTATOR)) {
                    return;
                }
                iCD.add(p.getEntityId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                    @Override
                    public void run() {
                        iCD.remove((Object) p.getEntityId());
                    }
                }, 5L);
                e.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getEntity().getLocation().add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
            } else {
                if (iCD.contains(e.getEntity().getEntityId())) {
                    return;
                }
                iCD.add(e.getEntity().getEntityId());
                World w = e.getEntity().getWorld();
                Location l = e.getEntity().getLocation();
                switch (e.getEntityType()) {
                    case SKELETON:
                        if (((Skeleton) e.getEntity()).getSkeletonType().equals(SkeletonType.NORMAL)) {
                            w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.BONE_BLOCK, (byte) 0));
                        } else {
                            w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.COAL_BLOCK, (byte) 0));
                        }
                        break;
                    case CREEPER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.STAINED_CLAY, (byte) 5));
                        break;
                    case SPIDER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.6, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case GIANT:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case ZOMBIE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case SLIME:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.SLIME_BLOCK, (byte) 0));
                        break;
                    case GHAST:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 1.5F, 1.5F, 1.5F, new MaterialData(Material.BONE_BLOCK, (byte) 0));
                        break;
                    case PIG_ZOMBIE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.STAINED_CLAY, (byte) 10));
                        break;
                    case ENDERMAN:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.ENDER_CHEST, (byte) 0));
                        break;
                    case CAVE_SPIDER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.6, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case SILVERFISH:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.1, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.STONE, (byte) 0));
                        break;
                    case BLAZE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.7, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.LAVA, (byte) 0));
                        break;
                    case MAGMA_CUBE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.LAVA, (byte) 0));
                        break;
                    case ENDER_DRAGON:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case WITHER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 1.5F, 1.5F, 1.5F, new MaterialData(Material.SOUL_SAND, (byte) 0));
                        break;
                    case BAT:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 20, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case WITCH:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case ENDERMITE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.1, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.ENDER_CHEST, (byte) 0));
                        break;
                    case GUARDIAN:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.8, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.LAVA, (byte) 0));
                        break;
                    case PIG:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.6, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case SHEEP:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case COW:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case CHICKEN:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.6, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case SQUID:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.COAL_BLOCK, (byte) 0));
                        break;
                    case WOLF:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.6, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case MUSHROOM_COW:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case SNOWMAN:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.SNOW_BLOCK, (byte) 0));
                        break;
                    case POLAR_BEAR:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.SNOW_BLOCK, (byte) 0));
                        break;
                    case OCELOT:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.2, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case IRON_GOLEM:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.IRON_BLOCK, (byte) 0));
                        break;
                    case HORSE:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case RABBIT:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 0.2, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case VILLAGER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case PLAYER:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                        break;
                    case ARMOR_STAND:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.WOOD, (byte) 0));
                        break;
                    default:
                        w.spawnParticle(Particle.BLOCK_CRACK, l.add(0, 1.0, 0), 50, 0.3F, 0.3F, 0.3F, new MaterialData(Material.REDSTONE_BLOCK, (byte) 0));
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {

                    @Override
                    public void run() {
                        iCD.remove((Object) e.getEntity().getEntityId());
                    }
                }, 10L);
            }

        }
    }

}
