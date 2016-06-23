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
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class CmdTeleport implements UltimateCommand {

    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getPermission() {
        return "uc.teleport";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tp", "tele", "tppos");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.teleport", false, true)) {
            return;
        }
        //Teleport menu
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (r.getOnlinePlayers().length > 64) {
                r.sendMes(cs, "teleportTooMuchPlayers");
                return;
            }
            Integer size = 9;
            while (r.getOnlinePlayers().length > size) {
                size = size + 9;
            }
            Inventory inv = Bukkit.createInventory(null, size, r.mes("teleportSelectPlayer"));
            for (Player pl : r.getOnlinePlayers()) {
                if (!(pl == p)) {
                    ItemStack item = new ItemStack(Material.SKULL_ITEM);
                    item.setDurability(Short.parseShort("3"));
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setDisplayName(r.neutral + pl.getName());
                    meta.setOwner(pl.getName());
                    item.setItemMeta(meta);
                    inv.addItem(item);
                }
            }
            UC.getPlayer(p).setInTeleportMenu(true);
            if (inv.getItem(0) == null) {
                Inventory inv2 = Bukkit.createInventory(null, 9, r.mes("teleportNoPlayersOnline"));
                p.openInventory(inv2);
                return;
            }
            p.openInventory(inv);
        } else if (r.checkArgs(args, 1) && (r.isDouble(args[0].replace("~", "")) || args[0].replace("~", "").isEmpty()) && (r.isDouble(args[1].replace("~", "")) || args[1].replace("~", "")
                .isEmpty())) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.teleport.coords", false, true)) {
                return;
            }
            World w = p.getWorld();
            Double x = LocationUtil.getCoordinate(args[0], p.getLocation().getX());
            Double y;
            Double z;
            Float pitch;
            Float yaw;
            if (r.checkArgs(args, 2) == false) {
                z = LocationUtil.getCoordinate(args[1], p.getLocation().getZ());
                y = (double) w.getHighestBlockYAt(x.intValue(), z.intValue());
                pitch = p.getLocation().getPitch();
                yaw = p.getLocation().getYaw();
            } else if (r.checkArgs(args, 3) == false) {
                y = LocationUtil.getCoordinate(args[1], p.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], p.getLocation().getZ());
                pitch = p.getLocation().getPitch();
                yaw = p.getLocation().getYaw();
            } else if (r.checkArgs(args, 4)) {
                y = LocationUtil.getCoordinate(args[1], p.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], p.getLocation().getZ());
                //y-rot
                yaw = LocationUtil.getCoordinate(args[3], Double.valueOf(p.getLocation().getYaw())).floatValue();
                //x-rot
                pitch = LocationUtil.getCoordinate(args[4], Double.valueOf(p.getLocation().getPitch())).floatValue();
            } else {
                r.sendMes(cs, "teleportUsage");
                return;
            }
            LocationUtil.teleport(p, new Location(w, x, y, z, yaw, pitch), PlayerTeleportEvent.TeleportCause.COMMAND, true, true);
            r.sendMes(cs, "teleportMessage3", "%x", x, "%y", y, "%z", z);
        } else if (r.checkArgs(args, 2) == true && (r.isDouble(args[1].replace("~", "")) || args[1].replace("~", "").isEmpty()) && (!r.isDouble(args[0].replace("~", "")) && !args[0].replace("~", "")
                .isEmpty())) {
            if (!r.perm(cs, "uc.teleport.coords.others", false, true)) {
                return;
            }
            Player t = r.searchPlayer(args[0]);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return;
            }
            World w = t.getWorld();
            Double x = LocationUtil.getCoordinate(args[0], t.getLocation().getX());
            Double y;
            Double z;
            Float pitch;
            Float yaw;
            if (r.checkArgs(args, 3) == false) {
                z = LocationUtil.getCoordinate(args[2], t.getLocation().getZ());
                y = (double) w.getHighestBlockYAt(x.intValue(), z.intValue());
                pitch = t.getLocation().getPitch();
                yaw = t.getLocation().getYaw();
            } else if (r.checkArgs(args, 4) == false) {
                y = LocationUtil.getCoordinate(args[2], t.getLocation().getY());
                z = LocationUtil.getCoordinate(args[3], t.getLocation().getZ());
                pitch = t.getLocation().getPitch();
                yaw = t.getLocation().getYaw();
            } else if (r.checkArgs(args, 5)) {
                y = LocationUtil.getCoordinate(args[2], t.getLocation().getY());
                z = LocationUtil.getCoordinate(args[3], t.getLocation().getZ());
                //y-rot
                yaw = LocationUtil.getCoordinate(args[4], Double.valueOf(t.getLocation().getYaw())).floatValue();
                //x-rot
                pitch = LocationUtil.getCoordinate(args[5], Double.valueOf(t.getLocation().getPitch())).floatValue();
            } else {
                r.sendMes(cs, "teleportUsage");
                return;
            }
            LocationUtil.teleport(t, new Location(w, x, y, z, yaw, pitch), PlayerTeleportEvent.TeleportCause.COMMAND, true, false);
            r.sendMes(cs, "teleportMessage4", "%Player", t.getName(), "%x", x, "%y", y, "%z", z);
        } else {
            Player tg = r.searchPlayer(args[0]);
            if (tg == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            } else {
                if (r.checkArgs(args, 1) == false) {
                    if (!r.isPlayer(cs)) {
                        return;
                    }
                    Player p = (Player) cs;
                    if (!UC.getPlayer(tg).hasTeleportEnabled() && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                        r.sendMes(cs, "teleportDisabled", "%Player", tg.getName());
                        return;
                    }
                    LocationUtil.teleport(p, tg, PlayerTeleportEvent.TeleportCause.COMMAND, true, true);
                    r.sendMes(cs, "teleportMessage1", "%Player", tg.getName());
                } else {
                    if (!r.perm(cs, "uc.teleport.others", false, true)) {
                        return;
                    }
                    Player tg2 = r.searchPlayer(args[1]);
                    if (tg2 == null) {
                        r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    } else {
                        if (UC.getPlayer(tg).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                            r.sendMes(cs, "teleportDisabled", "%Player", tg.getName());
                            return;
                        }
                        if (UC.getPlayer(tg2).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false, false)) {
                            r.sendMes(cs, "teleportDisabled", "%Player", tg2.getName());
                            return;
                        }
                        LocationUtil.teleport(tg, tg2, PlayerTeleportEvent.TeleportCause.COMMAND, true, false);
                        LocationUtil.playEffect(tg, tg2.getLocation());
                        r.sendMes(cs, "teleportMessage2", "%Player", tg.getName(), "%Target", tg2.getName());
                    }
                }

            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
