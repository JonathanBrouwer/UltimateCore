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

/**
 * Permission ids should be in the format uc.[Module].[Command].otherstuff
 */
public class Permission {
    private Module module;
    @Nullable
    private String commandid;
    private Optional<Command> command = null;
    private String identifier;
    private Text description;
    private PermissionLevel level;

    private Permission(String id, Module mod, PermissionLevel lev, @Nullable String commandid, Text desc) {
        this.module = mod;
        this.commandid = commandid;
        this.identifier = id;
        this.description = desc;
        this.level = lev;
    }

    public static Permission create(String identifier, Module module, PermissionLevel level, @Nullable Command command, Text description) {
        if (UltimateCore.get().getPermissionService().get(identifier).isPresent()) {
            return UltimateCore.get().getPermissionService().get(identifier).get();
        }
        Permission perm = new Permission(identifier, module, level, command != null ? command.getIdentifier() : null, description);
        UltimateCore.get().getPermissionService().register(perm);
        return perm;
    }

    public static Permission create(String identifier, String moduleid, PermissionLevel level, @Nullable String commandid, Text description) {
        if (UltimateCore.get().getPermissionService().get(identifier).isPresent()) {
            return UltimateCore.get().getPermissionService().get(identifier).get();
        }
        Optional<Module> module = UltimateCore.get().getModuleService().getModule(moduleid);
        if (!module.isPresent()) {
            Messages.log("Failed to register permission " + identifier + ": Invalid module " + moduleid);
            //TODO ErrorLogger?
            return null;
        }

        Permission perm = new Permission(identifier, module.get(), level, commandid, description);
        UltimateCore.get().getPermissionService().register(perm);
        return perm;
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
        if ((command == null || !command.isPresent()) && commandid != null) {
            command = UltimateCore.get().getCommandService().get(commandid);
            return command;
        }
        return command != null ? command : Optional.empty();
    }

    /**
     * Get the permission's id.
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
