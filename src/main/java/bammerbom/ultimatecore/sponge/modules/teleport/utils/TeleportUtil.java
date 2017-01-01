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
package bammerbom.ultimatecore.sponge.modules.teleport.utils;

import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TeleportKeys;
import bammerbom.ultimatecore.sponge.modules.teleport.api.TpaRequest;
import bammerbom.ultimatecore.sponge.utils.Selector;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class TeleportUtil {
    public static Optional<TpaRequest> getTpaRequestFor(Player p, @Nullable String arg) {
        HashMap<UUID, TpaRequest> requests = GlobalData.get(TeleportKeys.TELEPORT_ASK_REQUESTS).get();
        HashMap<UUID, TpaRequest> requests2 = GlobalData.get(TeleportKeys.TELEPORT_ASKHERE_REQUESTS).get();

        if (arg != null) {
            //Teleport id entered
            try {

                UUID uuid = UUID.fromString(arg);
                if (requests.containsKey(uuid) && requests.get(uuid).getAskerUUID() == p.getUniqueId()) {
                    return Optional.of(requests.get(uuid));
                }
                if (requests2.containsKey(uuid) && requests2.get(uuid).getAskerUUID() == p.getUniqueId()) {
                    return Optional.of(requests2.get(uuid));
                }
            } catch (Exception ignore) {
            }
        }

        //Player entered
        Player t = arg != null ? Selector.one(p, arg).orElse(null) : null;

        //Select most recent request
        TpaRequest request = null;
        Long time = 0L;
        for (TpaRequest req : requests.values()) {
            if (t == null || (p.getUniqueId() == req.getReceiverUUID() && t.getUniqueId() == req.getAskerUUID())) {
                if (req.getTeleportation().getCreationTime() > time) {
                    request = req;
                    time = req.getTeleportation().getCreationTime();
                }
            }
        }
        for (TpaRequest req : requests2.values()) {
            if (t == null || (p.getUniqueId() == req.getReceiverUUID() && t.getUniqueId() == req.getAskerUUID())) {
                if (req.getTeleportation().getCreationTime() > time) {
                    request = req;
                    time = req.getTeleportation().getCreationTime();
                }
            }
        }
        return Optional.ofNullable(request);
    }
}
