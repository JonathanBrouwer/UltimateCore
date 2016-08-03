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
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class AfkCommand implements Command {

    @Override
    public Module getModule() {
        return Modules.AFK.get();
    }

    @Override
    public String getIdentifier() {
        return "afk";
    }

    @Override
    public Permission getPermission() {
        return AfkPermissions.UC_AFK;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(AfkPermissions.UC_AFK, AfkPermissions.UC_AFK_MESSAGE, AfkPermissions.UC_AFK_OTHERS, AfkPermissions.UC_AFK_OTHERS_MESSAGE);
    }

    @Override
    public Text getUsage() {
        //TODO <command>?
        return CMGenerator.usage(this, Messages.getFormatted("afk.command.afk.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("afk.command.afk.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return CMGenerator.longDescription(this, Messages.getFormatted("afk.command.afk.longdescription"));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("afk", "idle", "away");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        //Permission check
        if (!sender.hasPermission(AfkPermissions.UC_AFK.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions", "%permission%", AfkPermissions.UC_AFK.get()));
            return CommandResult.empty();
        }
        //Get the user
        UltimateUser user;
        if (args.length >= 1 && Sponge.getServer().getPlayer(args[0]).isPresent()) {
            user = UltimateCore.get().getUserService().getUser(Sponge.getServer().getPlayer(args[0]).get());
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer", "%source%", sender.getName()));
                return CommandResult.empty();
            }
            user = UltimateCore.get().getUserService().getUser((Player) sender);
        }
        //No isPresent() needed because IS_AFK has a default value
        boolean newafk = !user.get(AfkKeys.IS_AFK).get();
        if (user.offer(AfkKeys.IS_AFK, newafk)) {
            if (newafk) {
                user.offer(AfkKeys.AFK_TIME, System.currentTimeMillis());
                if (args.length >= 2 || (args.length >= 1 && !Sponge.getServer().getPlayer(args[0]).isPresent())) {
                    String message = StringUtil.getFinalArg(args, Sponge.getServer().getPlayer(args[0]).isPresent() ? 1 : 0);
                    user.offer(AfkKeys.AFK_MESSAGE, message);
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk.message", "%player%", user.getUser().getName(), "%message%", message));
                } else {
                    Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.afk", "%player%", user.getUser().getName()));
                }
            } else {
                Sponge.getServer().getBroadcastChannel().send(sender, Messages.getFormatted("afk.broadcast.nolonger", "%player%", user.getUser().getName(), "%time%", TimeUtil
                        .formatDateDiff(user.get(AfkKeys.AFK_TIME).get(), 2, null)));
                user.offer(AfkKeys.AFK_TIME, null);
                user.offer(AfkKeys.AFK_MESSAGE, null);
            }
        } else {
            sender.sendMessage(Messages.getFormatted("afk.command.afk.datafailed"));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
