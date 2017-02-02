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
package bammerbom.ultimatecore.sponge.modules.nick.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionInfo;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.nick.NickModule;
import bammerbom.ultimatecore.sponge.modules.nick.api.NickKeys;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;

@CommandInfo(module = NickModule.class, aliases = {"delnick", "delnickname", "removenick", "removenickname", "resetnick", "resetnickname"})
@CommandPermissions(level = PermissionLevel.VIP)
public class DelnickCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).optional().build()
        };
    }

    @Override
    public Map<String, PermissionInfo> registerPermissionSuffixes() {
        HashMap<String, PermissionInfo> suffixes = new HashMap<>();
        suffixes.put("others.base", new PermissionInfo(Text.of("Allows you to remove other people's nicknames."), PermissionLevel.ADMIN));
        return suffixes;
    }


    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            up.offer(NickKeys.NICKNAME, null);
            sender.sendMessage(Messages.getFormatted(sender, "nick.command.delnick.self"));
        } else {
            checkPermSuffix(sender, "others.base");
            Player t = args.<Player>getOne("player").get();
            UltimateUser up = UltimateCore.get().getUserService().getUser(t);
            up.offer(NickKeys.NICKNAME, null);
            sender.sendMessage(Messages.getFormatted(sender, "nick.command.delnick.others.self", "%player%", t));
            t.sendMessage(Messages.getFormatted(t, "nick.command.delnick.others.others", "%player%", sender));
        }
        return CommandResult.success();
    }
}
