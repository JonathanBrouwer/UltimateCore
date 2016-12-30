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
package bammerbom.ultimatecore.sponge.modules.teleport.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.TeleportRequest;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportPermissions;
import bammerbom.ultimatecore.sponge.utils.*;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class TeleportCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.TELEPORT.get();
    }

    @Override
    public String getIdentifier() {
        return "teleport";
    }

    @Override
    public Permission getPermission() {
        return TeleportPermissions.UC_TELEPORT;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TeleportPermissions.UC_TELEPORT, TeleportPermissions.UC_TELEPORT_OTHERS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("teleport", "tp");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }

        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        } else if (args.length == 1) {
            //tp user
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            Entity t = PlayerSelector.oneEntity(sender, args[0]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.entitynotfound", "%entity%", args[0]));
                return CommandResult.empty();
            }

            //Teleport
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, Arrays.asList(p), t::getTransform, teleportRequest -> {
                //Complete
                p.sendMessage(Messages.getFormatted("teleport.command.teleport.self", "%target%", VariableUtil.getNameEntity(t)));
            }, teleportRequest -> {
            }, true);
            request.start();
            return CommandResult.success();
        } else if (args.length == 2 && TimeUtil.isDecimal(args[0]) && TimeUtil.isDecimal(args[1])) {
            //tp x z
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT_COORDINATES.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }

            Double x = Double.parseDouble(args[0]);
            Double z = Double.parseDouble(args[1]);
            Double y = Double.parseDouble(LocationUtil.getHighestY(p.getWorld(), x, z) + "") + 1;
            if (y == -1) {
                sender.sendMessage(Messages.getFormatted("teleport.command.teleport.noy"));
                return CommandResult.empty();
            }

            Location<World> target = new Location<>(p.getWorld(), x, y, z);
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, Arrays.asList(p), new Transform<>(target, p.getRotation(), p.getScale()),
                    teleportRequest -> {
                //Complete
                p.sendMessage(Messages.getFormatted("teleport.command.teleport.coords.self", "%x%", x.intValue(), "%y%", y.intValue(), "%z%", z.intValue()));
            }, teleportRequest -> {
            }, false);
            request.start();
            return CommandResult.success();
        } else if (args.length == 2) {
            //tp user user
            if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            List<Entity> e = PlayerSelector.multipleEntities(sender, args[0]);
            if (e.isEmpty()) {
                sender.sendMessage(Messages.getFormatted("core.entitynotfound", "%entity%", args[0]));
                return CommandResult.empty();
            }
            Entity t = PlayerSelector.oneEntity(sender, args[1]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.entitynotfound", "%entity%", args[1]));
                return CommandResult.empty();
            }

            //Teleport
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, e, t::getTransform, teleportRequest -> {
                //Complete
                sender.sendMessage(Messages.getFormatted("teleport.command.teleport.others", "%target1%", VariableUtil.getNamesEntity(e), "%target2%", VariableUtil.getNameEntity(t)));
            }, teleportRequest -> {
            }, true);
            request.start();
            return CommandResult.success();
        } else if (args.length == 3 && !TimeUtil.isDecimal(args[0])) {
            // tp user x z
            if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT_COORDINATES_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            List<Entity> e = PlayerSelector.multipleEntities(sender, args[0]);
            if (e.isEmpty()) {
                sender.sendMessage(Messages.getFormatted("core.entitynotfound", "%entity%", args[0]));
                return CommandResult.empty();
            }
            World w = sender instanceof Player ? ((Player) sender).getWorld() : e.get(0).getWorld();
            Double x = Double.parseDouble(args[0]);
            Double z = Double.parseDouble(args[1]);
            Double y = Double.parseDouble(LocationUtil.getHighestY(w, x, z) + "") + 1;
            if (y == -1) {
                sender.sendMessage(Messages.getFormatted("teleport.command.teleport.noy"));
                return CommandResult.empty();
            }

            Location<World> target = new Location<>(w, x, y, z);
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, e, new Transform<>(target), teleportRequest -> {
                //Complete
                sender.sendMessage(Messages.getFormatted("teleport.command.teleport.coords.self", "%x%", x.intValue(), "%y%", y.intValue(), "%z%", z.intValue()));
            }, teleportRequest -> {
            }, false);
            request.start();
            return CommandResult.success();
        } else if (args.length == 3) {
            //tp x y z
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT_COORDINATES.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }

            Double x = Double.parseDouble(args[0]);
            Double y = Double.parseDouble(args[1]);
            Double z = Double.parseDouble(args[2]);

            Location<World> target = new Location<>(p.getWorld(), x, y, z);
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, Arrays.asList(p), new Transform<>(target, p.getRotation(), p.getScale()),
                    teleportRequest -> {
                //Complete
                p.sendMessage(Messages.getFormatted("teleport.command.teleport.coords.self", "%x%", x.intValue(), "%y%", y.intValue(), "%z%", z.intValue()));
            }, teleportRequest -> {
            }, false);
            request.start();
            return CommandResult.success();
        } else if (args.length == 4) {
            //tp user x y z
            if (!sender.hasPermission(TeleportPermissions.UC_TELEPORT_COORDINATES_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            List<Entity> e = PlayerSelector.multipleEntities(sender, args[0]);
            if (e.isEmpty()) {
                sender.sendMessage(Messages.getFormatted("core.entitynotfound", "%entity%", args[0]));
                return CommandResult.empty();
            }

            World w = sender instanceof Player ? ((Player) sender).getWorld() : e.get(0).getWorld();
            Double x = Double.parseDouble(args[1]);
            Double y = Double.parseDouble(args[2]);
            Double z = Double.parseDouble(args[3]);

            Location<World> target = new Location<>(w, x, y, z);
            TeleportRequest request = UltimateCore.get().getTeleportService().createTeleportRequest(sender, e, new Transform<>(target), teleportRequest -> {
                //Complete
                sender.sendMessage(Messages.getFormatted("teleport.command.teleport.coords.self", "%x%", x.intValue(), "%y%", y.intValue(), "%z%", z.intValue()));
            }, teleportRequest -> {
            }, false);
            request.start();
            return CommandResult.success();
        }
        sender.sendMessage(getUsage());
        return CommandResult.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
