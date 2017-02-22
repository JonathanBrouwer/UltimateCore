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
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import bammerbom.ultimatecore.sponge.api.teleport.utils.LocationUtil;
import bammerbom.ultimatecore.sponge.modules.teleport.TeleportModule;
import bammerbom.ultimatecore.sponge.utils.RandomUtil;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;

@CommandInfo(module = TeleportModule.class, aliases = {"randomteleport", "randomtp", "rtp", "rteleport"})
@CommandPermissions(level = PermissionLevel.MOD)
public class RandomTeleportCommand implements HighPermCommand {

    //TODO configurable
    Integer maxDistance = 20000;
    Integer maxTries = 20;
    List<BlockType> bannedBlocks = Arrays.asList(BlockTypes.WATER, BlockTypes.FLOWING_WATER, BlockTypes.LAVA, BlockTypes.FLOWING_WATER, BlockTypes.WATERLILY);

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[0];
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        checkIfPlayer(src);
        Player p = (Player) src;
        Vector3d pos = p.getLocation().getPosition();

        TeleportHelper tph = Sponge.getGame().getTeleportHelper();
        int tries = 0;
        while (tries < this.maxTries) {
            tries++;

            //Try to find suitable location
            Integer x = pos.getFloorX() + RandomUtil.nextInt(-this.maxDistance, this.maxDistance);
            Integer z = pos.getFloorZ() + RandomUtil.nextInt(-this.maxDistance, this.maxDistance);
            Integer y = LocationUtil.getHighestY(p.getWorld(), x, z).orElse(null);
            if (y == null) continue;
            Location<World> loc = tph.getSafeLocation(new Location<>(p.getWorld(), x, y, z)).orElse(null);
            if (loc == null) continue;
            if (this.bannedBlocks.contains(loc.getBlockType()) || this.bannedBlocks.contains(loc.add(0, -1, 0).getBlockType())) continue;

            //Found suitable location
            Teleportation request = UltimateCore.get().getTeleportService().createTeleportation(p, Arrays.asList(p), new Transform<>(loc, p.getRotation(), p.getScale()), teleportRequest -> {
                //Complete
                Messages.send(p, "teleport.command.randomteleport.success");
            }, (teleportRequest, reason) -> {
            }, true, false);
            request.start();
            return CommandResult.success();
        }
        throw Messages.error(src, "teleport.command.randomteleport.fail");
    }
}
