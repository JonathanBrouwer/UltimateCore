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
import bammerbom.ultimatecore.sponge.api.command.exceptions.PlayerOnlyException;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.impl.command.UCCommandCallable;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import bammerbom.ultimatecore.sponge.utils.UsageGenerator;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.*;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.args.parsing.InputTokenizer;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.*;

public interface SmartCommand extends Command, CommandExecutor {

    @Override
    default CommandResult run(CommandSource sender, String[] rawargs) {
        InputTokenizer argumentParser = InputTokenizer.quotedStrings(false);
        String args = StringUtil.join(" ", rawargs);
        try {
            CommandArgs cargs = new CommandArgs(StringUtil.join(" ", rawargs), argumentParser.tokenize(args, false));
            CommandContext context = new CommandContext();
            GenericArguments.seq(getArguments()).parse(sender, cargs, context);
            return execute(sender, context);
        } catch (CommandPermissionException ex) {
            sender.sendMessage(Messages.getFormatted(sender, "core.nopermissions"));
            return CommandResult.empty();
        } catch (CommandException ex) {
            if (ex.getText() != null) {
                sender.sendMessage(ex.getText());
            }
            return CommandResult.empty();
        }
    }

    default CommandElement[] getArguments() {
        return new CommandElement[]{};
    }

    default Map<List<String>, ? extends Command> getChildren() {
        return new HashMap<>();
    }

    default CommandSpec getSpec() {
        CommandSpec.Builder cb = CommandSpec.builder();
        cb.executor(this);
        cb.arguments(getArguments());
        cb.permission(getPermission().get());
        cb.description(getShortDescription());
        cb.extendedDescription(getLongDescription());

        //Children
        HashMap<List<String>, CommandCallable> children = new HashMap<>();
        getChildren().forEach((aliases, cmd) -> {
            children.put(aliases, new UCCommandCallable(cmd));
        });

        return cb.build();
    }

    @Override
    default Text getUsage(@Nullable CommandSource source) {
        return UsageGenerator.usage(this, Text.of("/" + getIdentifier() + " " + getSpec().getUsage(source == null ? Sponge.getServer().getConsole() : source).toPlain().replaceAll("\\?\\|", "")));
    }

    //Implemented with @RegisterCommand
    @Override
    default Module getModule() {
        if (!this.getClass().isAnnotationPresent(RegisterCommand.class)) {
            return null;
        }
        Class<? extends Module> mclass = this.getClass().getAnnotation(RegisterCommand.class).module();
        for (Module module : UltimateCore.get().getModuleService().getModules()) {
            if (module.getClass().equals(mclass)) {
                return module;
            }
        }
        return null;
    }

    //Implemented with @RegisterCommand
    @Override
    default String getIdentifier() {
        if (!this.getClass().isAnnotationPresent(RegisterCommand.class)) {
            return null;
        }
        return this.getClass().getAnnotation(RegisterCommand.class).aliases()[0];
    }

    //Implemented with @RegisterCommand
    @Override
    default List<String> getAliases() {
        if (!this.getClass().isAnnotationPresent(RegisterCommand.class)) {
            return null;
        }
        return Arrays.asList(this.getClass().getAnnotation(RegisterCommand.class).aliases());
    }

    @Override
    default List<String> onTabComplete(CommandSource sender, String[] rawargs, String curs, Integer curn) {
        InputTokenizer argumentParser = InputTokenizer.quotedStrings(false);
        String args = StringUtil.join(" ", rawargs);
        try {
            CommandArgs cargs = new CommandArgs(StringUtil.join(" ", rawargs), argumentParser.tokenize(args, false));
            CommandContext context = new CommandContext();
            //Should this be here?
            GenericArguments.seq(getArguments()).parse(sender, cargs, context);
            //Add location where player is looking as argument (for some reason sponge can't do that itself)
            if (sender instanceof Player) {
                BlockRay<World> blockRay = BlockRay.from((Player) sender).stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
                if (blockRay.hasNext()) {
                    Location<World> loc = blockRay.next().getLocation();
                    context.putArg(CommandContext.TARGET_BLOCK_ARG, loc);
                }
            }
            List<String> ret = GenericArguments.seq(getArguments()).complete(sender, cargs, context);
            return ret == null ? ImmutableList.of() : ImmutableList.copyOf(ret);
        } catch (CommandException ex) {
            if (ex.getText() != null) {
                sender.sendMessage(ex.getText());
            }
            return new ArrayList<>();
        }
    }

    @Override
    CommandResult execute(CommandSource sender, CommandContext args) throws CommandException;

    //QUICK CHECKS
    default void checkPermission(CommandSource commander, String permission) throws CommandException {
        if (!commander.hasPermission(permission)) {
            throw new CommandPermissionException();
        }
    }

    default void checkPermission(CommandSource commander, Permission permission) throws CommandException {
        if (!commander.hasPermission(permission.get())) {
            throw new CommandPermissionException();
        }
    }

    default void checkIfPlayer(CommandSource sender) throws PlayerOnlyException {
        if (!(sender instanceof Player)) {
            throw new PlayerOnlyException();
        }
    }
}
