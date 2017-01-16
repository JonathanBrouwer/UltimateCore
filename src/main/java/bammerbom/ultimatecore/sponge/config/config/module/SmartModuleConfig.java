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
package bammerbom.ultimatecore.sponge.config.config.module;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.config.config.RawConfig;
import bammerbom.ultimatecore.sponge.config.config.SmartConfig;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.utils.Messages;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.io.File;
import java.nio.file.Path;

@ConfigSerializable
public class SmartModuleConfig implements ModuleConfig, SmartConfig, RawConfig {

    protected ModuleConfig rawConfig;
    protected String module;
    protected Path path;
    protected TypeToken token;

    protected SmartModuleConfig(String module, TypeToken<? extends SmartModuleConfig> token) {
        this.module = module;
        this.path = new File(UltimateCore.get().getConfigFolder().toFile().getPath() + "/modules/", module + ".conf").toPath();
        this.token = token;
    }

    @Override
    public void reload() {
        try {
            File file = path.toFile();
            if (!file.exists()) {
                file.createNewFile();
                HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setFile(file).build();
                CommentedConfigurationNode node = loader.load();
                node.setValue(token, this);
                loader.save(node);
            }
            if (rawConfig == null) {
                rawConfig = new RawModuleConfig(module);
            }
            rawConfig.reload();
        } catch (Exception e) {
            Messages.log(Messages.getFormatted("core.config.malformedfile", "%conf%", "modules/" + module + ".conf"));
            ErrorLogger.log(e, "Failed to load module config for " + module);
        }
    }

    @Override
    public String getModule() {
        return module;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public File getFile() {
        return path.toFile();
    }

    @Override
    public CommentedConfigurationNode get() {
        return rawConfig.get();
    }

    @Override
    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return rawConfig.getLoader();
    }

    @Override
    public boolean save(CommentedConfigurationNode node) {
        return rawConfig.save(node);
    }
}
