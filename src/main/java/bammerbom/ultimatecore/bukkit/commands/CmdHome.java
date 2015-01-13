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
import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdHome implements UltimateCommand {

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getPermission() {
        return "uc.home";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (r.checkArgs(args, 0) == false) {
            if (!(r.isPlayer(cs))) {
                return;
            }
            if (!r.perm(cs, "uc.home", true, true)) {
                return;
            }
            Player p = (Player) cs;
            ArrayList<String> homes = UC.getPlayer(p).getHomeNames();
            String a = "";
            Integer b = 0;
            try {
                Integer amount = homes.toArray().length;
                for (int i = 0; i < amount; i++) {
                    a = a + homes.get(b) + ", ";
                    b++;

                }
                a = a.substring(0, a.length() - 2);
            } catch (IndexOutOfBoundsException ex) {
            }
            if (a.equalsIgnoreCase("") || a.equalsIgnoreCase(null)) {
                r.sendMes(cs, "homeNoHomesFound");
                return;
            } else {
                r.sendMes(cs, "homeList", "%Homes", a);
                return;
            }
        } else {
            OfflinePlayer t;
            if (r.perm(cs, "uc.home", true, true) == false) {
                return;
            }
            if (args[0].contains(":")) {
                if (!r.perm(cs, "uc.home.others", false, true)) {
                    return;
                }
                if (args[0].endsWith(":") || args[0].endsWith(":list")) {
                    t = r.searchPlayer(args[0].split(":")[0]);
                    if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                        r.sendMes(cs, "PlayerNotFound", "%Player", args[0]);
                        return;
                    }
                    ArrayList<String> homes = UC.getPlayer(t).getHomeNames();
                    String a = "";
                    Integer b = 0;
                    try {
                        Integer amount = homes.toArray().length;
                        for (int i = 0; i < amount; i++) {
                            a = a + homes.get(b) + ", ";
                            b++;

                        }
                        a = a.substring(0, a.length() - 2);
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    if (a.equalsIgnoreCase("") || a.equalsIgnoreCase(null)) {
                        r.sendMes(cs, "homeNoHomesFound");
                        return;
                    } else {
                        r.sendMes(cs, "homeList", "%Homes", a);
                        return;
                    }
                }
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                t = r.searchOfflinePlayer(args[0].split(":")[0]);
                if (t == null || (!t.hasPlayedBefore() && !t.isOnline())) {
                    r.sendMes(cs, "PlayerNotFound", "%Player", args[0].split(":")[0]);
                    return;
                }
                List<String> homes = UC.getPlayer(t).getHomeNames();
                if (!homes.contains(args[0].split(":")[1].toLowerCase())) {
                    r.sendMes(cs, "homeNotExist", "%Home", args[0]);
                    return;
                }
                try {
                    //Teleport
                    Location location = UC.getPlayer(t).getHome(args[0].toLowerCase().split(":")[1]);
                    if (r.isPlayer(cs)) {
                        LocationUtil.teleport(p, location, TeleportCause.COMMAND);
                    }
                    r.sendMes(cs, "homeTeleport", "%Home", args[0]);
                } catch (Exception ex) {
                    r.sendMes(cs, "homeInvalid", "%Home", args[0]);
                    ErrorLogger.log(ex, "Failed to load home: " + args[0]);
                }
                return;
            }
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            //Exist
            List<String> homes = UC.getPlayer(p).getHomeNames();
            if (!homes.contains(args[0].toLowerCase())) {
                r.sendMes(cs, "homeNotExist", "%Home", args[0]);
                return;
            }
            try {
                //Teleport
                Location location = UC.getPlayer(p).getHome(args[0].toLowerCase());
                LocationUtil.teleport(p, location, TeleportCause.COMMAND);
                r.sendMes(cs, "homeTeleport", "%Home", args[0]);
            } catch (Exception ex) {
                r.sendMes(cs, "homeInvalid", "%Home", args[0]);
                ErrorLogger.log(ex, "Failed to load home: " + args[0]);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (!r.isPlayer(cs)) {
            return new ArrayList<>();
        }
        return UC.getPlayer((Player) cs).getHomeNames();
    }
}
