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
package bammerbom.ultimatecore.sponge.modules.item;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.modules.item.api.ItemPermissions;
import bammerbom.ultimatecore.sponge.modules.item.commands.*;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ItemModule implements Module {

    @Override
    public String getIdentifier() {
        return "item";
    }

    @Override
    public Text getDescription() {
        return Text.of("Modify items in multiple ways.");
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
        UltimateCore.get().getCommandService().register(new MoreCommand());
        UltimateCore.get().getCommandService().register(new RepairCommand());

        UltimateCore.get().getCommandService().register(new ItemnameCommand());
        UltimateCore.get().getCommandService().register(new ItemloreCommand());
        UltimateCore.get().getCommandService().register(new ItemquantityCommand());
        UltimateCore.get().getCommandService().register(new ItemdurabilityCommand());
        UltimateCore.get().getCommandService().register(new ItemunbreakableCommand());
        UltimateCore.get().getCommandService().register(new ItemcanbreakCommand());
        UltimateCore.get().getCommandService().register(new ItemcanplaceonCommand());
        UltimateCore.get().getCommandService().registerLater(new ItemenchantCommand(), ItemPermissions::new);
        UltimateCore.get().getCommandService().register(new ItemhidetagsCommand());

        //GENERAL PROPERTIES
        //itemname
        //itemlore
        //itemquantity
        //itemdurability
        //itemunbreakable
        //itemcanplaceon
        //itemcanbreak
        //itemhidetags
        //(item)enchant

        //ATTRIBUTES
        //itemmaxhealth
        //itemdamage
        //itemspeed
        //itemknockbackresistance

        //ITEMS
        //skull
        //bookauthor
        //booktitle
        //bookedit
        //firework
        //leatherarmorcolor

        //TODO spawner?
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
