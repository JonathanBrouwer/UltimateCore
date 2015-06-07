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
import bammerbom.ultimatecore.spongeapi.resources.utils.ParticleUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ParticleUtil.BlockData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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
        if (e.getEntity() == null || !(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (r.getCnfg().getBoolean("Blood.Enabled") == false) {
            return;
        }
        if (r.getOnlinePlayersL().isEmpty()) {
            return;
        }
        if (e.getEntity() instanceof Player || r.getCnfg().getBoolean("Blood.PlayersOnly") == false) {
            if (e.getEntity() instanceof Player) {
                final Player p = (Player) e.getEntity();
                if (UC.getPlayer(p).isGod()) {
                    return;
                }
                if (iCD.contains(p.getEntityId())) {
                    return;
                }
                if (((HumanEntity) e.getEntity()).getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }
                iCD.add(p.getEntityId());
                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                    @Override
                    public void run() {
                        iCD.remove((Object) p.getEntityId());
                    }
                }, 5L);
                ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
            } else {
                if (iCD.contains(e.getEntity().getEntityId())) {
                    return;
                }
                iCD.add(e.getEntity().getEntityId());
                switch (e.getEntityType()) {
                    case SKELETON:
                        if (((Skeleton) e.getEntity()).getSkeletonType().equals(SkeletonType.NORMAL)) {
                            ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.WOOL, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        } else {
                            ParticleUtil.BLOCK_CRACK
                                    .display(new BlockData(Material.COAL_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        }
                        break;
                    case CREEPER:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.WOOL, (byte) 13), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case SPIDER:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.6, 0), r.getOnlinePlayersL());
                        break;
                    case GIANT:
                        //TODO
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case ZOMBIE:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case SLIME:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.SLIME_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case GHAST:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.QUARTZ_ORE, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case PIG_ZOMBIE:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.STAINED_CLAY, (byte) 10), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case ENDERMAN:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.ENDER_CHEST, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case CAVE_SPIDER:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.6, 0), r.getOnlinePlayersL());
                        break;
                    case SILVERFISH:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.STONE, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation(), r.getOnlinePlayersL());
                        break;
                    case BLAZE:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.LAVA, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.7, 0), r.getOnlinePlayersL());
                        break;
                    case MAGMA_CUBE:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.LAVA, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case ENDER_DRAGON:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case WITHER:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.8F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case BAT:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 20, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case WITCH:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 20, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case ENDERMITE:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.ENDER_CHEST, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation(), r.getOnlinePlayersL());
                        break;
                    case GUARDIAN:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.LAVA, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.8, 0), r.getOnlinePlayersL());
                        break;
                    case PIG:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case SHEEP:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case COW:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case CHICKEN:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case SQUID:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.COAL_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case WOLF:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case MUSHROOM_COW:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case SNOWMAN:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.SNOW_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case OCELOT:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.2, 0), r.getOnlinePlayersL());
                        break;
                    case IRON_GOLEM:
                        ParticleUtil.BLOCK_CRACK.display(new BlockData(Material.IRON_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case HORSE:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case RABBIT:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 0.3, 0), r.getOnlinePlayersL());
                        break;
                    case VILLAGER:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    case PLAYER:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
                        break;
                    default:
                        ParticleUtil.BLOCK_CRACK
                                .display(new BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.3F, 50, e.getEntity().getLocation().add(0, 1.0, 0), r.getOnlinePlayersL());
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
