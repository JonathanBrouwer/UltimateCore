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
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdClean implements UltimateCommand {

    @Override
    public String getName() {
        return "clean";
    }

    @Override
    public String getPermission() {
        return "uc.clean";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.clean", false, true)) {
            return;
        }
        StringBuilder s = new StringBuilder("");
        for (World w : Bukkit.getWorlds()) {
            if (r.checkArgs(args, 0) && !w.getName().equalsIgnoreCase(args[0])) {
                continue;
            }
            Integer c = 0;
            for (Chunk chunk : w.getLoadedChunks()) {
                try {
                    chunk.unload(true, true);
                } catch (Exception ex) {
                    r.log("Failed to unload chunk: " + chunk.getX() + " " + chunk.getZ());
                    return;
                }
                c++;
            }
            c = c - w.getLoadedChunks().length;
            Integer e = 0;
            Integer d = 0;
            for (Entity en : w.getEntities()) {
                if (en instanceof Monster) {
                    if (en.getTicksLived() > 200 && en.getCustomName() == null) {
                        en.playEffect(EntityEffect.DEATH);
                        en.remove();
                        e++;
                    }
                }
                if (en instanceof Item) {
                    Item item = (Item) en;
                    if (item.getTicksLived() > 200) {
                        if (!item.isValid()) {
                            en.remove();
                        }
                        d++;
                    }
                }
            }
            s.append(r.mes("cleanWorld", "%World", w.getName(), "%Chunks", c, "%Entities", e, "%Drops", d));
        }
        cs.sendMessage(s.toString());
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> str = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            str.add(w.getName());
        }
        return str;
    }
}
