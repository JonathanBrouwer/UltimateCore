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
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.modules.home.HomeModule;
import bammerbom.ultimatecore.sponge.modules.home.api.Home;
import bammerbom.ultimatecore.sponge.modules.home.api.HomeKeys;
import bammerbom.ultimatecore.sponge.modules.home.api.HomePermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(module = HomeModule.class, aliases = {"sethome", "addhome"})
public class SethomeCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return HomePermissions.UC_HOME_SETHOME_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HomePermissions.UC_HOME_SETHOME_BASE, HomePermissions.UC_HOME_SETHOME_UNLIMITED);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("home"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, HomePermissions.UC_HOME_SETHOME_BASE);
        Player p = (Player) sender;
        String homename = args.<String>getOne("home").get();

        String shomecount = HomePermissions.UC_HOME_HOMECOUNT.getFor(sender);
        if (!ArgumentUtil.isInteger(shomecount)) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "home.command.sethome.invalidhomecount", "%homecount%", shomecount));
        }
        Integer homecount = Integer.parseInt(shomecount);
        UltimateUser user = UltimateCore.get().getUserService().getUser((Player) sender);
        List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
        boolean replace = homes.stream().filter(home -> home.getName().equalsIgnoreCase(homename)).count() >= 1;
        //If amount of homes (+1 if not replace) is higher than max amount of homes
        if ((homes.size() + (replace ? 0 : 1)) > homecount && !sender.hasPermission(HomePermissions.UC_HOME_SETHOME_UNLIMITED.get())) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "home.command.sethome.maxhomes"));
        }
        //Remove home with matching name
        homes = homes.stream().filter(home -> !home.getName().equalsIgnoreCase(homename)).collect(Collectors.toList());
        homes.add(new Home(homename.toLowerCase(), new Transform<>(p.getLocation(), p.getRotation(), p.getScale())));
        user.offer(HomeKeys.HOMES, homes);

        if (replace) {
            Messages.send(sender, "home.command.sethome.success.replace", "%home%", homename.toLowerCase());
        } else {
            Messages.send(sender, "home.command.sethome.success.set", "%home%", homename.toLowerCase());
        }
        return CommandResult.success();
    }
}
