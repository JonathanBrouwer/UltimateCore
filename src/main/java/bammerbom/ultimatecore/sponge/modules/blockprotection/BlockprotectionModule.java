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
package bammerbom.ultimatecore.sponge.modules.blockprotection;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.HighModule;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleIgnore;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleInfo;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.locktype.LockTypeRegistry;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;

import java.util.Optional;
import java.util.Set;

@ModuleIgnore
@ModuleInfo(name = "blockprotection", description = "Allows you to claim a block.")
public class BlockprotectionModule implements HighModule {

    LockTypeRegistry lockTypeRegistry = new LockTypeRegistry();
    ModuleConfig config;

    Set<BlockState> allowedSet;
    Set<BlockState> autolockSet;

    @Override
    public Optional<ModuleConfig> getConfig() {
        return Optional.of(this.config);
    }

    @Override
    public void onInit(GameInitializationEvent event) {
//        lockTypeRegistry.registerDefaults();
//        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Protection.class), new Protection.ProtectionSerializer());
//        config = new RawModuleConfig("blockprotection");
//        onReload(null);
//
//        UltimateCore.get().getCommandService().register(new LockCommand());
    }

    @Override
    public void onReload(GameReloadEvent event) {
//        try {
//            Set<BlockState> allowed = new HashSet<>();
//            for(String rawbs : config.get().getNode("allowed-blocks", "list").getList(TypeToken.of(String.class))){
//                try {
//                    BlockState bs = Sponge.getRegistry().getType(BlockState.class, rawbs).get();
//                    allowed.add(bs);
//                }catch (Exception ex){
//                    Messages.log("Invalid blockstate in allowed blocks of blockprotection: " + rawbs);
//                }
//            }
//            this.allowedSet = allowed;
//
//            Set<BlockState> autolock = new HashSet<>();
//            for(String rawbs : config.get().getNode("autolock-blocks", "list").getList(TypeToken.of(String.class))){
//                try {
//                    BlockState bs = Sponge.getRegistry().getType(BlockState.class, rawbs).get();
//                    allowed.add(bs);
//                }catch (Exception ex){
//                    Messages.log("Invalid blockstate in allowed blocks of blockprotection: " + rawbs);
//                }
//            }
//            this.autolockSet = autolock;
//        } catch (ObjectMappingException e) {
//            e.printStackTrace();
//        }
    }

    public Set<BlockState> getAllowedSet() {
        return this.allowedSet;
    }

    public Set<BlockState> getAutolockSet() {
        return this.autolockSet;
    }

    public LockTypeRegistry getLockTypeRegistry() {
        return this.lockTypeRegistry;
    }
}
