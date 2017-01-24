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
package bammerbom.ultimatecore.sponge.api.teleport.impl;

import bammerbom.ultimatecore.sponge.api.teleport.TeleportService;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UCTeleportService implements TeleportService {
    private ArrayList<Teleportation> requests = new ArrayList<>();
    private ArrayList<Consumer<Teleportation>> handlers = new ArrayList<>();

    @Override
    public Teleportation createTeleportation(@Nullable CommandSource source, List<Entity> entities, Transform<World> target, Consumer<Teleportation> complete, BiConsumer<Teleportation, String> cancel, boolean safe, boolean instant) {
        return createTeleportation(source, entities, () -> target, complete, cancel, safe, instant);
    }

    @Override
    public Teleportation createTeleportation(@Nullable CommandSource source, List<Entity> entities, Supplier<Transform<World>> target, Consumer<Teleportation> complete, BiConsumer<Teleportation, String> cancel, boolean safe, boolean instant) {
        Teleportation request = new UCTeleportation(source, entities, target, complete, cancel, safe, instant);
        requests.add(request);
        return request;
    }

    @Override
    public List<Teleportation> getUnfinishedTeleportations() {
        return requests;
    }

    @Override
    public void addHandler(Consumer<Teleportation> consumer) {
        handlers.add(consumer);
    }

    @Override
    public List<Consumer<Teleportation>> getHandlers() {
        return handlers;
    }

    @Override
    public void removeHandler(Consumer<Teleportation> handler) {
        handlers.remove(handler);
    }
}
