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
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.god.api.GodKeys;
import bammerbom.ultimatecore.sponge.modules.god.api.GodPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class GodCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.GOD.get();
    }

    @Override
    public String getIdentifier() {
        return "god";
    }

    @Override
    public Permission getPermission() {
        return GodPermissions.UC_GOD;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GodPermissions.UC_GOD, GodPermissions.UC_GOD_OTHERS);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("god", "godmode");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(GodPermissions.UC_GOD.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
            Player p = (Player) sender;
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            boolean god = up.get(GodKeys.GOD).get();
            god = !god;
            up.offer(GodKeys.GOD, god);
            sender.sendMessage(Messages.getFormatted("god.command.god.self", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled")));
            return CommandResult.success();
        } else {
            if (!sender.hasPermission(GodPermissions.UC_GOD_OTHERS.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            Player t = Selector.one(sender, args[0]).orElse(null);
            if (t == null) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }
            UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
            boolean god = ut.get(GodKeys.GOD).get();
            god = !god;
            ut.offer(GodKeys.GOD, god);
            sender.sendMessage(Messages.getFormatted("god.command.god.others.self", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled"), "%player%", VariableUtil.getNameEntity(t)));
            t.sendMessage(Messages.getFormatted("god.command.god.others.others", "%status%", god ? Messages.getFormatted("god.command.god.enabled") : Messages.getFormatted("god.command.god.disabled"), "%player%", VariableUtil.getNameSource(sender)));
            return CommandResult.success();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
