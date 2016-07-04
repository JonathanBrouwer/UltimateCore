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
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdTop implements UltimateCommand {

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getPermission() {
        return "uc.top";
    }

    @Override
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Teleport yourself to the highest location.");
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
        Player p = (Player) cs;
        if (!r.perm(cs, "uc.top", true)) {
            return CommandResult.empty();
        }
        Location loc = new Location(p.getWorld(), p.getLocation().getX(), LocationUtil.getHighestBlockYAt(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockZ()).orElse
                (null), p.getLocation().getZ());
        if (loc == null || loc.getY() == 0) {
            r.sendMes(cs, "topFailed");
            return CommandResult.empty();
        }
        loc.add(0, 1.01, 0);
        LocationUtil.teleport(p, loc, Cause.builder().build(), false, true);
        r.sendMes(cs, "topMessage");
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
