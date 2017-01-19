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
import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.arguments.TimeArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.jail.JailModule;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailData;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.modules.jail.commands.arguments.JailArgument;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RegisterCommand(module = JailModule.class, aliases = {"jail"})
public class JailCommand implements SmartCommand {
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
                Arguments.builder(GenericArguments.remainingJoinedStrings(Text.of("reason"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        if (!sender.hasPermission(JailPermissions.UC_JAIL_JAIL_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }

        //Find player
        UUID suuid = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
        Player t = args.<Player>getOne("player").get();
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);

        //Exempt power
        if ((JailPermissions.UC_JAIL_EXEMPTPOWER.getIntFor(t) > JailPermissions.UC_JAIL_POWER.getIntFor(sender)) && sender instanceof Player) {
            sender.sendMessage(Messages.getFormatted(sender, "jail.command.jail.exempt", "%player%", VariableUtil.getNameSource(t)));
            return CommandResult.empty();
        }

        //Find jail, time and reason
        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        Jail jail = args.hasAny("jail") ? args.<Jail>getOne("jail").get() : jails.get(random.nextInt(jails.size()));
        Long time = args.hasAny("time") ? args.<Long>getOne("time").get() : -1L;
        Text reason = args.hasAny("reason") ? Text.of(args.<String>getOne("reason").get()) : Messages.getFormatted("jail.command.jail.defaultreason");

        JailData data = new JailData(t.getUniqueId(), suuid, time == -1 ? -1 : (time + System.currentTimeMillis()), System.currentTimeMillis(), reason, jail.getName());
        ut.offer(JailKeys.JAIL, data);
        sender.sendMessage(Messages.getFormatted(sender, "jail.command.jail.success", "%player%", VariableUtil.getNameSource(t), "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
        t.sendMessage(Messages.getFormatted(t, "jail.target.jailed", "%player%", VariableUtil.getNameSource(sender), "%jail%", jail.getName(), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason));
        return CommandResult.success();
    }
}
