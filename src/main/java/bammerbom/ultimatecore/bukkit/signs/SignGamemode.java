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
package bammerbom.ultimatecore.bukkit.signs;

import bammerbom.ultimatecore.bukkit.UltimateSign;
import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignGamemode implements UltimateSign {

    @Override
    public String getName() {
        return "gamemode";
    }

    @Override
    public String getPermission() {
        return "uc.sign.gamemode";
    }

    @Override
    public void onClick(Player p, Sign sign) {
        if (!r.perm(p, "uc.sign.gamemode", true, true) && !r.perm(p, "uc.sign", true, true)) {
            return;
        }
        GameMode mode;
        switch (sign.getLine(1).toLowerCase()) {
            case "survival":
            case "s":
            case "surv":
            case "0":
                mode = GameMode.SURVIVAL;
                break;
            case "creative":
            case "c":
            case "crea":
            case "1":
                mode = GameMode.CREATIVE;
                break;
            case "adventure:":
            case "a":
            case "adven":
            case "2":
                mode = GameMode.ADVENTURE;
                break;
            case "sp":
            case "spec":
            case "spectate":
            case "spectator":
            case "3":
                mode = GameMode.SPECTATOR;
                break;
            default:
                r.sendMes(p, "signGamemodeNotFound");
                sign.setLine(0, ChatColor.RED + "[Gamemode]");
                return;
        }
        p.setGameMode(mode);
        r.sendMes(p, "gamemodeSelf", "%Gamemode", mode.toString().toLowerCase());
    }

    @Override
    public void onCreate(SignChangeEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.gamemode.create", false, true)) {
            event.setCancelled(true);
            event.getBlock().breakNaturally();
            return;
        }
        try {
            GameMode mode;
            switch (event.getLine(1).toLowerCase()) {
                case "survival":
                case "s":
                case "surv":
                case "0":
                    mode = GameMode.SURVIVAL;
                    break;
                case "creative":
                case "c":
                case "crea":
                case "1":
                    mode = GameMode.CREATIVE;
                    break;
                case "adventure:":
                case "a":
                case "adven":
                case "2":
                    mode = GameMode.ADVENTURE;
                    break;
                case "sp":
                case "spec":
                case "spectate":
                case "spectator":
                case "3":
                    mode = GameMode.SPECTATOR;
                    break;
                default:
                    r.sendMes(event.getPlayer(), "signGamemodeNotFound");
                    event.setLine(0, ChatColor.RED + "[Gamemode]");
                    return;
            }
        } catch (Exception ex) {
            r.sendMes(event.getPlayer(), "signGamemodeNotFound");
            event.setLine(0, ChatColor.RED + "[Gamemode]");
            return;
        }
        event.setLine(0, ChatColor.DARK_BLUE + "[Gamemode]");
        r.sendMes(event.getPlayer(), "signCreated");
    }

    @Override
    public void onDestroy(BlockBreakEvent event) {
        if (!r.perm(event.getPlayer(), "uc.sign.gamemode.destroy", false, true)) {
            event.setCancelled(true);
            return;
        }
        r.sendMes(event.getPlayer(), "signDestroyed");
    }
}
