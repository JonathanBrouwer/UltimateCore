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
package bammerbom.ultimatecore.sponge.modules.fly.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.fly.api.FlyKeys;
import bammerbom.ultimatecore.sponge.modules.fly.api.FlyPermissions;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class FlyListeners {
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player p = event.getTargetEntity();
        UltimateUser user = UltimateCore.get().getUserService().getUser(p);
        if (p.hasPermission(FlyPermissions.UC_FLY_FLY_BASE.get()) && user.get(FlyKeys.FLY).orElse(false)) {
            if (p.getLocation().add(0, -1, 0).getBlockType().equals(BlockTypes.AIR)) {
                p.offer(Keys.CAN_FLY, true);
                p.offer(Keys.IS_FLYING, true);
            }
        }
    }

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect event) {
        UltimateUser user = UltimateCore.get().getUserService().getUser(event.getTargetEntity());
        user.offer(FlyKeys.FLY, event.getTargetEntity().get(Keys.CAN_FLY).orElse(false));
    }
}
