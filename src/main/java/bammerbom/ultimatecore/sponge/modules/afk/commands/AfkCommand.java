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
package bammerbom.ultimatecore.sponge.modules.afk.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.DataFailedException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.afk.AfkModule;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkDetectionListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = AfkModule.class, aliases = {"afk", "idle", "away"})
public class AfkCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return AfkPermissions.UC_AFK_AFK_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(AfkPermissions.UC_AFK_AFK_BASE, AfkPermissions.UC_AFK_AFK_MESSAGE, AfkPermissions.UC_AFK_AFK_OTHERS_BASE, AfkPermissions.UC_AFK_AFK_OTHERS_MESSAGE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optionalWeak().build(),
                Arguments.builder(new RemainingStringsArgument(Text.of("message"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, AfkPermissions.UC_AFK_AFK_BASE);
        UltimateUser user;
        if (args.hasAny("player")) {
            checkPermission(sender, AfkPermissions.UC_AFK_AFK_OTHERS_BASE);
            user = UltimateCore.get().getUserService().getUser(args.<Player>getOne("player").get());
        } else {
            checkIfPlayer(sender);
            user = UltimateCore.get().getUserService().getUser((Player) sender);
        }

        //No isPresent() needed because IS_AFK has a default value
        boolean newafk = !user.get(AfkKeys.IS_AFK).get();
        if (user.offer(AfkKeys.IS_AFK, newafk)) {
            if (newafk) {
                user.offer(AfkKeys.AFK_TIME, System.currentTimeMillis());
                if (args.hasAny("message")) {
                    //Check perms
                    if (!args.hasAny("player")) {
                        checkPermission(sender, AfkPermissions.UC_AFK_AFK_MESSAGE);
                    } else {
                        checkPermission(sender, AfkPermissions.UC_AFK_AFK_OTHERS_MESSAGE);
                    }
                    String message = args.<String>getOne("message").get();
                    user.offer(AfkKeys.AFK_MESSAGE, message);
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk.message", "%player%", user.getUser(), "%message%", message));
                } else {
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk", "%player%", user.getUser()));
                }
                //Make sure the player is not un-afked instantly
                AfkDetectionListener.afktime.put(user.getIdentifier(), 0L);
                user.offer(AfkKeys.LAST_LOCATION, new Transform<>(user.getPlayer().get().getLocation(), user.getPlayer().get().getRotation(), user.getPlayer().get().getScale()));
            } else {
                Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.nolonger", "%player%", user.getUser(), "%time%", TimeUtil.formatDateDiff(user.get(AfkKeys.AFK_TIME).get(), 2, null)));
                user.offer(AfkKeys.AFK_TIME, null);
                user.offer(AfkKeys.AFK_MESSAGE, null);
            }
        } else {
            throw new DataFailedException(Messages.getFormatted(sender, "afk.command.afk.datafailed"));
        }
        return CommandResult.success();
    }
}
