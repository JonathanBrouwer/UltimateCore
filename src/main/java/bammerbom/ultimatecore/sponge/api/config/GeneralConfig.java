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
package bammerbom.ultimatecore.sponge.api.config;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.datafiles.DataFile;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class GeneralConfig implements DataFile {
    private static Path path = new File(UltimateCore.get().getConfigFolder().toFile(), "general.conf").toPath();
    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static CommentedConfigurationNode node;

    public void reload() {
        try {
            File file = path.toFile();
            Optional<Asset> asset = Sponge.getAssetManager().getAsset(UltimateCore.get(), "config/general.conf");
            if (!asset.isPresent()) {
                Messages.log(Messages.getFormatted("core.config.invalidjar", "%conf%", "general.conf"));
                return;
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                asset.get().copyToFile(path);
            }

            loader = HoconConfigurationLoader.builder().setPath(path).build();
            node = loader.load();

            //Complete
            ConfigCompleter.complete(this, asset.get());
        } catch (IOException e) {
            Messages.log(Messages.getFormatted("core.config.malformedfile", "%conf%", "general.conf"));
            ErrorLogger.log(e, "Failed to load general config file. (Malformed?)");
        }
    }

    @Override
    public File getFile() {
        return path.toFile();
    }

    @Override
    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return loader;
    }

    @Override
    public CommentedConfigurationNode get() {
        return node;
    }

    @Override
    public boolean save(CommentedConfigurationNode node) {
        try {
            loader.save(node);
            return true;
        } catch (IOException e) {
            ErrorLogger.log(e, "Failed to save general config file.");
            return false;
        }
    }
}
