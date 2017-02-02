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
package bammerbom.ultimatecore.sponge.modules.blood;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.modules.blood.api.BloodEffect;
import bammerbom.ultimatecore.sponge.modules.blood.api.BloodEffects;
import bammerbom.ultimatecore.sponge.modules.blood.listeners.BloodListener;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class BloodModule implements Module {
    ModuleConfig config;

    @Override
    public String getIdentifier() {
        return "blood";
    }

    @Override
    public Text getDescription() {
        return Text.of("Custom 'blood' particles when a player gets damaged");
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
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(BloodEffect.class), new BloodEffect.BloodEffectSerializer());
        config = new RawModuleConfig("blood");
        //Check if all entity types are in the config
        CommentedConfigurationNode node = config.get();
        boolean modified = false;
        //For every entitytype, if it doesnt exist in the config add it
        for (EntityType type : Sponge.getRegistry().getAllOf(CatalogTypes.ENTITY_TYPE)) {
            if (!Living.class.isAssignableFrom(type.getEntityClass())) {
                continue;
            }
            //If entitytype is not in config
            if (node.getNode("types", type.getId(), "enabled").getValue() == null) {
                modified = true;
                CommentedConfigurationNode typenode = node.getNode("types", type.getId());
                try {
                    typenode.setValue(TypeToken.of(BloodEffect.class), BloodEffects.DEFAULT);
                } catch (ObjectMappingException e) {
                    ErrorLogger.log(e, "Failed to set default blood effect.");
                }
            }
        }
        if (modified) {
            config.save(node);
        }
        //Load blood effects from config
        BloodEffects.reload();
        //Listeners
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new BloodListener());
    }

    @Override
    public void onPostInit(GamePostInitializationEvent event) {

    }

    @Override
    public void onStop(GameStoppingEvent event) {

    }
}
