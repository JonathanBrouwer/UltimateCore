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
package bammerbom.ultimatecore.sponge.modules.blacklist;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.modules.blacklist.listeners.BlockListener;
import bammerbom.ultimatecore.sponge.modules.blacklist.listeners.ItemListener;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class BlacklistModule implements Module {
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "blacklist";
    }

    @Override
    public Text getDescription() {
        return Text.of("Ban items from being used by players.");
    }

    @Override
    public Optional<ModuleConfig> getConfig() {
        return Optional.of(config);
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onInit(GameInitializationEvent event) {
        config = new RawModuleConfig("blacklist");

        //Generate config
        CommentedConfigurationNode node = config.get();
        for (ItemType type : Sponge.getRegistry().getAllOf(CatalogTypes.ITEM_TYPE)) {
            if (!node.getNode("items", type.getId()).isVirtual()) {
                continue;
            }
            node.getNode("items", type.getId(), "deny-use").setComment("Deny right-clicking with the item in your hand.");
            node.getNode("items", type.getId(), "deny-use").setValue(false);
            node.getNode("items", type.getId(), "deny-possession").setComment("Deny having the item in your inventory.");
            node.getNode("items", type.getId(), "deny-possession").setValue(false);
            node.getNode("items", type.getId(), "deny-drop").setComment("Deny dropping the item.");
            node.getNode("items", type.getId(), "deny-drop").setValue(false);
            node.getNode("items", type.getId(), "deny-pickup").setComment("Deny picking up the item.");
            node.getNode("items", type.getId(), "deny-pickup").setValue(false);

            node.getNode("items", type.getId(), "replace").setComment("If true, replaces the item with the item in 'replace-with'.");
            node.getNode("items", type.getId(), "replace").setValue(false);
            node.getNode("items", type.getId(), "replace-with").setComment("If 'replace-with' is true, replaces the item with this item.");
            node.getNode("items", type.getId(), "replace-with").setValue("minecraft:dirt");

        }
        for (BlockType type : Sponge.getRegistry().getAllOf(CatalogTypes.BLOCK_TYPE)) {
            if (!node.getNode("blocks", type.getId()).isVirtual()) {
                continue;
            }
            node.getNode("blocks", type.getId(), "deny-place").setComment("Deny placing the block on the ground.");
            node.getNode("blocks", type.getId(), "deny-place").setValue(false);
            node.getNode("blocks", type.getId(), "deny-break").setComment("Deny breaking the block.");
            node.getNode("blocks", type.getId(), "deny-break").setValue(false);

//            node.getNode("blocks", type.getId(), "replace").setComment("If true, replaces the block with the block in 'replace-with'.");
//            node.getNode("blocks", type.getId(), "replace").setValue(false);
//            node.getNode("blocks", type.getId(), "replace-with").setComment("If 'replace-with' is true, replaces the block with this block.");
//            node.getNode("blocks", type.getId(), "replace-with").setValue("minecraft:dirt");
        }
        config.save(node);

        Sponge.getEventManager().registerListeners(UltimateCore.get(), new BlockListener());
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new ItemListener());
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
