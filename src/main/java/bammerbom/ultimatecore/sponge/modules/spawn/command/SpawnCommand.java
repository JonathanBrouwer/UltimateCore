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
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.spawn.api.SpawnPermissions;
import bammerbom.ultimatecore.sponge.modules.spawn.utils.SpawnUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

public class SpawnCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.SPAWN.get();
    }

    @Override
    public String getIdentifier() {
        return "spawn";
    }

    @Override
    public Permission getPermission() {
        return SpawnPermissions.UC_SPAWN_SPAWN_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpawnPermissions.UC_SPAWN_SPAWN_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("spawn");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(SpawnPermissions.UC_SPAWN_SPAWN_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Find player to teleport
        Player t;
        boolean self;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            t = (Player) sender;
            self = true;
        } else {
            if (!sender.hasPermission(SpawnPermissions.UC_SPAWN_SPAWN_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            t = Selector.one(sender, args[0]).orElse(null);
            self = false;
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }
        }

        //Get group spawn, or global spawn, or world spawn
        Transform<World> loc = SpawnUtil.getSpawnLocation(t);

        Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(t), loc, tel -> {
            if (self) {
                sender.sendMessage(Messages.getFormatted("spawn.command.spawn.success.self"));
            } else {
                sender.sendMessage(Messages.getFormatted("spawn.command.spawn.success.others.self", "%player%", VariableUtil.getNameSource(t)));
                t.sendMessage(Messages.getFormatted("spawn.command.spawn.success.others.others", "%player%", VariableUtil.getNameSource(sender)));
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
