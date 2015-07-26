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
package bammerbom.ultimatecore.spongeapi.resources.utils;

import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VillagerUtil {

    public static void clearTrades(Villager villager) {
        /*try {
            EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
            Field recipes = entityVillager.getClass().getDeclaredField("br");
            recipes.setAccessible(true);
            MerchantRecipeList list = new MerchantRecipeList();
            recipes.set(entityVillager, list);
        } catch (Exception exc) {
            exc.printStackTrace();
        }*/
        try {
            Object entityVillager = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.execute("getHandle()", villager).fetch();
            Field recipes = entityVillager.getClass().getDeclaredField(bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.NMS_PATH.contains("v1_8_R1") ? "bp" : "br");
            recipes.setAccessible(true);
            bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject list = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject
                    .fromNMS("MerchantRecipeList");
            recipes.set(entityVillager, list.fetch());
        } catch (Exception exc) {
            ErrorLogger.log(exc, "Failed to clear trade.");
        }

        //

    }

    public static void addTrade(Villager villager, VillagerTrade villagerTrade) {
        /*EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
        try {
            Field recipes = entityVillager.getClass().getDeclaredField("br");
            recipes.setAccessible(true);
            MerchantRecipeList list = (MerchantRecipeList) recipes.get(entityVillager);
            if (VillagerTrade.hasItem2(villagerTrade)) {
                ItemStack item1 = CraftItemStack.asNMSCopy(VillagerTrade.getItem1(villagerTrade));
                ItemStack item2 = CraftItemStack.asNMSCopy(VillagerTrade.getItem2(villagerTrade));
                ItemStack rewardItem = CraftItemStack.asNMSCopy(VillagerTrade.getRewardItem(villagerTrade));
                list.a(new MerchantRecipe(item1, item2, rewardItem));
            } else {
                ItemStack item1 = CraftItemStack.asNMSCopy(VillagerTrade.getItem1(villagerTrade));
                ItemStack rewardItem = CraftItemStack.asNMSCopy(VillagerTrade.getRewardItem(villagerTrade));
                list.a(new MerchantRecipe(item1, rewardItem));
            }
            recipes.set(entityVillager, list);
        } catch (Exception exc) {
            exc.printStackTrace();
        }*/
        try {
            Object entityVillager = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.execute("getHandle()", villager).fetch();
            Field recipes = entityVillager.getClass().getDeclaredField(bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.NMS_PATH.contains("v1_8_R1") ? "bp" : "br");
            recipes.setAccessible(true);
            List list = (List) recipes.get(entityVillager);
            if (villagerTrade.hasItem2()) {
                Object item1 = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asNMSCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), villagerTrade.getItem1())
                        .fetch();
                Object item2 = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asNMSCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), villagerTrade.getItem2())
                        .fetch();
                Object reward = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asNMSCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), villagerTrade
                                .getRewardItem()).fetch();
                bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject recipe = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject
                        .fromNMS("MerchantRecipe", item1, item2, reward, villagerTrade.getUses(), villagerTrade.getMaxUses());
                recipe.set("rewardExp", villagerTrade.getRewardExp());
                list.add(recipe.fetch());
            } else {
                Object item1 = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asNMSCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), villagerTrade.getItem1())
                        .fetch();
                Object reward = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asNMSCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), villagerTrade
                                .getRewardItem()).fetch();
                bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject recipe = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject
                        .fromNMS("MerchantRecipe", item1, reward);
                recipe.set("rewardExp", villagerTrade.getRewardExp());
                recipe.set("uses", villagerTrade.getUses());
                recipe.set("maxUses", villagerTrade.getMaxUses());
                list.add(recipe.fetch());
            }
            recipes.set(entityVillager, list);
        } catch (Exception exc) {
            ErrorLogger.log(exc, "Failed to add villager trade.");
        }
    }

    public static List<VillagerTrade> listTrades(Villager villager) {
        try {
            Object entityVillager = bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.execute("getHandle()", villager).fetch();
            List<VillagerTrade> rtrn = new ArrayList<>();
            Field recipes = entityVillager.getClass().getDeclaredField(bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.NMS_PATH.contains("v1_8_R1") ? "bp" : "br");
            recipes.setAccessible(true);
            List list = (List) recipes.get(entityVillager);
            for (Object recipe : list) {
                bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject reflObj = new bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionObject(recipe);
                ItemStack buyingItem1 = (ItemStack) bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asBukkitCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), reflObj
                                .get("buyingItem1")).fetch();
                ItemStack buyingItem2 = null;
                if (reflObj.get("buyingItem2") != null) {
                    buyingItem2 = (ItemStack) bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                            .executeStatic("asBukkitCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), reflObj
                                    .get("buyingItem2")).fetch();
                }
                ItemStack sellingItem = (ItemStack) bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil
                        .executeStatic("asBukkitCopy({1})", bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic.fromOBC("inventory.CraftItemStack"), reflObj
                                .get("sellingItem")).fetch();
                Integer maxUses = (Integer) bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.execute("maxUses", recipe).fetch();
                Integer uses = (Integer) bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.execute("uses", recipe).fetch();
                Boolean rewardxp = (Boolean) ReflectionUtil.execute("rewardExp", recipe).fetch();

                if (buyingItem2 != null) {
                    rtrn.add(new VillagerTrade(buyingItem1, buyingItem2, sellingItem, maxUses, uses, rewardxp));
                } else {
                    rtrn.add(new VillagerTrade(buyingItem1, sellingItem, maxUses, uses, rewardxp));
                }
            }
            return rtrn;
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

