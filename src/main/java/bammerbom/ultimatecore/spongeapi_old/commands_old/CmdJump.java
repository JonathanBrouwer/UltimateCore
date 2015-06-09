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
package bammerbom.ultimatecore.spongeapi_old.commands_old;

import bammerbom.ultimatecore.spongeapi_old.r;
import bammerbom.ultimatecore.spongeapi_old.resources.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Arrays;
import java.util.List;

public class CmdJump implements UltimateCommand {

    @Override
    public String getName() {
        return "jump";
    }

    @Override
    public String getPermission() {
        return "uc.jump";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("jumpto");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!(r.isPlayer(cs))) {
            return;
        }
        if (!r.perm(cs, "uc.jump", false, true)) {
            return;
        }
        Player p = (Player) cs;
        final Location cloc = p.getLocation();
        Location loc = LocationUtil.getTarget(p);
        loc.setYaw(cloc.getYaw());
        loc.setPitch(cloc.getPitch());
        loc.setY(loc.getY() + 1);
        LocationUtil.teleport(p, loc, TeleportCause.COMMAND, true, true);
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
