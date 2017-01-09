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
package bammerbom.ultimatecore.sponge.modules.kick.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kick.api.KickPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class KickCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KICK.get();
    }

    @Override
    public String getIdentifier() {
        return "kick";
    }

    @Override
    public Permission getPermission() {
        return KickPermissions.UC_KICK_KICK;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KickPermissions.UC_KICK_KICK);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kick");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(KickPermissions.UC_KICK_KICK.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        Player target = Selector.one(sender, args[0]).orElse(null);
        if (target == null) {
            sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
            return CommandResult.empty();
        }
        if (sender.getName().equalsIgnoreCase(target.getName())) {
            sender.sendMessage(Messages.getFormatted("kick.command.kick.self"));
            return CommandResult.empty();
        }
        if ((KickPermissions.UC_KICK_EXEMPTPOWER.getIntFor(target) > KickPermissions.UC_KICK_POWER.getIntFor(sender)) && sender instanceof Player) {
            sender.sendMessage(Messages.getFormatted("kick.command.kick.exempt", "%player%", VariableUtil.getNameSource(target)));
            return CommandResult.empty();
        }
        if (args.length == 1) {
            Sponge.getServer().getBroadcastChannel().send(Messages.getFormatted("kick.command.kick.broadcast", "%kicker%", sender.getName(), "%kicked%", target.getName(), "%reason%", Messages.getFormatted("kick.command.kick.defaultreason")));
            target.kick(Messages.getFormatted("kick.command.kick.message", "%kicker%", sender.getName(), "%kicked%", target.getName(), "%reason%", Messages.getFormatted("kick.command.kick.defaultreason")));
        } else {
            Sponge.getServer().getBroadcastChannel().send(Messages.getFormatted("kick.command.kick.broadcast", "%kicker%", sender.getName(), "%kicked%", target.getName(), "%reason%", StringUtil.getFinalArg(args, 1)));
            target.kick(Messages.getFormatted("kick.command.kick.message", "%kicker%", sender.getName(), "%kicked%", target.getName(), "%reason%", StringUtil.getFinalArg(args, 1)));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
