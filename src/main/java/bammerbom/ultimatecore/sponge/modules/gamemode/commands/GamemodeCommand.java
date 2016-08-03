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
import bammerbom.ultimatecore.sponge.utils.CMGenerator;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class GamemodeCommand implements Command {
    @Override
    public Module getModule() {
        return Modules.GAMEMODE.get();
    }

    @Override
    public String getIdentifier() {
        return "gamemode";
    }

    @Override
    public Permission getPermission() {
        return GamemodePermissions.UC_GAMEMODE;
    }

    @Override
    public List<Permission> getPermissions() {
        return Arrays.asList(GamemodePermissions.UC_GAMEMODE, GamemodePermissions.UC_GAMEMODE_ADVENTURE, GamemodePermissions.UC_GAMEMODE_CREATIVE, GamemodePermissions
                .UC_GAMEMODE_SURVIVAL, GamemodePermissions.UC_GAMEMODE_SPECTATOR, GamemodePermissions.UC_GAMEMODE_OTHERS, GamemodePermissions.UC_GAMEMODE_OTHERS_ADVENTURE,
                GamemodePermissions.UC_GAMEMODE_OTHERS_CREATIVE, GamemodePermissions.UC_GAMEMODE_OTHERS_SURVIVAL, GamemodePermissions.UC_GAMEMODE_OTHERS_SPECTATOR);
    }

    @Override
    public Text getUsage() {
        return CMGenerator.usage(this, Messages.getFormatted("gamemode.command.gamemode.usage"));
    }

    @Override
    public Text getShortDescription() {
        return CMGenerator.shortDescription(this, Messages.getFormatted("gamemode.command.gamemode.shortdescription"));
    }

    @Override
    public Text getLongDescription() {
        return CMGenerator.longDescription(this, Messages.getFormatted("gamemode.command.gamemode.longdescription"));
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("gamemode", "gm");
    }

    @Override
    public CommandResult run(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getUsage());
            return CommandResult.empty();
        }
        GameMode gm;
        switch (args[0].toLowerCase()) {
            case "survial":
            case "0":
            case "s":
                gm = GameModes.SURVIVAL;
                break;
            case "creative":
            case "c":
            case "1":
                gm = GameModes.CREATIVE;
                break;
            case "adventure":
            case "2":
            case "a":
                gm = GameModes.ADVENTURE;
                break;
            case "spectator":
            case "3":
            case "spec":
            case "sp":
                gm = GameModes.SPECTATOR;
                break;
            default:
                sender.sendMessage(Messages.getFormatted("gamemode.command.gamemode.invalidgamemode", "%gamemode%", args[0].toLowerCase()));
                return CommandResult.empty();
        }
        Player player;
        if (args.length == 2) {
            if (Sponge.getServer().getPlayer(args[1]).isPresent()) {
                player = Sponge.getServer().getPlayer(args[1]).get();
                if (gm == GameModes.SURVIVAL && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS.get()) && !sender.hasPermission(GamemodePermissions
                        .UC_GAMEMODE_OTHERS_SURVIVAL.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.CREATIVE && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS.get()) && !sender.hasPermission(GamemodePermissions
                        .UC_GAMEMODE_OTHERS_CREATIVE.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.ADVENTURE && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS.get()) && !sender.hasPermission(GamemodePermissions
                        .UC_GAMEMODE_OTHERS_ADVENTURE.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.SPECTATOR && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_OTHERS.get()) && !sender.hasPermission(GamemodePermissions
                        .UC_GAMEMODE_OTHERS_SPECTATOR.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                }

                player.sendMessage(Messages.getFormatted("gamemode.command.gamemode.succes.others", "%sender%", sender.getName(), "%gamemode%", gm.getName()));
                sender.sendMessage(Messages.getFormatted("gamemode.command.gamemode.succes.self", "%player%", player.getName(), "%gamemode%", gm.getName()));

            } else {
                sender.sendMessage(Messages.getFormatted("core.playernotfound", "%player%", args[1]));
                return CommandResult.empty();
            }
        } else {
            if (sender instanceof Player) {
                player = (Player) sender;
                if (gm == GameModes.SURVIVAL && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_SURVIVAL.get())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.CREATIVE && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_CREATIVE.get()
                )) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.ADVENTURE && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_ADVENTURE.get
                        ())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                } else if (gm == GameModes.SPECTATOR && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE.get()) && !sender.hasPermission(GamemodePermissions.UC_GAMEMODE_SPECTATOR.get
                        ())) {
                    sender.sendMessage(Messages.getFormatted("core.nopermssions"));
                    return CommandResult.empty();
                }
                sender.sendMessage(Messages.getFormatted("gamemode.command.gamemode.succes", "%gamemode%", gm.getName()));
            } else {
                sender.sendMessage(Messages.getFormatted("core.noplayer"));
                return CommandResult.empty();
            }
        }
        player.offer(Keys.GAME_MODE, gm);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args, String curs, Integer curn) {
        return null;
    }
}
