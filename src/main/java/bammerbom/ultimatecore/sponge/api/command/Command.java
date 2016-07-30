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

import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.List;

public interface Command {

    /**
     * Get the module that registered this command.
     *
     * @return The module that registered this command
     */
    Module getModule();

    /**
     * Get the primary alias for this command.
     *
     * @return The primary alias
     */
    String getIdentifier();

    /**
     * Get the base permission for this command.
     * For example: uc.home
     *
     * @return The base permission
     */
    Permission getPermission();

    /**
     * Get a list of all permissions for this command.
     * The base permission will be the first
     */
    List<Permission> getPermissions();

    /**
     * Get the usage for this command.
     *
     * @return The usage
     */
    Text getUsage();

    /**
     * Get an one-line description of this command.
     *
     * @return A short description
     */
    Text getShortDescription();

    /**
     * Get a long description for this command.
     *
     * @return A long description
     */
    Text getLongDescription();

    /**
     * Get all aliases for this command.
     *
     * @return All aliases
     */
    List<String> getAliases();

    /**
     * Execute this command.
     *
     * @param sender The CommandSource who executed the command
     * @param args   The provided arguments, split by a space
     * @return The result of the command
     */
    CommandResult run(CommandSource sender, String[] args);

    /**
     * Get the results for tabcompleting this command.
     *
     * @param sender The CommandSource who asked for the tab completion
     * @param args   The provided arguments, split by a space, including the argument which is currently being typed
     * @param curs   The argument which is being completed
     * @param curn   The index of the argument which is being completed
     * @return A list of all tab completions, including the ones which do not match what the user typed
     */
    List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn);

}
