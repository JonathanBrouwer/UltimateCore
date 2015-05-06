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

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.databases.ItemDatabase;
import bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil.ReflectionStatic;
import net.milkbowl.vault.item.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings({"deprecation", "unchecked"})
public class ItemUtil {

    static HashMap<Material, String> ids = new HashMap<>();

    public static void start() {
        //ids
        try {
            Object ma = ReflectionUtil.executeStatic("REGISTRY", ReflectionStatic.fromNMS("Item")).fetch();
            Object ba = ReflectionUtil.executeStatic("REGISTRY", ReflectionStatic.fromNMS("Block")).fetch();
            Set<Object> mas = ReflectionUtil.execute("keySet()", ma).fetchAs(Set.class);
            Set<Object> bas = ReflectionUtil.execute("keySet()", ba).fetchAs(Set.class);

            for (Object s : mas) {
                try {
                    Object item = ReflectionUtil.execute("get({1})", ma, s).fetch();
                    Material matt = Material.getMaterial((Integer) ReflectionUtil.executeStatic("getId({1})", ReflectionStatic.fromNMS("Item"), item).fetch());
                    ids.put(matt, s.toString());
                } catch (Exception ex) {
                    r.log("Failed " + s.toString());
                }
            }
            for (Object s : bas) {
                try {
                    Object item = ReflectionUtil.execute("get({1})", ba, s).fetch();
                    Material matt = Material.getMaterial((Integer) ReflectionUtil.executeStatic("getId({1})", ReflectionStatic.fromNMS("Block"), item).fetch());
                    ids.put(matt, s.toString());
                } catch (Exception ex) {
                    r.log("Failed " + s.toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ItemStack addGlow(ItemStack item) {
        EnchantGlow.addGlow(item);
        return item;
    }

    public static ItemStack setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        return setLore(item, Arrays.asList(lore));
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack searchItem(String str) {
        return ItemDatabase.getItem(str);
    }

    public static String getName(ItemStack stack) {
        try {
            return ReflectionUtil.execute("getName()", ReflectionUtil.executeStatic("asNMSCopy({1})", ReflectionStatic.fromOBC("inventory.CraftItemStack"), stack).fetch()).fetch().toString();
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to get item name.");
            return "NAME";
        }
    }

    public static String getTypeName(ItemStack stack) {
        if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
            return "hand";
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vault") && Items.itemByStack(stack) != null && Items.itemByStack(stack).getName() != null) {
            return StringUtil.firstUpperCase(Items.itemByStack(stack).getName().toLowerCase());
        } else {
            return StringUtil.firstUpperCase(stack.getType().name().replace("_", " ").toLowerCase());
        }
    }

    public static boolean isRepairable(ItemStack stack) {
        return stack.getType().equals(Material.WOOD_AXE) || stack.getType().equals(Material.WOOD_PICKAXE) || stack.getType().equals(Material.WOOD_SPADE) || stack.getType().equals(Material.WOOD_SWORD) || stack.getType().equals(Material.WOOD_HOE) || stack.getType().equals(Material.STONE_AXE) || stack.getType().equals(Material.STONE_PICKAXE) || stack.getType().equals(Material.STONE_SPADE) || stack.getType().equals(Material.STONE_SWORD) || stack.getType().equals(Material.STONE_HOE) || stack.getType().equals(Material.IRON_AXE) || stack.getType().equals(Material.IRON_PICKAXE) || stack.getType().equals(Material.IRON_SPADE) || stack.getType().equals(Material.IRON_SWORD) || stack.getType().equals(Material.IRON_HOE) || stack.getType().equals(Material.GOLD_AXE) || stack.getType().equals(Material.GOLD_PICKAXE) || stack.getType().equals(Material.GOLD_SPADE) || stack.getType().equals(Material.GOLD_SWORD) || stack.getType().equals(Material.GOLD_HOE) || stack.getType().equals(Material.DIAMOND_AXE) || stack.getType().equals(Material.DIAMOND_PICKAXE) || stack.getType().equals(Material.DIAMOND_SPADE) || stack.getType().equals(Material.DIAMOND_SWORD) || stack.getType().equals(Material.DIAMOND_HOE) || stack.getType().equals(Material.LEATHER_BOOTS) || stack.getType().equals(Material.LEATHER_LEGGINGS) || stack.getType().equals(Material.LEATHER_CHESTPLATE) || stack.getType().equals(Material.LEATHER_HELMET) || stack.getType().equals(Material.CHAINMAIL_BOOTS) || stack.getType().equals(Material.CHAINMAIL_LEGGINGS) || stack.getType().equals(Material.CHAINMAIL_CHESTPLATE) ||
                stack.getType().equals(Material.CHAINMAIL_HELMET) || stack.getType().equals(Material.IRON_BOOTS) ||
                stack.getType().equals(Material.IRON_LEGGINGS) || stack.getType().equals(Material.IRON_CHESTPLATE) ||
                stack.getType().equals(Material.IRON_HELMET) || stack.getType().equals(Material.GOLD_BOOTS) || stack.getType().equals(Material.GOLD_LEGGINGS) || stack.getType().equals(Material.GOLD_CHESTPLATE) ||
                stack.getType().equals(Material.GOLD_HELMET) || stack.getType().equals(Material.DIAMOND_BOOTS) ||
                stack.getType().equals(Material.DIAMOND_LEGGINGS) || stack.getType().equals(Material.DIAMOND_CHESTPLATE) || stack.getType().equals(Material.DIAMOND_HELMET) || stack.getType().equals(Material.BOW) || stack.getType().equals(Material.FISHING_ROD) || stack.getType().equals(Material.CARROT_STICK) || stack.getType().equals(Material.FLINT_AND_STEEL) || stack.getType().equals(Material.SHEARS) || stack.getType().equals(Material.ANVIL);
    }

    public static String getID(Material mat) {
        return ids.get(mat);
    }

    public static Material getMaterialFromId(String id) {
        for (Entry<Material, String> en : ids.entrySet()) {
            if (en.getValue().equalsIgnoreCase(id)) {
                return en.getKey();
            }
        }
        return null;
    }
}

class EnchantGlow extends EnchantmentWrapper {

    private static Enchantment glow;

    public EnchantGlow(int id) {
        super(id);
    }

    public static Enchantment getGlow() {
        if (glow != null) {
            return glow;
        }

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        glow = new EnchantGlow(255);
        Enchantment.registerEnchantment(glow);
        return glow;
    }

    public static void addGlow(ItemStack item) {
        Enchantment glow = getGlow();

        item.addEnchantment(glow, 1);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

}
