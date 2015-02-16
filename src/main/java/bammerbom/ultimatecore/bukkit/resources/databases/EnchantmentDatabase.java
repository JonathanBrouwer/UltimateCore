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
package bammerbom.ultimatecore.bukkit.resources.databases;

import java.util.HashMap;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentDatabase {

    static HashMap<String, Enchantment> ench = new HashMap<>();

    public static Enchantment getByName(String str) {
        if (ench.isEmpty()) {
            ench.put("power", Enchantment.ARROW_DAMAGE);
            ench.put("flame", Enchantment.ARROW_FIRE);
            ench.put("infinite", Enchantment.ARROW_INFINITE);
            ench.put("infinity", Enchantment.ARROW_INFINITE);
            ench.put("punch", Enchantment.ARROW_KNOCKBACK);
            ench.put("sharp", Enchantment.DAMAGE_ALL);
            ench.put("sharpness", Enchantment.DAMAGE_ALL);
            ench.put("smite", Enchantment.DAMAGE_UNDEAD);
            ench.put("bane", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("arthropods", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("efficiency", Enchantment.DIG_SPEED);
            ench.put("digspeed", Enchantment.DIG_SPEED);
            ench.put("durability", Enchantment.DURABILITY);
            ench.put("fireaspect", Enchantment.FIRE_ASPECT);
            ench.put("fire", Enchantment.FIRE_ASPECT);
            ench.put("knockback", Enchantment.KNOCKBACK);
            ench.put("looting", Enchantment.LOOT_BONUS_MOBS);
            ench.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
            ench.put("luck", Enchantment.LUCK);
            ench.put("luckofthesea", Enchantment.LUCK);
            ench.put("lure", Enchantment.LURE);
            ench.put("respiration", Enchantment.OXYGEN);
            ench.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
            ench.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
            ench.put("blastprot", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("fallprot", Enchantment.PROTECTION_FALL);
            ench.put("fallprotection", Enchantment.PROTECTION_FALL);
            ench.put("featherfall", Enchantment.PROTECTION_FALL);
            ench.put("featherfalling", Enchantment.PROTECTION_FALL);
            ench.put("fireprot", Enchantment.PROTECTION_FIRE);
            ench.put("fireprotection", Enchantment.PROTECTION_FIRE);
            ench.put("projprot", Enchantment.PROTECTION_PROJECTILE);
            ench.put("projprotection", Enchantment.PROTECTION_PROJECTILE);
            ench.put("projectileprot", Enchantment.PROTECTION_PROJECTILE);
            ench.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
            ench.put("silktouch", Enchantment.SILK_TOUCH);
            ench.put("thorns", Enchantment.THORNS);
            ench.put("aqua", Enchantment.WATER_WORKER);
            ench.put("aquaaffinity", Enchantment.WATER_WORKER);
            ench.put("unbreak", Enchantment.DURABILITY);
            ench.put("unbreaking", Enchantment.DURABILITY);
            //
            ench.put("alldamage", Enchantment.DAMAGE_ALL);
            ench.put("alldmg", Enchantment.DAMAGE_ALL);
            ench.put("sharpness", Enchantment.DAMAGE_ALL);
            ench.put("sharp", Enchantment.DAMAGE_ALL);
            ench.put("dal", Enchantment.DAMAGE_ALL);

            ench.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
            ench.put("dar", Enchantment.DAMAGE_ARTHROPODS);

            ench.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
            ench.put("smite", Enchantment.DAMAGE_UNDEAD);
            ench.put("du", Enchantment.DAMAGE_UNDEAD);

            ench.put("digspeed", Enchantment.DIG_SPEED);
            ench.put("efficiency", Enchantment.DIG_SPEED);
            ench.put("minespeed", Enchantment.DIG_SPEED);
            ench.put("cutspeed", Enchantment.DIG_SPEED);
            ench.put("ds", Enchantment.DIG_SPEED);
            ench.put("eff", Enchantment.DIG_SPEED);

            ench.put("durability", Enchantment.DURABILITY);
            ench.put("dura", Enchantment.DURABILITY);
            ench.put("unbreaking", Enchantment.DURABILITY);
            ench.put("d", Enchantment.DURABILITY);

            ench.put("thorns", Enchantment.THORNS);
            ench.put("highcrit", Enchantment.THORNS);
            ench.put("thorn", Enchantment.THORNS);
            ench.put("highercrit", Enchantment.THORNS);
            ench.put("t", Enchantment.THORNS);

            ench.put("fireaspect", Enchantment.FIRE_ASPECT);
            ench.put("fire", Enchantment.FIRE_ASPECT);
            ench.put("meleefire", Enchantment.FIRE_ASPECT);
            ench.put("meleeflame", Enchantment.FIRE_ASPECT);
            ench.put("fa", Enchantment.FIRE_ASPECT);

            ench.put("knockback", Enchantment.KNOCKBACK);
            ench.put("kback", Enchantment.KNOCKBACK);
            ench.put("kb", Enchantment.KNOCKBACK);
            ench.put("k", Enchantment.KNOCKBACK);

            ench.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
            ench.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
            ench.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
            ench.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);

            ench.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
            ench.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
            ench.put("looting", Enchantment.LOOT_BONUS_MOBS);
            ench.put("lbm", Enchantment.LOOT_BONUS_MOBS);

            ench.put("oxygen", Enchantment.OXYGEN);
            ench.put("respiration", Enchantment.OXYGEN);
            ench.put("breathing", Enchantment.OXYGEN);
            ench.put("breath", Enchantment.OXYGEN);
            ench.put("o", Enchantment.OXYGEN);

            ench.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
            ench.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
            ench.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
            ench.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);

            ench.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("bprotection", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("bprotect", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
            ench.put("pe", Enchantment.PROTECTION_EXPLOSIONS);

            ench.put("fallprotection", Enchantment.PROTECTION_FALL);
            ench.put("fallprot", Enchantment.PROTECTION_FALL);
            ench.put("featherfall", Enchantment.PROTECTION_FALL);
            ench.put("featherfalling", Enchantment.PROTECTION_FALL);
            ench.put("pfa", Enchantment.PROTECTION_FALL);

            ench.put("fireprotection", Enchantment.PROTECTION_FIRE);
            ench.put("flameprotection", Enchantment.PROTECTION_FIRE);
            ench.put("fireprotect", Enchantment.PROTECTION_FIRE);
            ench.put("flameprotect", Enchantment.PROTECTION_FIRE);
            ench.put("fireprot", Enchantment.PROTECTION_FIRE);
            ench.put("flameprot", Enchantment.PROTECTION_FIRE);
            ench.put("pf", Enchantment.PROTECTION_FIRE);

            ench.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
            ench.put("projprot", Enchantment.PROTECTION_PROJECTILE);
            ench.put("pp", Enchantment.PROTECTION_PROJECTILE);

            ench.put("softtouch", Enchantment.SILK_TOUCH);
            ench.put("st", Enchantment.SILK_TOUCH);

            ench.put("aquaaffinity", Enchantment.WATER_WORKER);
            ench.put("watermine", Enchantment.WATER_WORKER);
            ench.put("ww", Enchantment.WATER_WORKER);

            ench.put("firearrow", Enchantment.ARROW_FIRE);
            ench.put("flame", Enchantment.ARROW_FIRE);
            ench.put("flamearrow", Enchantment.ARROW_FIRE);
            ench.put("af", Enchantment.ARROW_FIRE);

            ench.put("power", Enchantment.ARROW_DAMAGE);
            ench.put("arrowpower", Enchantment.ARROW_DAMAGE);
            ench.put("ad", Enchantment.ARROW_DAMAGE);

            ench.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
            ench.put("punch", Enchantment.ARROW_KNOCKBACK);
            ench.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
            ench.put("ak", Enchantment.ARROW_KNOCKBACK);

            ench.put("infinitearrows", Enchantment.ARROW_INFINITE);
            ench.put("infarrows", Enchantment.ARROW_INFINITE);
            ench.put("infinity", Enchantment.ARROW_INFINITE);
            ench.put("infinite", Enchantment.ARROW_INFINITE);
            ench.put("unlimited", Enchantment.ARROW_INFINITE);
            ench.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
            ench.put("ai", Enchantment.ARROW_INFINITE);

            ench.put("luckofsea", Enchantment.LUCK);
            ench.put("luckofseas", Enchantment.LUCK);
            ench.put("rodluck", Enchantment.LUCK);

            ench.put("lure", Enchantment.LURE);
            ench.put("rodlure", Enchantment.LURE);

            ench.put("depthstrider", Enchantment.DEPTH_STRIDER);
            ench.put("depth", Enchantment.DEPTH_STRIDER);
            ench.put("ds", Enchantment.DEPTH_STRIDER);

            for (Enchantment enc : Enchantment.values()) {
                if (!ench.containsKey(enc.getName().toLowerCase())) {
                    ench.put(enc.getName().toLowerCase(), enc);
                    if (enc.getName().contains("_")) {
                        ench.put(enc.getName().toLowerCase().replace("_", ""), enc);
                    }
                }
            }
        }
        return ench.get(str.toLowerCase());
    }
}
