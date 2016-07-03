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
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

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
    public String getUsage() {
        return "/<command> <Clear/Clearall/Add/Remove/list/<Command>> [Command]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Bind an item to a command.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("pt");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.powertool", true)) {
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0) || args[0].equalsIgnoreCase("clear")) {
            if (!p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem())) {
                r.sendMes(cs, "powertoolNothingAssigned");
                return CommandResult.empty();
            }
            UC.getPlayer(p).clearPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem());
            r.sendMes(cs, "powertoolClear");
        } else if (args[0].equalsIgnoreCase("clearall")) {
            UC.getPlayer(p).clearAllPowertools();
            r.sendMes(cs, "powertoolClearall");
        } else if (args[0].equalsIgnoreCase("add")) {
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "powertoolAddUsage");
                return CommandResult.empty();
            }
            if (p.getItemInHand(HandTypes.MAIN_HAND) == null) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            UC.getPlayer(p).addPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem(), r.getFinalArg(args, 1));
            r.sendMes(cs, "powertoolAdd", "%Command", r.getFinalArg(args, 1), "%Item", p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get());
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!r.checkArgs(args, 1)) {
                r.sendMes(cs, "powertoolUsageRemove");
                return CommandResult.empty();
            }
            if (p.getItemInHand(HandTypes.MAIN_HAND) == null) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem())) {
                r.sendMes(cs, "powertoolNothingAssigned");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).getPowertools(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem()).contains(r.getFinalArg(args, 1))) {
                r.sendMes(cs, "powertoolNoSuchCommandAssigned");
            }
            UC.getPlayer(p).removePowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem(), r.getFinalArg(args, 1));
            r.sendMes(cs, "powertoolRemove", "%Command", r.getFinalArg(args, 1), "%Item", p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get());
        } else if (args[0].equalsIgnoreCase("list")) {
            if (p.getItemInHand(HandTypes.MAIN_HAND) == null) {
                r.sendMes(cs, "powertoolSomethingInHand");
                return CommandResult.empty();
            }
            if (!UC.getPlayer(p).hasPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem())) {
                r.sendMes(cs, "powertoolList", "%List", r.mes("powertoolNone"));
                return CommandResult.empty();
            }
            String s = StringUtil.joinList(UC.getPlayer(p).getPowertools(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem()));
            r.sendMes(cs, "powertoolList", "%List", s);
        } else if (args[0].equalsIgnoreCase("set")) {
            UC.getPlayer(p).setPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem(), Arrays.asList(r.getFinalArg(args, 1)));
            r.sendMes(cs, "powertoolSet", "%Command", r.getFinalArg(args, 1), "%Item", p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get());
        } else {
            UC.getPlayer(p).setPowertool(p.getItemInHand(HandTypes.MAIN_HAND).get().getItem(), Arrays.asList(r.getFinalArg(args, 0)));
            r.sendMes(cs, "powertoolSet", "%Command", r.getFinalArg(args, 0), "%Item", p.getItemInHand(HandTypes.MAIN_HAND).get().get(Keys.DISPLAY_NAME).get());
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        if (curn == 0) {
            return Arrays.asList("clear", "clearall", "add", "remove", "list", "set");
        }
        return new ArrayList<>();
    }
}
