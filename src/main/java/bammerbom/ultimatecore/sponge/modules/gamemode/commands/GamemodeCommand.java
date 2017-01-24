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
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.GamemodeArgument;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.gamemode.GamemodeModule;
import bammerbom.ultimatecore.sponge.modules.gamemode.api.GamemodePermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = GamemodeModule.class, aliases = {"gamemode", "gm"})
public class GamemodeCommand implements HighCommand {
    @Override
    public Permission getPermission() {
        return GamemodePermissions.UC_GAMEMODE_GAMEMODE_BASE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GamemodePermissions.UC_GAMEMODE_GAMEMODE_BASE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_CREATIVE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_SURVIVAL, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_SPECTATOR, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_BASE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_ADVENTURE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_CREATIVE, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_SURVIVAL, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_SPECTATOR);
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new GamemodeArgument(Text.of("gamemode"))).onlyOne().build(), Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optional().build()
        };
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_BASE);
        GameMode gm = args.<GameMode>getOne("gamemode").get();
        if (!args.hasAny("player")) {
            checkIfPlayer(sender);
            Player p = (Player) sender;
            //Perm check
            if (gm == GameModes.SURVIVAL) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE);
            } else if (gm == GameModes.CREATIVE) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_CREATIVE);
            } else if (gm == GameModes.ADVENTURE) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_ADVENTURE);
            } else if (gm == GameModes.SPECTATOR) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_SELF_SPECTATOR);
            }
            p.offer(Keys.GAME_MODE, gm);
            sender.sendMessage(Messages.getFormatted(sender, "gamemode.command.gamemode.success", "%gamemode%", gm.getName()));
            return CommandResult.success();
        } else {
            checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_BASE);
            Player t = args.<Player>getOne("player").get();
            //Perm check
            if (gm == GameModes.SURVIVAL) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_ADVENTURE);
            } else if (gm == GameModes.CREATIVE) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_CREATIVE);
            } else if (gm == GameModes.ADVENTURE) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_ADVENTURE);
            } else if (gm == GameModes.SPECTATOR) {
                checkPermission(sender, GamemodePermissions.UC_GAMEMODE_GAMEMODE_OTHERS_SPECTATOR);
            }
            t.offer(Keys.GAME_MODE, gm);
            t.sendMessage(Messages.getFormatted(t, "gamemode.command.gamemode.success.others", "%sender%", sender.getName(), "%gamemode%", gm.getName()));
            sender.sendMessage(Messages.getFormatted(sender, "gamemode.command.gamemode.success.self", "%player%", t.getName(), "%gamemode%", gm.getName()));
            return CommandResult.success();
        }
    }
}
