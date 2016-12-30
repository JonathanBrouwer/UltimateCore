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

import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.modules.gamemode.api.GamemodePermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.Selector;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import java.util.Arrays;
import java.util.List;

public class SurvivalCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.GAMEMODE.get();
    }

    @Override
    public String getIdentifier() {
        return "survival";
    }

    @Override
    public Permission getPermission() {
        return GamemodePermissions.UC_GAMEMODE_SURVIVAL;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GamemodePermissions.UC_GAMEMODE, GamemodePermissions.UC_GAMEMODE_SURVIVAL, GamemodePermissions.UC_GAMEMODE_OTHERS, GamemodePermissions
                .UC_GAMEMODE_OTHERS_SURVIVAL);
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("survival", "s");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        Player player;
        if (args.length >= 1) {
            //Check permissions
            if (!sender.hasPermission(GamemodePermissions.UC_GAMEMODE.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_SURVIVAL.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            //Send messages
            if (Selector.one(sender, args[0]).isPresent()) {
                player = Selector.one(sender, args[0]).get();
                //Not uuids because a sender does not have an UUID.
                if (!sender.getName().equals(player.getName())) {
                    player.sendMessage(Messages.getFormatted("gamemode.command.gamemode.success.others", "%sender%", sender.getName(), "%gamemode%", "survival"));
                }
                sender.sendMessage(Messages.getFormatted("gamemode.command.gamemode.success.self", "%player%", player.getName(), "%gamemode%", "survival"));
            } else {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[0]));
                return CommandResult.empty();
            }
        } else {
            //Check permissions
            if (!sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS_SURVIVAL.get())) {
                sender.sendMessage(Messages.getFormatted("core.nopermissions"));
                return CommandResult.empty();
            }
            //Send messages
            if (sender instanceof Player) {
                player = (Player) sender;
                sender.sendMessage(Messages.getFormatted("gamemode.command.gamemode.success", "%gamemode%", "survival"));
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
        }
        player.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
