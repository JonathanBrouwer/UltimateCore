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
package bammerbom.ultimatecore.sponge.modules.back;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.ModuleConfig;
import bammerbom.ultimatecore.sponge.modules.back.api.BackPermissions;
import bammerbom.ultimatecore.sponge.modules.back.commands.BackCommand;
import bammerbom.ultimatecore.sponge.modules.back.runnables.BackTeleportHandler;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;

import java.util.Optional;

public class BackModule implements Module {
    //TODO teleport back more than one time
    //TODO paginated list?
    @Override
    public String getIdentifier() {
        return "back";
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
        UltimateCore.get().getTeleportService().addHandler(new BackTeleportHandler());
        UltimateCore.get().getCommandService().register(new BackCommand());
        //Register permissions
        new BackPermissions();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
