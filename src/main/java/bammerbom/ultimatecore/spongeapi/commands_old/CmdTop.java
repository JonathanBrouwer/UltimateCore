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
package bammerbom.ultimatecore.spongeapi.commands_old;

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdTop implements UltimateCommand {

    public static Location getHighestY(Location loc) {
        Integer highest = 0;
        Integer current = 0;
        while (current < loc.getWorld().getMaxHeight()) {
            Integer cur = current;
            current++;
            Location loc2 = new Location(loc.getWorld(), loc.getX(), cur, loc.getZ());
            if (loc2.getBlock() != null && loc2.getBlock().getType() != null && !loc2.getBlock().getType().equals(Material.AIR)) {
                highest = cur;
            }
        }
        Location loc3 = new Location(loc.getWorld(), loc.getX(), highest, loc.getZ());
        loc3.setPitch(loc.getPitch());
        loc3.setYaw(loc.getYaw());
        return loc3;
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String getPermission() {
        return "uc.top";
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
        Player p = (Player) cs;
        if (!r.perm(cs, "uc.top", false, true)) {
            return;
        }
        Location loc = getHighestY(p.getLocation());
        if (loc == null || loc.getY() == 0) {
            r.sendMes(cs, "topFailed");
            return;
        }
        loc.add(0, 1.01, 0);
        LocationUtil.teleport(p, loc, TeleportCause.COMMAND, false, true);
        r.sendMes(cs, "topMessage");
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
