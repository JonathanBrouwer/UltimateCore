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
package bammerbom.ultimatecore.sponge.modules.deaf.runnables;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.deaf.api.Deaf;
import bammerbom.ultimatecore.sponge.modules.deaf.api.DeafKeys;
import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class DeafTickRunnable implements Runnable {
    @Override
    public void run() {
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            if (up.get(DeafKeys.DEAF).isPresent()) {
                Deaf deaf = up.get(DeafKeys.DEAF).get();
                if (System.currentTimeMillis() > deaf.getEndtime() && deaf.getEndtime() != -1L) {
                    up.offer(DeafKeys.DEAF, null);
                    p.sendMessage(Messages.getFormatted(p, "deaf.undeafed"));
                }
            }
        }
    }
}
