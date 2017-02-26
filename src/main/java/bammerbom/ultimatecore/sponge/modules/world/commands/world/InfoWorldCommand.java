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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.WorldPropertiesArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.world.WorldModule;
import bammerbom.ultimatecore.sponge.modules.world.commands.WorldCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.storage.WorldProperties;

@CommandParentInfo(parent = WorldCommand.class)
@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandInfo(module = WorldModule.class, aliases = {"info", "worldinfo"})
public class InfoWorldCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new WorldPropertiesArgument(Text.of("world"))).optional().build()};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties world;
        if (!args.hasAny("world")) {
            checkIfPlayer(src);
            Player p = (Player) src;
            world = p.getWorld().getProperties();
        } else {
            world = args.<WorldProperties>getOne("world").get();
        }
        Messages.send(src, "world.command.world.info.single.title", "%world%", world.getWorldName());
        Messages.send(src, "world.command.world.info.single.id-uuid", "%id%", world.getUniqueId().toString());
        String numeric;
        try {
            numeric = world.getAdditionalProperties().getView(DataQuery.of("SpongeData")).get().get(DataQuery.of("dimensionId")).get().toString();
        } catch (Exception ex) {
            numeric = "?";
        }
        Messages.send(src, "world.command.world.info.single.id-numeric", "%id%", numeric);
        Messages.send(src, "world.command.world.info.single.enabled", "%enabled%", world.loadOnStartup() ? Messages.getFormatted(src, "world.enabled") : Messages.getFormatted(src, "world.disabled"));
        Messages.send(src, "world.command.world.info.single.difficulty", "%difficulty%", world.getDifficulty().getName());
        Messages.send(src, "world.command.world.info.single.gamemode", "%gamemode%", world.getGameMode().getName());
        Messages.send(src, "world.command.world.info.single.hardcore", "%hardcore%", world.isHardcore() ? Messages.getFormatted(src, "world.enabled") : Messages.getFormatted(src, "world.disabled"));
        Messages.send(src, "world.command.world.info.single.pvp", "%pvp%", world.isPVPEnabled() ? Messages.getFormatted(src, "world.enabled") : Messages.getFormatted(src, "world.disabled"));
        src.sendMessage(Messages.getFormatted(src, "world.command.world.info.single.gamerules").toBuilder().onClick(TextActions.runCommand("/world gamerule " + world.getWorldName())).build());
        return CommandResult.success();
    }
}