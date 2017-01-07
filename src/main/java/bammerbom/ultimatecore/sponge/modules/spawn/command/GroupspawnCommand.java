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
package bammerbom.ultimatecore.sponge.modules.spawn.command;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GroupspawnCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.SPAWN.get();
    }

    @Override
    public String getIdentifier() {
        return "groupspawn";
    }

    @Override
    public Permission getPermission() {
        return SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("groupspawn");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }

        HashMap<String, Transform<World>> spawns = GlobalData.get(SpawnKeys.GROUP_SPAWNS).get();
        //Find player to teleport & Group to teleport to
        Player t;
        boolean self;
        String group;
        if (args.length == 0) {
            //groupspawn
            //Player
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            t = (Player) sender;
            self = true;

            //Group
            group = SpawnPermissions.UC_SPAWN_GROUPSPAWN.getFor(t);
            if (group == null || !spawns.containsKey(group.toLowerCase())) {
                sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.nogroup", "%player%", VariableUtil.getNameSource(t)));
                return CommandResult.empty();
            }
            group = group.toLowerCase();
        } else if (args.length == 1) {
            //Player
            t = Selector.one(sender, args[0]).orElse(null);
            if (t == null) {
                //groupspawn group
                self = true;
                //Player
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.getFormatted("core.noplayer"));
                    return CommandResult.empty();
                }
                t = (Player) sender;
                //Group
                group = args[0].toLowerCase();
                if (!spawns.containsKey(group)) {
                    sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.noplayerorgroup", "%arg%", args[0]));
                    return CommandResult.empty();
                }
            } else {
                //groupspawn player
                self = false;
                if (!sender.hasPermission(SpawnPermissions.UC_SPAWN_GROUPSPAWN_OTHERS_BASE.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                    return CommandResult.empty();
                }
                //Group
                group = SpawnPermissions.UC_SPAWN_GROUPSPAWN.getFor(t);
                if (group == null || !spawns.containsKey(group.toLowerCase())) {
                    sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.nogroup", "%player%", VariableUtil.getNameSource(t)));
                    return CommandResult.empty();
                }
                group = group.toLowerCase();
            }
        } else {
            //groupspawn player group
            //Player
            self = false;
            t = Selector.one(sender, args[0]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }
            //Group
            group = args[1].toLowerCase();
            if (!spawns.containsKey(group)) {
                sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.noplayerorgroup", "%arg%", args[1]));
                return CommandResult.empty();
            }
        }

        //Perm check
        if (self) {
            if (!sender.hasPermission("uc.spawn.groupspawn.group." + group.toLowerCase())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
        } else {
            if (!sender.hasPermission("uc.spawn.groupspawn.others.group." + group.toLowerCase())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
        }

        //Teleport
        Transform<World> loc = spawns.get(group);
        final Player t2 = t;
        final String g2 = group;
        Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(t), loc, tel -> {
            if (self) {
                sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.success.self", "%group%", g2));
            } else {
                sender.sendMessage(Messages.getFormatted("spawn.command.groupspawn.success.others.self", "%player%", VariableUtil.getNameSource(t2), "%group%", g2));
                t2.sendMessage(Messages.getFormatted("spawn.command.groupspawn.success.others.others", "%player%", VariableUtil.getNameSource(sender), "%group%", g2));
            }
        }, (tel, reason) -> {
        }, false, false);
        tp.start();
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
