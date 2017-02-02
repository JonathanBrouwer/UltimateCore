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
package bammerbom.ultimatecore.sponge.api.config.config.module;

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

public class RawModuleConfig implements ModuleConfig, DataFile {

    protected String module;
    protected Path path;
    protected ConfigurationLoader<CommentedConfigurationNode> loader;
    protected CommentedConfigurationNode node;

    public RawModuleConfig(String id) {
        this.module = id;
        this.path = new File(UltimateCore.get().getConfigFolder().toFile().getPath() + "/modules/", module + ".conf").toPath();
        reload();
    }

    public void reload() {
        try {
            File file = path.toFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Optional<Asset> asset = Sponge.getAssetManager().getAsset(UltimateCore.get(), "config/modules/" + module + ".conf");
                if (!asset.isPresent()) {
                    Messages.log(Messages.getFormatted("core.config.invalidjar", "%conf%", "modules/" + module + ".conf"));
                    return;
                }
                asset.get().copyToFile(path);
            }
            loader = HoconConfigurationLoader.builder().setPath(path).build();
            node = loader.load();
        } catch (IOException e) {
            Messages.log(Messages.getFormatted("core.config.malformedfile", "%conf%", "modules/" + module + ".conf"));
            ErrorLogger.log(e, "Failed to load module config for " + module);
        }
    }

    public Path getPath() {
        return path;
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
            getLoader().save(node);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getModule() {
        return module;
    }
}
