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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.databases.EffectDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdEffect implements UltimateCommand {

    @Override
    public String getName() {
        return "effect";
    }

    @Override
    public String getPermission() {
        return "uc.effect";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.effect", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "effectUsage");
            return;
        }
        Player t = r.searchPlayer(args[0]);
        if (t == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return;
        }
        PotionEffectType ef = EffectDatabase.getByName(args[1]);
        if (ef == null) {
            if (args[1].equalsIgnoreCase("clear")) {
                for (PotionEffect effect : t.getActivePotionEffects()) {
                    t.removePotionEffect(effect.getType());
                }
                r.sendMes(cs, "effectClear", "%Target", t.getName());
                return;
            }
            r.sendMes(cs, "effectNotFound", "%Effect", args[1]);
            return;
        }
        Integer dur = 120;
        Integer lev = 1;
        if (r.checkArgs(args, 2)) {
            if (!r.isInt(args[2])) {
                r.sendMes(cs, "numberFormat", args[2]);
                return;
            }
            dur = Integer.parseInt(args[2]);
        }
        if (r.checkArgs(args, 3)) {
            if (!r.isInt(args[3])) {
                r.sendMes(cs, "numberFormat", args[3]);
                return;
            }
            lev = Integer.parseInt(args[3]);
        }
        lev = r.normalize(lev, 0, 999999);
        dur = r.normalize(dur, 0, 999999);
        if (lev == 0 || dur == 0) {
            t.removePotionEffect(ef);
            r.sendMes(cs, "effectSucces", "%Effect", ef.getName().toLowerCase(), "%Target", t.getName(), "%Duration", dur, "%Level", lev);
            return;
        }
        t.removePotionEffect(ef);
        PotionEffect effect = new PotionEffect(ef, dur * 20, lev - 1);
        t.addPotionEffect(effect, true);
        r.sendMes(cs, "effectSucces", "%Effect", ef.getName().toLowerCase(), "%Target", t.getName(), "%Duration", dur, "%Level", lev);
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return null;
        } else if (curn == 1) {
            ArrayList<String> s = new ArrayList<>();
            for (PotionEffectType t : EffectDatabase.values()) {
                if (t == null || t.getName() == null) {
                    continue;
                }
                s.add(t.getName().toLowerCase().replaceAll("_", ""));
            }
            return s;
        } else {
            return new ArrayList<>();
        }
    }
}
