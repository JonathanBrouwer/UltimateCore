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

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface TeleportService {
    /**
     * Create a new teleportation, to teleport the entities to a target.
     * The consumer will be run when the teleport has completed.
     *
     * @param source   The commandsource to send error messages to, can be null
     * @param entities The entities to teleport
     * @param target   The target to teleport the entities to
     * @param complete The consumer to be run when the teleport is completed
     * @param safe     Whether the teleportation should be safe
     * @param instant  Whether the teleportation should be instant or can be delayed by teleport delays, etc
     * @return The teleportation
     */
    Teleportation createTeleportation(@Nullable CommandSource source, List<? extends Entity> entities, Transform<World> target, Consumer<Teleportation> complete, BiConsumer<Teleportation, String> cancel, boolean safe, boolean instant);

    /**
     * Create a new teleportation, to teleport the entities to a target.
     * The consumer will be run when the teleport has completed.
     *
     * @param source   The commandsource to send error messages to, can be null
     * @param entities The entities to teleport
     * @param target   The target to teleport the entities to
     * @param complete The consumer to be run when the teleport is complete
     * @param safe     Whether the teleportation should be safe
     * @param instant  Whether the teleportation should be instant or can be delayed by teleport delays, etc
     * @return The teleportation
     */
    Teleportation createTeleportation(@Nullable CommandSource source, List<? extends Entity> entities, Supplier<Transform<World>> target, Consumer<Teleportation> complete, BiConsumer<Teleportation, String> cancel, boolean safe, boolean instant);

    /**
     * Returns a list of all unfinished teleportations.
     *
     * @return A list of all unfinished teleportations.
     */
    List<Teleportation> getUnfinishedTeleportations();

    /**
     * Register a handler.
     * This will be run when the teleportation has started.
     *
     * @param consumer The consumer which will be run
     */
    void addHandler(Consumer<Teleportation> consumer);

    /**
     * Get a list of all registered handlers.
     *
     * @return A list of all registered handlers
     */
    List<Consumer<Teleportation>> getHandlers();

    /**
     * Unregister a handler.
     *
     * @param handler The handler to unregister
     */
    void removeHandler(Consumer<Teleportation> handler);
}
