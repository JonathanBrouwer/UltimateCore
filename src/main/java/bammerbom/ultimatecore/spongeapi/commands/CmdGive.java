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
import bammerbom.ultimatecore.spongeapi.resources.classes.MetaItemStack;
import bammerbom.ultimatecore.spongeapi.resources.utils.InventoryUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdGive implements UltimateCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getPermission() {
        return "uc.give";
    }

    @Override
    public String getUsage() {
        return "/<command> <Player> <Item> [Amount] [Data...]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Give an item to a certain player.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.give", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 1)) {
            r.sendMes(cs, "giveUsage");
            return CommandResult.empty();
        }
        Player target = r.searchPlayer(args[0]).orElse(null);
        if (target == null) {
            r.sendMes(cs, "playerNotFound", "%Player", args[0]);
            return CommandResult.empty();
        }
        ItemStack item;
        try {
            item = ItemUtil.searchItem(args[1]);
        } catch (Exception e) {
            r.sendMes(cs, "giveItemNotFound", "%Item", args[1]);
            return CommandResult.empty();
        }
        if (item == null || item.getItem() == null || item.getItem().equals(ItemTypes.NONE)) {
            r.sendMes(cs, "giveItemNotFound", "%Item", args[1]);
            return CommandResult.empty();
        }
        if (InventoryUtil.isFullInventory(target.getInventory())) {
            r.sendMes(cs, "giveInventoryFull", "%Item", args[1]);
            return CommandResult.empty();
        }
        Integer amount = item.getMaxStackQuantity();
        if (r.checkArgs(args, 2)) {
            if (!r.isInt(args[2])) {
                r.sendMes(cs, "numberFormat", "%Number", args[2]);
                return CommandResult.empty();
            }
            amount = Integer.parseInt(args[2]);
        }
        item.setQuantity(amount);
        if (r.checkArgs(args, 3)) {
            if (r.isInt(args[3])) {
                item.setDurability(Short.parseShort(args[3]));
            }
            MetaItemStack meta = new MetaItemStack(item);
            int metaStart = r.isInt(args[3]) ? 4 : 3;

            if (args.length > metaStart) {
                try {
                    String s = r.getFinalArg(args, metaStart);
                    if (s.startsWith("\\{")) {
                        item = Bukkit.getUnsafe().modifyItemStack(item, s); //TODO wait for api
                    } else {
                        try {
                            meta.parseStringMeta(cs, r.perm(cs, "uc.give.unsafe", false), args, metaStart);
                        } catch (IllegalArgumentException ex) {
                            if (ex.getMessage() != null && (ex.getMessage().contains("Enchantment level is either too" + " low or too high") || ex.getMessage()
                                    .contains("Specified enchantment cannot be " + "applied"))) {
                                r.sendMes(cs, "enchantUnsafe");
                            } else {
                                r.sendMes(cs, "giveMetadataFailed");
                            }
                            return CommandResult.empty();
                        }
                        item = meta.getItemStack();
                    }
                } catch (Exception e) {
                    r.sendMes(cs, "giveMetadataFailed");
                    return CommandResult.empty();
                }
            }
        }
        InventoryUtil.addItem(target.getInventory(), item);
        r.sendMes(cs, "giveMessage", "%Item", ItemUtil.getName(item), "%Amount", amount, "%Player", target.getName());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
