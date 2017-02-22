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
package bammerbom.ultimatecore.sponge.modules.votifier;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.HighModule;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleDisableByDefault;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleInfo;
import bammerbom.ultimatecore.sponge.api.variable.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VoteSerializer;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierScheme;
import bammerbom.ultimatecore.sponge.modules.votifier.listeners.VotifierListener;
import bammerbom.ultimatecore.sponge.modules.votifier.runnables.VotifierTickRunnable;
import com.google.common.reflect.TypeToken;
import com.vexsoftware.votifier.model.Vote;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ModuleDisableByDefault
@ModuleInfo(name = "votifier", description = "Give players rewards for voting.")
public class VotifierModule implements HighModule {
    ModuleConfig config = null;

    List<VotifierScheme> schemes = new ArrayList<>();
    HashMap<Integer, VotifierScheme> cumulativeSchemes = new HashMap<>();

    @Override
    public Optional<? extends ModuleConfig> getConfig() {
        return Optional.ofNullable(this.config);
    }

    @Override
    public void onInit(GameInitializationEvent event) {
        if (!Sponge.getPluginManager().getPlugin("nuvotifier").isPresent()) {
            return;
        }
        Sponge.getEventManager().registerListeners(UltimateCore.get(), new VotifierListener());
        UltimateCore.get().getTickService().addRunnable("votifier", new VotifierTickRunnable());

        //Config
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(Vote.class), new VoteSerializer());
        this.config = new RawModuleConfig("votifier");

        onReload(null);
    }

    @Override
    public void onReload(@Nullable GameReloadEvent event) {
        try {
            this.schemes = this.config.get().getNode("schemes").getList(TypeToken.of(VotifierScheme.class));
            this.config.get().getNode("cumulative-schemes").getChildrenMap().forEach((number, node) -> {
                if (!ArgumentUtil.isInteger(number.toString())) {
                    return;
                }
                Integer num = Integer.parseInt(number.toString());
                try {
                    VotifierScheme scheme = node.getValue(TypeToken.of(VotifierScheme.class));
                    this.cumulativeSchemes.put(num, scheme);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            });
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public List<VotifierScheme> getSchemes() {
        return this.schemes;
    }

    public HashMap<Integer, VotifierScheme> getCumulativeSchemes() {
        return this.cumulativeSchemes;
    }
}
