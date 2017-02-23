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
import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.modules.kit.KitModule;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitPermissions;
import bammerbom.ultimatecore.sponge.modules.kit.commands.arguments.KitArgument;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
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

import java.util.*;

@CommandInfo(module = KitModule.class, aliases = {"kit"})
public class KitCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_KIT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_KIT_BASE, KitPermissions.UC_KIT_KIT_KIT, KitPermissions.UC_KIT_KITLIST_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new KitArgument(Text.of("kit"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, KitPermissions.UC_KIT_KIT_BASE);
        //Send the player a paginated list of all kits
        if (!args.hasAny("kit")) {
            //Permissions
            checkPermission(sender, KitPermissions.UC_KIT_KITLIST_BASE);
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
                throw new ErrorMessageException(Messages.getFormatted(sender, "kit.command.kitlist.empty"));
            }
            //Sort alphabetically
            Collections.sort(texts);

            PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
            PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("kit.command.kitlist.header").toBuilder().format(Messages.getFormatted("kit.command.kitlist.char").getFormat()).build()).padding(Messages.getFormatted("kit.command.kitlist.char")).build();
            paginationList.sendTo(sender);
            return CommandResult.empty();
        }
        //Check is the sender is a player
        checkIfPlayer(sender);
        Player p = (Player) sender;
        //Try to find kit
        Kit kit = args.<Kit>getOne("kit").get();
        //Check permissions
        checkPermission(sender, "uc.kit.kit." + kit.getId().toLowerCase());
        //Check & set lastused
        UltimateUser up = UltimateCore.get().getUserService().getUser(p);
        HashMap<String, Long> lastused = up.get(KitKeys.KIT_LASTUSED).get();
        Long kitlastused = lastused.get(kit.getId()) != null ? lastused.get(kit.getId()) : 0L;
        Long kitcooldown = kit.getCooldown();
        if (!sender.hasPermission(KitPermissions.UC_KIT_COOLDOWN_EXEMPT.get()) && ((kitcooldown <= -1L && kitlastused != 0L) || (System.currentTimeMillis() - kitlastused) < kitcooldown)) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "kit.command.kit.cooldown", "%time%", TimeUtil.format(kitcooldown - (System.currentTimeMillis() - kitlastused))));
        }
        lastused.put(kit.getId(), System.currentTimeMillis());
        up.offer(KitKeys.KIT_LASTUSED, lastused);

        //Give items
        for (ItemStackSnapshot snapshot : kit.getItems()) {
            InventoryTransactionResult result = p.getInventory().offer(snapshot.createStack());
            result.getRejectedItems().forEach(item -> {
                Item itementity = (Item) p.getWorld().createEntity(EntityTypes.ITEM, p.getLocation().getPosition());
                itementity.offer(Keys.REPRESENTED_ITEM, item);
                p.getWorld().spawnEntity(itementity, Cause.builder().owner(UltimateCore.getContainer()).named(NamedCause.of("player", p)).build());
            });
        }
        Messages.send(sender, "kit.command.kit.success", "%kit%", kit.getId());
        return CommandResult.success();
    }
}
