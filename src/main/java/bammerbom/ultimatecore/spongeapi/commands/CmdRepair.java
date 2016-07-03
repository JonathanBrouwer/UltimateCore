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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CmdRepair implements UltimateCommand {

    @Override
    public String getName() {
        return "repair";
    }

    @Override
    public String getPermission() {
        return "uc.repair";
    }

    @Override
    public String getUsage() {
        return "/<command> [All] [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Repair the item in your hand, or all items in your inventory.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("fix");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.checkArgs(args, 0)) {
            //repair
            if (!r.perm(cs, "uc.repair", true)) {
                return CommandResult.empty();
            }
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
            if (stack == null) {
                r.sendMes(cs, "repairNoItemInHand");
                return CommandResult.empty();
            }
            if (!stack.supports(Keys.ITEM_DURABILITY)) {
                r.sendMes(cs, "repairNotRepairable");
                return CommandResult.empty();
            }
            stack.offer(Keys.ITEM_DURABILITY, 0);
            r.sendMes(cs, "repairSelfHand");
        } else if (!args[0].equalsIgnoreCase("all")) {
            //repair <Player>
            if (!r.perm(cs, "uc.repair.others", true)) {
                return CommandResult.empty();
            }
            Player p = r.searchPlayer(args[0]).orElse(null);
            if (p == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                return CommandResult.empty();
            }
            ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
            if (stack == null) {
                r.sendMes(cs, "repairNoItemInHand");
                return CommandResult.empty();
            }
            if (!stack.supports(Keys.ITEM_DURABILITY)) {
                r.sendMes(cs, "repairNotRepairable");
                return CommandResult.empty();
            }
            stack.offer(Keys.ITEM_DURABILITY, 0);
            r.sendMes(cs, "repairOtherSelfHand", "%Player", r.getDisplayName(p));
            r.sendMes(p, "repairOtherOtherHand", "%Player", r.getDisplayName(cs));
        } else if (args[0].equalsIgnoreCase("all")) {
            //repair all <Player>
            if (r.checkArgs(args, 1)) {
                if (!r.perm(cs, "uc.repair.others.all", true)) {
                    return CommandResult.empty();
                }
                Player p = r.searchPlayer(args[1]).orElse(null);
                if (p == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
                for (Inventory istack : p.getInventory().slots()) {
                    Optional<ItemStack> stack = istack.peek();
                    if (stack == null) {
                        continue;
                    }
                    if (!stack.get().supports(Keys.ITEM_DURABILITY)) {
                        continue;
                    }
                    stack.get().offer(Keys.ITEM_DURABILITY, 0);
                }
                r.sendMes(cs, "repairOtherSelfAll", "%Player", r.getDisplayName(p));
                r.sendMes(p, "repairOtherOtherAll", "%Player", r.getDisplayName(cs));
            } else {
                //repair all
                if (!r.perm(cs, "uc.repair.all", true)) {
                    return CommandResult.empty();
                }
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                for (Inventory istack : p.getInventory().slots()) {
                    Optional<ItemStack> stack = istack.peek();
                    if (stack == null) {
                        continue;
                    }
                    if (!stack.get().supports(Keys.ITEM_DURABILITY)) {
                        continue;
                    }
                    stack.get().offer(Keys.ITEM_DURABILITY, 0);
                }
                r.sendMes(cs, "repairSelfAll");
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
