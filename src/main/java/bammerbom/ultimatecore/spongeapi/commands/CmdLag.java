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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class CmdLag implements UltimateCommand {

    public static float maxRam() {
        return Runtime.getRuntime().maxMemory() / 1048576F;
    }

    public static float totalRam() {
        return Runtime.getRuntime().totalMemory() / 1048576F;
    }

    public static float freeRam() {
        return Runtime.getRuntime().freeMemory() / 1048576F;
    }

    public static float usedRam() {
        return maxRam() - freeRam();
    }

    public static float percentageUsed() {
        return Math.round((((usedRam() * 1.0) / (maxRam() * 1.0)) * 100.0) * 10) / 10F;
    }

    @Override
    public String getName() {
        return "lag";
    }

    @Override
    public String getPermission() {
        return "uc.lag";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("View the performance of the server.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ram", "tps", "mem", "memory");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.lag", true)) {
            return CommandResult.empty();
        }
        r.sendMes(cs, "lagTps", "%Tps", Sponge.getServer().getTicksPerSecond());
        r.sendMes(cs, "lagMem", "%Mem", (Math.round(usedRam()) + "/" + Math.round(totalRam()) + "/" +
                Math.round(maxRam())), "%Per", percentageUsed());
        int ws = 0;
        for (World w : Sponge.getServer().getWorlds()) {
            if (r.checkArgs(args, 0) && !args[0].equalsIgnoreCase(w.getName())) {
                return CommandResult.empty();
            }
            ws++;
            int tiles = 0;
            int chunks = 0;
            for (Chunk c : w.getLoadedChunks()) {
                chunks++;
                tiles += c.getTileEntities().size();
            }
            r.sendMes(cs, "lagWorld", "%World", w.getName(), "%Chunks", chunks, "%Entities", w.getEntities().size(), "%Tiles", tiles);
        }
        if (ws == 0) {
            r.sendMes(cs, "worldNotFound", "%World", args[0]);
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
