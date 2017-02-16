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
package bammerbom.ultimatecore.sponge.modules.poke.listeners;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundCategories;
import org.spongepowered.api.effect.sound.SoundCategory;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;

public class PokeListener {
    @Listener
    public void onChat(MessageChannelEvent.Chat event) {
        String message = event.getRawMessage().toPlain();
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (message.contains(p.getName())) {
                ModuleConfig config = Modules.POKE.get().getConfig().get();
                CommentedConfigurationNode node = config.get();
                String soundname = node.getNode("sound", "sound").getString();
                String categoryname = node.getNode("sound", "category").getString();
                Double volume = node.getNode("sound", "volume").getDouble();
                Double pitch = node.getNode("sound", "pitch").getDouble();
                Double minVolume = node.getNode("sound", "minVolume").getDouble();
                SoundType type = Sponge.getRegistry().getType(CatalogTypes.SOUND_TYPE, soundname.toUpperCase()).get();
                //TODO wait for CatalogTypes.SOUND_CATEGORY to be added
                SoundCategory category;
                try {
                    category = Sponge.getRegistry().getType(CatalogTypes.SOUND_CATEGORY, categoryname.toUpperCase()).get();
                } catch (Exception ex) {
                    category = SoundCategories.PLAYER;
                }
                p.playSound(type, category, p.getLocation().getPosition(), volume, pitch, minVolume);
            }
        }
    }
}
