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
package bammerbom.ultimatecore.sponge.modules.mail.commands.mail;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.GameprofileArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.mail.MailModule;
import bammerbom.ultimatecore.sponge.modules.mail.api.Mail;
import bammerbom.ultimatecore.sponge.modules.mail.api.MailKeys;
import bammerbom.ultimatecore.sponge.modules.mail.commands.MailCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = MailModule.class, aliases = {"send"})
@CommandParentInfo(parent = MailCommand.class)
@CommandPermissions(level = PermissionLevel.EVERYONE)
public class SendMailCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new GameprofileArgument(Text.of("player"))).onlyOne().build(), Arguments.builder(new RemainingStringsArgument(Text.of("message"))).onlyOne().build()};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Get sender
        checkIfPlayer(src);
        Player p = (Player) src;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);

        //Get target & construct mail
        GameProfile t = args.<GameProfile>getOne("player").get();
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t.getUniqueId()).get();
        String message = args.<String>getOne("message").get();
        Mail mail = new Mail(p.getUniqueId(), Arrays.asList(t.getUniqueId()), System.currentTimeMillis(), message);

        //Offer to data api
        List<Mail> sentMail = up.get(MailKeys.MAILS_SENT).get();
        sentMail.add(mail);
        up.offer(MailKeys.MAILS_SENT, sentMail);
        List<Mail> receivedMail = ut.get(MailKeys.MAILS_RECEIVED).get();
        receivedMail.add(mail);
        ut.offer(MailKeys.MAILS_RECEIVED, receivedMail);

        //Increase unread count
        up.offer(MailKeys.UNREAD_MAIL, up.get(MailKeys.UNREAD_MAIL).get() + 1);

        Messages.send(src, "mail.command.mail.send", "%player%", t.getName().orElse(""));
        if (ut.getPlayer().isPresent()) {
            ut.getPlayer().get().sendMessage(Messages.getFormatted(ut.getPlayer().get(), "mail.command.mail.newmail", "%count%", up.get(MailKeys.UNREAD_MAIL).get()).toBuilder().onHover(TextActions.showText(Messages.getFormatted(ut.getPlayer().get(), "mail.command.mail.newmail.hover"))).onClick(TextActions.runCommand("/mail read")).build());
        }
        return CommandResult.success();
    }
}
