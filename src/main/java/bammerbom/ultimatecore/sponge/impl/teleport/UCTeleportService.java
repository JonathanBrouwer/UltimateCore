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
import bammerbom.ultimatecore.sponge.api.teleport.TeleportService;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UCTeleportService implements TeleportService {
    private ArrayList<TeleportRequest> requests = new ArrayList<>();
    private ArrayList<Consumer<TeleportRequest>> handlers = new ArrayList<>();

    @Override
    public TeleportRequest createTeleportRequest(@Nullable CommandSource source, List<Entity> entities, Transform<World> target, Consumer<TeleportRequest> complete,
                                                 Consumer<TeleportRequest> cancel) {
        return createTeleportRequest(source, entities, new Supplier<Transform<World>>() {
            @Override
            public Transform get() {
                return target;
            }
        }, complete, cancel);
    }

    @Override
    public TeleportRequest createTeleportRequest(@Nullable CommandSource source, List<Entity> entities, Supplier<Transform<World>> target, Consumer<TeleportRequest> complete,
                                                 Consumer<TeleportRequest> cancel) {
        TeleportRequest request = new UCTeleportRequest(source, entities, target, complete, cancel);
        requests.add(request);
        return request;
    }

    @Override
    public List<TeleportRequest> getUnfinishedTeleportRequests() {
        return requests;
    }

    @Override
    public void addHandler(Consumer<TeleportRequest> consumer) {
        handlers.add(consumer);
    }

    @Override
    public List<Consumer<TeleportRequest>> getHandlers() {
        return handlers;
    }

    @Override
    public void removeHandler(Consumer<TeleportRequest> handler) {
        handlers.remove(handler);
    }
}
