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
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SethomeCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.HOME.get();
    }

    @Override
    public String getIdentifier() {
        return "sethome";
    }

    @Override
    public Permission getPermission() {
        return HomePermissions.UC_SETHOME;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HomePermissions.UC_SETHOME, HomePermissions.UC_SETHOME_UNLIMITED);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sethome", "addhome");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (!sender.hasPermission(HomePermissions.UC_SETHOME.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        //Home count, or else 1 home
        String shomecount = sender.getOption("home-count").orElse("1");
        if (!TimeUtil.isNumber(shomecount)) {
            sender.sendMessage(Messages.getFormatted("home.command.sethome.invalidhomecount", "%homecount%", shomecount));
            return CommandResult.empty();
        }
        Integer homecount = Integer.parseInt(shomecount);
        UltimateUser user = UltimateCore.get().getUserService().getUser((Player) sender);
        List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
        boolean replace = homes.stream().filter(home -> home.getName().equalsIgnoreCase(args[0])).count() >= 1;
        //If amount of homes (+1 if not replace) is higher than max amount of homes
        if ((homes.size() + (replace ? 0 : 1)) > homecount && !sender.hasPermission(HomePermissions.UC_SETHOME_UNLIMITED.get())) {
            sender.sendMessage(Messages.getFormatted("home.command.sethome.maxhomes"));
            return CommandResult.empty();
        }
        //Remove home with matching name
        homes = homes.stream().filter(home -> !home.getName().equalsIgnoreCase(args[0])).collect(Collectors.toList());
        homes.add(new Home(args[0].toLowerCase(), new Transform<World>(p.getLocation(), p.getRotation(), p.getScale())));
        user.offer(HomeKeys.HOMES, homes);

        if (replace) {
            sender.sendMessage(Messages.getFormatted("home.command.sethome.success.replace", "%home%", args[0].toLowerCase()));
        } else {
            sender.sendMessage(Messages.getFormatted("home.command.sethome.success.set", "%home%", args[0].toLowerCase()));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
