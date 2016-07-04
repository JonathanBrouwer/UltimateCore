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
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
    public String getUsage() {
        return "/<command> [Player] <Player> OR [Player] <X> [Y] <Z>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Teleport yourself or someone else.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("tp", "tele", "tppos");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.teleport", true)) {
            return CommandResult.empty();
        }
        //Teleport menu
        if (!r.checkArgs(args, 0)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (r.getOnlinePlayers().length > 64) {
                r.sendMes(cs, "teleportTooMuchPlayers");
                return CommandResult.empty();
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
                return CommandResult.empty();
            }
            p.openInventory(inv);
        } else if (r.checkArgs(args, 1) && (r.isDouble(args[0].replace("~", "")) || args[0].replace("~", "").isEmpty()) && (r.isDouble(args[1].replace("~", "")) || args[1].replace("~",
                "").isEmpty())) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (!r.perm(cs, "uc.teleport.coords", true)) {
                return CommandResult.empty();
            }
            World w = p.getWorld();
            Double x = LocationUtil.getCoordinate(args[0], p.getLocation().getX());
            Double y;
            Double z;
            Vector3d rot = p.getRotation(); // pitch, yaw, roll
            if (r.checkArgs(args, 2) == false) {
                z = LocationUtil.getCoordinate(args[1], p.getLocation().getZ());
                y = (double) LocationUtil.getHighestBlockYAt(w, x.intValue(), z.intValue()).orElse(256);
            } else if (r.checkArgs(args, 3) == false) {
                y = LocationUtil.getCoordinate(args[1], p.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], p.getLocation().getZ());
            } else if (r.checkArgs(args, 4)) {
                y = LocationUtil.getCoordinate(args[1], p.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], p.getLocation().getZ());
                if (r.checkArgs(args, 5)) {
                    rot = new Vector3d(LocationUtil.getCoordinate(args[3], Double.valueOf(p.getRotation().getX())), LocationUtil.getCoordinate(args[4], Double.valueOf(p.getRotation().getY
                            ())), LocationUtil.getCoordinate(args[5], Double.valueOf(p.getRotation().getZ())));
                } else {
                    rot = new Vector3d(LocationUtil.getCoordinate(args[3], Double.valueOf(p.getRotation().getX())), LocationUtil.getCoordinate(args[4], Double.valueOf(p.getRotation().getY
                            ())), p.getRotation().getZ());
                }
            } else {
                r.sendMes(cs, "teleportUsage");
                return CommandResult.empty();
            }
            LocationUtil.teleport(p, new Location(w, x, y, z), Cause.builder().build(), true, true);
            p.setRotation(rot);
            r.sendMes(cs, "teleportMessage3", "%x", x, "%y", y, "%z", z);
        } else if (r.checkArgs(args, 2) == true && (r.isDouble(args[1].replace("~", "")) || args[1].replace("~", "").isEmpty()) && (!r.isDouble(args[0].replace("~", "")) && !args[0]
                .replace("~", "").isEmpty())) {
            if (!r.perm(cs, "uc.teleport.coords.others", true)) {
                return CommandResult.empty();
            }
            Player t = r.searchPlayer(args[0]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            World w = t.getWorld();
            Double x = LocationUtil.getCoordinate(args[0], t.getLocation().getX());
            Double y;
            Double z;
            Vector3d rot = t.getRotation(); // pitch, yaw, roll
            if (r.checkArgs(args, 2) == false) {
                z = LocationUtil.getCoordinate(args[1], t.getLocation().getZ());
                y = (double) LocationUtil.getHighestBlockYAt(w, x.intValue(), z.intValue()).orElse(256);
            } else if (r.checkArgs(args, 3) == false) {
                y = LocationUtil.getCoordinate(args[1], t.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], t.getLocation().getZ());
            } else if (r.checkArgs(args, 4)) {
                y = LocationUtil.getCoordinate(args[1], t.getLocation().getY());
                z = LocationUtil.getCoordinate(args[2], t.getLocation().getZ());
                if (r.checkArgs(args, 5)) {
                    rot = new Vector3d(LocationUtil.getCoordinate(args[3], Double.valueOf(t.getRotation().getX())), LocationUtil.getCoordinate(args[4], Double.valueOf(t.getRotation().getY
                            ())), LocationUtil.getCoordinate(args[5], Double.valueOf(t.getRotation().getZ())));
                } else {
                    rot = new Vector3d(LocationUtil.getCoordinate(args[3], Double.valueOf(t.getRotation().getX())), LocationUtil.getCoordinate(args[4], Double.valueOf(t.getRotation().getY
                            ())), t.getRotation().getZ());
                }
            } else {
                r.sendMes(cs, "teleportUsage");
                return CommandResult.empty();
            }
            LocationUtil.teleport(t, new Location(w, x, y, z), Cause.builder().build(), true, false);
            t.setRotation(rot);
            r.sendMes(cs, "teleportMessage4", "%Player", t.getName(), "%x", x, "%y", y, "%z", z);
        } else {
            Player tg = r.searchPlayer(args[0]).orElse(null);
            if (tg == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            } else {
                if (r.checkArgs(args, 1) == false) {
                    if (!r.isPlayer(cs)) {
                        return CommandResult.empty();
                    }
                    Player p = (Player) cs;
                    if (!UC.getPlayer(tg).hasTeleportEnabled() && !r.perm(cs, "uc.tptoggle.override", false)) {
                        r.sendMes(cs, "teleportDisabled", "%Player", tg.getName());
                        return CommandResult.empty();
                    }
                    LocationUtil.teleport(p, tg, Cause.builder().build(), true, true);
                    r.sendMes(cs, "teleportMessage1", "%Player", tg.getName());
                } else {
                    if (!r.perm(cs, "uc.teleport.others", true)) {
                        return CommandResult.empty();
                    }
                    Player tg2 = r.searchPlayer(args[1]).orElse(null);
                    if (tg2 == null) {
                        r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    } else {
                        if (UC.getPlayer(tg).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false)) {
                            r.sendMes(cs, "teleportDisabled", "%Player", tg.getName());
                            return CommandResult.empty();
                        }
                        if (UC.getPlayer(tg2).hasTeleportEnabled() == false && !r.perm(cs, "uc.tptoggle.override", false)) {
                            r.sendMes(cs, "teleportDisabled", "%Player", tg2.getName());
                            return CommandResult.empty();
                        }
                        LocationUtil.teleport(tg, tg2, Cause.builder().build(), true, false);
                        LocationUtil.playEffect(tg, tg2.getLocation());
                        r.sendMes(cs, "teleportMessage2", "%Player", tg.getName(), "%Target", tg2.getName());
                    }
                }

            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
