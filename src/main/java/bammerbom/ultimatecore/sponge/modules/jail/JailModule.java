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
package bammerbom.ultimatecore.sponge.modules.jail;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.jail.api.Jail;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailData;
import bammerbom.ultimatecore.sponge.modules.jail.api.JailPermissions;
import bammerbom.ultimatecore.sponge.modules.jail.commands.*;
import bammerbom.ultimatecore.sponge.modules.jail.listeners.JailListener;
import bammerbom.ultimatecore.sponge.modules.jail.runnables.JailTickRunnable;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class JailModule implements Module {
    //jail
    //unjail
    //setjail
    //deljail
    //jails
    //jailtp
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "jail";
    }

    @Override
    public Text getDescription() {
        return Text.of("Put players in a jail as a punishment for misbehaving.");
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
        config = new RawModuleConfig("jail");
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Jail.class), new Jail.JailSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(JailData.class), new JailData.JailDataSerializer());

        UltimateCore.get().getCommandService().register(new SetjailCommand());
        UltimateCore.get().getCommandService().register(new DeljailCommand());
        UltimateCore.get().getCommandService().register(new JaillistCommand());
        UltimateCore.get().getCommandService().register(new JailCommand());
        UltimateCore.get().getCommandService().register(new UnjailCommand());
        UltimateCore.get().getCommandService().register(new JailtpCommand());

        Sponge.getEventManager().registerListeners(UltimateCore.get(), new JailListener());
        UltimateCore.get().getTickService().addRunnable("jail", new JailTickRunnable());

        new JailPermissions();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
