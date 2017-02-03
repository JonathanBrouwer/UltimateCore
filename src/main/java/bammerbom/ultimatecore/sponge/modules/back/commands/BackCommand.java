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
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.exceptions.DataFailedException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.back.BackModule;
import bammerbom.ultimatecore.sponge.modules.back.api.BackKeys;
import bammerbom.ultimatecore.sponge.modules.back.api.BackPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CommandInfo(module = BackModule.class, aliases = {"back", "return"})
public class BackCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return BackPermissions.UC_BACK_BACK_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(BackPermissions.UC_BACK_BACK_BASE, BackPermissions.UC_BACK_BACK_ONDEATH);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{};
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, BackPermissions.UC_BACK_BACK_BASE);
        Player p = (Player) sender;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        Optional<Transform<World>> loc = up.get(BackKeys.BACK);
        if (!loc.isPresent()) {
            throw new DataFailedException(Messages.getFormatted(sender, "back.command.back.notfound"));
        }

        Teleportation tp = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(p), loc.get(), tel -> {
            Messages.send(sender, "back.command.back.success");
        }, (tel, reason) -> {
        }, true, false);
        tp.start();
        return CommandResult.success();
    }
}
