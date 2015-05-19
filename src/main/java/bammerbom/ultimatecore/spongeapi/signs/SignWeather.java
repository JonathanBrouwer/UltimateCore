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

public class SignWeather implements UltimateSign {

    @Override
    public String getName() {
        return "weather";
    }

    @Override
    public String getPermission() {
        return "uc.sign.weather";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.weather", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        if (sign.getLine(1).equalsIgnoreCase("sun")) {
            p.getWorld().setThundering(false);
            p.getWorld().setStorm(false);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherSun"));
            return;
        } else if (sign.getLine(1).equalsIgnoreCase("rain")) {
            p.getWorld().setThundering(false);
            p.getWorld().setStorm(true);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherRain"));
            return;
        } else if (sign.getLine(1).equalsIgnoreCase("thunderstorm")) {
            p.getWorld().setStorm(true);
            p.getWorld().setThundering(true);
            r.sendMes(p, "weatherSet", "%Weather", r.mes("weatherThunder"));
            return;
        } else {
            r.sendMes(p, "signWeatherNotFound");
            sign.setLine(0, ChatColor.RED + "[Weather]");
            return;
        }
    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.weather.create", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        if (!event.getLine(1).equalsIgnoreCase("sun") && !event.getLine(1).equalsIgnoreCase("rain") && !event.getLine(1).equalsIgnoreCase("thunderstorm")) {
            r.sendMes(event.getPlayer(), "signWeatherNotFound");
            event.setLine(0, ChatColor.RED + "[Weather]");
            return;
        }
        event.setLine(0, ChatColor.DARK_BLUE + "[Weather]");
        r.sendMes(event.getPlayer(), "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.weather.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getPlayer(), "signDestroyed");
    }
}
