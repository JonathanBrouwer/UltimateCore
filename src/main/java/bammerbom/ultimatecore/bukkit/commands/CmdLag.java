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
import bammerbom.ultimatecore.bukkit.resources.utils.PerformanceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class CmdLag implements UltimateCommand {

    @Override
    public String getName() {
        return "lag";
    }

    @Override
    public String getPermission() {
        return "uc.lag";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ram", "tps", "mem", "memory");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.lag", true, true)) {
            return;
        }
        r.sendMes(cs, "lagTps", "%Tps", PerformanceUtil.getTps());
        r.sendMes(cs, "lagMem", "%Mem", (PerformanceUtil.usedRam() + "/" + PerformanceUtil.totalRam() + "/" + PerformanceUtil.maxRam()), "%Per", PerformanceUtil.percentageUsed());
        int ws = 0;
        for (World w : Bukkit.getWorlds()) {
            if (r.checkArgs(args, 0) && !args[0].equalsIgnoreCase(w.getName())) {
                return;
            }
            ws++;
            int tiles = 0;
            for (Chunk c : w.getLoadedChunks()) {
                tiles += c.getTileEntities().length;
            }
            r.sendMes(cs, "lagWorld", "%World", w.getName(), "%Chunks", w.getLoadedChunks().length, "%Entities", w.getEntities().size(), "%Tiles", tiles);
        }
        if (ws == 0) {
            r.sendMes(cs, "worldNotFound", "%World", args[0]);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
