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
package bammerbom.ultimatecore.sponge.modules.automessage;

import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.automessage.api.Automessage;
import bammerbom.ultimatecore.sponge.modules.automessage.api.AutomessageSerializer;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class AutomessageModule implements Module {
    //TODO run command with automessage
    //TODO per player variables
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "automessage";
    }

    @Override
    public Text getDescription() {
        return Text.of("A lot of automessage functionality, including chat, bossbar, actionbar, title and random messages.");
    }

    @Override
    public Optional<ModuleConfig> getConfig() {
        return Optional.of(config);
    }

    @Override
    public void onInit(GameInitializationEvent event) {
        //Config
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Automessage.class), new AutomessageSerializer());
        config = new RawModuleConfig("automessage");
        //Runnables
        try {
            for (Automessage message : config.get().getNode("automessages").getList(TypeToken.of(Automessage.class))) {
                message.start();
            }
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to load automessages from config.");
        }
    }

    @Override
    public void onReload(GameReloadEvent event) {
        //Runnables
        try {
            for (Automessage message : config.get().getNode("automessages").getList(TypeToken.of(Automessage.class))) {
                message.start();
            }
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to load automessages from config.");
        }
    }
}
