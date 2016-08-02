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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.automessage.config.AutomessageConfig;
import bammerbom.ultimatecore.sponge.modules.automessage.runnable.ActionbarRunnable;
import bammerbom.ultimatecore.sponge.modules.automessage.runnable.BossbarRunnable;
import bammerbom.ultimatecore.sponge.modules.automessage.runnable.ChatRunnable;
import bammerbom.ultimatecore.sponge.modules.automessage.runnable.TitleRunnable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AutomessageModule implements Module {
    //TODO random or top to bottom
    //TODO random with no repeat
    //TODO run command with automessage
    //TODO per player variables
    AutomessageConfig config;

    @Override
    public String getIdentifier() {
        return "automessage";
    }

    @Override
    public Optional<? extends Object> getApi() {
        return null;
    }

    @Override
    public Optional<AutomessageConfig> getConfig() {
        return Optional.of(config);
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onInit(GameInitializationEvent event) {
        //Config
        config = new AutomessageConfig();
        config.reload();
        //Runnables
        if (config.get().getNode("chat", "enable").getBoolean(false)) {
            long time = config.get().getNode("chat", "time").getLong();
            ChatRunnable run = new ChatRunnable();
            run.init();
            Sponge.getScheduler().createTaskBuilder().interval(time, TimeUnit.SECONDS).delay(time, TimeUnit.SECONDS).execute(run).name("UC chat automessage task").submit(UltimateCore.get());
        }
        if (config.get().getNode("actionbar", "enable").getBoolean(false)) {
            long time = config.get().getNode("actionbar", "time").getLong();
            ActionbarRunnable run = new ActionbarRunnable();
            run.init();
            Sponge.getScheduler().createTaskBuilder().interval(time, TimeUnit.SECONDS).delay(time, TimeUnit.SECONDS).execute(run).name("UC chat automessage task").submit(UltimateCore.get());
        }
        if (config.get().getNode("bossbar", "enable").getBoolean(false)) {
            long time = config.get().getNode("bossbar", "time").getLong();
            BossbarRunnable run = new BossbarRunnable();
            run.init();
            Sponge.getScheduler().createTaskBuilder().interval(time, TimeUnit.SECONDS).delay(time, TimeUnit.SECONDS).execute(run).name("UC chat automessage task").submit(UltimateCore.get());
        }
        if (config.get().getNode("title", "enable").getBoolean(false)) {
            long time = config.get().getNode("title", "time").getLong();
            TitleRunnable run = new TitleRunnable();
            Sponge.getScheduler().createTaskBuilder().interval(time, TimeUnit.SECONDS).delay(time, TimeUnit.SECONDS).execute(run).name("UC chat automessage task").submit(UltimateCore.get());
        }
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
