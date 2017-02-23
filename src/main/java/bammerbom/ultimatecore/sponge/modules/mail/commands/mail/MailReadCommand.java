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
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.UuidArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.mail.MailModule;
import bammerbom.ultimatecore.sponge.modules.mail.api.Mail;
import bammerbom.ultimatecore.sponge.modules.mail.api.MailKeys;
import bammerbom.ultimatecore.sponge.modules.mail.commands.MailCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@CommandInfo(module = MailModule.class, aliases = {"read", "view", "list"})
@CommandParentInfo(parent = MailCommand.class)
public class MailReadCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new UuidArgument(Text.of("mail"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        checkIfPlayer(src);
        Player p = (Player) src;
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);

        //If a specific mail is supplied, view that mail
        if (args.getOne("mail").isPresent()) {
            try {
                UUID uuid = args.<UUID>getOne("mail").get();
                List<Mail> mails = up.get(MailKeys.MAILS_RECEIVED).get();
                Mail mail = mails.stream().filter(m -> m.getMailid().equals(uuid)).findFirst().orElse(null);
                if (mail == null) {
                    throw Messages.error(src, "core.invaliduuid", "%uuid%", uuid);
                }
                GameProfile profile = Sponge.getServer().getGameProfileManager().get(mail.getSender()).get();
                Messages.send(src, "mail.command.mail.read.single", "%player%", profile.getName().orElse(profile.getUniqueId().toString()), "%mailid%", mail.getMailid(), "%date%", TimeUtil.format(System.currentTimeMillis() - mail.getDate()), "%sender%", profile.getName().orElse(profile.getUniqueId().toString()), "%message%", mail.getMessage());
                return CommandResult.success();
            } catch (Exception ex) {
                ex.printStackTrace();
                return CommandResult.empty();
            }
        }

        //Get and sort received mails
        List<Mail> received = up.get(MailKeys.MAILS_RECEIVED).get();
        received.sort(Comparator.comparing(Mail::getDate));

        //Check if empty
        if (received.isEmpty()) {
            Messages.send(src, "mail.command.mail.read.empty");
            return CommandResult.success();
        }

        //Convert to text
        List<Text> content = new ArrayList<>();
        for (Mail mail : received) {
            try {
                GameProfile sender = Sponge.getServer().getGameProfileManager().get(mail.getSender()).get();
                content.add(Messages.getFormatted(src, "mail.command.mail.read.format", "%player%", sender.getName().orElse(""), "%message%", mail.getMessage()).toBuilder().onClick(TextActions.runCommand("/mail read " + mail.getMailid().toString())).onHover(TextActions.showText(Messages.getFormatted("mail.command.mail.read.moreinfo"))).build());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Send page
        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(content).title(Messages.getFormatted("mail.command.mail.read.header").toBuilder().color(TextColors.DARK_GREEN).build()).build();
        paginationList.sendTo(src);

        return CommandResult.success();

        /*
        Builder I used for the mail.command.mail.read.single message:
                Text read = Text.builder()
                .append(Text.builder("[Reply] ").color(TextColors.GREEN).onHover(TextActions.showText(Text.of(TextColors.GREEN, "Click to reply"))).onClick(TextActions.suggestCommand("/mail send %player% ")).build())
                .append(Text.builder("[Delete]\n").color(TextColors.RED).onHover(TextActions.showText(Text.of(TextColors.RED, "Click to delete mail"))).onClick(TextActions.suggestCommand("/mail delete %mailid%")).build())
                .append(Text.of(TextColors.DARK_AQUA, "Date sent: ")).append(Text.of(TextColors.AQUA, "%date% ago\n"))
                .append(Text.of(TextColors.DARK_AQUA, "Sender: ")).append(Text.of(TextColors.AQUA, "%sender%\n"))
                .append(Text.of(TextColors.DARK_AQUA, "Message: \n")).append(Text.of(TextColors.AQUA, "%message%"))
                .build();
         */
    }
}
