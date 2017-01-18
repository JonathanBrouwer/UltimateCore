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
import bammerbom.ultimatecore.sponge.api.module.Module;
import bammerbom.ultimatecore.sponge.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.config.config.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VoteSerializer;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierScheme;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierSchemeSerializer;
import bammerbom.ultimatecore.sponge.modules.votifier.listeners.VotifierListener;
import bammerbom.ultimatecore.sponge.modules.votifier.runnables.VotifierTickRunnable;
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import com.google.common.reflect.TypeToken;
import com.vexsoftware.votifier.model.Vote;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class VotifierModule implements Module {
    ModuleConfig config = null;

    List<VotifierScheme> schemes = new ArrayList<>();
    HashMap<Integer, VotifierScheme> cumulativeSchemes = new HashMap<>();

    @Override
    public String getIdentifier() {
        return "votifier";
    }

    @Override
    public Text getDescription() {
        return Text.of();
    }

    @Override
    public Optional<? extends ModuleConfig> getConfig() {
        return Optional.ofNullable(config);
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
        VotifierSchemeSerializer schemeSerializer = new VotifierSchemeSerializer();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(VotifierScheme.class), schemeSerializer);
        config = new RawModuleConfig("votifier");

        try {
            schemes = config.get().getNode("schemes").getList(TypeToken.of(VotifierScheme.class));
            config.get().getNode("cumulative-schemes").getChildrenMap().forEach((number, node) -> {
                if (!ArgumentUtil.isInteger(number.toString())) {
                    return;
                }
                Integer num = Integer.parseInt(number.toString());
                try {
                    VotifierScheme scheme = schemeSerializer.deserialize(TypeToken.of(VotifierScheme.class), node);
                    cumulativeSchemes.put(num, scheme);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            });
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReload(@Nullable GameReloadEvent event) {
        try {
            VotifierSchemeSerializer schemeSerializer = new VotifierSchemeSerializer();
            schemes = config.get().getNode("schemes").getList(TypeToken.of(VotifierScheme.class));
            config.get().getNode("cumulative-schemes").getChildrenMap().forEach((number, node) -> {
                if (!ArgumentUtil.isInteger(number.toString())) {
                    return;
                }
                Integer num = Integer.parseInt(number.toString());
                try {
                    VotifierScheme scheme = schemeSerializer.deserialize(TypeToken.of(VotifierScheme.class), node);
                    cumulativeSchemes.put(num, scheme);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            });
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    public List<VotifierScheme> getSchemes() {
        return schemes;
    }

    public HashMap<Integer, VotifierScheme> getCumulativeSchemes() {
        return cumulativeSchemes;
    }
}
