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
package bammerbom.ultimatecore.sponge.modules.god.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.god.GodModule;
import bammerbom.ultimatecore.sponge.modules.god.api.GodKeys;
import bammerbom.ultimatecore.sponge.modules.god.api.GodPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = GodModule.class, aliases = {"god", "godmode"})
public class GodCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return GodPermissions.UC_GOD_GOD_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GodPermissions.UC_GOD_GOD_BASE, GodPermissions.UC_GOD_GOD_OTHERS);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, GodPermissions.UC_GOD_GOD_BASE);
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;

            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            boolean god = up.get(GodKeys.GOD).get();
            god = !god;
            up.offer(GodKeys.GOD, god);
            sender.sendMessage(Messages.getFormatted(sender, "god.command.god.self", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled")));
            return CommandResult.success();
        } else {
            checkPermission(sender, GodPermissions.UC_GOD_GOD_OTHERS);
            Player t = args.<Player>getOne("player").get();

            UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
            boolean god = ut.get(GodKeys.GOD).get();
            god = !god;
            ut.offer(GodKeys.GOD, god);
            sender.sendMessage(Messages.getFormatted(sender, "god.command.god.others.self", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled"), "%player%", t));
            t.sendMessage(Messages.getFormatted(t, "god.command.god.others.others", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled"), "%player%", sender));
            return CommandResult.success();
        }
    }
}
