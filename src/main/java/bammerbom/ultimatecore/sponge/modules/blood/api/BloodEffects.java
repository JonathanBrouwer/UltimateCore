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
package bammerbom.ultimatecore.sponge.modules.blood.api;

import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.EntityType;

import java.util.HashMap;
import java.util.Optional;

public class BloodEffects {
    public static BloodEffect DEFAULT = new BloodEffect(true, BlockState.builder().blockType(BlockTypes.STAINED_HARDENED_CLAY).add(Keys.DYE_COLOR, DyeColors.RED).build(), new Vector3d(0.0, 1.0, 0.0), new Vector3d(0.3, 0.3, 0.3), 50);
    public static HashMap<EntityType, BloodEffect> effects = new HashMap<>();

    public static void reload() {
        effects.clear();
        ModuleConfig config = Modules.BLOOD.get().getConfig().get();
        for (EntityType type : Sponge.getRegistry().getAllOf(CatalogTypes.ENTITY_TYPE)) {
            CommentedConfigurationNode node = config.get().getNode("types", type.getId());
            try {
                BloodEffect effect = node.getValue(TypeToken.of(BloodEffect.class));
                if (effect == null) {
                    continue;
                }
                effects.put(type, effect);
            } catch (ObjectMappingException e) {
                ErrorLogger.log(e, "Failed to deserialize bloodeffect for " + type.getId() + " (" + e.getMessage() + ")");
            }
        }
    }

    public static Optional<BloodEffect> get(EntityType type) {
        return Optional.ofNullable(effects.get(type));
    }
}
