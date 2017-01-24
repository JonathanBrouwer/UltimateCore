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
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandChildrenInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.exceptions.PlayerOnlyException;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.UsageGenerator;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface HighCommand extends Command, CommandExecutor {

    //COMMANDSPEC METHODS
    CommandElement[] getArguments();

    default List<HighSubCommand> getChildren() {
        if (getClass().isAnnotationPresent(CommandChildrenInfo.class)) {
            CommandChildrenInfo childrenInfo = getClass().getAnnotation(CommandChildrenInfo.class);
            List<HighSubCommand> children = new ArrayList<>();
            for (Class<? extends HighSubCommand> child : childrenInfo.children()) {
                try {
                    children.add(child.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return children;
        }
        return new ArrayList<>();
    }

    default CommandSpec getCallable() {
        CommandSpec.Builder cb = CommandSpec.builder();
        cb.executor(this);
        cb.arguments(getArguments());
        cb.permission(getPermission().get());
        cb.description(getShortDescription(null));
        cb.extendedDescription(getLongDescription(null));

        //Children
        HashMap<List<String>, CommandCallable> children = new HashMap<>();
        getChildren().forEach((cmd) -> {
            children.put(cmd.getAliases(), cmd.getCallable());
        });
        //Sponge bug: If you submit an empty children list as children, sponge fucks up the children
        if (!children.isEmpty()) {
            cb.children(children);
        }

        return cb.build();
    }

    //IMPLEMENTED METHODS

    //Auto-generated
    @Override
    default Text getUsage(@Nullable CommandSource source) {
        Text paramusage = getCallable().getUsage(source != null ? source : Sponge.getServer().getConsole());
        return Text.of("/" + getFullIdentifier(), (!paramusage.isEmpty() ? " " : ""), paramusage);
    }

    //From language file (TODO better system?)
    @Override
    default Text getShortDescription(@Nullable CommandSource source) {
        return UsageGenerator.shortDescription(this, Messages.getFormatted(getModule().getIdentifier() + ".command." + getIdentifier() + ".shortdescription"));
    }

    //From language file (TODO better system?)
    @Override
    default Text getLongDescription(@Nullable CommandSource source) {
        return UsageGenerator.longDescription(this, Messages.getFormatted(getModule().getIdentifier() + ".command." + getIdentifier() + ".longdescription"));
    }

    //Implemented with @CommandInfo
    @Override
    default Module getModule() {
        if (!this.getClass().isAnnotationPresent(CommandInfo.class)) {
            return null;
        }
        Class<? extends Module> mclass = this.getClass().getAnnotation(CommandInfo.class).module();
        for (Module module : UltimateCore.get().getModuleService().getModules()) {
            if (module.getClass().equals(mclass)) {
                return module;
            }
        }
        return null;
    }

    //Implemented with @CommandInfo
    @Override
    default String getIdentifier() {
        if (!this.getClass().isAnnotationPresent(CommandInfo.class)) {
            return null;
        }
        return this.getClass().getAnnotation(CommandInfo.class).aliases()[0];
    }

    //Implemented with @CommandInfo
    //Parent(s) + Identifier
    default String getFullIdentifier() {
        return getIdentifier();
    }

    //Implemented with @CommandInfo
    @Override
    default List<String> getAliases() {
        if (!this.getClass().isAnnotationPresent(CommandInfo.class)) {
            return null;
        }
        return Arrays.asList(this.getClass().getAnnotation(CommandInfo.class).aliases());
    }

    //QUICK CHECKS

    default void checkPermission(CommandSource sender, String permission) throws CommandException {
        if (!sender.hasPermission(permission)) {
            throw new CommandPermissionException();
        }
    }

    default void checkPermission(CommandSource sender, Permission permission) throws CommandException {
        if (!sender.hasPermission(permission.get())) {
            throw new CommandPermissionException();
        }
    }

    default void checkIfPlayer(CommandSource sender) throws PlayerOnlyException {
        if (!(sender instanceof Player)) {
            throw new PlayerOnlyException();
        }
    }
}
