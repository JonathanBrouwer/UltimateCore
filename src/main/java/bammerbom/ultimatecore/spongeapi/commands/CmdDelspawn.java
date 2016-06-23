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

public class CmdDelspawn implements UltimateCommand {

    @Override
    public String getName() {
        return "delspawn";
    }

    @Override
    public String getPermission() {
        return "uc.delspawn";
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
//        return Arrays.asList("removespawn");
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.isPlayer(cs)) {
//            return;
//        }
//        if (!r.perm(cs, "uc.delspawn", false, true)) {
//            return;
//        }
//        Player p = (Player) cs;
//
//        Boolean world = false;
//        Boolean newbie = false;
//        String group = null;
//        for (String s : args) {
//            if (s.equalsIgnoreCase("-w") || s.equalsIgnoreCase("-world")) {
//                world = true;
//            }
//            if (s.equalsIgnoreCase("-n") || s.equalsIgnoreCase("-newbie")) {
//                newbie = true;
//            }
//            if (s.startsWith("g:")) {
//                group = s.split(":")[1];
//            }
//        }
//
//        if (UC.getServer().delSpawn(world ? p.getWorld() : null, group, newbie)) {
//            r.sendMes(cs, "delspawnMessage", "%Args", StringUtil.joinList(args));
//        } else {
//            r.sendMes(cs, "delspawnNotFound", "%Args", StringUtil.joinList(args));
//        }
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        return null;
//    }
}
