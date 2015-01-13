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

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.classes.MetaItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdModify implements UltimateCommand {

    @Override
    public String getName() {
        return "modify";
    }

    @Override
    public String getPermission() {
        return "uc.modify";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.modify", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "modifyUsage");
            return;
        }
        Player p = (Player) cs;
        ItemStack stack = p.getItemInHand();
        if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
            r.sendMes(cs, "modifyAir");
            return;
        }
        try {
            String s = r.getFinalArg(args, 0);
            if (s.startsWith("\\{")) {
                stack = Bukkit.getUnsafe().modifyItemStack(stack, s);
            } else {
                MetaItemStack meta = new MetaItemStack(stack);
                meta.parseStringMeta(cs, r.perm(cs, "uc.give.unsafe", false, false), args, 0);
                stack = meta.getItemStack();
            }
        } catch (Exception e) {
            r.sendMes(cs, "giveMetadataFailed");
        }
        p.setItemInHand(stack);
        r.sendMes(cs, "modifyMessage");
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
