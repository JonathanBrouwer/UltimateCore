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
import bammerbom.ultimatecore.spongeapi.resources.classes.MobType;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.spongepowered.api.block.tileentity.MobSpawner;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public String getUsage() {
        return "/<command> <MobType>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set the mob type of a spawner.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.spawner", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "spawnerUsage");
            return CommandResult.empty();
        }
        Location b = LocationUtil.getTarget(p).orElse(p.getLocation());
        if (!b.getTileEntity().isPresent() || !(b.getTileEntity().get() instanceof MobSpawner)) {
            r.sendMes(cs, "spawnerNotLooking");
            return CommandResult.empty();
        }
        MobSpawner spawner = (MobSpawner) b.getTileEntity().get();
        MobType m = MobType.fromName(args[0]);
        if (m == null || m.getType() == null || m.getType().equals(EntityTypes.UNKNOWN)) {
            r.sendMes(cs, "spawnerNotFound", "%MobType", args[0]);
            return CommandResult.empty();
        }
        spawner.offer(Keys.SPAWNER_ENTITIES, Arrays.asList(EntitySnapshot.builder().type(m.getType()).build()));
        r.sendMes(cs, "spawnerMessage", "%MobType", m.name().toLowerCase());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        List<String> rtrn = new ArrayList<>();
        for (MobType t : MobType.values()) {
            rtrn.add(t.name);
        }
        return rtrn;
    }
}
