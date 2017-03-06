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
package bammerbom.ultimatecore.sponge.modules.nick.api;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.PlayerDataFile;
import bammerbom.ultimatecore.sponge.api.data_old.Key;
import bammerbom.ultimatecore.sponge.api.data_old.providers.KeyProvider;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.tablist.TablistModule;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class NickKeys {
    //Don't convert to UserKeyProvider because of custom checks!
    public static Key.User<Text> NICKNAME = new Key.User<>("nick", new KeyProvider.User<Text>() {
        @Override
        public Text load(UltimateUser user) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            Text name = Messages.toText(node.getNode("nick").getString());
            if (name == null) return null;
            if (!name.toPlain().matches("[A-Za-z0-9]+")) {
                //Please don't crash the server
                return null;
            }
            return name;
        }

        @Override
        public void save(UltimateUser user, Text data) {
            PlayerDataFile config = new PlayerDataFile(user.getIdentifier());
            CommentedConfigurationNode node = config.get();
            node.getNode("nick").setValue(data != null ? TextSerializers.JSON.serialize(data) : null);
            config.save(node);
            if (Modules.TABLIST.isPresent()) {
                TablistModule tab = (TablistModule) Modules.TABLIST.get();
                tab.getRunnable().removeCache(user.getIdentifier());
            }
        }
    });
}
