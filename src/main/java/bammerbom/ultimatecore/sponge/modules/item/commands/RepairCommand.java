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
package bammerbom.ultimatecore.sponge.modules.item.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.LiteralArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.item.ItemModule;
import bammerbom.ultimatecore.sponge.modules.item.api.ItemPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.DurabilityData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = ItemModule.class, aliases = {"repair", "fix"})
public class RepairCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_REPAIR_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_REPAIR_BASE, ItemPermissions.UC_ITEM_REPAIR_ONE, ItemPermissions.UC_ITEM_REPAIR_ALL);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new LiteralArgument(Text.of("all"), Arrays.asList("inventory", "all"))).optionalWeak().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, ItemPermissions.UC_ITEM_REPAIR_BASE);
        Player p = (Player) sender;

        boolean fullInv = args.hasAny("all") ? args.<Boolean>getOne("all").get() : false;

        if (fullInv) {
            checkPermission(sender, ItemPermissions.UC_ITEM_REPAIR_ALL);
            p.getInventory().slots().forEach(slot -> {
                ItemStack stack = slot.peek();
                if (stack.getType().equals(ItemTypes.NONE)) {
                    return;
                }
                if (!stack.supports(DurabilityData.class)) {
                    return;
                }
                stack.offer(Keys.ITEM_DURABILITY, stack.get(DurabilityData.class).get().durability().getMaxValue());
                slot.set(stack);
            });
            Messages.send(sender, "item.command.repair.success.all");
            return CommandResult.success();
        } else {
            checkPermission(sender, ItemPermissions.UC_ITEM_REPAIR_ONE);
            if (p.getItemInHand(HandTypes.MAIN_HAND).getType().equals(ItemTypes.NONE)) {
                throw new ErrorMessageException(Messages.getFormatted(p, "item.noiteminhand"));
            }
            ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND);
            if (!stack.supports(DurabilityData.class)) {
                throw new ErrorMessageException(Messages.getFormatted(sender, "item.command.repair.nodurability"));
            }
            stack.offer(Keys.ITEM_DURABILITY, stack.get(DurabilityData.class).get().durability().getMaxValue());
            p.setItemInHand(HandTypes.MAIN_HAND, stack);
            Messages.send(sender, "item.command.repair.success.one");
            return CommandResult.success();
        }
    }
}
