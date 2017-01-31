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
package bammerbom.ultimatecore.sponge.modules.kit.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kit.KitModule;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CommandInfo(module = KitModule.class, aliases = {"kitlist", "kits"})
public class KitlistCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_KITLIST_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_KITLIST_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        //Permissions
        checkPermission(sender, KitPermissions.UC_KIT_KITLIST_BASE);
        //Get all kits
        List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
        List<Text> texts = new ArrayList<>();
        //Add entry to texts for every kit
        for (Kit kit : kits) {
            if (!sender.hasPermission("uc.kit.kit." + kit.getName().toLowerCase())) {
                continue;
            }
            texts.add(Messages.getFormatted("kit.command.kitlist.entry", "%kit%", kit.getName(), "%description%", kit.getDescription()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("kit.command.kitlist.hoverentry", "%kit%", kit))).onClick(TextActions.runCommand("/kit " + kit)).build());
        }
        //If empty send message
        if (texts.isEmpty()) {
            sender.sendMessage(Messages.getFormatted(sender, "kit.command.kitlist.empty"));
            return CommandResult.empty();
        }
        //Sort alphabetically
        Collections.sort(texts);

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("kit.command.kitlist.header").toBuilder().format(Messages.getFormatted("kit.command.kitlist.char").getFormat()).build()).padding(Messages.getFormatted("kit.command.kitlist.char")).build();
        paginationList.sendTo(sender);
        return CommandResult.success();
    }
}
