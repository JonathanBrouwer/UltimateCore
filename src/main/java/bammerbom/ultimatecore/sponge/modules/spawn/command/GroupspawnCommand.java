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
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.teleport.serializabletransform.SerializableTransform;
import bammerbom.ultimatecore.sponge.modules.spawn.SpawnModule;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnKeys;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CommandInfo(module = SpawnModule.class, aliases = {"groupspawn"})
public class GroupspawnCommand implements HighCommand {

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
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optionalWeak().build(),
                Arguments.builder(new StringArgument(Text.of("group"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, SpawnPermissions.UC_SPAWN_GROUPSPAWN_BASE);
        HashMap<String, SerializableTransform> spawns = GlobalData.get(SpawnKeys.GROUP_SPAWNS).get();

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
                    throw new ErrorMessageException(Messages.getFormatted(sender, "spawn.command.groupspawn.nogroup", "%player%", p));
                } else {
                    throw new ErrorMessageException(Messages.getFormatted(sender, "spawn.command.groupspawn.noplayerorgroup", "%arg%", group));
                }
            }

            //Teleport
            Transform<World> loc = spawns.get(group).toOptional().orElseThrow(() -> Messages.error(sender, "spawn.command.spawn.notavailable"));
            Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(p), loc, tel -> {
                Messages.send(sender, "spawn.command.groupspawn.success.self", "%group%", group);
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
                    throw new ErrorMessageException(Messages.getFormatted(sender, "spawn.command.groupspawn.nogroup", "%player%", t));
                } else {
                    throw new ErrorMessageException(Messages.getFormatted(sender, "spawn.command.groupspawn.noplayerorgroup", "%arg%", group));
                }
            }

            //Teleport
            Transform<World> loc = spawns.get(group).toOptional().orElseThrow(() -> Messages.error(sender, "spawn.command.spawn.notavailable"));
            Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(t), loc, tel -> {
                Messages.send(sender, "spawn.command.groupspawn.success.others.self", "%player%", t, "%group%", group);
                Messages.send(t, "spawn.command.groupspawn.success.others.others", "%player%", sender, "%group%", group);
            }, (tel, reason) -> {
            }, false, false);
            tp.start();
            return CommandResult.success();
        }
    }
}
