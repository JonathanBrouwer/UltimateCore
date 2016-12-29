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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.PlayerSelector;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;

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
            p.setTransform(t.getTransform());
            return CommandResult.success();
        } else if (args.length == 2) {
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
            e.forEach(en -> en.setTransform(t.getTransform()));
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
