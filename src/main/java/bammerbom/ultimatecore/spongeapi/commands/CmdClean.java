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
import com.google.common.collect.Iterables;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.clean", true)) {
            return;
        }
        StringBuilder s = new StringBuilder("");
        for (World w : Sponge.getServer().getWorlds()) {
            if (r.checkArgs(args, 0) && !w.getName().equalsIgnoreCase(args[0])) {
                continue;
            }
            Integer c = 0;
            for (Chunk chunk : w.getLoadedChunks()) {
                try {
                    chunk.unloadChunk();
                } catch (Exception ex) {
                    r.log("Failed to unload chunk: " + chunk.getPosition().getX() + " " + chunk.getPosition().getZ());
                    continue;
                }
                c++;
            }
            Iterator<Chunk> chunks = w.getLoadedChunks().iterator();
            int sum = 0;
            while (chunks.hasNext()) {
                chunks.next();
                sum++;
            }
            c = c - Iterables.size(w.getLoadedChunks());
            Integer e = 0;
            Integer d = 0;
            for (Entity en : w.getEntities()) {
                if (en instanceof Monster) {
                    if (en.get(Keys.AGE).get() > 200 && !en.get(Keys.DISPLAY_NAME).isPresent()) {
                        en.playEffect(EntityEffect.DEATH); //TODO wait for api
                        en.remove();
                        e++;
                    }
                }
                if (en instanceof Item) {
                    Item item = (Item) en;
                    if (en.get(Keys.AGE).get() > 200) {
                        en.remove();
                        d++;
                    }
                }
            }
            s.append(r.mes("cleanWorld", "%World", w.getName(), "%Chunks", c, "%Entities", e, "%Drops", d));
        }
        cs.sendMessage(Text.of(s));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> str = new ArrayList<>();
        for (World w : Sponge.getServer().getWorlds()) {
            str.add(w.getName());
        }
        return str;
    }
}
