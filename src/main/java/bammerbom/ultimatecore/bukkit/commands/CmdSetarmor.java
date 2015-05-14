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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                p.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                p.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                break;
            case DIAMOND:
                p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                break;
            case GOLD:
                p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
                p.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                p.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                p.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
                break;
            case IRON:
                p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                break;
            case LEATHER:
                p.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                p.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
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
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.setarmor", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "setarmorUsage");
        } else if (r.checkArgs(args, 0) && !r.checkArgs(args, 1)) {
            if (!r.isPlayer(cs)) {
                return;
            }
            Player p = (Player) cs;
            if (isArmor(args[0])) {
                setArmor(p, getArmor(args[0]));
                r.sendMes(cs, "setarmorSet", "%Player", cs.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
            } else {
                r.sendMes(cs, "setarmorNotFound", "%Armor", args[0]);
            }
        } else if (r.checkArgs(args, 1)) {
            if (!r.perm(cs, "uc.setarmor.others", false, true)) {
                ;
            }
            if (isArmor(args[0])) {
                Player t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                setArmor(t, getArmor(args[0]));
                r.sendMes(cs, "setarmorSet", "%Player", t.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
                r.sendMes(t, "setarmorOthers", "%Player", cs.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[0]).name().toLowerCase()));
            } else if (isArmor(args[1])) {
                Player t = r.searchPlayer(args[0]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                setArmor(t, getArmor(args[1]));
                r.sendMes(cs, "setarmorSet", "%Player", t.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name().toLowerCase()));
                r.sendMes(t, "setarmorOthers", "%Player", cs.getName(), "%Armor", StringUtil.firstUpperCase(getArmor(args[1]).name().toLowerCase()));
            } else {
                r.sendMes(cs, "setarmorNotFound", "%Armor", args[0]);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
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
        DIAMOND;
    }
}
