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
package bammerbom.ultimatecore.sponge.utils;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Docgen {
    public static void generateDocs() {
        generateModulesOverview();
        generateCommandsOverview();
        generatePermissionsOverview();
        generateModules();
        generateCommands();
    }

    private static void generateModulesOverview() {
        File file = new File(UltimateCore.get().getDataFolder().toFile() + "/docs/", "modules.md");
        StringBuilder builder = new StringBuilder();
        builder.append("Modules\n====\n\n");
        List<Module> moduleslist = UltimateCore.get().getModuleService().getModules();
        Module[] modules = moduleslist.toArray(new Module[moduleslist.size()]);
        Arrays.sort(modules, Comparator.comparing(Module::getIdentifier));
        for (Module mod : modules) {
            builder.append("[" + mod.getIdentifier() + " - " + mod.getDescription().toPlain() + "](modules/" + mod.getIdentifier() + ".md)<br>\n");
        }

        //Save to file
        try {
            file.getParentFile().mkdirs();
            FileUtil.writeLines(file, Arrays.asList(builder.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateCommandsOverview() {
        File file = new File(UltimateCore.get().getDataFolder().toFile() + "/docs/", "commands.md");
        StringBuilder builder = new StringBuilder();
        builder.append("Commands\n====\n\n");
        List<Command> commandslist = UltimateCore.get().getCommandService().getCommands();
        Command[] commands = commandslist.toArray(new Command[commandslist.size()]);
        Arrays.sort(commands, Comparator.comparing(Command::getIdentifier));
        for (Command mod : commands) {
            builder.append("[" + mod.getUsage().toPlain() + " - " + mod.getShortDescription().toPlain() + "](commands/" + mod.getIdentifier() + ".md)<br>\n");
        }

        //Save to file
        try {
            file.getParentFile().mkdirs();
            FileUtil.writeLines(file, Arrays.asList(builder.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePermissionsOverview() {
        File file = new File(UltimateCore.get().getDataFolder().toFile() + "/docs/", "permissions.md");
        StringBuilder builder = new StringBuilder();
        builder.append("Permissions\n====\n\n");
        List<Permission> permissionslist = UltimateCore.get().getPermissionService().getPermissions();
        Permission[] permissions = permissionslist.toArray(new Permission[permissionslist.size()]);
        Arrays.sort(permissions, Comparator.comparing(Permission::get));
        for (Permission mod : permissions) {
            builder.append(mod.get() + " - " + mod.getDescription().toPlain() + " (Recommended for " + mod.getLevel().name().toLowerCase() + ")<br>\n");
        }

        //Save to file
        try {
            file.getParentFile().mkdirs();
            FileUtil.writeLines(file, Arrays.asList(builder.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateModules() {
        for (Module mod : UltimateCore.get().getModuleService().getModules()) {
            File file = new File(UltimateCore.get().getDataFolder().toFile() + "/docs/modules/", mod.getIdentifier() + ".md");
            StringBuilder builder = new StringBuilder();
            builder.append(StringUtil.firstUpperCase(mod.getIdentifier()) + "\n====\n");
            builder.append(mod.getDescription().toPlain() + "\n\n");

            builder.append("Commands: " + "<br>\n");
            List<Command> cmds = UltimateCore.get().getCommandService().getCommands().stream().filter(perm -> perm.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
            for (Command cmd : cmds) {
                builder.append("* **" + cmd.getIdentifier() + "**<br>");
                builder.append(cmd.getLongDescription().toPlain() + "\n");
            }

            builder.append("\nPermissions: " + "<br>\n");
            List<Permission> perms = UltimateCore.get().getPermissionService().getPermissions().stream().filter(perm -> perm.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
            for (Permission perm : perms) {
                builder.append("* **" + perm.get() + "** - Recommended role: " + perm.getLevel().name().toLowerCase() + "<br>");
                builder.append(perm.getDescription().toPlain() + "\n");
            }

            //Save to file
            try {
                file.getParentFile().mkdirs();
                FileUtil.writeLines(file, Arrays.asList(builder.toString().split("\n")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generateCommands() {
        for (Command cmd : UltimateCore.get().getCommandService().getCommands()) {
            File file = new File(UltimateCore.get().getDataFolder().toFile() + "/docs/commands/", cmd.getIdentifier() + ".md");
            StringBuilder builder = new StringBuilder();
            builder.append(StringUtil.firstUpperCase(cmd.getIdentifier()) + "\n====\n");
            builder.append(cmd.getLongDescription().toPlain() + "\n\n");

            builder.append("Usage: " + cmd.getUsage().toPlain() + "<br>\n");
            builder.append("Aliases: " + StringUtil.join(", ", cmd.getAliases()) + "<br>\n");
            builder.append("Module: [" + cmd.getModule().getIdentifier() + "](../modules/" + cmd.getModule().getIdentifier() + ".md)<br>\n\n");

            builder.append("Basic permission: " + cmd.getPermission().get() + "<br>\n");
            builder.append("Basic role: " + cmd.getPermission().getLevel().name().toLowerCase() + "<br>\n\n");
            builder.append("Permissions: " + "<br>\n");
            for (Permission perm : cmd.getPermissions()) {
                builder.append("* **" + perm.get() + "** - Recommended role: " + perm.getLevel().name().toLowerCase() + "<br>");
                builder.append(perm.getDescription().toPlain() + "\n");
            }

            //Save to file
            try {
                file.getParentFile().mkdirs();
                FileUtil.writeLines(file, Arrays.asList(builder.toString().split("\n")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
