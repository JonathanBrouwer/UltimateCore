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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdShowkit implements UltimateCommand {

    @Override
    public String getName() {
        return "showkit";
    }

    @Override
    public String getPermission() {
        return "uc.showkit";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
    //    @Override
    //    public List<String> getAliases() {
    //        return Arrays.asList();
    //    }
    //
    //    @Override
    //    public void run(final CommandSource cs, String label, String[] args) {
    //        if (!r.perm(cs, "uc.showkit", false, true)) {
    //            return CommandResult.empty();
    //        }
    //        if (!r.checkArgs(args, 0)) {
    //            r.sendMes(cs, "showkitUsage");
    //            return CommandResult.empty();
    //        }
    //        if (!UC.getServer().getKitNames().contains(args[0])) {
    //            r.sendMes(cs, "kitNotFound", "%Kit", args[0]);
    //            return CommandResult.empty();
    //        }
    //        UKit kit = UC.getServer().getKit(args[0]);
    //        r.sendMes(cs, "showkitContains", "%Kit", kit.getName());
    //        if (kit.getCooldown() == 0) {
    //            r.sendMes(cs, "kitList3", "%Cooldown", r.mes("kitNoCooldown"));
    //        } else if (kit.getCooldown() == -1) {
    //            r.sendMes(cs, "kitList3", "%Cooldown", r.mes("kitOnlyOnce"));
    //        } else {
    //            r.sendMes(cs, "kitList3", "%Cooldown", DateUtil.format(kit.getCooldown()));
    //        }
    //        for (ItemStack stack : kit.getItems()) {
    //            HashMap<String, Object> map = ItemUtil.serialize(stack);
    //            StringBuilder sb = new StringBuilder();
    //            for (String key : map.keySet()) {
    //                sb.append(key + ":" + map.get(key) + " ");
    //            }
    //            r.sendMes(cs, "showkitItem", "%Item", TextColorUtil.strip(sb.toString()));
    //        }
    //    }
    //
    //    @Override
    //    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
    //        return null;
    //    }
}
