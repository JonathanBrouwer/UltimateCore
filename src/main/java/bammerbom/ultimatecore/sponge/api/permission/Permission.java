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
package bammerbom.ultimatecore.sponge.api.permission;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.Optional;

public class Permission {
    private Module module;
    private Optional<Command> command;
    private String identifier;
    private Text description;
    private PermissionLevel level;

    private Permission(String id, Module mod, PermissionLevel lev, @Nullable Command com, Text desc) {
        this.module = mod;
        this.command = com == null ? Optional.empty() : Optional.of(com);
        this.identifier = id;
        this.description = desc;
        this.level = lev;
    }

    public static Permission create(String identifier, Module module, PermissionLevel level, @Nullable Command command, Text description) {
        Permission perm = new Permission(identifier, module, level, command, description);
        UltimateCore.get().getPermissionService().register(perm);
        return perm;
    }

    public static Permission create(String identifier, String moduleid, PermissionLevel level, @Nullable String commandid, Text description) {
        Optional<Module> module = UltimateCore.get().getModuleService().getModule(moduleid);
        Optional<Command> command = commandid == null ? null : UltimateCore.get().getCommandService().get(commandid);
        if (!module.isPresent()) {
            Messages.log("Failed to register permission " + identifier + ": Invalid module");
            //TODO ErrorLogger?
            return null;
        }
        //When an invalid command is provided
        if (command != null && !command.isPresent()) {
            Messages.log("Semi-failed to register permission " + identifier + ": Invalid command");
            //TODO ErrorLogger?
            command = null;
        }
        return create(identifier, module.get(), level, command == null ? null : command.get(), description);
    }

    /**
     * Get the module that registered this permission.
     *
     * @return The module that registered this permission
     */
    public Module getModule() {
        return module;
    }

    /**
     * Get the command this {@link Permission} is associated with, or {@link Optional#empty()} when not associated with a command.
     *
     * @return The command
     */
    public Optional<Command> getCommand() {
        return command;
    }

    /**
     * Get the permission's id. //TODO is id correct term?
     * For example: uc.kick
     *
     * @return The permission's id
     */
    public String get() {
        return identifier;
    }

    /**
     * Get a description of this permission.
     *
     * @return The description of this permission
     */
    public Text getDescription() {
        return description;
    }

    /**
     * Get the {@link PermissionLevel} of this permission.
     *
     * @return The {@link PermissionLevel}
     */
    public PermissionLevel getLevel() {
        return level;
    }
}
