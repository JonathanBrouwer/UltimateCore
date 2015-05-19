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
package bammerbom.ultimatecore.spongeapi.signs;

import bammerbom.ultimatecore.bukkit.UltimateSign;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignTime implements UltimateSign {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public String getPermission() {
        return "uc.sign.time";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.time", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        long time = p.getWorld().getTime();
        time -= time % 24000L;
        if (sign.getLine(1).equalsIgnoreCase("day")) {
            p.getWorld().setTime(time + 24000L);
            r.sendMes(p, "timeMessage", "%Time", r.mes("timeDay"));
        } else if (sign.getLine(1).equalsIgnoreCase("night")) {
            p.getWorld().setTime(time + 37700L);
            r.sendMes(p, "timeMessage", "%Time", r.mes("timeNight"));
        } else if (r.isInt(sign.getLine(1))) {
            p.getWorld().setTime(time + Integer.parseInt(sign.getLine(1)));
            r.sendMes(p, "timeMessage", "%Time", sign.getLine(1));
        } else {
            r.sendMes(p, "signTimeNotFound");
            sign.setLine(0, ChatColor.RED + "[Time]");
            return;
        }

    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.time.create", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        if (!event.getLine(1).equalsIgnoreCase("day") && !event.getLine(1).equalsIgnoreCase("night") && !r.isInt(event.getLine(1))) {
            r.sendMes(event.getPlayer(), "signTimeNotFound");
            event.setLine(0, ChatColor.RED + "[Time]");
            return;
        }
        event.setLine(0, ChatColor.DARK_BLUE + "[Time]");
        r.sendMes(event.getPlayer(), "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.time.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getPlayer(), "signDestroyed");
    }
}
