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
package bammerbom.ultimatecore.spongeapi_old.resources.databases;

import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;

import java.util.HashMap;

public class EffectDatabase {

    static HashMap<String, PotionEffectType> pf = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static PotionEffectType getByName(String str) {
        if (pf.isEmpty()) {
            pf.put("swiftness", PotionEffectTypes.SPEED);
            pf.put("slowness", PotionEffectTypes.SLOWNESS);
            pf.put("haste", PotionEffectTypes.HASTE);
            pf.put("miningfatigue", PotionEffectTypes.MINING_FATIGUE);
            pf.put("strength", PotionEffectTypes.STRENGTH);
            pf.put("instanthealth", PotionEffectTypes.INSTANT_HEALTH);
            pf.put("instantdamage", PotionEffectTypes.INSTANT_DAMAGE);
            pf.put("jumpboost", PotionEffectTypes.JUMP_BOOST);
            pf.put("nausea", PotionEffectTypes.NAUSEA);
            pf.put("regen", PotionEffectTypes.REGENERATION);
            pf.put("resistance", PotionEffectTypes.RESISTANCE);
            pf.put("invis", PotionEffectTypes.INVISIBILITY);
            pf.put("blind", PotionEffectTypes.BLINDNESS);
            for (PotionEffectType type : CatalogTypes.POTION_EFFECT_TYPE.getEnumConstants()) {
                if (type == null) {
                    continue;
                }
                if (type.getName().contains("_")) {
                    pf.put(type.getName().toLowerCase().replace("_", ""), type);
                    pf.put(type.getName().toLowerCase(), type);
                } else {
                    pf.put(type.getName().toLowerCase(), type);
                }
            }
        }
        return pf.get(str.toLowerCase());
    }

    public static PotionEffectType[] values() {
        return CatalogTypes.POTION_EFFECT_TYPE.getEnumConstants();
    }
}
