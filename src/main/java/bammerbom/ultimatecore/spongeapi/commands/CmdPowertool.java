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

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdPowertool implements bammerbom.ultimatecore.spongeapi.UltimateCommand {

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
    public void run(final CommandSender cs, String label, String[] args) {
        if (!bammerbom.ultimatecore.spongeapi.r.perm(cs, "uc.powertool", false, true)) {
            return;
        }
        if (!bammerbom.ultimatecore.spongeapi.r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        if (!bammerbom.ultimatecore.spongeapi.r.checkArgs(args, 0) || args[0].equalsIgnoreCase("clear")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolSomethingInHand");
                return;
            }
            if (!bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolNothingAssigned");
                return;
            }
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).clearPowertool(p.getItemInHand().getType());
            bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolClear");
        } else if (args[0].equalsIgnoreCase("clearall")) {
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).clearAllPowertools();
            bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolClearall");
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!bammerbom.ultimatecore.spongeapi.r.checkArgs(args, 1)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolAddUsage");
                return;
            }
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolSomethingInHand");
                return;
            }
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).addPowertool(p.getItemInHand().getType(), bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1));
            bammerbom.ultimatecore.spongeapi.r
                    .sendMes(cs, "powertoolAdd", "%Command", bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1), "%Item", bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil
                            .getName(p.getItemInHand()));
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!bammerbom.ultimatecore.spongeapi.r.checkArgs(args, 1)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolUsageRemove");
                return;
            }
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolSomethingInHand");
                return;
            }
            if (!bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolNothingAssigned");
                return;
            }
            if (!bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).getPowertools(p.getItemInHand().getType()).contains(bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1))) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolNoSuchCommandAssigned");
            }
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).removePowertool(p.getItemInHand().getType(), bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1));
            bammerbom.ultimatecore.spongeapi.r
                    .sendMes(cs, "powertoolRemove", "%Command", bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1), "%Item", bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil
                            .getName(p.getItemInHand()));
        } else if (args[0].equalsIgnoreCase("list")) {
            if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR)) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolSomethingInHand");
                return;
            }
            if (!bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).hasPowertool(p.getItemInHand().getType())) {
                bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolList", "%List", bammerbom.ultimatecore.spongeapi.r.mes("powertoolNone"));
                return;
            }
            String s = bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil.joinList(bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).getPowertools(p.getItemInHand().getType()));
            bammerbom.ultimatecore.spongeapi.r.sendMes(cs, "powertoolList", "%List", s);
        } else if (args[0].equalsIgnoreCase("set")) {
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).setPowertool(p.getItemInHand().getType(), Arrays.asList(bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1)));
            bammerbom.ultimatecore.spongeapi.r
                    .sendMes(cs, "powertoolSet", "%Command", bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 1), "%Item", bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil
                            .getName(p.getItemInHand()));
        } else {
            bammerbom.ultimatecore.spongeapi.api.UC.getPlayer(p).setPowertool(p.getItemInHand().getType(), Arrays.asList(bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 0)));
            bammerbom.ultimatecore.spongeapi.r
                    .sendMes(cs, "powertoolSet", "%Command", bammerbom.ultimatecore.spongeapi.r.getFinalArg(args, 0), "%Item", bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil
                            .getName(p.getItemInHand()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("clear", "clearall", "add", "remove", "list", "set");
        }
        return new ArrayList<>();
    }
}
