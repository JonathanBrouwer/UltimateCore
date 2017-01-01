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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KitlistCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KIT.get();
    }

    @Override
    public String getIdentifier() {
        return "kitlist";
    }

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KITLIST;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KITLIST);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kitlist", "kits");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        //Permissions
        if (!sender.hasPermission(KitPermissions.UC_KITLIST.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Get all kits
        List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
        List<Text> texts = new ArrayList<>();
        //Add entry to texts for every kit
        for (Kit kit : kits) {
            if (!sender.hasPermission(KitPermissions.UC_KIT.get()) && !sender.hasPermission("uc.kit." + kit.getId().toLowerCase())) {
                continue;
            }
            texts.add(Messages.getFormatted("kit.command.kitlist.entry", "%kit%", kit.getId(), "%description%", kit.getDescription()).toBuilder().onHover(TextActions.showText(Messages.getFormatted
                    ("kit.command.kitlist.hoverentry", "%kit%", kit.getId()))).onClick(TextActions.runCommand("/kit " + kit.getId())).build());
        }
        //If empty send message
        if (texts.isEmpty()) {
            sender.sendMessage(Messages.getFormatted("kit.command.kitlist.empty"));
            return CommandResult.empty();
        }
        //Sort alphabetically
        Collections.sort(texts);

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("kit.command.kitlist.header").toBuilder().format(Messages.getFormatted("kit.command"
                + ".kitlist.char").getFormat()).build()).padding(Messages.getFormatted("kit.command.kitlist.char")).build();
        paginationList.sendTo(sender);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
