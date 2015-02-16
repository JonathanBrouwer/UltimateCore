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
import bammerbom.ultimatecore.bukkit.resources.classes.MobType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdSpawner implements UltimateCommand {

    @Override
    public String getName() {
        return "spawner";
    }

    @Override
    public String getPermission() {
        return "uc.spawner";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.perm(cs, "uc.spawner", false, true)) {
            return;
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "spawnerUsage");
            return;
        }
        Block b = p.getTargetBlock(null, 10);
        if (!(b.getState() instanceof CreatureSpawner)) {
            r.sendMes(cs, "spawnerNotLooking");
            return;
        }
        CreatureSpawner c = (CreatureSpawner) b.getState();
        MobType m = MobType.fromName(args[0]);
        if (m == null || m.getType() == null || m.getType().equals(EntityType.UNKNOWN) || !m.getType().isSpawnable()) {
            r.sendMes(cs, "spawnerNotFound", "%MobType", args[0]);
            return;
        }
        c.setSpawnedType(m.getType());
        c.update();
        r.sendMes(cs, "spawnerMessage", "%MobType", m.name().toLowerCase());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        List<String> rtrn = new ArrayList<>();
        for (MobType t : MobType.values()) {
            rtrn.add(t.name);
        }
        return rtrn;
    }
}
