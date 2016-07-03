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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.type.SkeletonTypes;
import org.spongepowered.api.effect.particle.BlockParticle;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import java.util.ArrayList;
import java.util.UUID;

public class BloodListener {

    ArrayList<UUID> iCD = new ArrayList<>();

    public static void start() {
        if (r.getCnfg().getBoolean("Blood.Enabled")) {
            Sponge.getEventManager().registerListeners(r.getUC(), new BloodListener());
        }
    }

    @Listener(order = Order.POST)
    public void bleed(final DamageEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getTargetEntity() == null) {
            return;
        }
        if (r.getCnfg().getBoolean("Blood.Enabled") == false) {
            return;
        }
        if (r.getOnlinePlayers().length == 0) {
            return;
        }
        Entity en = e.getTargetEntity();
        if (en instanceof Player || r.getCnfg().getBoolean("Blood.PlayersOnly") == false) {
            if (en instanceof Player) {
                final Player p = (Player) en;
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
                p.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                        (BlockTypes.REDSTONE_BLOCK).build()).build(), p.getLocation().getPosition().add(0, 1, 0));
                p.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                        (BlockTypes.REDSTONE_BLOCK).build()).build(), p.getLocation().getPosition().add(0, 1, 0));
            } else {
                if (iCD.contains(en.getUniqueId())) {
                    return;
                }
                iCD.add(en.getUniqueId());
                if (en.getType().equals(EntityTypes.SKELETON)) {
                    if (en.get(Keys.SKELETON_TYPE).get().equals(SkeletonTypes.NORMAL)) {
                        en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder()
                                .blockType(BlockTypes.WOOL).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                    } else {
                        en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder()
                                .blockType(BlockTypes.COAL_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                    }
                } else if (en.getType().equals(EntityTypes.CREEPER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.WOOL).add(Keys.DYE_COLOR, DyeColors.GREEN).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.SPIDER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 0.6, 0));
                } else if (en.getType().equals(EntityTypes.GIANT)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.ZOMBIE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.SLIME)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.SLIME).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.GHAST)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.QUARTZ_ORE).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.PIG_ZOMBIE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.STAINED_HARDENED_CLAY).add(Keys.DYE_COLOR, DyeColors.PURPLE).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.ENDERMAN)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.ENDER_CHEST).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.CAVE_SPIDER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 0.6, 0));
                } else if (en.getType().equals(EntityTypes.SILVERFISH)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.STONE).build()).build(), en.getLocation().getPosition());
                } else if (en.getType().equals(EntityTypes.BLAZE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.LAVA).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.MAGMA_CUBE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.LAVA).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.WITHER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.BAT)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(20).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.WITCH)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.ENDERMITE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.ENDER_CHEST).build()).build(), en.getLocation().getPosition());
                } else if (en.getType().equals(EntityTypes.GUARDIAN)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.SEA_LANTERN).build()).build(), en.getLocation().getPosition().add(0, 0.8, 0));
                } else if (en.getType().equals(EntityTypes.PIG)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.SHEEP)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.COW)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.CHICKEN)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 0.6, 0));
                } else if (en.getType().equals(EntityTypes.SQUID)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.COAL_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.WOLF)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.MUSHROOM_COW)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.SNOWMAN)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.SNOW).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.OCELOT)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 0.2, 0));
                } else if (en.getType().equals(EntityTypes.IRON_GOLEM)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.IRON_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.HORSE)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.RABBIT)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 0.3, 0));
                } else if (en.getType().equals(EntityTypes.VILLAGER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.ARMOR_STAND)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.PLANKS).build()).build(), en.getLocation().getPosition().add(0, 1, 0));
                } else if (en.getType().equals(EntityTypes.PLAYER)) {
                    en.getWorld().spawnParticles(BlockParticle.builder().type(ParticleTypes.BLOCK_CRACK).count(50).offset(new Vector3d(0.3, 0.3, 0.3)).block(BlockState.builder().blockType
                            (BlockTypes.REDSTONE_BLOCK).build()).build(), en.getLocation().getPosition().add(0, 1, 0));

                    Sponge.getGame().getScheduler().createTaskBuilder().name("UC: Blood task").delayTicks(5L).execute(new Runnable() {
                        @Override
                        public void run() {
                            iCD.remove(en.getUniqueId());
                        }
                    }).submit(r.getUC());
                }

            }
        }

    }
}
