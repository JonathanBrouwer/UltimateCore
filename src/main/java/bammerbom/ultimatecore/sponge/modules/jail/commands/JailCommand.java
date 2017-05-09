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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.TimeArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.jail.JailModule;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailData;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.modules.jail.commands.arguments.JailArgument;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@CommandInfo(module = JailModule.class, aliases = {"jail"})
public class JailCommand implements HighCommand {
    static Random random = new Random();

    @Override
    public Permission getPermission() {
        return JailPermissions.UC_JAIL_JAIL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(JailPermissions.UC_JAIL_JAIL_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build(),
                Arguments.builder(new JailArgument(Text.of("jail"))).optional().onlyOne().build(),
                Arguments.builder(new TimeArgument(Text.of("time"))).optional().onlyOne().build(),
                Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, JailPermissions.UC_JAIL_JAIL_BASE);

        //Find player
        UUID suuid = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
        Player t = args.<Player>getOne("player").get();
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);

        //Exempt power
        if ((JailPermissions.UC_JAIL_EXEMPTPOWER.getIntFor(t) > JailPermissions.UC_JAIL_POWER.getIntFor(sender)) && sender instanceof Player) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "jail.command.jail.exempt", "%player%", t));
        }

        //Find jail, time and reason
        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        if (jails.isEmpty() && !args.hasAny("jail")) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "jail.command.jaillist.empty"));
        }
        Jail jail = args.hasAny("jail") ? args.<Jail>getOne("jail").get() : jails.get(random.nextInt(jails.size()));
        Long time = args.hasAny("time") ? args.<Long>getOne("time").get() : -1L;
        Text reason = args.hasAny("reason") ? Text.of(args.<String>getOne("reason").get()) : Messages.getFormatted("jail.command.jail.defaultreason");

        //If jail is not available
        if (!jail.getLocation().isPresent()) {
            throw Messages.error(sender, "jail.command.jail.notavailable");
        }

        JailData data = new JailData(t.getUniqueId(), suuid, time == -1 ? -1 : (time + System.currentTimeMillis()), System.currentTimeMillis(), reason, jail.getName());
        ut.offer(JailKeys.JAIL, data);
        Messages.send(sender, "jail.command.jail.success", "%player%", t, "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        Messages.send(t, "jail.target.jailed", "%player%", sender, "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        return CommandResult.success();
    }
}
