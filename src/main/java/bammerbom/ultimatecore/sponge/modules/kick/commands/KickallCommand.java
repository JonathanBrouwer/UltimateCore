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
package bammerbom.ultimatecore.sponge.modules.kick.commands;

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kick.KickModule;
import bammerbom.ultimatecore.sponge.modules.kick.api.KickPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = KickModule.class, aliases = {"kickall", "kickeveryone"})
public class KickallCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return KickPermissions.UC_KICK_KICKALL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KickPermissions.UC_KICK_KICKALL_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, KickPermissions.UC_KICK_KICKALL_BASE);

        Text reason = Text.of(args.<String>getOne("reason").get());

        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (p.getName().equals(sender.getName())) {
                continue;
            }
            p.kick(Messages.getFormatted("kick.command.kickall.message", "%kicker%", sender.getName(), "%reason%", reason));
        }
        sender.sendMessage(Messages.getFormatted(sender, "kick.command.kickall.success"));
        return CommandResult.success();
    }
}
