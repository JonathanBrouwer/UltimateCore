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
package bammerbom.ultimatecore.sponge.modules.blacklist.listeners;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;

public class BlockListener {
    @Listener
    public void onBreak(ChangeBlockEvent.Break event) {
        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        for (Transaction<BlockSnapshot> trans : event.getTransactions()) {
            if (!trans.isValid()) continue;
            CommentedConfigurationNode node = hnode.getNode("blocks", trans.getOriginal().getState().getType().getId());
            if (!node.isVirtual()) {
                if (node.getNode("deny-break").getBoolean()) {
                    trans.setValid(false);
                }
            }
        }
    }

    @Listener
    public void onPlace(ChangeBlockEvent.Place event) {
        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        for (Transaction<BlockSnapshot> trans : event.getTransactions()) {
            if (!trans.isValid()) continue;
            CommentedConfigurationNode node = hnode.getNode("blocks", trans.getFinal().getState().getType().getId());
            if (!node.isVirtual() && node.getNode("deny-place").getBoolean()) {
                trans.setValid(false);
            }
        }
    }
}
