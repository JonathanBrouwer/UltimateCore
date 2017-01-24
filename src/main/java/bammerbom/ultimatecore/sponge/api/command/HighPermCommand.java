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
package bammerbom.ultimatecore.sponge.api.command;

import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionInfo;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HighPermCommand extends HighCommand {

    default String getBasePermission() {
        return "uc." + getModule().getIdentifier().toLowerCase() + "." + getIdentifier().toLowerCase();
    }

    //Implemented with @CommandPermissions
    @Override
    default Permission getPermission() {
        return getPermissions().get(0);
    }

    //Implemented with @CommandPermissions
    @Override
    default List<Permission> getPermissions() {
        CommandPermissions pi = this.getClass().getAnnotation(CommandPermissions.class);
        ArrayList<Permission> perms = new ArrayList<>();

        //TODO default perm level?
        PermissionLevel level = pi != null ? pi.level() : PermissionLevel.NOBODY;

        //Get base permission
        Permission baseperm = Permission.create(getBasePermission() + ".base", getModule(), level, this, Text.of("Allows you to use the /" + this.getIdentifier() + " command."));
        perms.add(baseperm);

        //Get children permissions
        for (HighSubCommand child : getChildren()) {
            perms.addAll(child.getPermissions());
        }

        //Get custom suffixes
        registerPermissionSuffixes().forEach((id, info) -> {
            Permission perm = Permission.create(id, getModule(), info.getLevel(), this, info.getDescription());
            perms.add(perm);
        });

        return perms;
    }

    default Map<String, PermissionInfo> registerPermissionSuffixes() {
        return new HashMap<>();
    }

    //QUICK CHECKS
    default void checkPermSuffix(CommandSource sender, String suffix) throws CommandException {
        String perm = getBasePermission() + "." + suffix;
        if (!sender.hasPermission(perm)) {
            throw new CommandPermissionException();
        }
    }
}
