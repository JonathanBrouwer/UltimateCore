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

import org.bukkit.entity.Player;

public class XpUtil {

    public static void setTotalExp(Player player, int exp) {
        if (exp < 0) {
            exp = 0;
        }
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);
        int amount = exp;
        while (amount > 0) {
            int expToLevel = getExpAtLevel(player.getLevel());
            amount -= expToLevel;
            if (amount >= 0) {
                player.giveExp(expToLevel);
            } else {
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return (2 * level) + 7;
        }
        if (level >= 16 && level <= 30) {
            return (5 * level) - 38;
        }
        return (9 * level) - 158;
    }

    public static int getExpToLevel(int level) {
        int currentLevel = 0;
        int exp = 0;

        while (currentLevel < level) {
            exp += getExpAtLevel(currentLevel);
            currentLevel++;
        }
        if (exp < 0) {
            exp = 0;
        }
        return exp;
    }

    public static int getExp(Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int currentLevel = player.getLevel();
        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }
        if (exp < 0) {
            exp = 2147483647;
        }
        return exp;
    }

    public static int getExpUntilNextLevel(Player player) {
        int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
        int nextLevel = player.getLevel();
        return getExpAtLevel(nextLevel) - exp;
    }
}
