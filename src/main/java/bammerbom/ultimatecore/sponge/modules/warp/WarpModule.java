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
package bammerbom.ultimatecore.sponge.modules.warp;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.modules.warp.api.Warp;
import bammerbom.ultimatecore.sponge.modules.warp.api.WarpPermissions;
import bammerbom.ultimatecore.sponge.modules.warp.commands.DelwarpCommand;
import bammerbom.ultimatecore.sponge.modules.warp.commands.SetwarpCommand;
import bammerbom.ultimatecore.sponge.modules.warp.commands.WarpCommand;
import bammerbom.ultimatecore.sponge.modules.warp.commands.WarplistCommand;
import bammerbom.ultimatecore.sponge.modules.warp.signs.WarpSign;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class WarpModule implements Module {
    @Override
    public String getIdentifier() {
        return "warp";
    }

    @Override
    public Text getDescription() {
        return Text.of("Let the admin set certain locations where a player can teleport to.");
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
        //Config
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Warp.class), new Warp.WarpSerializer());
        //Commands
        UltimateCore.get().getCommandService().register(new WarpCommand());
        UltimateCore.get().getCommandService().register(new SetwarpCommand());
        UltimateCore.get().getCommandService().register(new DelwarpCommand());
        UltimateCore.get().getCommandService().register(new WarplistCommand());
        //Signs
        Optional<SignService> serv = UltimateCore.get().getSignService();
        if (serv.isPresent()) {
            serv.get().registerSign(new WarpSign());
        }
        //Register permissions
        new WarpPermissions();
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
