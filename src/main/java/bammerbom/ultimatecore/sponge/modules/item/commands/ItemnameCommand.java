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

import bammerbom.ultimatecore.sponge.api.command.Arguments;
import bammerbom.ultimatecore.sponge.api.command.RegisterCommand;
import bammerbom.ultimatecore.sponge.api.command.SmartCommand;
import bammerbom.ultimatecore.sponge.api.command.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.item.ItemModule;
import bammerbom.ultimatecore.sponge.modules.item.api.ItemPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@RegisterCommand(module = ItemModule.class, aliases = {"itemname", "setitemname", "name"})
public class ItemnameCommand implements SmartCommand {

    @Override
    public Permission getPermission() {
        return ItemPermissions.UC_ITEM_ITEMNAME_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(ItemPermissions.UC_ITEM_ITEMNAME_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new RemainingStringsArgument(Text.of("name"))).onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, ItemPermissions.UC_ITEM_ITEMNAME_BASE);
        Player p = (Player) sender;

        if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent() || p.getItemInHand(HandTypes.MAIN_HAND).get().getItem().equals(ItemTypes.NONE)) {
            p.sendMessage(Messages.getFormatted(p, "item.noiteminhand"));
            return CommandResult.empty();
        }
        ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();

        Text name = Messages.toText(args.<String>getOne("name").get());
        stack.offer(Keys.DISPLAY_NAME, name);
        p.setItemInHand(HandTypes.MAIN_HAND, stack);
        sender.sendMessage(Messages.getFormatted(sender, "item.command.itemname.success", "%arg%", name));
        return CommandResult.success();
    }
}
