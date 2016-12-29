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
package bammerbom.ultimatecore.sponge.impl.teleport;

import bammerbom.ultimatecore.sponge.api.teleport.TeleportRequest;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class UCTeleportRequest implements TeleportRequest {

    private List<UUID> entities;
    private Transform target;
    private Consumer<TeleportRequest> complete;

    public UCTeleportRequest(List<Entity> entities, Transform target, Consumer<TeleportRequest> complete) {
        this.entities = new ArrayList<>();
        entities.forEach(en -> this.entities.add(en.getUniqueId()));
        this.target = target;
        this.complete = complete;
    }

    @Override
    public void start() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> rtrn = new ArrayList<>();
        for (UUID uuid : entities) {
            for (World w : Sponge.getServer().getWorlds()) {
                w.getEntity(uuid).ifPresent(rtrn::add);
            }
        }
        return rtrn;
    }

    @Override
    public Transform getTarget() {
        return target;
    }

    @Override
    public Consumer<TeleportRequest> getCompleteConsumer() {
        return complete;
    }
}
