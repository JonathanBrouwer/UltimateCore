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
package bammerbom.ultimatecore.sponge.modules.world.commands.world;

import bammerbom.ultimatecore.sponge.api.command.HighSubCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandParentInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.world.WorldModule;
import bammerbom.ultimatecore.sponge.modules.world.commands.WorldCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandInfo(module = WorldModule.class, aliases = {"import"})
@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandParentInfo(parent = WorldCommand.class)
public class ImportWorldCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new StringArgument(Text.of("name"))).build()};
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        String name = (String) args.getOne("name").get();

        Path path = Sponge.getGame().getGameDirectory();
        if (!(Files.exists(path.resolve(name.toLowerCase())) || Files.exists(path.resolve(name)) || Sponge.getServer().getAllWorldProperties().stream().anyMatch(x -> x.getWorldName().equalsIgnoreCase(name)))) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "world.command.world.import.notfound", "%world%", name));
        }

        Messages.send(sender, "world.command.world.import.starting", "%name%", name);

        WorldProperties props;
        try {
            props = Sponge.getServer().createWorldProperties(name.toLowerCase(), WorldArchetype.builder().build(name.toLowerCase(), name));
        } catch (IOException e) {
            throw new ErrorMessageException(Messages.getFormatted(sender, "world.command.world.import.fileerror", "%error%", e.getMessage()));
        }
        Sponge.getServer().saveWorldProperties(props);
        World world = Sponge.getServer().loadWorld(props).orElseThrow(() -> new ErrorMessageException(Messages.getFormatted(sender, "world.command.world.import.loaderror")));
        Messages.send(sender, "world.command.world.import.success", "%name%", world.getName());
        return CommandResult.success();
    }
}