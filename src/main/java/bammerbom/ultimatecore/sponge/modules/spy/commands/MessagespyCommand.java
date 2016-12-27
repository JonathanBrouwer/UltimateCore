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
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyKeys;
import bammerbom.ultimatecore.sponge.modules.spy.api.SpyPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class MessagespyCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.SPY.get();
    }

    @Override
    public String getIdentifier() {
        return "messagespy";
    }

    @Override
    public Permission getPermission() {
        return SpyPermissions.UC_MESSAGESPY;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(SpyPermissions.UC_MESSAGESPY, SpyPermissions.UC_MESSAGESPY_OTHERS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("messagespy", "msgspy", "spymessage", "spymsg", "whisperspy", "spywhisper");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(getPermission().get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            //Toggle own
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            UltimateUser user = UltimateCore.get().getUserService().getUser(p);
            boolean status = user.get(SpyKeys.MESSAGESPY_ENABLED).get();
            status = !status;
            user.offer(SpyKeys.MESSAGESPY_ENABLED, status);

            sender.sendMessage(Messages.getFormatted("spy.command.messagespy.self", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled")));
            return CommandResult.success();
        } else {
            //Toggle someone else
            if (!sender.hasPermission(SpyPermissions.UC_MESSAGESPY_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            Player t = Sponge.getServer().getPlayer(args[0]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }

            UltimateUser user = UltimateCore.get().getUserService().getUser(t);
            boolean status = user.get(SpyKeys.MESSAGESPY_ENABLED).get();
            status = !status;
            user.offer(SpyKeys.MESSAGESPY_ENABLED, status);

            t.sendMessage(Messages.getFormatted("spy.command.messagespy.self", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled")));
            sender.sendMessage(Messages.getFormatted("spy.command.messagespy.others", "%status%", status ? Messages.getFormatted("spy.enabled") : Messages.get("spy.disabled"), "%player%",
                    VariableUtil.getName(t)));
            return CommandResult.success();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
