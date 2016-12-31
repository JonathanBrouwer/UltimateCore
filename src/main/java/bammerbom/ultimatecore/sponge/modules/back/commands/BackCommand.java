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
package bammerbom.ultimatecore.sponge.modules.back.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.back.api.BackKeys;
import bammerbom.ultimatecore.sponge.modules.back.api.BackPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BackCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.BACK.get();
    }

    @Override
    public String getIdentifier() {
        return "back";
    }

    @Override
    public Permission getPermission() {
        return BackPermissions.UC_BACK;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(BackPermissions.UC_BACK, BackPermissions.UC_BACK_ONDEATH);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("back", "return");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer"));
            return CommandResult.empty();
        }
        if (!sender.hasPermission(BackPermissions.UC_BACK.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        Optional<Transform<World>> loc = up.get(BackKeys.BACK);
        if (!loc.isPresent()) {
            sender.sendMessage(Messages.getFormatted("back.command.back.notfound"));
            return CommandResult.empty();
        }

        Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(p), loc.get(), tel -> {
            sender.sendMessage(Messages.getFormatted("back.command.back.success"));
        }, tel -> {
        }, true);
        tp.start();
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
