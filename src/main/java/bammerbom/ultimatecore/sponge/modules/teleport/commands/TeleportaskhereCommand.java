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
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportKeys;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportPermissions;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TpaRequest;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CommandInfo(module = TeleportModule.class, aliases = {"teleportaskhere", "teleportah", "tpahere", "asktphere", "askteleporthere"})
public class TeleportaskhereCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return TeleportPermissions.UC_TELEPORT_TELEPORTASKHERE_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(TeleportPermissions.UC_TELEPORT_TELEPORTASKHERE_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, TeleportPermissions.UC_TELEPORT_TELEPORTASKHERE_BASE);
        Player p = (Player) sender;
        Player t = args.<Player>getOne("player").get();

        UUID tpid = UUID.randomUUID();
        Teleportation tel = UltimateCore.get().getTeleportService().createTeleportation(sender, Arrays.asList(t), p::getTransform, tele -> {
            p.sendMessage(Messages.getFormatted(p, "teleport.command.teleportaskhere.accept", "%player%", t));
        }, (tele, reason) -> {
            if (reason.equalsIgnoreCase("tpdeny")) {
                p.sendMessage(Messages.getFormatted(p, "teleport.command.teleportaskhere.deny", "%player%", t));
            }
        }, true, false);
        HashMap<UUID, TpaRequest> tels = GlobalData.get(TeleportKeys.TELEPORT_ASKHERE_REQUESTS).get();
        tels.put(tpid, new TpaRequest(p, t, tel));
        GlobalData.offer(TeleportKeys.TELEPORT_ASKHERE_REQUESTS, tels);

        sender.sendMessage(Messages.getFormatted(sender, "teleport.command.teleportaskhere.send", "%player%", VariableUtil.getNameEntity(t)));
        t.sendMessage(Messages.getFormatted(t, "teleport.command.teleportaskhere.receive", "%player%", sender, "%tpid%", tpid));
        return CommandResult.success();
    }
}
