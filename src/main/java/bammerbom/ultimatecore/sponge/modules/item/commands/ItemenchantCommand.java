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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.item.api.ItemPermissions;
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemenchantCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.ITEM.get();
    }

    @Override
    public String getIdentifier() {
        return "itemenchant";
    }

    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_ITEMENCHANT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_ITEMENCHANT_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("itemenchant", "setitemenchant", "enchant", "enchantment");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (!sender.hasPermission(ItemPermissions.UC_ITEM_ITEMENCHANT_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage(sender));
            return CommandResult.empty();
        }

        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent() || p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.NONE)) {
            p.sendMessage(Messages.getFormatted(p, "item.noiteminhand"));
            return CommandResult.empty();
        }
        ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();

        Optional<Enchantment> ench = Sponge.getRegistry().getType(CatalogTypes.ENCHANTMENT, args[0]);
        int level = 1;
        if (!ench.isPresent()) {
            sender.sendMessage(Messages.getFormatted(sender, "item.command.itemenchant.notfound", "%enchantment%", args[0]));
            return CommandResult.empty();
        }
        if (args.length >= 2) {
            if (!ArgumentUtil.isInteger(args[1])) {
                sender.sendMessage(Messages.getFormatted(sender, "core.number.invalid", "%number%", args[0]));
                return CommandResult.empty();
            }
            level = Integer.parseInt(args[1]);
        }

        List<ItemEnchantment> enchs = stack.get(Keys.ITEM_ENCHANTMENTS).orElse(new ArrayList<>());
        if (level > 0) {
            enchs.add(new ItemEnchantment(ench.get(), level));
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchs);
            p.setItemInHand(HandTypes.MAIN_HAND, stack);
            sender.sendMessage(Messages.getFormatted(sender, "item.command.itemenchant.success", "%enchant%", ench.get().getTranslation().get(), "%level%", level));
            return CommandResult.success();
        } else {
            enchs = enchs.stream().filter(e -> !e.getEnchantment().equals(ench.get())).collect(Collectors.toList());
            stack.offer(Keys.ITEM_ENCHANTMENTS, enchs);
            p.setItemInHand(HandTypes.MAIN_HAND, stack);
            sender.sendMessage(Messages.getFormatted(sender, "item.command.itemenchant.success2", "%enchant%", ench.get().getTranslation().get(), "%level%", level));
            return CommandResult.success();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Sponge.getRegistry().getAllOf(CatalogTypes.ENCHANTMENT).stream().map(CatalogType::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
