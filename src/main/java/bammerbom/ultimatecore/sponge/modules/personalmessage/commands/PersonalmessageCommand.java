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
package bammerbom.ultimatecore.sponge.modules.personalmessage.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.CommandsourceArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.personalmessage.PersonalmessageModule;
import bammerbom.ultimatecore.sponge.modules.personalmessage.api.PersonalmessageEvent;
import bammerbom.ultimatecore.sponge.modules.personalmessage.api.PersonalmessageKeys;
import bammerbom.ultimatecore.sponge.modules.personalmessage.api.PersonalmessagePermissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@CommandInfo(module = PersonalmessageModule.class, aliases = {"personalmessage", "pm", "dm", "msg", "w", "whisper", "tell"})
public class PersonalmessageCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return PersonalmessagePermissions.UC_PERSONALMESSAGE_PERSONALMESSAGE_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(PersonalmessagePermissions.UC_PERSONALMESSAGE_PERSONALMESSAGE_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new CommandsourceArgument(Text.of("player"))).onlyOne().build(), Arguments.builder(new RemainingStringsArgument(Text.of("message"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, PersonalmessagePermissions.UC_PERSONALMESSAGE_PERSONALMESSAGE_BASE);
        CommandSource t = args.<CommandSource>getOne("player").get();

        String message = args.<String>getOne("message").get();
        Text fmessage = Messages.getFormatted("personalmessage.command.personalmessage.format.receive", "%player%", sender, "%message%", message);

        //Event
        Cause cause = Cause.builder().append(UltimateCore.getContainer()).append(sender).append(t).build(EventContext.builder().build());
        MessageEvent.MessageFormatter formatter = new MessageEvent.MessageFormatter(fmessage);
        final CommandSource tf = t;
        MessageChannel channel = () -> Arrays.asList(tf);
        PersonalmessageEvent event = new PersonalmessageEvent(cause, sender, t, formatter, channel, message, fmessage);
        Sponge.getEventManager().post(event);
        if (!event.isMessageCancelled()) {
            Text received = event.getFormatter().toText();
            t.sendMessage(received);
            //Reply
            UUID uuid_s = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
            UUID uuid_t = t instanceof Player ? ((Player) t).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
            if (t instanceof Player) {
                UltimateUser user = UltimateCore.get().getUserService().getUser((Player) t);
                user.offer(PersonalmessageKeys.REPLY, uuid_s);
            }
            if (sender instanceof Player) {
                UltimateUser user2 = UltimateCore.get().getUserService().getUser((Player) sender);
                user2.offer(PersonalmessageKeys.REPLY, uuid_t);
            }
            //TODO better system for this message?
            Text send = Messages.getFormatted("personalmessage.command.personalmessage.format.send", "%player%", t, "%message%", message);
            sender.sendMessage(send);
            return CommandResult.success();
        } else {
            throw new ErrorMessageException(Messages.getFormatted(t, "personalmessage.command.personalmessage.cancelled"));
        }
    }
}
