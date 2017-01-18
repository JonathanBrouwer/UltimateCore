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
package bammerbom.ultimatecore.sponge.modules.home.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.home.api.Home;
import bammerbom.ultimatecore.sponge.modules.home.api.HomeKeys;
import bammerbom.ultimatecore.sponge.modules.home.api.HomePermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DelhomeCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.HOME.get();
    }

    @Override
    public String getIdentifier() {
        return "delhome";
    }

    @Override
    public Permission getPermission() {
        return HomePermissions.UC_HOME_DELHOME_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HomePermissions.UC_HOME_DELHOME_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("delhome", "removehome");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (!sender.hasPermission(HomePermissions.UC_HOME_DELHOME_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage(sender));
            return CommandResult.empty();
        }
        UltimateUser user = UltimateCore.get().getUserService().getUser(p);
        List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
        //Remove home with matching name
        homes = homes.stream().filter(home -> !home.getName().equalsIgnoreCase(args[0])).collect(Collectors.toList());
        user.offer(HomeKeys.HOMES, homes);

        sender.sendMessage(Messages.getFormatted(sender, "home.command.delhome.success", "%home%", args[0].toLowerCase()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UltimateUser user = UltimateCore.get().getUserService().getUser(p);
            List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
            return homes.stream().map(Home::getName).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
