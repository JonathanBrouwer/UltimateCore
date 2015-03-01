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
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TreeListener implements Listener {

    public static void start() {
        Bukkit.getServer().getPluginManager().registerEvents(new TreeListener(), r.getUC());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Material block = event.getBlock().getType();
        if (!block.equals(Material.LOG) && !block.equals(Material.LOG_2)) {
            return;
        }
        if (r.getCnfg().getBoolean("Timber.Enable") == false) {
            return;
        }
        Player player = event.getPlayer();
        Location brokenBlock = event.getBlock().getLocation();
        if (r.perm(player, "uc.timber", true, false)) {
            Timb(player, block, brokenBlock);
        }
    }

    public boolean isTool(ItemStack handItem) {
        return !((!handItem.getType().name().toLowerCase().contains("axe")) || (handItem.getType().name().toLowerCase().contains("pickaxe")));
    }

    public void Timb(Player player, Material block, Location brokenBlock) {
        ItemStack handItem = player.getItemInHand();
        World world = player.getWorld();

        if ((player.isSneaking())) {
            return;
        }
        if (isTool(handItem) || r.getCnfg().getBoolean("Timber.NeedAxe") == false) {
            double x = brokenBlock.getBlockX();
            double x2 = brokenBlock.getBlockX() - 3;
            double x3 = brokenBlock.getBlockX() - 3;

            double y = brokenBlock.getBlockY();
            double y2 = brokenBlock.getBlockY() - 2;

            double z = brokenBlock.getBlockZ();
            double z2 = brokenBlock.getBlockZ() - 3;
            double z3 = brokenBlock.getBlockZ() - 3;

            double height = 1D;

            boolean logsLeft = true;
            boolean isTree = false;
            while (logsLeft) {
                y += 1D;
                height += 1D;
                Location blockAbove = new Location(world, x, y, z);

                Material blockAboveType = blockAbove.getBlock().getType();

                if (blockAboveType == Material.LOG || blockAboveType == Material.LOG_2) {
                    blockAbove.getBlock().breakNaturally();
                    player.getWorld().playEffect(blockAbove, Effect.SMOKE, 4);

                    if ((!player.getGameMode().equals(GameMode.CREATIVE))) {
                        int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                        long random = Math.round(Math.random() * enchLvl);

                        if ((random == 0L)
                                && (handItem.getType().getMaxDurability() > handItem.getDurability())) {
                            handItem.setDurability((short) (handItem.getDurability() + 1));
                        }
                    }

                    logsLeft = true;
                } else if (blockAboveType == Material.LEAVES || blockAboveType == Material.LEAVES_2) {
                    logsLeft = false;
                    isTree = true;
                } else {
                    logsLeft = false;
                    isTree = false;
                }
            }
            if (isTree == true) {

                for (int yVec = 0; yVec < height + 6.0D; yVec++) {
                    x2 = x3;
                    z2 = z3;
                    for (int xVec = 0; xVec < 7; xVec++) {
                        z2 = z3;
                        for (int zVec = 0; zVec < 7; zVec++) {
                            Location surround = new Location(world, x2, y2, z2);
                            Material surroundType = surround.getBlock().getType();

                            if (surroundType == Material.LEAVES || surroundType == Material.LEAVES_2) {
                                if (r.getCnfg().getBoolean("Timber.Leaves") == true) {
                                    surround.getBlock().breakNaturally();
                                }
                            }

                            if (surroundType == Material.LOG || surroundType == Material.LOG_2) {
                                surround.getBlock().breakNaturally();
                                player.getWorld().playEffect(surround, Effect.SMOKE, 4);
                                if (r.getCnfg().getBoolean("Timber.AllDurability") == true) {

                                    if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                                        int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                                        long random = Math.round(Math.random() * enchLvl);

                                        if (random == 0L) {
                                            if (handItem.getType().getMaxDurability() > handItem.getDurability()) {
                                                handItem.setDurability((short) (handItem.getDurability() + 1));
                                            } else if (handItem.getDurability() == handItem.getType().getMaxDurability()) {
                                                player.getInventory().setItemInHand(null);
                                            }
                                        }
                                    }
                                }
                            }
                            z2 += 1D;
                        }
                        x2 += 1D;
                    }
                    y2 += 1D;
                }
            }
        }
    }
}
