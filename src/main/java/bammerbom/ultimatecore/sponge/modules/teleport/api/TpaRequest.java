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
package bammerbom.ultimatecore.sponge.modules.teleport.api;

import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

public class TpaRequest {
    UUID u1;
    UUID u2;
    Teleportation tel;

    public TpaRequest(Player asker, Player receiver, Teleportation tel) {
        this.u1 = asker.getUniqueId();
        this.u2 = receiver.getUniqueId();
        this.tel = tel;
    }

    public UUID getAskerUUID() {
        return u1;
    }

    public UUID getReceiverUUID() {
        return u2;
    }

    public Optional<Player> getAskerPlayer() {
        return Sponge.getServer().getPlayer(u1);
    }

    public Optional<Player> getReceiverPlayer() {
        return Sponge.getServer().getPlayer(u2);
    }

    public Teleportation getTeleportation() {
        return tel;
    }
}
