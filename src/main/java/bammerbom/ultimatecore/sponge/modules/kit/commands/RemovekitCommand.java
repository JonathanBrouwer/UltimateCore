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
package bammerbom.ultimatecore.sponge.modules.kit.commands;

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.kit.api.Kit;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitKeys;
import bammerbom.ultimatecore.sponge.modules.kit.api.KitPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemovekitCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.KIT.get();
    }

    @Override
    public String getIdentifier() {
        return "removekit";
    }

    @Override
    public Permission getPermission() {
        return KitPermissions.UC_KIT_REMOVEKIT_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(KitPermissions.UC_KIT_REMOVEKIT_BASE);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("removekit", "kitremove", "deletekit", "kitdelete", "delkit", "kitdel");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (!sender.hasPermission(KitPermissions.UC_KIT_REMOVEKIT_BASE.get())) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        }

        List<Kit> kits = GlobalData.get(KitKeys.KITS).get();
        List<Kit> results = kits.stream().filter(war -> args[0].toLowerCase().equalsIgnoreCase(war.getId().toLowerCase())).collect(Collectors.toList());
        if (results.isEmpty()) {
            sender.sendMessage(Messages.getFormatted(sender, "kit.command.kit.notfound", "%kit%", args[0]));
            return CommandResult.empty();
        }

        kits.removeAll(results);
        GlobalData.offer(KitKeys.KITS, kits);
        sender.sendMessage(Messages.getFormatted(sender, "kit.command.removekit.success", "%kit%", args[0].toLowerCase()));
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
