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

import bammerbom.ultimatecore.sponge.UltimateCore;
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
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KitCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KIT.get();
    }

    @Override
    public String getIdentifier() {
        return "kit";
    }

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_KIT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_KIT_BASE, KitPermissions.UC_KIT_KIT_KIT, KitPermissions.UC_KIT_KITLIST_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("kit");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(KitPermissions.UC_KIT_KIT_BASE.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Send the player a paginated list of all kits
        if (args.length == 0) {
            //Permissions
            if (!sender.hasPermission(KitPermissions.UC_KIT_KITLIST_BASE.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            //Get all kits
            List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
            List<Text> texts = new ArrayList<>();
            //Add entry to texts for every kit
            for (Kit kit : kits) {
                if (!sender.hasPermission("uc.kit.kit." + kit.getId().toLowerCase())) {
                    continue;
                }
                texts.add(Messages.getFormatted("kit.command.kitlist.entry", "%kit%", kit.getId(), "%description%", kit.getDescription()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("kit.command.kitlist.hoverentry", "%kit%", kit.getId()))).onClick(TextActions.runCommand("/kit " + kit.getId())).build());
            }
            //If empty send message
            if (texts.isEmpty()) {
                sender.sendMessage(Messages.getFormatted("kit.command.kitlist.empty"));
                return CommandResult.empty();
            }
            //Sort alphabetically
            Collections.sort(texts);

            PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
            PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("kit.command.kitlist.header").toBuilder().format(Messages.getFormatted("kit.command.kitlist.char").getFormat()).build()).padding(Messages.getFormatted("kit.command.kitlist.char")).build();
            paginationList.sendTo(sender);
            return CommandResult.empty();
        }
        //Teleport the player to a kit
        //Check is the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        //Try to find kit
        List<Kit> results = GlobalData.get(KitKeys.KITS).get().stream().filter(war -> args[0].toLowerCase().equalsIgnoreCase(war.getId().toLowerCase())).collect(Collectors.toList());
        if (results.isEmpty()) {
            sender.sendMessage(Messages.getFormatted("kit.command.kit.notfound", "%kit%", args[0]));
            return CommandResult.empty();
        }
        Kit kit = results.get(0);
        //Check permissions
        if (!sender.hasPermission(KitPermissions.UC_KIT_KIT_BASE.get()) && !sender.hasPermission("uc.kit." + kit.getId().toLowerCase())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Give items
        for (ItemStackSnapshot snapshot : kit.getItems()) {
            InventoryTransactionResult result = p.getInventory().offer(snapshot.createStack());
            result.getRejectedItems().forEach(item -> {
                Item itementity = (Item) p.getWorld().createEntity(EntityTypes.ITEM, p.getLocation().getPosition());
                itementity.offer(Keys.REPRESENTED_ITEM, item);
                p.getWorld().spawnEntity(itementity, Cause.builder().owner(UltimateCore.get()).named(NamedCause.of("player", p)).build());
            });
        }
        sender.sendMessage(Messages.getFormatted("kit.command.kit.success", "%kit%", kit.getId()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        List<Kit> results = GlobalData.get(KitKeys.KITS).get();
        List<String> names = new ArrayList<>();
        results.forEach(kit -> names.add(kit.getId()));
        return names;
    }
}
