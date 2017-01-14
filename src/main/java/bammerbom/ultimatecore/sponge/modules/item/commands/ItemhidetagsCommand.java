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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemhidetagsCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.ITEM.get();
    }

    @Override
    public String getIdentifier() {
        return "itemhidetags";
    }

    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("itemhidetags", "setitemhidetags", "hidetags");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (!sender.hasPermission(ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length <= 1) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }

        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent() || p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.NONE)) {
            p.sendMessage(Messages.getFormatted(p, "item.noiteminhand"));
            return CommandResult.empty();
        }
        ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();
        Key<Value<Boolean>> key;
        switch (args[0].toLowerCase()) {
            case "attribute":
            case "attributes":
                key = Keys.HIDE_ATTRIBUTES;
                break;
            case "candestroy":
            case "canbreak":
                key = Keys.HIDE_CAN_DESTROY;
                break;
            case "canplace":
            case "canplaceon":
                key = Keys.HIDE_CAN_PLACE;
                break;
            case "ench":
            case "enchantment":
            case "enchantments":
                key = Keys.HIDE_ENCHANTMENTS;
                break;
            case "miscellaneous":
            case "misc":
                key = Keys.HIDE_MISCELLANEOUS;
                break;
            case "unbreakable":
                key = Keys.HIDE_UNBREAKABLE;
                break;
            default:
                sender.sendMessage(getUsage());
                return CommandResult.empty();
        }

        if (!ArgumentUtil.isBoolean(args[1])) {
            sender.sendMessage(Messages.getFormatted(sender, "item.booleaninvalid", "%argument%", args[0]));
            return CommandResult.empty();
        }
        boolean value = Boolean.parseBoolean(args[1]);

        stack.offer(key, value);
        p.setItemInHand(HandTypes.MAIN_HAND, stack);
        sender.sendMessage(Messages.getFormatted(sender, "item.command.itemhidetags.success", "%tag%", key.getName(), "%status%", Messages.getFormatted(value ? "item.command.itemhidetags.hidden" : "item.command.itemhidetags.shown")));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("attributes", "candestroy", "canbreak", "canplaceon", "enchantment", "miscellaneous", "unbreakable");
        }
        if (curn == 1) {
            return Arrays.asList("false", "true");
        }
        return new ArrayList<>();
    }
}
