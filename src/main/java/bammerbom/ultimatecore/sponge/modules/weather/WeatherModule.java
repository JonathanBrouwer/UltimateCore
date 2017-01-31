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
package bammerbom.ultimatecore.sponge.modules.weather;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.weather.api.WeatherPermissions;
import bammerbom.ultimatecore.sponge.modules.weather.commands.RainCommand;
import bammerbom.ultimatecore.sponge.modules.weather.commands.SunCommand;
import bammerbom.ultimatecore.sponge.modules.weather.commands.ThunderCommand;
import bammerbom.ultimatecore.sponge.modules.weather.commands.WeatherCommand;
import bammerbom.ultimatecore.sponge.modules.weather.listeners.WeatherListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class WeatherModule implements Module {
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "weather";
    }

    @Override
    public Text getDescription() {
        return Text.of("Change the minecraft world's weather, or disable it.");
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
        //Config
        config = new RawModuleConfig("weather");
        //Commands
        UltimateCore.get().getCommandService().register(new WeatherCommand());
        UltimateCore.get().getCommandService().register(new SunCommand());
        UltimateCore.get().getCommandService().register(new RainCommand());
        UltimateCore.get().getCommandService().register(new ThunderCommand());
        //Listeners
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new WeatherListener());
        //Register permissions
        new WeatherPermissions();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
