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
package bammerbom.ultimatecore.sponge.modules.afk;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.ModuleConfig;
import bammerbom.ultimatecore.sponge.modules.afk.commands.AfkCommand;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkDetectionListener;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkSwitchListener;
import bammerbom.ultimatecore.sponge.modules.afk.runnable.AfkTitleTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;

import java.util.Optional;

public class AfkModule implements Module {

    //TODO is back after x time
    //TODO "user may not respond" with chat/pm to user

    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "afk";
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
        config = new ModuleConfig("afk");
        //Commands
        UltimateCore.get().getCommandService().register(new AfkCommand());
        //Listeners
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new AfkSwitchListener());
        AfkDetectionListener.start();
        //Runnables
        if (config.get().getNode("title", "enabled").getBoolean(true)) {
            Sponge.getScheduler().createTaskBuilder().intervalTicks(config.get().getNode("title", "subtitle-refresh").getLong()).name("UC afk title task").execute(new AfkTitleTask())
                    .submit(UltimateCore.get());

        }
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}