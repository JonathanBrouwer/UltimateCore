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
package bammerbom.ultimatecore.sponge.defaultmodule.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.defaultmodule.api.DefaultPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.PlayerSelector;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UltimatecoreCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.DEFAULT.get();
    }

    @Override
    public String getIdentifier() {
        return "ultimatecore";
    }

    @Override
    public Permission getPermission() {
        return DefaultPermissions.UC_ULTIMATECORE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(DefaultPermissions.UC_ULTIMATECORE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("ultimatecore", "uc");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getLongDescription());
            return CommandResult.empty();
        }
        if (!sender.hasPermission(getPermission().get())) {
            sender.sendMessage(Messages.getFormatted("core.nopermissions"));
            return CommandResult.empty();
        }

        if (args[0].equalsIgnoreCase("clearcache")) {
            UltimateCore.get().getUserService().clearcache();
            sender.sendMessage(Messages.getFormatted("default.command.ultimatecore.clearcache.success"));
            return CommandResult.success();
        }
        if (args[0].equalsIgnoreCase("resetuser")) {
            if (args.length == 1) {
                sender.sendMessage(getLongDescription());
                return CommandResult.empty();
            }
            Optional<Player> t = PlayerSelector.one(sender, args[1]);
            if (!t.isPresent()) {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[1]));
                return CommandResult.empty();
            }
            UltimateUser user = UltimateCore.get().getUserService().getUser(t.get());
            //Clear the cache for the user
            user.datas.clear();
            PlayerDataFile file = new PlayerDataFile(user.getIdentifier());
            //Delete the user's file
            file.getFile().delete();
            sender.sendMessage(Messages.getFormatted("default.command.ultimatecore.resetuser.success", "%player%", t.get().getName()));
            return CommandResult.success();
        }
        if (args[0].equalsIgnoreCase("modules")) {
            List<String> modules = UltimateCore.get().getModuleService().getRegisteredModules().stream().map(Module::getIdentifier).filter(name -> !name.equalsIgnoreCase("default"))
                    .collect(Collectors.toList());
            sender.sendMessage(Messages.getFormatted("default.command.ultimatecore.modules.success", "%modules%", StringUtil.join(", ", modules)));
            return CommandResult.success();
        }
        sender.sendMessage(getLongDescription());
        return CommandResult.empty();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
