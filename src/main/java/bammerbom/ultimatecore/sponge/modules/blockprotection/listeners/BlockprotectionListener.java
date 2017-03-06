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
package bammerbom.ultimatecore.sponge.modules.blockprotection.listeners;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data_old.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.BlockprotectionKeys;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.Protection;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.stream.Collectors;

public class BlockprotectionListener {

    @Listener
    public void onInteractPrimary(InteractBlockEvent.Primary event, @First Player p) {
        ModuleConfig config = Modules.BLOCKPROTECTION.get().getConfig().get();
        if (!config.get().getNode("protections", "allow-interact-primary").getBoolean()) return;
        if (!event.getTargetBlock().getLocation().isPresent()) return;

        for (Protection prot : GlobalData.get(BlockprotectionKeys.PROTECTIONS).get()) {
            if (prot.getPlayers().contains(p.getUniqueId())) continue;
            if (!prot.getLocations().contains(event.getTargetBlock().getLocation().get())) continue;

            //Check if it should be cancelled
            if (prot.getLocktype().shouldBeCancelled()) {
                event.setCancelled(true);
                p.sendMessage(prot.getLocktype().getErrorMessage(p, prot));
            }
        }
    }

    @Listener
    public void onInteractSecondary(InteractBlockEvent.Secondary event, @First Player p) {
        ModuleConfig config = Modules.BLOCKPROTECTION.get().getConfig().get();
        if (!config.get().getNode("protections", "allow-interact-secondary").getBoolean()) return;
        if (!event.getTargetBlock().getLocation().isPresent()) return;

        for (Protection prot : GlobalData.get(BlockprotectionKeys.PROTECTIONS).get()) {
            if (prot.getPlayers().contains(p.getUniqueId())) continue;
            if (!prot.getLocations().contains(event.getTargetBlock().getLocation().get())) continue;

            //Check if it should be cancelled
            if (prot.getLocktype().shouldBeCancelled()) {
                event.setCancelled(true);
                p.sendMessage(prot.getLocktype().getErrorMessage(p, prot));
            }
        }
    }

    @Listener
    public void onBreak(ChangeBlockEvent.Break event) {
        ModuleConfig config = Modules.BLOCKPROTECTION.get().getConfig().get();
        if (!config.get().getNode("protections", "allow-interact-primary").getBoolean()) return;
        Player p = event.getCause().first(Player.class).orElse(null);

        boolean modified = false;
        for (Protection prot : GlobalData.get(BlockprotectionKeys.PROTECTIONS).get()) {
            //Ignore protection if the player is allowed to modify it
            if (p != null && prot.getPlayers().contains(p.getUniqueId())) continue;
            //For each location of the protection,
            for (Transaction trans : event.getTransactions().stream().filter(trans -> trans.getFinal().getLocation().isPresent() && prot.getLocations().contains(trans.getFinal().getLocation().get())).collect(Collectors.toList())) {
                modified = true;
                trans.setValid(false);
            }

            //If anything has been cancelled & caused by player, send message
            if (p != null && modified) {
                p.sendMessage(prot.getLocktype().getErrorMessage(p, prot));
            }
        }
    }
}
