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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class Permission {
    private Module module;
    private Optional<Command> command;
    private String id;
    private Text description;
    private PermissionLevel level;

    public Permission(Module mod, Command command, String i, Text desc, PermissionLevel lev) {
        this.module = mod;
        this.command = command == null ? Optional.empty() : Optional.of(command);
        this.id = i;
        this.description = desc;
        this.level = lev;
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
     * Get the permission's id. //TODO is id correct term?
     * For example: uc.kick
     *
     * @return The permission's id
     */
    public String get() {
        return id;
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
