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
package bammerbom.ultimatecore.sponge.api.command.arguments;

import bammerbom.ultimatecore.sponge.api.command.UCommandElement;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class GamemodeArgument extends UCommandElement {

    public GamemodeArgument(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    public GameMode parseValue(CommandSource sender, CommandArgs args) throws ArgumentParseException {
        String value = args.next();
        try {
            if (Sponge.getRegistry().getType(CatalogTypes.GAME_MODE, value).isPresent()) {
                return Sponge.getRegistry().getType(CatalogTypes.GAME_MODE, value).get();
            }
        } catch (NullPointerException ignore) {
        }
        
        switch (value.toLowerCase()) {
            case "survival":
            case "0":
            case "s":
                return GameModes.SURVIVAL;
            case "creative":
            case "c":
            case "1":
                return GameModes.CREATIVE;
            case "adventure":
            case "2":
            case "a":
                return GameModes.ADVENTURE;
            case "spectator":
            case "3":
            case "spec":
            case "sp":
                return GameModes.SPECTATOR;
            default:
                throw (args.createError(Messages.getFormatted(sender, "gamemode.command.gamemode.invalidgamemode", "%gamemode%", value)));
        }
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Sponge.getRegistry().getAllOf(CatalogTypes.GAME_MODE).stream().map(CatalogType::getId).collect(Collectors.toList());
    }
}
