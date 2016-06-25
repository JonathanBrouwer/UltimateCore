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
package bammerbom.ultimatecore.spongeapi.resources.databases;

import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;

import java.util.HashMap;

public class EnchantmentDatabase {

    static HashMap<String, Enchantment> ench = new HashMap<>();

    public static Enchantment getByName(String str) {
        if (ench.isEmpty()) {
            ench.put("infinite", Enchantments.INFINITY);
            ench.put("sharp", Enchantments.SHARPNESS);
            ench.put("bane", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("arthropods", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("digspeed", Enchantments.EFFICIENCY);
            ench.put("durability", Enchantments.UNBREAKING);
            ench.put("fire", Enchantments.FIRE_ASPECT);
            ench.put("luck", Enchantments.LUCK_OF_THE_SEA);
            ench.put("lure", Enchantments.LURE);
            ench.put("prot", Enchantments.PROTECTION);
            ench.put("blastprot", Enchantments.BLAST_PROTECTION);
            ench.put("fallprot", Enchantments.FEATHER_FALLING);
            ench.put("fallprotection", Enchantments.FEATHER_FALLING);
            ench.put("featherfall", Enchantments.FEATHER_FALLING);
            ench.put("fireprot", Enchantments.FIRE_PROTECTION);
            ench.put("projprot", Enchantments.PROJECTILE_PROTECTION);
            ench.put("projprotection", Enchantments.PROJECTILE_PROTECTION);
            ench.put("projectileprot", Enchantments.PROJECTILE_PROTECTION);
            ench.put("aqua", Enchantments.AQUA_AFFINITY);
            ench.put("unbreak", Enchantments.UNBREAKING);
            //
            ench.put("alldamage", Enchantments.SHARPNESS);
            ench.put("alldmg", Enchantments.SHARPNESS);
            ench.put("sharpness", Enchantments.SHARPNESS);
            ench.put("sharp", Enchantments.SHARPNESS);
            ench.put("dal", Enchantments.SHARPNESS);

            ench.put("ardmg", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("baneofarthropods", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("baneofarthropod", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("arthropod", Enchantments.BANE_OF_ARTHROPODS);
            ench.put("dar", Enchantments.BANE_OF_ARTHROPODS);

            ench.put("undeaddamage", Enchantments.SMITE);
            ench.put("smite", Enchantments.SMITE);
            ench.put("du", Enchantments.SMITE);

            ench.put("digspeed", Enchantments.EFFICIENCY);
            ench.put("efficiency", Enchantments.EFFICIENCY);
            ench.put("minespeed", Enchantments.EFFICIENCY);
            ench.put("cutspeed", Enchantments.EFFICIENCY);
            ench.put("ds", Enchantments.EFFICIENCY);
            ench.put("eff", Enchantments.EFFICIENCY);

            ench.put("durability", Enchantments.UNBREAKING);
            ench.put("dura", Enchantments.UNBREAKING);
            ench.put("unbreaking", Enchantments.UNBREAKING);
            ench.put("d", Enchantments.UNBREAKING);

            ench.put("thorns", Enchantments.THORNS);
            ench.put("highcrit", Enchantments.THORNS);
            ench.put("thorn", Enchantments.THORNS);
            ench.put("highercrit", Enchantments.THORNS);
            ench.put("t", Enchantments.THORNS);

            ench.put("fireaspect", Enchantments.FIRE_ASPECT);
            ench.put("fire", Enchantments.FIRE_ASPECT);
            ench.put("meleefire", Enchantments.FIRE_ASPECT);
            ench.put("meleeflame", Enchantments.FIRE_ASPECT);
            ench.put("fa", Enchantments.FIRE_ASPECT);

            ench.put("knockback", Enchantments.KNOCKBACK);
            ench.put("kback", Enchantments.KNOCKBACK);
            ench.put("kb", Enchantments.KNOCKBACK);
            ench.put("k", Enchantments.KNOCKBACK);

            ench.put("blockslootbonus", Enchantments.FORTUNE);
            ench.put("fortune", Enchantments.FORTUNE);
            ench.put("fort", Enchantments.FORTUNE);
            ench.put("lbb", Enchantments.FORTUNE);

            ench.put("mobslootbonus", Enchantments.LOOTING);
            ench.put("mobloot", Enchantments.LOOTING);
            ench.put("looting", Enchantments.LOOTING);
            ench.put("lbm", Enchantments.LOOTING);

            ench.put("oxygen", Enchantments.RESPIRATION);
            ench.put("respiration", Enchantments.RESPIRATION);
            ench.put("breathing", Enchantments.RESPIRATION);
            ench.put("breath", Enchantments.RESPIRATION);
            ench.put("o", Enchantments.RESPIRATION);

            ench.put("protection", Enchantments.PROTECTION);
            ench.put("prot", Enchantments.PROTECTION);
            ench.put("protect", Enchantments.PROTECTION);
            ench.put("p", Enchantments.PROTECTION);

            ench.put("explosionsprotection", Enchantments.BLAST_PROTECTION);
            ench.put("explosionprotection", Enchantments.BLAST_PROTECTION);
            ench.put("expprot", Enchantments.BLAST_PROTECTION);
            ench.put("blastprotection", Enchantments.BLAST_PROTECTION);
            ench.put("bprotection", Enchantments.BLAST_PROTECTION);
            ench.put("bprotect", Enchantments.BLAST_PROTECTION);
            ench.put("blastprotect", Enchantments.BLAST_PROTECTION);
            ench.put("pe", Enchantments.BLAST_PROTECTION);

            ench.put("fallprotection", Enchantments.FEATHER_FALLING);
            ench.put("fallprot", Enchantments.FEATHER_FALLING);
            ench.put("featherfall", Enchantments.FEATHER_FALLING);
            ench.put("featherfalling", Enchantments.FEATHER_FALLING);
            ench.put("pfa", Enchantments.FEATHER_FALLING);

            ench.put("fireprotection", Enchantments.FIRE_PROTECTION);
            ench.put("flameprotection", Enchantments.FIRE_PROTECTION);
            ench.put("fireprotect", Enchantments.FIRE_PROTECTION);
            ench.put("flameprotect", Enchantments.FIRE_PROTECTION);
            ench.put("fireprot", Enchantments.FIRE_PROTECTION);
            ench.put("flameprot", Enchantments.FIRE_PROTECTION);
            ench.put("pf", Enchantments.FIRE_PROTECTION);

            ench.put("projectileprotection", Enchantments.PROJECTILE_PROTECTION);
            ench.put("projprot", Enchantments.PROJECTILE_PROTECTION);
            ench.put("pp", Enchantments.PROJECTILE_PROTECTION);

            ench.put("softtouch", Enchantments.SILK_TOUCH);
            ench.put("st", Enchantments.SILK_TOUCH);

            ench.put("aquaaffinity", Enchantments.AQUA_AFFINITY);
            ench.put("watermine", Enchantments.AQUA_AFFINITY);
            ench.put("ww", Enchantments.AQUA_AFFINITY);

            ench.put("firearrow", Enchantments.FLAME);
            ench.put("flame", Enchantments.FLAME);
            ench.put("flamearrow", Enchantments.FLAME);
            ench.put("af", Enchantments.FLAME);

            ench.put("power", Enchantments.POWER);
            ench.put("arrowpower", Enchantments.POWER);
            ench.put("ad", Enchantments.POWER);

            ench.put("arrowkb", Enchantments.PUNCH);
            ench.put("punch", Enchantments.PUNCH);
            ench.put("arrowpunch", Enchantments.PUNCH);
            ench.put("ak", Enchantments.PUNCH);

            ench.put("infinitearrows", Enchantments.INFINITY);
            ench.put("infarrows", Enchantments.INFINITY);
            ench.put("infinity", Enchantments.INFINITY);
            ench.put("infinite", Enchantments.INFINITY);
            ench.put("unlimited", Enchantments.INFINITY);
            ench.put("unlimitedarrows", Enchantments.INFINITY);
            ench.put("ai", Enchantments.INFINITY);

            ench.put("luckofsea", Enchantments.LUCK_OF_THE_SEA);
            ench.put("luckofseas", Enchantments.LUCK_OF_THE_SEA);
            ench.put("rodluck", Enchantments.LUCK_OF_THE_SEA);

            ench.put("lure", Enchantments.LURE);
            ench.put("rodlure", Enchantments.LURE);

            ench.put("depthstrider", Enchantments.DEPTH_STRIDER);
            ench.put("depth", Enchantments.DEPTH_STRIDER);
            ench.put("ds", Enchantments.DEPTH_STRIDER);

            ench.put("frost", Enchantments.FROST_WALKER);
            ench.put("walker", Enchantments.FROST_WALKER);
            ench.put("frozenwalker", Enchantments.FROST_WALKER);

            ench.put("mend", Enchantments.MENDING);
            ench.put("xprepair", Enchantments.MENDING);

            for (Enchantment enc : Sponge.getRegistry().getAllOf(CatalogTypes.ENCHANTMENT)) {
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
