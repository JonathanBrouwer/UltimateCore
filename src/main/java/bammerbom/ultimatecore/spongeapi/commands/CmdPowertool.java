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
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdPowertool implements UltimateCommand {

    @Override
    public String getName() {
        return "powertool";
    }

    @Override
    public String getPermission() {
        return "uc.powertool";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pt");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.powertool", false, true)) {
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0) || args[0].equalsIgnoreCase("clear")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                r.sendMes(cs, "powertoolNothingAssigned");
                return CommandResult.empty();
            }
            UC.getPlayer(p).clearPowertool(p.getItemInHand().getType());
            r.sendMes(cs, "powertoolClear");
        } else if (args[0].equalsIgnoreCase("clearall")) {
            UC.getPlayer(p).clearAllPowertools();
            r.sendMes(cs, "powertoolClearall");
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "powertoolAddUsage");
                return CommandResult.empty();
            }
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            UC.getPlayer(p).addPowertool(p.getItemInHand().getType(), r.getFinalArg(args, 1));
            r.sendMes(cs, "powertoolAdd", "%Command", r.getFinalArg(args, 1), "%Item", ItemUtil.getName(p.getItemInHand()));
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "powertoolUsageRemove");
                return CommandResult.empty();
            }
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                r.sendMes(cs, "powertoolNothingAssigned");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).getPowertools(p.getItemInHand().getType()).contains(r.getFinalArg(args, 1))) {
                r.sendMes(cs, "powertoolNoSuchCommandAssigned");
            }
            UC.getPlayer(p).removePowertool(p.getItemInHand().getType(), r.getFinalArg(args, 1));
            r.sendMes(cs, "powertoolRemove", "%Command", r.getFinalArg(args, 1), "%Item", ItemUtil.getName(p.getItemInHand()));
        } else if (args[0].equalsIgnoreCase("list")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                r.sendMes(cs, "powertoolList", "%List", r.mes("powertoolNone"));
                return CommandResult.empty();
            }
            String s = StringUtil.joinList(UC.getPlayer(p).getPowertools(p.getItemInHand().getType()));
            r.sendMes(cs, "powertoolList", "%List", s);
        } else if (args[0].equalsIgnoreCase("set")) {
            UC.getPlayer(p).setPowertool(p.getItemInHand().getType(), Arrays.asList(r.getFinalArg(args, 1)));
            r.sendMes(cs, "powertoolSet", "%Command", r.getFinalArg(args, 1), "%Item", ItemUtil.getName(p.getItemInHand()));
        } else {
            UC.getPlayer(p).setPowertool(p.getItemInHand().getType(), Arrays.asList(r.getFinalArg(args, 0)));
            r.sendMes(cs, "powertoolSet", "%Command", r.getFinalArg(args, 0), "%Item", ItemUtil.getName(p.getItemInHand()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("clear", "clearall", "add", "remove", "list", "set");
        }
        return new ArrayList<>();
    }
}
