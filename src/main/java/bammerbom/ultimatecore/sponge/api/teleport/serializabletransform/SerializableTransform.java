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
package bammerbom.ultimatecore.sponge.api.teleport.serializabletransform;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SerializableTransform {

    public static TransformSerializer ts = new TransformSerializer();
    public static SerializableTransformSerializer sts = new SerializableTransformSerializer();

    @Nullable
    private Transform<World> transform = null;
    @Nonnull
    private ConfigurationNode node;

    public SerializableTransform(@Nonnull Transform<World> transform, @Nonnull ConfigurationNode node) {
        this.transform = transform;
        this.node = node;
    }

    public SerializableTransform(Transform<World> transform) {
        this.transform = transform;
        ConfigurationNode node = SimpleCommentedConfigurationNode.root();
        try {
            ts.serialize(TypeToken.of(Transform.class), transform, node);
        } catch (ObjectMappingException e) {
            //This should never happen
            e.printStackTrace();
        }
        this.node = node;
    }

    public SerializableTransform(ConfigurationNode node) {
        try {
            this.node = node;
            this.transform = ts.deserialize(TypeToken.of(SerializableTransform.class), node);
        } catch (ObjectMappingException ignored) {
        }
    }

    public boolean isPresent() {
        return this.transform != null;
    }

    public Transform<World> get() {
        return Optional.ofNullable(this.transform).get();
    }

    public ConfigurationNode getNode() {
        return this.node;
    }

    public void setTransform(Transform<World> transform) {
        this.transform = transform;
    }

    public Optional<Transform<World>> toOptional() {
        return Optional.ofNullable(this.transform);
    }

    public Transform<World> refreshGet() {
        try {
            this.transform = ts.deserialize(TypeToken.of(SerializableTransform.class), this.node);
        } catch (ObjectMappingException ignored) {
        }
        return get();
    }
}
