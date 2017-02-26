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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.GameruleArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.StringArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.WorldPropertiesArgument;
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
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.*;

@CommandInfo(module = WorldModule.class, aliases = {"gamerule"})
@CommandPermissions(level = PermissionLevel.ADMIN)
@CommandParentInfo(parent = WorldCommand.class)
public class GameruleWorldCommand implements HighSubCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new WorldPropertiesArgument(Text.of("world"))).build(), Arguments.builder(new GameruleArgument(Text.of("gamerule"))).optional().build(), Arguments.builder(new StringArgument(Text.of("value"))).optional().build()};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties world = (WorldProperties) args.getOne("world").get();
        if (!args.hasAny("gamerule")) {
            Map<String, String> gamerules = world.getGameRules();
            List<Text> texts = new ArrayList<>();
            //Add entry to texts for every warp
            for (Map.Entry<String, String> gamerule : gamerules.entrySet()) {
                if (!src.hasPermission(getFullIdentifier() + gamerule.getKey().toLowerCase())) {
                    continue;
                }
                texts.add(Messages.getFormatted("world.command.world.gamerule.entry", "%gamerule%", gamerule.getKey(), "%value%", gamerule.getValue()).toBuilder().onHover(TextActions.showText(Messages.getFormatted("world.command.world.gamerule.hoverentry", "%gamerule%", gamerule.getKey()))).onClick(TextActions.suggestCommand("/world gamerule " + gamerule.getKey() + " ")).build());
            }
            //Sort alphabetically
            Collections.sort(texts);
            //Send page
            PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
            PaginationList paginationList = paginationService.builder().contents(texts).title(Messages.getFormatted("world.command.world.gamerule.header", "%world%", world.getWorldName()).toBuilder().color(TextColors.DARK_GREEN).build()).build();
            paginationList.sendTo(src);
            return CommandResult.empty();
        }
        if (!args.hasAny("value")) {
            String gamerule = args.<String>getOne("gamerule").get();
            Optional<String> value = world.getGameRule(gamerule);
            if (!value.isPresent()) {
                throw new ErrorMessageException(Messages.getFormatted(src, "world.command.world.gamerule.notfound", "%gamerule%", gamerule));
            }
            Messages.send(src, "world.command.world.gamerule.viewsingle", "%gamerule%", gamerule, "%value%", value.get());
            return CommandResult.empty();
        }

        String gamerule = args.<String>getOne("gamerule").get();
        String value = args.<String>getOne("value").get();
        world.setGameRule(gamerule, value);
        Messages.send(src, "world.command.world.gamerule.success", "%gamerule%", world.getWorldName());
        return CommandResult.success();
    }
}