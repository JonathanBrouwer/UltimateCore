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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailKeys;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeljailCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.JAIL.get();
    }

    @Override
    public String getIdentifier() {
        return "deljail";
    }

    @Override
    public Permission getPermission() {
        return JailPermissions.UC_JAIL_DELJAIL_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(JailPermissions.UC_JAIL_DELJAIL_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("deljail", "removejail");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer", "%source%", sender.getName()));
            return CommandResult.empty();
        }
        if (!sender.hasPermission(JailPermissions.UC_JAIL_DELJAIL_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        String name = args[0].toLowerCase();
        //Remove jail
        List<Jail> jails = GlobalData.get(JailKeys.JAILS).get();
        for (Jail jail : new ArrayList<>(jails)) {
            if (jail.getName().equalsIgnoreCase(name)) {
                jails.remove(jail);
                GlobalData.offer(JailKeys.JAILS, jails);
                sender.sendMessage(Messages.getFormatted("jail.command.deljail.success", "%jail%", name));
                return CommandResult.success();
            }
        }
        sender.sendMessage(Messages.getFormatted("jail.command.deljail.notfound", "%jail%", args[0]));
        return CommandResult.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
