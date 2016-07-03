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
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdSetarmor implements UltimateCommand {

    //
    public static boolean isArmor(String a) {
        for (ArmorType t : ArmorType.values()) {
            if (a.toUpperCase().contains(t.name())) {
                return true;
            }
        }
        return false;
    }

    public static ArmorType getArmor(String a) {
        for (ArmorType t : ArmorType.values()) {
            if (a.toUpperCase().contains(t.name())) {
                return t;
            }
        }
        return null;
    }

    public static void setArmor(Player p, ArmorType a) {
        switch (a) {
            case CHAIN:
                p.setHelmet(ItemStack.builder().itemType(ItemTypes.CHAINMAIL_HELMET).build());
                p.setChestplate(ItemStack.builder().itemType(ItemTypes.CHAINMAIL_CHESTPLATE).build());
                p.setLeggings(ItemStack.builder().itemType(ItemTypes.CHAINMAIL_LEGGINGS).build());
                p.setBoots(ItemStack.builder().itemType(ItemTypes.CHAINMAIL_BOOTS).build());
                break;
            case DIAMOND:
                p.setHelmet(ItemStack.builder().itemType(ItemTypes.DIAMOND_HELMET).build());
                p.setChestplate(ItemStack.builder().itemType(ItemTypes.DIAMOND_CHESTPLATE).build());
                p.setLeggings(ItemStack.builder().itemType(ItemTypes.DIAMOND_LEGGINGS).build());
                p.setBoots(ItemStack.builder().itemType(ItemTypes.DIAMOND_BOOTS).build());
                break;
            case GOLD:
                p.setHelmet(ItemStack.builder().itemType(ItemTypes.GOLDEN_HELMET).build());
                p.setChestplate(ItemStack.builder().itemType(ItemTypes.GOLDEN_CHESTPLATE).build());
                p.setLeggings(ItemStack.builder().itemType(ItemTypes.GOLDEN_LEGGINGS).build());
                p.setBoots(ItemStack.builder().itemType(ItemTypes.GOLDEN_BOOTS).build());
                break;
            case IRON:
                p.setHelmet(ItemStack.builder().itemType(ItemTypes.IRON_HELMET).build());
                p.setChestplate(ItemStack.builder().itemType(ItemTypes.IRON_CHESTPLATE).build());
                p.setLeggings(ItemStack.builder().itemType(ItemTypes.IRON_LEGGINGS).build());
                p.setBoots(ItemStack.builder().itemType(ItemTypes.IRON_BOOTS).build());
                break;
            case LEATHER:
                p.setHelmet(ItemStack.builder().itemType(ItemTypes.LEATHER_HELMET).build());
                p.setChestplate(ItemStack.builder().itemType(ItemTypes.LEATHER_CHESTPLATE).build());
                p.setLeggings(ItemStack.builder().itemType(ItemTypes.LEATHER_LEGGINGS).build());
                p.setBoots(ItemStack.builder().itemType(ItemTypes.LEATHER_BOOTS).build());
                break;
            default:
                break;
        }
    }

    @Override
    public String getName() {
        return "setarmor";
    }

    @Override
    public String getPermission() {
        return "uc.setarmor";
    }

    @Override
    public String getUsage() {
        return "/<command> <ArmorType> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set someones armor type.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.setarmor", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "setarmorUsage");
        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            if (!r.isPlayer(cs)) {
                return CommandResult.empty();
            }
            Player p = (Player) cs;
            if (isArmor(args[0])) {
                setArmor(p, getArmor(args[0]));
                r.sendMes(cs, "setarmorSet", "%Player", r.getDisplayName(cs), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
            } else {
                r.sendMes(cs, "setarmorNotFound", "%Armor", args[0]);
            }
        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.setarmor.others", true)) {
            }
            if (isArmor(args[0])) {
                Player t = r.searchPlayer(args[1]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
                setArmor(t, getArmor(args[0]));
                r.sendMes(cs, "setarmorSet", "%Player", t.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
                r.sendMes(t, "setarmorOthers", "%Player", r.getDisplayName(cs), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
            } else if (isArmor(args[1])) {
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                setArmor(t, getArmor(args[1]));
                r.sendMes(cs, "setarmorSet", "%Player", t.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name().toLowerCase()));
                r.sendMes(t, "setarmorOthers", "%Player", r.getDisplayName(cs), "%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name().toLowerCase()));
            } else {
                r.sendMes(cs, "setarmorNotFound", "%Armor", args[0]);
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> l = new ArrayList<>();
        if (curn == 0) {
            for (ArmorType t : ArmorType.values()) {
                l.add(t.name());
            }
            return l;
        }
        return null;
    }

    public enum ArmorType {

        LEATHER,
        GOLD,
        CHAIN,
        IRON,
        DIAMOND
    }
}
