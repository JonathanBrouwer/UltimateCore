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

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kick.KickModule;
import bammerbom.ultimatecore.sponge.modules.kick.api.KickPermissions;
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

@CommandInfo(module = KickModule.class, aliases = {"kick"})
public class KickCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return KickPermissions.UC_KICK_KICK_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KickPermissions.UC_KICK_KICK_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build(), Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, KickPermissions.UC_KICK_KICK_BASE);

        Player target = args.<Player>getOne("player").get();
        Text reason = args.hasAny("reason") ? Text.of(args.<String>getOne("reason").get()) : Messages.getFormatted("kick.command.kick.defaultreason");
        if (sender.equals(target)) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "kick.command.kick.self"));
        }
        if ((KickPermissions.UC_KICK_EXEMPTPOWER.getIntFor(target) > KickPermissions.UC_KICK_POWER.getIntFor(sender)) && sender instanceof Player) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "kick.command.kick.exempt", "%player%", target));
        }
        Sponge.getServer().getBroadcastChannel().send(Messages.getFormatted("kick.command.kick.broadcast", "%kicker%", sender, "%kicked%", target, "%reason%", reason));
        target.kick(Messages.getFormatted("kick.command.kick.message", "%kicker%", sender, "%kicked%", target, "%reason%", reason));
        return CommandResult.success();
    }
}
