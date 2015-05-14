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

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.MobType;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdKillall implements UltimateCommand {

    @Override
    public String getName() {
        return "killall";
    }

    @Override
    public String getPermission() {
        return "uc.killall";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.perm(cs, "uc.killall", false, true)) {
            return;
        }
        Integer range = 100;
        if (r.checkArgs(args, 0) == true && r.isInt(args[0])) {
            range = Integer.parseInt(args[0]);
            if (range > 1000) {
                range = 1000;
            }
        } else if (r.checkArgs(args, 1) && r.isInt(args[1])) {
            range = Integer.parseInt(args[1]);
            if (range > 1000) {
                range = 1000;
            }
        }
        EntityType et = null;
        if (r.checkArgs(args, 0)) {
            if (EntityType.fromName(args[0].toUpperCase()) != null) {
                et = EntityType.fromName(args[0].toUpperCase());
            }
            if (EntityType.fromName(args[0].replaceAll("_", "").toUpperCase()) != null) {
                et = EntityType.fromName(args[0].replaceAll("_", "").toUpperCase());
            }
            if (MobType.fromName(args[0]) != null) {
                et = MobType.fromName(args[0]).getType();
            }

        }
        Player p = (Player) cs;
        Integer amount = 0;
        for (Entity en : r.getNearbyEntities(p, range)) {
            if (en instanceof LivingEntity && !(en instanceof Player)) {
                if (et != null && !en.getType().equals(et)) {
                    continue;
                }
                en.remove();
                amount++;
                en.playEffect(EntityEffect.DEATH);
            }
        }
        r.sendMes(cs, "killAll", "%Amount", amount, "%Radius", range);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
