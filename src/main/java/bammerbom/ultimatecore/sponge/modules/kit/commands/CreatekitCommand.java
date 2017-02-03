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
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.RemainingStringsArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.TimeArgument;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kit.KitModule;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@CommandInfo(module = KitModule.class, aliases = {"createkit", "kitcreate", "addkit", "kitadd"})
public class CreatekitCommand implements HighCommand {

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_CREATEKIT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_CREATEKIT_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new StringArgument(Text.of("name"))).onlyOne().build(),
                Arguments.builder(new TimeArgument(Text.of("cooldown"))).optional().onlyOne().build(),
                Arguments.builder(new RemainingStringsArgument(Text.of("description"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        checkPermission(sender, KitPermissions.UC_KIT_CREATEKIT_BASE);
        Player p = (Player) sender;

        String name = args.<String>getOne("name").get().toLowerCase();
        long cooldown = args.hasAny("cooldown") ? args.<Long>getOne("cooldown").get() : -1L;
        Text description = args.hasAny("description") ? Text.of(args.<String>getOne("description").get()) : Messages.getFormatted("kit.defaultdescription");

        List<ItemStackSnapshot> items = new ArrayList<>();
        p.getInventory().slots().forEach(slot -> {
            Optional<ItemStack> stack = slot.peek();
            if (stack.isPresent() && !stack.get().getItem().equals(ItemTypes.NONE)) {
                items.add(stack.get().createSnapshot());
            }
        });

        Kit kit = new Kit(name, description, items, new ArrayList<>(), cooldown);
        List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
        kits.add(kit);
        GlobalData.offer(KitKeys.KITS, kits);
        Messages.send(sender, "kit.command.createkit.success", "%name%", name);
        return CommandResult.success();
    }
}
