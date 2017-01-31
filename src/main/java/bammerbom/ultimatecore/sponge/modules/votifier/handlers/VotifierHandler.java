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
package bammerbom.ultimatecore.sponge.modules.votifier.handlers;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.votifier.VotifierModule;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierKeys;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierScheme;
import com.google.common.reflect.TypeToken;
import com.vexsoftware.votifier.model.Vote;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

public class VotifierHandler {
    public static boolean handle(Vote vote, boolean cache) {
        //Check player
        Player t = Sponge.getServer().getPlayer(vote.getUsername()).orElse(null);
        if (t == null) {
            if (cache) {
                //Cache vote because player is offline
                List<Vote> cached = GlobalData.get(VotifierKeys.VOTES_CACHED).get();
                cached.add(vote);
                GlobalData.offer(VotifierKeys.VOTES_CACHED, cached);
            }
            return false;
        }

        //Check world
        ModuleConfig config = Modules.VOTIFIER.get().getConfig().get();
        try {
            List<String> bannedWorlds = config.get().getNode("blocked-worlds").getList(TypeToken.of(String.class));
            if (bannedWorlds.contains(t.getWorld().getName()) || bannedWorlds.contains(t.getWorld().getUniqueId().toString())) {
                if (cache) {
                    //Cache vote because player is in blocked world
                    List<Vote> cached = GlobalData.get(VotifierKeys.VOTES_CACHED).get();
                    cached.add(vote);
                    GlobalData.offer(VotifierKeys.VOTES_CACHED, cached);
                }
                return false;
            }
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        //Increase vote count
        UltimateUser ut = UltimateCore.get().getUserService().getUser(t);
        int votes = ut.get(VotifierKeys.VOTES_COUNT).get();
        votes++;
        ut.offer(VotifierKeys.VOTES_COUNT, votes);

        //Handle schemes
        VotifierModule module = (VotifierModule) Modules.VOTIFIER.get();
        for (VotifierScheme scheme : module.getSchemes()) {
            scheme.accept(vote, t);
        }
        if (module.getCumulativeSchemes().containsKey(votes)) {
            module.getCumulativeSchemes().get(votes).accept(vote, t);
        }

        return true;
    }
}
