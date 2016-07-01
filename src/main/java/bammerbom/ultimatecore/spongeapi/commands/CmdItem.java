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

public class CmdItem implements UltimateCommand {

    @Override
    public String getName() {
        return "item";
    }

    @Override
    public String getPermission() {
        return "uc.item";
    }

    @Override
    public String getUsage() {
        return "/<command> <Item> [Amount] [Data...]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Give an item to yourself.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("i");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.item", true)) {
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "itemUsage");
            return CommandResult.empty();
        }
        ItemStack item;
        try {
            item = ItemUtil.searchItem(args[0]);
        } catch (Exception e) {
            r.sendMes(cs, "itemItemNotFound", "%Item", args[0]);
            return CommandResult.empty();
        }
        if (item == null || item.getItem() == null || item.getItem().equals(ItemTypes.NONE)) {
            r.sendMes(cs, "itemItemNotFound", "%Item", args[0]);
            return CommandResult.empty();
        }
        if (InventoryUtil.isFullInventory(p.getInventory())) {
            r.sendMes(cs, "itemInventoryFull");
            return CommandResult.empty();
        }
        Integer amount = item.getMaxStackQuantity();
        if (r.checkArgs(args, 1)) {
            if (!r.isInt(args[1])) {
                r.sendMes(cs, "numberFormat", "%Number", args[1]);
                return CommandResult.empty();
            }
            amount = Integer.parseInt(args[1]);
        }
        item.setQuantity(amount);
        if (r.checkArgs(args, 2)) {
            if (r.isInt(args[2])) {
                item.setDurability(Short.parseShort(args[2]));
            }
            MetaItemStack meta = new MetaItemStack(item);
            int metaStart = r.isInt(args[2]) ? 3 : 2;

            if (args.length > metaStart) {
                try {
                    String s = r.getFinalArg(args, metaStart);
                    if (s.startsWith("\\{")) {
                        item = Bukkit.getUnsafe().modifyItemStack(item, s);
                    } else {
                        try {
                            meta.parseStringMeta(cs, r.perm(cs, "uc.item.unsafe", false), args, metaStart);
                            item = meta.getItemStack();
                        } catch (IllegalArgumentException ex) {
                            if (ex.getMessage() != null && ex.getMessage().contains("Enchantment level is either too " + "low or too high")) {
                                r.sendMes(cs, "enchantUnsafe");
                                return CommandResult.empty();
                            } else {
                                r.sendMes(cs, "itemMetadataFailed");
                            }
                            return CommandResult.empty();
                        }

                    }
                } catch (Exception e) {
                    r.sendMes(cs, "itemMetadataFailed");
                    return CommandResult.empty();
                }
            }
        }
        InventoryUtil.addItem(p.getInventory(), item);
        r.sendMes(cs, "itemMessage", "%Item", ItemUtil.getName(item), "%Amount", amount, "%Player", r.getDisplayName(p));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
