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
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.SkeletonTypes;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class BloodListener {

    ArrayList<UUID> iCD = new ArrayList<>();

    public static void start() {
        if (r.getCnfg().getBoolean("Blood.Enabled")) {
            Sponge.getGame().getEventManager().registerListeners(r.getUC(), new BloodListener());
        }
    }

    @Listener(order = Order.POST)
    public void bleed(final DamageEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() == null) {
            return;
        }
        if (r.getCnfg().getBoolean("Blood.Enabled") == false) {
            return;
        }
        if (e.getNewData().get(Keys.HEALTH).get().doubleValue() > e.getOldData().get(Keys.HEALTH).get().doubleValue()) {
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
                if (iCD.contains(p.getUniqueId())) {
                    return;
                }
                if (p.get(Keys.GAME_MODE).get().equals(GameModes.CREATIVE) || p.get(Keys.GAME_MODE).get().equals(GameModes.SPECTATOR)) {
                    return;
                }
                iCD.add(p.getUniqueId());
                Sponge.getGame().getScheduler().createTaskBuilder().name("UC: Blood task").delayTicks(5L).execute(new Runnable() {
                    @Override
                    public void run() {
                        iCD.remove(p.getUniqueId());
                    }
                }).submit(r.getUC());
                p.getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                        .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), p.getLocation().getPosition().add(0, 1, 0));
            } else {
                if (iCD.contains(e.getEntity().getUniqueId())) {
                    return;
                }
                iCD.add(e.getEntity().getUniqueId());
                if (e.getEntity().getType().equals(EntityTypes.SKELETON)) {
                    if (e.getEntity().get(Keys.SKELETON_TYPE).get().equals(SkeletonTypes.NORMAL)) {
                        e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                                .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.WOOL).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                    } else {
                        e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                                .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.COAL_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                    }
                } else if (e.getEntity().getType().equals(EntityTypes.CREEPER)) {
                    ItemStack green = Sponge.getGame().getRegistry().createItemBuilder().itemType(ItemTypes.WOOL).build();
                    green.offer(Keys.DYE_COLOR, DyeColors.GREEN);
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).item(green).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SPIDER)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 0.6, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.GIANT)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.ZOMBIE)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SLIME)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.SLIME).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.GHAST)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.QUARTZ_ORE).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.PIG_ZOMBIE)) {
                    ItemStack purple = Sponge.getGame().getRegistry().createItemBuilder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
                    purple.offer(Keys.DYE_COLOR, DyeColors.PURPLE);
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).item(purple).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.ENDERMAN)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.ENDER_CHEST).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.CAVE_SPIDER)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 0.6, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SILVERFISH)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.STONE).build(), e.getEntity().getLocation().getPosition());
                } else if (e.getEntity().getType().equals(EntityTypes.BLAZE)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.LAVA).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.MAGMA_CUBE)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.LAVA).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.WITHER)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.BAT)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(20)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.WITCH)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.ENDERMITE)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.ENDER_CHEST).build(), e.getEntity().getLocation().getPosition());
                } else if (e.getEntity().getType().equals(EntityTypes.GUARDIAN)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.SEA_LANTERN).build(), e.getEntity().getLocation().getPosition().add(0, 0.8, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.PIG)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SHEEP)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.COW)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.CHICKEN)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 0.6, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SQUID)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.COAL_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.WOLF)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.MUSHROOM_COW)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.SNOWMAN)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.SNOW).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.OCELOT)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 0.2, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.IRON_GOLEM)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.IRON_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.HORSE)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.RABBIT)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 0.3, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.VILLAGER)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                } else if (e.getEntity().getType().equals(EntityTypes.PLAYER)) {
                    e.getEntity().getWorld().spawnParticles(((ParticleEffectBuilder.Material) Sponge.getGame().getRegistry().createParticleEffectBuilder(ParticleTypes.BLOCK_CRACK)).count(50)
                            .offset(new Vector3d(0.3, 0.3, 0.3)).itemType(ItemTypes.REDSTONE_BLOCK).build(), e.getEntity().getLocation().getPosition().add(0, 1, 0));
                }

                Sponge.getGame().getScheduler().createTaskBuilder().name("UC: Blood task").delayTicks(5L).execute(new Runnable() {
                    @Override
                    public void run() {
                        iCD.remove(e.getEntity().getUniqueId());
                    }
                }).submit(r.getUC());
            }

        }
    }

}