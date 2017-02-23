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
package bammerbom.ultimatecore.sponge.modules.mute.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.TimeArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.mute.MuteModule;
import bammerbom.ultimatecore.sponge.modules.mute.api.Mute;
import bammerbom.ultimatecore.sponge.modules.mute.api.MuteKeys;
import bammerbom.ultimatecore.sponge.modules.mute.api.MutePermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@CommandInfo(module = MuteModule.class, aliases = {"mute"})
public class MuteCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return MutePermissions.UC_MUTE_MUTE_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(MutePermissions.UC_MUTE_MUTE_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().build(),
                Arguments.builder(new TimeArgument(Text.of("time"))).optionalWeak().onlyOne().build(),
                Arguments.builder(new RemainingStringsArgument(Text.of("reason"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, MutePermissions.UC_MUTE_MUTE_BASE);

        //Get player & Check exempt
        Player t = args.<Player>getOne("player").get();
        if ((MutePermissions.UC_MUTE_EXEMPTPOWER.getIntFor(t) > MutePermissions.UC_MUTE_POWER.getIntFor(sender)) && sender instanceof Player) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "mute.command.mute.exempt", "%player%", t));
        }

        //Get time & reason
        Long time = args.hasAny("time") ? args.<Long>getOne("time").get() : -1L;
        Text reason = args.hasAny("reason") ? Text.of(args.<String>getOne("reason").get()) : Messages.getFormatted("mute.command.mute.defaultreason");

        Long endtime = time == -1L ? -1L : System.currentTimeMillis() + time;
        Long starttime = System.currentTimeMillis();
        UUID muter = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID muted = t.getUniqueId();

        Mute mute = new Mute(muted, muter, endtime, starttime, reason);
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
        ut.offer(MuteKeys.MUTE, mute);

        Messages.send(sender, "mute.command.mute.success", "%player%", VariableUtil.getNameEntity(t), "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        Messages.send(t, "mute.muted", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", reason);
        return CommandResult.success();
    }
}
