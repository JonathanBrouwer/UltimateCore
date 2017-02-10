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
package bammerbom.ultimatecore.sponge.modules.world.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandChildrenInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.exceptions.NotEnoughArgumentsException;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.world.WorldModule;
import bammerbom.ultimatecore.sponge.modules.world.commands.world.*;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;

@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandInfo(module = WorldModule.class, aliases = {"world"})
@CommandChildrenInfo(children = {
        CreateWorldCommand.class,
        DeleteWorldCommand.class,
        DisableWorldCommand.class,
        EnableWorldCommand.class,
        GameruleWorldCommand.class,
        ImportWorldCommand.class,
        InfoWorldCommand.class,
//        SetdifficultyWorldCommand.class,
//        SetgamemodeWorldCommand.class,
//        SethardcoreWorldCommand.class,
//        SetkeepspawnloadedWorldCommand.class,
//        SetloadonstartupWorldCommand.class,
//        SetpvpenabledWorldCommand.class,
//        SetspawnWorldCommand.class,
//        SetspawnWorldCommand.class,
        TeleportWorldCommand.class
})
public class WorldCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        throw new NotEnoughArgumentsException(getUsage(src));
    }
}
