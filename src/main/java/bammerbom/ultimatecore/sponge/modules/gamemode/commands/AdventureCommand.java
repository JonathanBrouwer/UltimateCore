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
package bammerbom.ultimatecore.sponge.modules.gamemode.commands;

import bammerbom.ultimatecore.sponge.api.command.HighCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.gamemode.GamemodeModule;
import bammerbom.ultimatecore.sponge.modules.gamemode.api.GamemodePermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = GamemodeModule.class, aliases = {"adventure", "a"})
public class AdventureCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GamemodePermissions.UC_GAMEMODE_GAMEMODE_BASE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_BASE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_ADVENTURE);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).optional().onlyOne().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_BASE);
        checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE);
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;
            p.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
            Messages.send(sender, "gamemode.command.gamemode.success", "%gamemode%", "adventure");
            return CommandResult.success();
        } else {
            checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_BASE);
            checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_ADVENTURE);
            Player t = args.<Player>getOne("player").get();
            t.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
            Messages.send(t, "gamemode.command.gamemode.success.others", "%sender%", sender, "%gamemode%", "adventure");
            Messages.send(sender, "gamemode.command.gamemode.success.self", "%player%", t, "%gamemode%", "adventure");
            return CommandResult.success();
        }
    }
}
