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
package bammerbom.ultimatecore.sponge.modules.sudo.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.sudo.SudoModule;
import bammerbom.ultimatecore.sponge.modules.sudo.api.SudoPermissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CommandInfo(module = SudoModule.class, aliases = {"sudo", "fcommand", "fcmd", "fchat", "forcechat", "forcecmd", "forcecommand"})
public class SudoCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return SudoPermissions.UC_SUDO_SUDO_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SudoPermissions.UC_SUDO_SUDO_BASE, SudoPermissions.UC_SUDO_SUDO_CHAT, SudoPermissions.UC_SUDO_SUDO_COMMAND);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build(), Arguments.builder(new RemainingStringsArgument(Text.of("command"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, SudoPermissions.UC_SUDO_SUDO_BASE);

        Player t = args.<Player>getOne("player").get();
        String message = args.<String>getOne("command").get();
        boolean cmd = message.startsWith("/");
        if (cmd) {
            //COMMAND
            checkPermission(sender, SudoPermissions.UC_SUDO_SUDO_COMMAND);
            try {
                if (Sponge.getCommandManager().process(t, message.replaceFirst("/", "")).getSuccessCount().orElse(0) >= 1) {
                    //Success
                    Messages.send(sender, "sudo.command.sudo.command.success", "%target%", t, "%command%", message);
                } else {
                    //Failed
                    throw new ErrorMessageException(Messages.getFormatted(sender, "sudo.command.sudo.command.failed"));
                }
            } catch (Exception ex) {
                //Error
                Messages.send(sender, "sudo.command.sudo.command.error");
            }
        } else {
            //CHAT
            checkPermission(sender, SudoPermissions.UC_SUDO_SUDO_CHAT);
            MessageChannelEvent.Chat event = SpongeEventFactory.createMessageChannelEventChat(Cause.source(t).named(NamedCause.notifier(sender)).build(), t.getMessageChannel(), Optional.of(t.getMessageChannel()), new MessageEvent.MessageFormatter(Text.of(t), Text.of(message)), Text.of(message), false);
            if (!Sponge.getEventManager().post(event)) {
                //Success
                t.getMessageChannel().send(t, event.getMessage(), ChatTypes.CHAT);
                Messages.send(sender, "sudo.command.sudo.chat.success", "%target%", t, "%message%", message);
            } else {
                //Failed
                throw new ErrorMessageException(Messages.getFormatted("sudo.command.sudo.chat.failed"));
            }
        }
        return CommandResult.success();
    }
}
