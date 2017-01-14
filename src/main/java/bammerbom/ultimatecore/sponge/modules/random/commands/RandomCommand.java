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
package bammerbom.ultimatecore.sponge.modules.random.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.random.api.RandomPermissions;
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCommand implements Command {
    static Random random = new Random();

    @Override
    public Module getModule() {
        return Modules.RANDOM.get();
    }

    @Override
    public String getIdentifier() {
        return "random";
    }

    @Override
    public Permission getPermission() {
        return RandomPermissions.UC_RANDOM_RANDOM_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(RandomPermissions.UC_RANDOM_RANDOM_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("random");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(RandomPermissions.UC_RANDOM_RANDOM_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        } else if (args.length == 1 || args.length == 2) {
            if (!ArgumentUtil.isNumber(args[0])) {
                sender.sendMessage(Messages.getFormatted(sender, "core.nonumber", "%number%", args[0]));
                return CommandResult.empty();
            }

            int min = args.length == 2 ? Integer.parseInt(args[0]) : 1;
            int max = args.length == 2 ? Integer.parseInt(args[1]) : Integer.parseInt(args[0]);
            if (min > max) {
                sender.sendMessage(Messages.getFormatted(sender, "random.command.random.invalid", "%min%", min, "%max%", max));
                return CommandResult.empty();
            }
            int rand = random.nextInt((max + 1) - min) + min;

            sender.sendMessage(Messages.getFormatted(sender, "random.command.random.success", "%min%", min, "%max%", max, "%value%", rand));
            return CommandResult.success();
        }
        sender.sendMessage(getUsage());
        return CommandResult.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
