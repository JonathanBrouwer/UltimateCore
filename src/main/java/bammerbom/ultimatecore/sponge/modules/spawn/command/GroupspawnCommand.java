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
import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.spawn.SpawnModule;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RegisterCommand(module = SpawnModule.class, aliases = {"groupspawn"})
public class GroupspawnCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optionalWeak().build(), Arguments.builder(GenericArguments.string(Text.of("group"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE);
        HashMap<String, Transform<World>> spawns = GlobalData.get(SpawnKeys.GROUP_SPAWNS).get();

        //Find player to teleport & Group to teleport to
        if (!args.hasAny("player")) {
            //groupspawn
            //Player
            checkIfPlayer(sender);
            Player p = (Player) sender;
            String group = args.hasAny("group") ? args.<String>getOne("group").get().toLowerCase() : SpawnPermissions.UC_SPAWN_GROUPSPAWN.getFor(p).toLowerCase();
            //Perm check
            if (args.hasAny("group")) {
                checkPermission(sender, SpawnPermissions.UC_SPAWN_GROUPSPAWN_OTHERS_GROUP);
            }
            //Null check
            if (group == null || !spawns.containsKey(group)) {
                if (!args.hasAny("group")) {
                    sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.nogroup", "%player%", VariableUtil.getNameSource(p)));
                } else {
                    sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.noplayerorgroup", "%arg%", group));
                }
                return CommandResult.empty();
            }

            //Teleport
            Transform<World> loc = spawns.get(group);
            Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(p), loc, tel -> {
                sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.success.self", "%group%", group));
            }, (tel, reason) -> {
            }, false, false);
            tp.start();
            return CommandResult.success();
        } else {
            checkPermission(sender, SpawnPermissions.UC_SPAWN_GROUPSPAWN_OTHERS_BASE);
            Player t = args.<Player>getOne("player").get();
            String group = args.hasAny("group") ? args.<String>getOne("group").get().toLowerCase() : SpawnPermissions.UC_SPAWN_GROUPSPAWN.getFor(t).toLowerCase();
            //Perm check
            if (args.hasAny("group")) {
                checkPermission(sender, SpawnPermissions.UC_SPAWN_GROUPSPAWN_OTHERS_GROUP);
            }
            //Null check
            if (group == null || !spawns.containsKey(group)) {
                if (!args.hasAny("group")) {
                    sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.nogroup", "%player%", VariableUtil.getNameSource(t)));
                } else {
                    sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.noplayerorgroup", "%arg%", group));
                }
                return CommandResult.empty();
            }

            //Teleport
            Transform<World> loc = spawns.get(group);
            Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(t), loc, tel -> {
                sender.sendMessage(Messages.getFormatted(sender, "spawn.command.groupspawn.success.others.self", "%player%", VariableUtil.getNameSource(t), "%group%", group));
                t.sendMessage(Messages.getFormatted(t, "spawn.command.groupspawn.success.others.others", "%player%", VariableUtil.getNameSource(sender), "%group%", group));
            }, (tel, reason) -> {
            }, false, false);
            tp.start();
            return CommandResult.success();
        }
    }
}
