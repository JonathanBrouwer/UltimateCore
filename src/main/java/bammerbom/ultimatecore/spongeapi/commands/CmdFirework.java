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

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.MetaItemStack;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Arrays;
import java.util.List;

public class CmdFirework implements UltimateCommand {

    @Override
    public String getName() {
        return "firework";
    }

    @Override
    public String getPermission() {
        return "uc.firework";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.perm(cs, "uc.firework", false, true)) {
            return;
        }
        Player p = (Player) cs;
        Boolean spawnin = !(p.getItemInHand().getType() == Material.FIREWORK);
        ItemStack stack = p.getItemInHand().getType() == Material.FIREWORK ? p.getItemInHand() : new ItemStack(Material.FIREWORK);
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "fireworkUsage");
            return;
        }
        if (args[0].equalsIgnoreCase("clear") && (p.getItemInHand().getType() == Material.FIREWORK)) {
            FireworkMeta fmeta = (FireworkMeta) stack.getItemMeta();
            fmeta.clearEffects();
            stack.setItemMeta(fmeta);
            r.sendMes(cs, "fireworkClear");
            return;
        }
        MetaItemStack mStack = new MetaItemStack(stack);
        for (String arg : args) {
            if (arg.equalsIgnoreCase("power") || arg.equalsIgnoreCase("p")) {
                FireworkMeta fmeta = (FireworkMeta) stack.getItemMeta();
                try {
                    int power = Integer.parseInt(arg.split(":")[1]);
                    try {
                        mStack.addFireworkMeta(true, "power:" + power);
                    } catch (Exception e) {
                        r.sendMes(cs, "fireworkFailed");
                    }
                } catch (NumberFormatException e) {
                    r.sendMes(cs, "fireworkFailed");
                    return;
                }
                stack.setItemMeta(fmeta);
            } else if (r.isInt(arg)) {
                stack.setAmount(Integer.parseInt(arg));
            } else {
                try {
                    mStack.addFireworkMeta(true, arg);
                } catch (Exception e) {
                    r.sendMes(cs, "fireworkFailed");
                    return;
                }
            }
        }
        if (mStack.isValidFirework()) {
            FireworkMeta fmeta = (FireworkMeta) mStack.getItemStack().getItemMeta();
            FireworkEffect effect = mStack.getFireworkBuilder().build();
            fmeta.addEffect(effect);
            stack.setItemMeta(fmeta);
        } else {
            r.sendMes(cs, "fireworkFailed");
            return;
        }
        if (spawnin) {
            r.sendMes(cs, "fireworkSpawnin");
            p.getInventory().addItem(stack);
        } else {
            r.sendMes(cs, "fireworkModify");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
