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

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportPermissions;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TpaRequest;
import bammerbom.ultimatecore.sponge.modules.teleport.utils.TeleportUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = TeleportModule.class, aliases = {"teleportdeny", "tpdeny", "tpno"})
public class TeleportdenyCommand implements SmartCommand {
    @Override
    public Permission getPermission() {
        return TeleportPermissions.UC_TELEPORT_TELEPORTDENY_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TeleportPermissions.UC_TELEPORT_TELEPORTDENY_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("tpid"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, TeleportPermissions.UC_TELEPORT_TELEPORTDENY_BASE);
        Player p = (Player) sender;

        //Find request
        String id = args.hasAny("tpid") ? args.<String>getOne("tpid").get() : null;
        List<TpaRequest> requests = TeleportUtil.getRequestsFor(p, id);
        if (requests.isEmpty()) {
            sender.sendMessage(Messages.getFormatted(sender, "teleport.command.teleportdeny.none"));
            return CommandResult.empty();
        }
        requests.forEach(request -> request.getTeleportation().cancel("tpdeny"));

        sender.sendMessage(Messages.getFormatted(sender, "teleport.command.teleportdeny.success"));
        return CommandResult.success();
    }

}