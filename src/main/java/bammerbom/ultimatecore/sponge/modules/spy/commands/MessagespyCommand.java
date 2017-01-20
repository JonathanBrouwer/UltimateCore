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
package bammerbom.ultimatecore.sponge.modules.spy.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.spy.SpyModule;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyKeys;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = SpyModule.class, aliases = {"messagespy", "msgspy", "spymessage", "spymsg", "whisperspy", "spywhisper"})
public class MessagespyCommand implements SmartCommand {
    @Override
    public Permission getPermission() {
        return SpyPermissions.UC_SPY_MESSAGESPY_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpyPermissions.UC_SPY_MESSAGESPY_BASE, SpyPermissions.UC_SPY_MESSAGESPY_OTHERS);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, SpyPermissions.UC_SPY_MESSAGESPY_BASE);
        if (!args.hasAny("player")) {
            //Toggle own
            checkIfPlayer(sender);
            Player p = (Player) sender;
            UltimateUser user = UltimateCore.get().getUserService().getUser(p);
            boolean status = user.get(SpyKeys.MESSAGESPY_ENABLED).get();
            status = !status;
            user.offer(SpyKeys.MESSAGESPY_ENABLED, status);

            sender.sendMessage(Messages.getFormatted(sender, "spy.command.messagespy.self", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled")));
            return CommandResult.success();
        } else {
            //Toggle someone else
            checkPermission(sender, SpyPermissions.UC_SPY_MESSAGESPY_OTHERS);
            Player t = args.<Player>getOne("player").get();

            UltimateUser user = UltimateCore.get().getUserService().getUser(t);
            boolean status = user.get(SpyKeys.MESSAGESPY_ENABLED).get();
            status = !status;
            user.offer(SpyKeys.MESSAGESPY_ENABLED, status);

            t.sendMessage(Messages.getFormatted(t, "spy.command.messagespy.self", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled")));
            sender.sendMessage(Messages.getFormatted(sender, "spy.command.messagespy.others", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled"), "%player%", VariableUtil.getNameEntity(t)));
            return CommandResult.success();
        }
    }
}
