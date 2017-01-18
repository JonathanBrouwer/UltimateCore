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
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CreatekitCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KIT.get();
    }

    @Override
    public String getIdentifier() {
        return "createkit";
    }

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_CREATEKIT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_CREATEKIT_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("createkit", "kitcreate", "addkit", "kitadd");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(KitPermissions.UC_KIT_CREATEKIT_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted(sender, "core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(getUsage(sender));
            return CommandResult.empty();
        }
        long delay = -1;
        Text description = Messages.getFormatted("kit.defaultdescription");
        if (args.length >= 2) {
            if (ArgumentUtil.isNumber(args[1])) {
                delay = Long.parseLong(args[1]);
            }
            int dstart = delay == -1 ? 1 : 2;
            if (args.length >= (dstart + 1)) {
                description = Text.of(StringUtil.getFinalArg(args, dstart));
            }
        }

        List<ItemStackSnapshot> items = new ArrayList<>();
        p.getInventory().slots().forEach(slot -> {
            Optional<ItemStack> stack = slot.peek();
            if (stack.isPresent() && !stack.get().getItem().equals(ItemTypes.AIR)) {
                items.add(stack.get().createSnapshot());
            }
        });

        Kit kit = new Kit(args[0].toLowerCase(), description, items, new ArrayList<>(), delay);
        List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
        kits.add(kit);
        GlobalData.offer(KitKeys.KITS, kits);
        sender.sendMessage(Messages.getFormatted(sender, "kit.command.createkit.success", "%name%", args[0].toLowerCase()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
