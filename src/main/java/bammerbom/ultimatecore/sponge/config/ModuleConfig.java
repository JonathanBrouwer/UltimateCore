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
package bammerbom.ultimatecore.sponge.config;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.config.datafiles.DataFile;
import bammerbom.ultimatecore.sponge.config.serializers.BlockStateSerializer;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.block.BlockState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class ModuleConfig implements DataFile {

    private String module;
    private Path path;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode node;

    public ModuleConfig(String id) {
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

            //Fix for bad blockstate deserialization
            BlockStateSerializer blockStateSerializer = new BlockStateSerializer();
            TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();
            serializers.registerType(TypeToken.of(BlockState.class), blockStateSerializer);
            ConfigurationOptions options = ConfigurationOptions.defaults().setSerializers(serializers);

            loader = HoconConfigurationLoader.builder().setPath(path).setDefaultOptions(options).build();
            node = loader.load();
        } catch (IOException e) {
            Messages.log(Messages.getFormatted("core.config.malformedfile", "%conf%", "modules/" + module + ".conf"));
            e.printStackTrace();
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
}
