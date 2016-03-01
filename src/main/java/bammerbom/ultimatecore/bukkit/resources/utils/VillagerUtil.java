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
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class VillagerUtil {

    public static void clearTrades(Villager villager) {
        villager.getRecipes().clear();
    }

    public static void addTrade(Villager villager, VillagerTrade villagerTrade) {
        MerchantRecipe recipe = new MerchantRecipe(villagerTrade.getRewardItem(), villagerTrade.getUses(), villagerTrade.getMaxUses(), villagerTrade.getRewardExp());
        recipe.addIngredient(villagerTrade.getItem1());
        recipe.addIngredient(villagerTrade.getItem2());
        villager.setRecipe(villager.getRecipeCount(), recipe);
    }

    public static List<VillagerTrade> listTrades(Villager villager) {
        try {
            List<VillagerTrade> trades = new ArrayList();
            for (MerchantRecipe trade : villager.getRecipes()) {
                if (trade.getIngredients().size() == 1) {
                    VillagerTrade t = new VillagerTrade(trade.getIngredients().get(0), trade.getResult(), trade.getMaxUses(), trade.getUses(), trade.hasExperienceReward());
                    trades.add(t);
                } else if (trade.getIngredients().size() == 2) {
                    VillagerTrade t = new VillagerTrade(trade.getIngredients().get(0), trade.getIngredients().get(1), trade.getResult(), trade.getMaxUses(), trade.getUses(), trade
                            .hasExperienceReward());
                    trades.add(t);
                }
            }
            return trades;
        } catch (Exception exc) {
            ErrorLogger.log(exc, "Failed to list villager trades.");
            return null;
        }
    }

    public static void setTrades(Villager villager, List<VillagerTrade> trades) {
        clearTrades(villager);
        for (VillagerTrade trade : trades) {
            addTrade(villager, trade);
        }
    }

    public static final class VillagerTrade {
        public Integer maxUses;
        public Integer uses;
        public Boolean rewardExp;
        public ItemStack item1;
        public ItemStack item2;
        public ItemStack rewardItem;

        public VillagerTrade(ItemStack item1, ItemStack item2, ItemStack rewardItem, Integer maxUses, Integer uses, Boolean rewardExp) {
            this.item1 = item1;
            this.item2 = item2;
            this.rewardItem = rewardItem;
            this.maxUses = maxUses;
            this.uses = uses;
            this.rewardExp = rewardExp;
        }

        public VillagerTrade(ItemStack item1, ItemStack rewardItem, Integer maxUses, Integer uses, Boolean rewardExp) {
            this.item1 = item1;
            this.rewardItem = rewardItem;
            this.maxUses = maxUses;
            this.uses = uses;
            this.rewardExp = rewardExp;
        }

        public boolean hasItem2() {
            return item2 != null;
        }

        public ItemStack getItem1() {
            return item1;
        }

        public ItemStack getItem2() {
            return item2;
        }

        public ItemStack getRewardItem() {
            return rewardItem;
        }

        public Integer getMaxUses() {
            return maxUses;
        }

        public Integer getUses() {
            return uses;
        }

        public Boolean getRewardExp() {
            return rewardExp;
        }
    }
}

