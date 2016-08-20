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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomelistCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.HOME.get();
    }

    @Override
    public String getIdentifier() {
        return "homelist";
    }

    @Override
    public Permission getPermission() {
        return HomePermissions.UC_HOME;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(HomePermissions.UC_HOME);
    }

    @Override
    public Text getUsage() {
        return Messages.getFormatted("home.command.homelist.usage");
    }

    @Override
    public Text getShortDescription() {
        return Messages.getFormatted("home.command.homelist.shortdescription");
    }

    @Override
    public Text getLongDescription() {
        return Messages.getFormatted("home.command.homelist.longdescription");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("homelist", "homes");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        UltimateUser user = UltimateCore.get().getUserService().getUser(p);
        if (!sender.hasPermission(HomePermissions.UC_HOME.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        List<Home> homes = user.get(HomeKeys.HOMES).orElse(new ArrayList<>());
        if (homes.isEmpty()) {
            sender.sendMessage(Messages.getFormatted("home.command.homelist.empty"));
            return CommandResult.empty();
        }
        List<Text> entries = new ArrayList<>();
        for (Home home : homes) {
            entries.add(Messages.getFormatted("home.command.homelist.entry", "%home%", home.getName()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("home.command" + "" +
                    ".homelist.hoverentry", "%home%", home.getName()))).onClick(TextActions.runCommand("/home " + home.getName())).build());
        }
        Collections.sort(entries);

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(entries).title(Messages.getFormatted("home.command.homelist.header").toBuilder().color(TextColors.DARK_GREEN)
                .build()).build();
        paginationList.sendTo(sender);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
