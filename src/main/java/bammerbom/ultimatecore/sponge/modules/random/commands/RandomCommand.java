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
package bammerbom.ultimatecore.sponge.modules.random.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.IntegerArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.random.RandomModule;
import bammerbom.ultimatecore.sponge.modules.random.api.RandomPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@CommandInfo(module = RandomModule.class, aliases = {"random"})
public class RandomCommand implements HighCommand {
    static Random random = new Random();

    @Override
    public Permission getPermission() {
        return RandomPermissions.UC_RANDOM_RANDOM_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(RandomPermissions.UC_RANDOM_RANDOM_BASE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new IntegerArgument(Text.of("first"))).usage("<Min>").onlyOne().build(),
                Arguments.builder(new IntegerArgument(Text.of("second"))).usage("[Max]").onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, RandomPermissions.UC_RANDOM_RANDOM_BASE);

        int min = args.hasAny("second") ? args.<Integer>getOne("first").get() : 1;
        int max = args.hasAny("second") ? args.<Integer>getOne("second").get() : args.<Integer>getOne("first").get();
        if (min > max) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "random.command.random.invalid", "%min%", min, "%max%", max));
        }
        int rand = random.nextInt((max + 1) - min) + min;

        Messages.send(sender, "random.command.random.success", "%min%", min, "%max%", max, "%value%", rand);
        return CommandResult.success();
    }
}
