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
package bammerbom.ultimatecore.sponge.modules.warp.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.warp.api.Warp;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpKeys;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelwarpCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.WARP.get();
    }

    @Override
    public String getIdentifier() {
        return "delwarp";
    }

    @Override
    public Permission getPermission() {
        return WarpPermissions.UC_DELWARP;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WarpPermissions.UC_DELWARP);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("delwarp", "removewarp");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        //Has permission
        if (!sender.hasPermission(WarpPermissions.UC_DELWARP.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Get name
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        String name = args[0].toLowerCase();
        //Find warp instance and remove it
        List<Warp> warps = GlobalData.get(WarpKeys.WARPS).get();
        boolean found = false;
        List<Warp> remove = new ArrayList<>();
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                remove.add(warp);
                found = true;
            }
        }
        warps.removeAll(remove);
        //Did the warp already exist?
        GlobalData.offer(WarpKeys.WARPS, warps);
        if (found) {
            sender.sendMessage(Messages.getFormatted("warp.command.delwarp.success", "%warp%", args[0]));
        } else {
            sender.sendMessage(Messages.getFormatted("warp.command.warp.notfound", "%warp%", args[0]));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}

