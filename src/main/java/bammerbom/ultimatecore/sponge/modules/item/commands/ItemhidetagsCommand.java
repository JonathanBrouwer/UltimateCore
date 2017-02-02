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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.BooleanArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.ChoicesArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.item.ItemModule;
import bammerbom.ultimatecore.sponge.modules.item.api.ItemPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@CommandInfo(module = ItemModule.class, aliases = {"itemhidetags", "setitemhidetags", "hidetags"})
public class ItemhidetagsCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        HashMap<String, Key<Value<Boolean>>> choices = new HashMap<>();
        choices.put("attribute", Keys.HIDE_ATTRIBUTES);
        choices.put("attributes", Keys.HIDE_ATTRIBUTES);
        choices.put("candestroy", Keys.HIDE_CAN_DESTROY);
        choices.put("canbreak", Keys.HIDE_CAN_DESTROY);
        choices.put("canplace", Keys.HIDE_CAN_PLACE);
        choices.put("canplaceon", Keys.HIDE_CAN_PLACE);
        choices.put("ench", Keys.HIDE_ENCHANTMENTS);
        choices.put("enchantment", Keys.HIDE_ENCHANTMENTS);
        choices.put("enchantments", Keys.HIDE_ENCHANTMENTS);
        choices.put("misc", Keys.HIDE_MISCELLANEOUS);
        choices.put("miscellaneous", Keys.HIDE_MISCELLANEOUS);
        choices.put("unbreakable", Keys.HIDE_UNBREAKABLE);

        return new CommandElement[]{
                Arguments.builder(new ChoicesArgument(Text.of("tag"), choices)).onlyOne().usage("attributes/candestroy/canplaceon/enchantments/miscellaneous/unbreakable").build(),
                Arguments.builder(new BooleanArgument(Text.of("enabled"))).usageKey("Enable/Disable").onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, ItemPermissions.UC_ITEM_ITEMHIDETAGS_BASE);
        Player p = (Player) sender;

        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent() || p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.NONE)) {
            p.sendMessage(Messages.getFormatted(p, "item.noiteminhand"));
            return CommandResult.empty();
        }
        ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();

        Key<Value<Boolean>> key = args.<Key<Value<Boolean>>>getOne("tag").get();
        boolean value = args.<Boolean>getOne("enabled").get();

        stack.offer(key, value);
        p.setItemInHand(HandTypes.MAIN_HAND, stack);
        sender.sendMessage(Messages.getFormatted(sender, "item.command.itemhidetags.success", "%tag%", key.getName(), "%status%", Messages.getFormatted(value ? "item.command.itemhidetags.hidden" : "item.command.itemhidetags.shown")));
        return CommandResult.success();
    }
}
