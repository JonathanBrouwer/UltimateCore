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
package bammerbom.ultimatecore.sponge.modules.burn.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.burn.api.BurnPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.PlayerSelector;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;

public class BurnCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.BURN.get();
    }

    @Override
    public String getIdentifier() {
        return "burn";
    }

    @Override
    public Permission getPermission() {
        return BurnPermissions.UC_BURN;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(BurnPermissions.UC_BURN);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("burn", "fire", "ignite");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(BurnPermissions.UC_BURN.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        Player t;
        if (PlayerSelector.one(sender, args[0]).isPresent()) {
            t = PlayerSelector.one(sender, args[0]).get();
        } else {
            sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
            return CommandResult.empty();
        }
        Double time = 10.0;
        if (args.length >= 2) {
            try {
                time = Double.parseDouble(args[1]);
            } catch (Exception ex) {
            }
        }
        t.offer(Keys.FIRE_TICKS, Double.valueOf(time * 20).intValue());
        sender.sendMessage(Messages.getFormatted("burn.command.burn.success", "%player%", t.getName(), "%time%", time));

        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
