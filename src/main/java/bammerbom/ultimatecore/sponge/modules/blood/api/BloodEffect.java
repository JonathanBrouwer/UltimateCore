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

import bammerbom.ultimatecore.sponge.api.config.Serializers;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;

public class BloodEffect {

    boolean enabled;
    BlockState state;
    Vector3d c_offset;
    Vector3d p_offset;
    int count;

    public BloodEffect(boolean enabled, BlockState state, Vector3d c_offset, Vector3d p_offset, int count) {
        this.enabled = enabled;
        this.state = state;
        this.c_offset = c_offset;
        this.p_offset = p_offset;
        this.count = count;
    }

    public ParticleEffect getEffect() {
        ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.BLOCK_CRACK).option(ParticleOptions.BLOCK_STATE, state).option(ParticleOptions.QUANTITY, count).offset(p_offset).build();
        return particle;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BlockState getState() {
        return state;
    }

    public Vector3d getCenterOffset() {
        return c_offset;
    }

    public Vector3d getParticleOffset() {
        return p_offset;
    }

    public int getCount() {
        return count;
    }

    public static class BloodEffectSerializer implements TypeSerializer<BloodEffect> {

        @Override
        public BloodEffect deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            Vector3d coffset = node.getNode("center-offset").getValue(TypeToken.of(Vector3d.class));
            Vector3d poffset = node.getNode("particle-offset").getValue(TypeToken.of(Vector3d.class));
            BlockState state = Serializers.BLOCKSTATE.deserialize(TypeToken.of(BlockState.class), node.getNode("blockstate"));
            int count = node.getNode("count").getInt();
            boolean enabled = node.getNode("enabled").getBoolean();
            return new BloodEffect(enabled, state, coffset, poffset, count);
        }

        @Override
        public void serialize(TypeToken<?> type, BloodEffect ef, ConfigurationNode node) throws ObjectMappingException {
            Serializers.BLOCKSTATE.serialize(TypeToken.of(BlockState.class), ef.getState(), node.getNode("blockstate"));
            node.getNode("center-offset").setValue(TypeToken.of(Vector3d.class), ef.getCenterOffset());
            node.getNode("count").setValue(ef.getCount());
            node.getNode("enabled").setValue(ef.isEnabled());
            node.getNode("particle-offset").setValue(TypeToken.of(Vector3d.class), ef.getParticleOffset());
        }
    }
}
