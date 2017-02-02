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
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportKeys;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TpaRequest;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandInfo(module = TeleportModule.class, aliases = {"teleportaskallhere", "tpaallhere", "teleportaskall", "tpaall"})
public class TeleportaskallhereCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, getPermission());
        Player p = (Player) sender;
        List<Player> ts = new ArrayList<>(Sponge.getServer().getOnlinePlayers());

        for (Player t : ts) {
            if (t.equals(sender)) continue;
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

            t.sendMessage(Messages.getFormatted(t, "teleport.command.teleportaskhere.receive", "%player%", sender, "%tpid%", tpid));
        }
        sender.sendMessage(Messages.getFormatted(sender, "teleport.command.teleportaskallhere.self"));

        return CommandResult.success();
    }
}
