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
package bammerbom.ultimatecore.sponge.api.teleport;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface TeleportService {
    /**
     * Create a new teleport request, to teleport the entities to a target.
     * The consumer will be run when the teleport has completed.
     *
     * @param entities The entities to teleport
     * @param target   The target to teleport the entities to
     * @param complete The consumer to be run when the teleport is completed
     * @return The teleport request
     */
    TeleportRequest createTeleportRequest(List<Entity> entities, Transform<World> target, Consumer<TeleportRequest> complete, Consumer<TeleportRequest> cancel);

    /**
     * Create a new teleport request, to teleport the entities to a target.
     * The consumer will be run when the teleport has completed.
     *
     * @param entities The entities to teleport
     * @param target   The target to teleport the entities to
     * @param complete The consumer to be run when the teleport is completed
     * @return The teleport request
     */
    TeleportRequest createTeleportRequest(List<Entity> entities, Supplier<Transform<World>> target, Consumer<TeleportRequest> complete, Consumer<TeleportRequest> cancel);

    /**
     * Returns a list of all unfinished teleport requests.
     *
     * @return A list of all unfinished teleport requests.
     */
    List<TeleportRequest> getUnfinishedTeleportRequests();

    /**
     * Register a handler.
     * This will be run when the teleport request has started.
     *
     * @param consumer The consumer which will be run
     */
    void addHandler(Consumer<TeleportRequest> consumer);

    /**
     * Get a list of all registered handlers.
     *
     * @return A list of all registered handlers
     */
    List<Consumer<TeleportRequest>> getHandlers();

    /**
     * Unregister a handler.
     *
     * @param handler The handler to unregister
     */
    void removeHandler(Consumer<TeleportRequest> handler);
}
