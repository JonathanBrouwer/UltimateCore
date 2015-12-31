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
import bammerbom.ultimatecore.bukkit.resources.classes.MobType.Enemies;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdButcher implements UltimateCommand {

    @Override
    public String getName() {
        return "butcher";
    }

    @Override
    public String getPermission() {
        return "uc.butcher";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("killmonsters");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.perm(cs, "uc.butcher", false, true)) {
            return;
        }
        Player p = (Player) cs;
        Integer range = 100;
        MobType filter = null;
        Boolean all = false;
        Integer amount = 0;
        if (r.checkArgs(args, 0)) {
            if (r.isInt(args[0])) {
                range = Integer.parseInt(args[0]);
                if (range > 1000) {
                    range = 1000;
                }
            } else if (args[0].equalsIgnoreCase("global")) {
                all = true;
            } else {
                if (MobType.fromName(args[0]) != null) {
                    filter = MobType.fromName(args[0]);
                } else {

                }
                if (r.checkArgs(args, 1)) {
                    if (r.isInt(args[1])) {
                        range = Integer.parseInt(args[1]);
                        if (range > 1000) {
                            range = 1000;
                        }
                    }
                }
            }
        }
        if (!all) {
            for (Entity en : r.getNearbyEntities(p, range)) {
                if (filter != null && !filter.getType().equals(en.getType())) {
                    continue;
                }
                if (en instanceof LivingEntity && !(en instanceof Player) && !(en instanceof ArmorStand)) {
                    MobType mob = MobType.fromBukkitType(en.getType());
                    if ((mob != null && mob.type.equals(MobType.Enemies.ENEMY)) || en instanceof Monster) {
                        en.remove();
                        amount++;
                        en.playEffect(EntityEffect.DEATH);
                    }
                }
            }
        } else {
            for (Entity en : p.getWorld().getEntities()) {
                if (MobType.fromBukkitType(en.getType()) == null) {
                    continue;
                }
                if (filter != null && !filter.equals(MobType.fromBukkitType(en.getType()))) {
                    continue;
                }
                if (en instanceof LivingEntity && !(en instanceof Player) && !(en instanceof ArmorStand)) {
                    MobType mob = MobType.fromBukkitType(en.getType());
                    if (mob.type.equals(MobType.Enemies.ENEMY) || en instanceof Monster) {
                        en.remove();
                        amount++;
                        en.playEffect(EntityEffect.DEATH);
                    }
                }
            }
        }
        r.sendMes(cs, "butcherMessage", "%Amount", amount, "%Radius", range, "%Monster", (filter == null ? r.mes("butcherMonsters") : filter.name.toLowerCase() + "s"));
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            ArrayList<String> types = new ArrayList<>();
            for (MobType type : MobType.values()) {
                if (type.type.equals(Enemies.ENEMY)) {
                    types.add(type.name);
                }
            }
            return types;
        } else {
            return new ArrayList<>();
        }
    }
}
