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
package bammerbom.ultimatecore.sponge.modules.teleport;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportPermissions;
import bammerbom.ultimatecore.sponge.modules.teleport.commands.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class TeleportModule implements Module {
    @Override
    public String getIdentifier() {
        return "teleport";
    }

    @Override
    public Text getDescription() {
        return Text.of("A module with multiple teleport command.");
    }

    @Override
    public Optional<ModuleConfig> getConfig() {
        return Optional.empty();
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onInit(GameInitializationEvent event) {
        Sponge.getEventManager().registerListeners(UltimateCore.get(), this);
        UltimateCore.get().getCommandService().registerLater(new TeleportCommand(), TeleportPermissions::new);
        UltimateCore.get().getCommandService().register(new TeleportaskCommand());
        UltimateCore.get().getCommandService().register(new TeleportaskhereCommand());
        UltimateCore.get().getCommandService().register(new TeleportacceptCommand());
        UltimateCore.get().getCommandService().register(new TeleportdenyCommand());
        UltimateCore.get().getCommandService().register(new TeleporthereCommand());
        UltimateCore.get().getCommandService().register(new TeleportallCommand());
        UltimateCore.get().getCommandService().register(new TeleportaskallhereCommand());

        UltimateCore.get().getCommandService().register(new RandomTeleportCommand());
        UltimateCore.get().getCommandService().register(new BiomeTeleportCommand());
        UltimateCore.get().getCommandService().register(new TopCommand());

        new TeleportPermissions();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
