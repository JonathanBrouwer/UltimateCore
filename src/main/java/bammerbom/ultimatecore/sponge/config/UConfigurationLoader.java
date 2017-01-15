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
package bammerbom.ultimatecore.sponge.config;

import bammerbom.ultimatecore.sponge.config.serializers.BlockStateSerializer;
import bammerbom.ultimatecore.sponge.config.serializers.ItemStackSnapshotSerializer;
import bammerbom.ultimatecore.sponge.config.serializers.TransformSerializer;
import bammerbom.ultimatecore.sponge.config.serializers.Vector3dSerializer;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

public class UConfigurationLoader {
    public static HoconConfigurationLoader.Builder newHocon() {
        HoconConfigurationLoader.Builder loader = HoconConfigurationLoader.builder();
        ConfigurationOptions options = ConfigurationOptions.defaults();

        //Serializers
        TypeSerializerCollection serializers = TypeSerializers.getDefaultSerializers().newChild();
        serializers.registerType(TypeToken.of(BlockState.class), new BlockStateSerializer());
        serializers.registerType(TypeToken.of(ItemStackSnapshot.class), new ItemStackSnapshotSerializer());
        serializers.registerType(TypeToken.of(Transform.class), new TransformSerializer());
        serializers.registerType(TypeToken.of(Vector3d.class), new Vector3dSerializer());
        options.setSerializers(serializers);

        return loader.setDefaultOptions(options);
    }
}
