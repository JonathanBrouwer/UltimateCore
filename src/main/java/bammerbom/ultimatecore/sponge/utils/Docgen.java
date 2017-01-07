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
import bammerbom.ultimatecore.sponge.api.command.CommandService;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Docgen {
    public static void generateDocs() {
        //This is some code I use to generate the temporary docs
        //Shouldnt really be runned on a normal server
        File file = new File(UltimateCore.get().getDataFolder().toFile(), "DOCS.md");
        StringWriter writer = new StringWriter();
        for (Module mod : UltimateCore.get().getModuleService().getRegisteredModules()) {
            writer.write("## " + StringUtil.firstUpperCase(mod.getIdentifier()) + "\n");
            //Commands
            CommandService service = UltimateCore.get().getCommandService();
            List<Command> commands = service.getCommands().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
            if (!commands.isEmpty()) {
                writer.write("### Commands<br>\n");
                for (Command cmd : commands) {
                    writer.write("**" + cmd.getUsage().toPlain() + "**: " + cmd.getLongDescription().toPlain() + "<br>\n");
                }
            }
            //Permissions
            PermissionService service2 = UltimateCore.get().getPermissionService();
            List<Permission> perms = service2.getPermissions().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
            if (!perms.isEmpty()) {
                writer.write("### Permissions<br>\n");
                for (Permission perm : perms) {
                    writer.write("**" + perm.get() + "**: " + perm.getDescription().toPlain() + " (Recommended for " + perm.getLevel().name() + ")<br>\n");
                }
            }
            //Permission options
            List<PermissionOption> permops = service2.getPermissionOptions().stream().filter(cmd -> cmd.getModule().getIdentifier().equalsIgnoreCase(mod.getIdentifier())).collect(Collectors.toList());
            if (!permops.isEmpty()) {
                writer.write("### Permissions options<br>\n");
                for (PermissionOption perm : permops) {
                    writer.write("**" + perm.get() + "**: " + perm.getDescription().toPlain() + "<br>\n");
                }
            }
            if (commands.isEmpty() && perms.isEmpty() && permops.isEmpty()) {
                writer.write("This module has no commands or permissions.<br>\n");
            }
        }
        try {
            FileUtil.writeLines(file, Arrays.asList(writer.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
