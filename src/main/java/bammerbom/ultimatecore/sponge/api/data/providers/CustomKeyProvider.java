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

//TODO fix this class

//package bammerbom.ultimatecore.sponge.api.data.providers;
//
//import bammerbom.ultimatecore.sponge.api.config.config.RawConfig;
//import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
//import com.google.common.reflect.TypeToken;
//import ninja.leaping.configurate.commented.CommentedConfigurationNode;
//import ninja.leaping.configurate.objectmapping.ObjectMappingException;
//import org.spongepowered.api.Game;
//
//import java.util.function.Supplier;
//
//public class CustomKeyProvider<E> implements KeyProvider.Global<E> {
//    private Supplier<RawConfig> configs;
//    private String key;
//    private TypeToken<E> token;
//    private E def = null;
//
//    public CustomKeyProvider(Supplier<RawConfig> config, String key, TypeToken<E> token) {
//        this.configs = config;
//        this.key = key;
//        this.token = token;
//    }
//
//    public CustomKeyProvider(Supplier<RawConfig> config, String key, TypeToken<E> token, E def) {
//        this.configs = config;
//        this.key = key;
//        this.token = token;
//        this.def = def;
//    }
//
//    @Override
//    public E load(Game game) {
//        RawConfig config = configs.get();
//        CommentedConfigurationNode node = config.get();
//        try {
//            return node.getNode(key).getValue(token, def);
//        } catch (ObjectMappingException e) {
//            ErrorLogger.log(e, "Failed to save " + key + " key for global");
//            return def;
//        }
//    }
//
//    @Override
//    public void save(Game game, E data) {
//        RawConfig config = configs.get();
//        CommentedConfigurationNode node = config.get();
//        try {
//            node.getNode(key).setValue(token, data);
//        } catch (ObjectMappingException e) {
//            ErrorLogger.log(e, "Failed to save " + key + " key for global");
//        }
//        config.save(node);
//    }
//}
