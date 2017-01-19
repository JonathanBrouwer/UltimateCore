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
import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.home.HomeModule;
import bammerbom.ultimatecore.sponge.modules.home.api.Home;
import bammerbom.ultimatecore.sponge.modules.home.api.HomeKeys;
import bammerbom.ultimatecore.sponge.modules.home.api.HomePermissions;
import bammerbom.ultimatecore.sponge.modules.home.commands.arguments.HomeArgument;
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RegisterCommand(module = HomeModule.class, aliases = {"home"})
public class HomeCommand implements SmartCommand {
    @Override
    public Permission getPermission() {
        return HomePermissions.UC_HOME_HOME_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HomePermissions.UC_HOME_HOME_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new HomeArgument(Text.of("home"))).onlyOne().optional().usage("<Home>").build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, HomePermissions.UC_HOME_HOME_BASE);
        Player p = (Player) sender;
        UltimateUser user = UltimateCore.get().getUserService().getUser(p);
        List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
        if (!args.hasAny("home")) {
            if (homes.isEmpty()) {
                sender.sendMessage(Messages.getFormatted(sender, "home.command.homelist.empty"));
                return CommandResult.empty();
            }
            List<Text> entries = new ArrayList<>();
            for (Home home : homes) {
                entries.add(Messages.getFormatted("home.command.homelist.entry", "%home%", home.getName()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("home.command.homelist.hoverentry", "%home%", home.getName()))).onClick(TextActions.runCommand("/home " + home.getName())).build());
            }
            Collections.sort(entries);

            Text footer;
            if (!p.hasPermission(HomePermissions.UC_HOME_SETHOME_UNLIMITED.get())) {
                String shomecount = HomePermissions.UC_HOME_HOMECOUNT.getFor(sender);
                if (!ArgumentUtil.isInteger(shomecount)) {
                    sender.sendMessage(Messages.getFormatted(sender, "home.command.sethome.invalidhomecount", "%homecount%", shomecount));
                    return CommandResult.empty();
                }
                Integer homecount = Integer.parseInt(shomecount);
                footer = Messages.getFormatted("home.command.homelist.footer", "%current%", homes.size(), "%max%", homecount);
            } else {
                footer = Messages.getFormatted("home.command.homelist.footer.unlimited");
            }

            PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
            PaginationList paginationList = paginationService.builder().contents(entries).title(Messages.getFormatted("home.command.homelist.header").toBuilder().format(Messages.getFormatted("home.command.homelist.char").getFormat()).build()).footer(footer).padding(Messages.getFormatted("home.command.homelist.char")).build();
            paginationList.sendTo(sender);
            return CommandResult.empty();
        }

        Home home = args.<Home>getOne("home").get();
        //Teleport
        Teleportation request = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), home.getLocation(), req -> {
            sender.sendMessage(Messages.getFormatted(sender, "home.command.home.success", "%home%", home.getName()));
        }, (red, reason) -> {
        }, true, false);
        request.start();
        return CommandResult.success();
    }

}
