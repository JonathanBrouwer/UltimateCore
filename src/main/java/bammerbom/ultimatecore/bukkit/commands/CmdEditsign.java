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
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdEditsign implements UltimateCommand {

    @Override
    public String getName() {
        return "editsign";
    }

    @Override
    public String getPermission() {
        return "uc.editsign";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("signedit");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.editsign", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "editsignUsage");
            return;
        }
        Block b = LocationUtil.getAbsoluteTarget(p).getBlock();
        if ((b == null) || b.getState() == null || (!(b.getState() instanceof Sign))) {
            if (b != null && b.getState() != null) {
                r.sendMes(cs, "editsignNoSignA", "%Block", ItemUtil.getName(new ItemStack(b.getType(), b.getData())));
            } else {
                r.sendMes(cs, "editsignNoSignB");
            }
            return;
        }
        BlockBreakEvent ev = new BlockBreakEvent(b, p);
        Bukkit.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            r.sendMes(cs, "editsignNoAccess");
            return;
        }
        Sign s = (Sign) b.getState();
        int lineNumber;
        try {
            lineNumber = Integer.parseInt(args[0]);
            lineNumber--;
        } catch (NumberFormatException e) {
            r.sendMes(cs, "editsignUsage");
            return;
        }
        if ((lineNumber < 0) || (lineNumber > 3)) {
            r.sendMes(cs, "editsignUsage");
            return;
        }
        if (args.length < 2) {
            s.setLine(lineNumber, "");
            s.update();
            r.sendMes(cs, "editsignClear", "%Line", lineNumber + 1);
            return;
        }
        String text = r.getFinalArg(args, 1);
        if (r.perm(p, "uc.sign.colored", false, false)) {
            s.setLine(0, ChatColor.translateAlternateColorCodes('&', s.getLine(0)));
            s.setLine(1, ChatColor.translateAlternateColorCodes('&', s.getLine(1)));
            s.setLine(2, ChatColor.translateAlternateColorCodes('&', s.getLine(2)));
            s.setLine(3, ChatColor.translateAlternateColorCodes('&', s.getLine(3)));
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        s.setLine(lineNumber, text);
        s.update();
        r.sendMes(cs, "editsignSet", "%Line", lineNumber + 1, "%Text", r.getFinalArg(args, 1));
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("1", "2", "3", "4");
        } else {
            return new ArrayList<>();
        }
    }
}
