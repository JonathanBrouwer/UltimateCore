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

import bammerbom.ultimatecore.sponge.UltimateCore;
import org.spongepowered.api.text.Text;

public interface SubCommand extends PermSmartCommand {
    default PermSmartCommand getParent() {
        if (!this.getClass().isAnnotationPresent(CommandParentInfo.class)) {
            return null;
        }
        CommandParentInfo pi = this.getClass().getAnnotation(CommandParentInfo.class);
        try {
            Class<? extends SmartCommand> parentClass = pi.parent();
            //Get cached copy
            for (Command cmd : UltimateCore.get().getCommandService().getCommands()) {
                if (cmd instanceof PermSmartCommand && cmd.getClass().equals(parentClass)) {
                    return (PermSmartCommand) cmd;
                }
            }
            //No cached copy available, create a new one
            return pi.parent().newInstance();
        } catch (Exception ex) {
            return null;
        }
    }

    //Set base perm for subcommand
    default String getBasePermission() {
        return getParent().getBasePermission() + "." + getIdentifier().toLowerCase();
    }

    default Text getShortDescription() {
        return getParent().getShortDescription();
    }

    default Text getLongDescription() {
        return getParent().getLongDescription();
    }

    //Usage
    default String getFullIdentifier() {
        return getParent().getFullIdentifier() + " " + getIdentifier();
    }
}
