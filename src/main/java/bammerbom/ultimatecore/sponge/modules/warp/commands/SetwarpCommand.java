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
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.ExtendedLocation;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetwarpCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.WARP.get();
    }

    @Override
    public String getIdentifier() {
        return "setwarp";
    }

    @Override
    public Permission getPermission() {
        return WarpPermissions.UC_SETWARP;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(WarpPermissions.UC_SETWARP);
    }

    @Override
    public Text getUsage() {
        return CMGenerator.usage(this, Messages.getFormatted("warp.command.setwarp.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("warp.command.setwarp.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return CMGenerator.longDescription(this, Messages.getFormatted("warp.command.setwarp.longdescription"));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("setwarp", "addwarp");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        //Has permission
        if (!sender.hasPermission(WarpPermissions.UC_SETWARP.get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }
        //Is player
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.getFormatted("core.noplayer"));
            return CommandResult.empty();
        }
        Player p = (Player) sender;
        //Get name & description
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        String name = args[0].toLowerCase();
        String description = Messages.getColored("warp.command.setwarp.defaultdescription");
        if (args.length >= 2) {
            description = StringUtil.getFinalArg(args, 1);
        }
        //Create warp instance
        Warp warp = new Warp(name, description, new ExtendedLocation(p.getLocation(), p.getRotation()));
        List<Warp> warps = GlobalData.get(WarpKeys.WARPS).get();
        warps = warps.stream().filter(w -> !w.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        //Did the warp already exist?
        boolean replace = GlobalData.get(WarpKeys.WARPS).get().size() != warps.size();
        warps.add(warp);
        GlobalData.offer(WarpKeys.WARPS, warps);
        if (replace) {
            sender.sendMessage(Messages.getFormatted("warp.command.setwarp.replace", "%warp%", warp.getName()));
        } else {
            sender.sendMessage(Messages.getFormatted("warp.command.setwarp.set", "%warp%", warp.getName()));
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }
}

