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
package bammerbom.ultimatecore.sponge.modules.kit.api;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data_old.Key;
import bammerbom.ultimatecore.sponge.api.data_old.providers.KeyProvider;
import bammerbom.ultimatecore.sponge.api.data_old.providers.UserKeyProvider;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KitKeys {
    //    public static Key.Global<List<Kit>> KITS = new Key.Global<>("kits", new CustomKeyProvider<>(() -> Modules.KIT.get().getConfig().get(), "kits", new TypeToken<List<Kit>>() {
//    }, new ArrayList<>()));
    public static Key.Global<List<Kit>> KITS = new Key.Global<>("kits", new KeyProvider.Global<List<Kit>>() {
        @Override
        public List<Kit> load(Game arg) {
            List<Kit> kits = new ArrayList<>();
            CommentedConfigurationNode node = Modules.KIT.get().getConfig().get().get();
            for (CommentedConfigurationNode wnode : node.getNode("kits").getChildrenMap().values()) {
                try {
                    kits.add(wnode.getValue(TypeToken.of(Kit.class)));
                } catch (ObjectMappingException e) {
                    Messages.log(Messages.getFormatted("kit.command.kit.invalidkit", "%kit%", wnode.getNode("name").getString()));
                }
            }
            return kits;
        }

        @Override
        public void save(Game arg, List<Kit> kits) {
            ModuleConfig loader = Modules.KIT.get().getConfig().get();
            CommentedConfigurationNode node = loader.get();
            node.getNode("kits").getChildrenMap().keySet().forEach(node.getNode("kits")::removeChild);
            for (Kit kit : kits) {
                try {
                    node.getNode("kits", kit.getId()).setValue(TypeToken.of(Kit.class), kit);
                } catch (ObjectMappingException e) {
                    ErrorLogger.log(e, "Failed to save kits key");
                }
            }
            loader.save(node);
        }
    });

    public static Key.User<HashMap<String, Long>> KIT_LASTUSED = new Key.User<>("kitlastused", new UserKeyProvider<>("kitlastused", new TypeToken<HashMap<String, Long>>() {
    }, new HashMap<>()));
}
