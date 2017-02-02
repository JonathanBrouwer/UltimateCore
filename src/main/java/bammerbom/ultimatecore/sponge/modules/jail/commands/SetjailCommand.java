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
package bammerbom.ultimatecore.sponge.modules.jail.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.jail.JailModule;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = JailModule.class, aliases = {"setjail", "addjail", "modifyjail"})
public class SetjailCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return JailPermissions.UC_JAIL_SETJAIL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(JailPermissions.UC_JAIL_SETJAIL_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("jail"))).onlyOne().build(), Arguments.builder(new RemainingStringsArgument(Text.of("description"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, JailPermissions.UC_JAIL_SETJAIL_BASE);
        Player p = (Player) sender;

        String name = args.<String>getOne("jail").get();
        String desc = args.hasAny("description") ? args.<String>getOne("description").get() : Messages.getFormatted("jail.command.setjail.defaultdescription").toPlain();
        Jail jail = new Jail(name, desc, p.getTransform());

        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        jails.add(jail);
        GlobalData.offer(JailKeys.JAILS, jails);

        sender.sendMessage(Messages.getFormatted(sender, "jail.command.setjail.success", "%jail%", name, "%description%", desc));
        return CommandResult.success();
    }
}
