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
package bammerbom.ultimatecore.sponge.modules.teleport.commands;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.BiomeTypeArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.modules.teleport.handlers.BiomeFindHandler;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

import java.util.Arrays;

@CommandInfo(module = TeleportModule.class, aliases = {"biometeleport", "biometp", "btp", "bteleport"})
@CommandPermissions(level = PermissionLevel.MOD)
public class BiomeTeleportCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{Arguments.builder(new BiomeTypeArgument(Text.of("biome"))).onlyOne().build()};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        checkIfPlayer(src);
        Player p = (Player) src;
        BiomeType type = args.<BiomeType>getOne("biome").get();

        //Find location
        Location<World> loc = BiomeFindHandler.findBiome(p, type).orElse(null);
        if (loc == null) {
            throw Messages.error(src, "teleport.command.biometeleport.fail", "%biome%", type.getName());
        }

        //Found suitable location
        Teleportation request = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), new Transform<>(loc, p.getRotation(), p.getScale()), teleportRequest -> {
            //Complete
            Messages.send(p, "teleport.command.biometeleport.success", "%biome%", type.getName());
        }, (teleportRequest, reason) -> {
        }, true, false);
        request.start();
        return CommandResult.success();
    }
}
