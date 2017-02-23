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
package bammerbom.ultimatecore.sponge.api.command.selectiontask.selectiontasks;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.selectiontask.SelectionTask;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockSelectionTask implements SelectionTask<Location<World>> {

    static HashMap<UUID, Consumer<Location<World>>> consumers = new HashMap<>();

    static {
        //Register events after first use
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new BlockSelectionTask());
    }

    @Override
    public void select(Player p, Function<Location<World>, Boolean> test, Consumer<Location<World>> callable) {
        //Check if player is looking at block
        BlockRay<World> blockray = BlockRay.from(p).distanceLimit(5).stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1)).build();
        Optional<BlockRayHit<World>> hit = blockray.end();
        //If player is looking at block & block is correct type
        if (hit.isPresent() && test.apply(hit.get().getLocation())) {
            callable.accept(hit.get().getLocation());
            return;
        }

        //If not looking at block
        Messages.send(p, "core.selection.block");
        consumers.put(p.getUniqueId(), callable);
    }

    @Listener
    public void onInteract(InteractBlockEvent.Secondary event, @First Player p) {
        if (!consumers.containsKey(p.getUniqueId())) return;
        Location loc = event.getTargetBlock().getLocation().orElse(null);
        if (loc == null) return;
        consumers.remove(p.getUniqueId()).accept(loc);
    }
}
