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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.r;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class CmdHeal implements UltimateCommand {

    static Boolean healPositiveEffects = false;

    public static void start() {
        if (r.getCnfg().getBoolean("HealPositive")) {
            healPositiveEffects = true;
        }
    }

    @Override
    public String getName() {
        return "heal";
    }

    @Override
    public String getPermission() {
        return "uc.heal";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.heal", false, true)) {
            return;
        }
        if (r.checkArgs(args, 0) == false) {
            if (!(r.isPlayer(cs))) {
                return;
            }
            Player p = (Player) cs;
            p.setHealth(p.getMaxHealth());
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (!healPositiveEffects) {
                    for (NegativeEffect bad : NegativeEffect.values()) {
                        if (effect.getType().getName().equalsIgnoreCase(bad.name())) {
                            p.removePotionEffect(effect.getType());
                        }
                    }
                } else {
                    p.removePotionEffect(effect.getType());
                }
            }
            p.setFoodLevel(20);
            p.setSaturation(10F);
            p.setFireTicks(0);
            p.setRemainingAir(p.getMaximumAir());
            r.sendMes(cs, "healSelf");
        } else {
            if (!r.perm(cs, "uc.heal.others", false, true)) {
                return;
            }
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            } else {
                r.sendMes(cs, "healOthersSelf", "%Player", t.getName());
                r.sendMes(t, "healOthersOther", "%Player", cs.getName());
                for (PotionEffect effect : t.getActivePotionEffects()) {
                    if (!healPositiveEffects) {
                        for (NegativeEffect bad : NegativeEffect.values()) {
                            if (effect.getType().getName().equalsIgnoreCase(bad.name())) {
                                t.removePotionEffect(effect.getType());
                            }
                        }
                    } else {
                        t.removePotionEffect(effect.getType());
                    }
                }
                t.setHealth(t.getMaxHealth());
                t.setFoodLevel(20);
                t.setSaturation(10F);
                t.setFireTicks(0);
                t.setRemainingAir(t.getMaximumAir());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }

    public enum NegativeEffect {

        CONFUSION,
        HARM,
        HUNGER,
        POISON,
        SLOW_DIGGING,
        SLOW,
        WEAKNESS,
        WITHER,
        BLINDNESS;
    }
}
