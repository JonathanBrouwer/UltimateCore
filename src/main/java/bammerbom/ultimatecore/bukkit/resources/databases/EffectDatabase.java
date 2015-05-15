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

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class EffectDatabase {

    static HashMap<String, PotionEffectType> pf = new HashMap<>();

    @SuppressWarnings("deprecation")
    public static PotionEffectType getByName(String str) {
        if (pf.isEmpty()) {
            pf.put("swiftness", PotionEffectType.SPEED);
            pf.put("slowness", PotionEffectType.SLOW);
            pf.put("haste", PotionEffectType.FAST_DIGGING);
            pf.put("miningfatigue", PotionEffectType.SLOW_DIGGING);
            pf.put("strength", PotionEffectType.INCREASE_DAMAGE);
            pf.put("instanthealth", PotionEffectType.HEAL);
            pf.put("instantdamage", PotionEffectType.HARM);
            pf.put("jumpboost", PotionEffectType.JUMP);
            pf.put("nausea", PotionEffectType.CONFUSION);
            pf.put("regen", PotionEffectType.REGENERATION);
            pf.put("resistance", PotionEffectType.DAMAGE_RESISTANCE);
            pf.put("invis", PotionEffectType.INVISIBILITY);
            pf.put("blind", PotionEffectType.BLINDNESS);
            for (PotionEffectType type : PotionEffectType.values()) {
                if (type == null || type.getName() == null) {
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
        if (str.contains(":")) {
            str = str.split(":")[1];
        }
        return !r.isInt(str) ? pf.get(str.toLowerCase()) : PotionEffectType.getById(Integer.parseInt(str));
    }

    public static PotionEffectType[] values() {
        return PotionEffectType.values();
    }
}
