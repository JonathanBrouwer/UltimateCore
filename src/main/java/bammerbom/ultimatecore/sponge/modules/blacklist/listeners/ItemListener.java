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
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class ItemListener {
    @Listener
    public void onDrop(DropItemEvent.Dispense event, @Root EntitySpawnCause cause) {
        if (cause.getEntity() instanceof Player) {
            //TODO exempt check
        }

        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        for (Entity en : event.getEntities()) {
            if (!(en instanceof Item)) continue;
            Item item = (Item) en;
            CommentedConfigurationNode node = hnode.getNode("items", item.getItemType().getId());
            if (!node.isVirtual()) {
                if (node.getNode("deny-drop").getBoolean()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Listener
    public void onPickup(ChangeInventoryEvent.Pickup event, @First Player p) {
        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        Item item = event.getTargetEntity();
        CommentedConfigurationNode node = hnode.getNode("items", item.getItemType().getId());
        if (!node.isVirtual()) {
            if (node.getNode("deny-drop").getBoolean()) {
                event.setCancelled(true);
            }
        }
    }

    @Listener
    public void onChange(ChangeInventoryEvent event, @First Player p) {
        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        try {
            for (Inventory s : event.getTargetInventory().slots()) {
                ItemStack item = s.peek().orElse(null);
                if (item == null) continue;
                CommentedConfigurationNode node = hnode.getNode("items", item.getItem().getId());
                if (!node.isVirtual()) {
                    if (node.getNode("deny-possession").getBoolean()) {
                        s.poll();
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    @Listener
    public void onInteract(InteractItemEvent event) {
        ModuleConfig config = Modules.BLACKLIST.get().getConfig().get();
        CommentedConfigurationNode hnode = config.get();
        ItemStackSnapshot item = event.getItemStack();
        CommentedConfigurationNode node = hnode.getNode("items", item.getType().getId());
        if (!node.isVirtual()) {
            if (node.getNode("deny-use").getBoolean()) {
                event.setCancelled(true);
            }
        }
    }
}
