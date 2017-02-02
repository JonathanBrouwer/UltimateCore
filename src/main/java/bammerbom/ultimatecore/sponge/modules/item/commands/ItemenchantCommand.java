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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.BoundedIntegerArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.EnchantmentArgument;
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
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(module = ItemModule.class, aliases = {"itemenchant", "setitemenchant", "enchant", "enchantment"})
public class ItemenchantCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_ITEMENCHANT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_ITEMENCHANT_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new EnchantmentArgument(Text.of("enchantment"))).onlyOne().build(),
                Arguments.builder(new BoundedIntegerArgument(Text.of("level"), 0, null)).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, ItemPermissions.UC_ITEM_ITEMENCHANT_BASE);
        Player p = (Player) sender;

        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent() || p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.NONE)) {
            p.sendMessage(Messages.getFormatted(p, "item.noiteminhand"));
            return CommandResult.empty();
        }
        ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();

        Enchantment ench = args.<Enchantment>getOne("enchantment").get();
        int level = args.hasAny("level") ? args.<Integer>getOne("level").get() : 1;

        List<ItemEnchantment> enchs = stack.get(Keys.ITEM_ENCHANTMENTS).orElse(new ArrayList<>());
        if (level > 0) {
            enchs.add(new ItemEnchantment(ench, level));
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchs);
            p.setItemInHand(HandTypes.MAIN_HAND, stack);
            sender.sendMessage(Messages.getFormatted(sender, "item.command.itemenchant.success", "%enchant%", ench.getTranslation().get(), "%level%", level));
            return CommandResult.success();
        } else {
            enchs = enchs.stream().filter(e -> !e.getEnchantment().equals(ench)).collect(Collectors.toList());
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchs);
            p.setItemInHand(HandTypes.MAIN_HAND, stack);
            sender.sendMessage(Messages.getFormatted(sender, "item.command.itemenchant.success2", "%enchant%", ench.getTranslation().get(), "%level%", level));
            return CommandResult.success();
        }
    }
}
