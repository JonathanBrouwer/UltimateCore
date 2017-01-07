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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.teleport.Teleportation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UCTeleportation implements Teleportation {

    //0 = stopped, 1 = executing, 2 = paused / waiting, 3 = cancelled, 4 = completed
    private int state = 0;
    private CommandSource source;
    private List<UUID> entities;
    private Supplier<Transform<World>> target;
    private BiConsumer<Teleportation, String> cancel;
    private Consumer<Teleportation> complete;
    private List<Consumer<Teleportation>> remainingHandlers;
    private boolean safe;
    private boolean instant;
    private Long createTime;

    public UCTeleportation(@Nullable CommandSource source, List<Entity> entities, Supplier<Transform<World>> target, Consumer<Teleportation> complete, BiConsumer<Teleportation, String> cancel, boolean safe, boolean instant) {
        this.source = source;
        this.entities = new ArrayList<>();
        entities.forEach(en -> this.entities.add(en.getUniqueId()));
        this.target = target;
        this.cancel = cancel;
        this.complete = complete;
        this.safe = safe;
        this.instant = instant;
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public void start() {
        if (state != 0 && state != 3) {
            throw new IllegalStateException();
        }
        state = 1;
        remainingHandlers = new ArrayList<>(UltimateCore.get().getTeleportService().getHandlers());
        new ArrayList<>(remainingHandlers).forEach(handler -> {
            handler.accept(this);
            remainingHandlers.remove(handler);
            if (state != 1) {
                return;
            }
        });
        complete();
    }

    @Override
    public void pause() {
        if (state != 1) {
            throw new IllegalStateException();
        }
        state = 2;
    }

    @Override
    public void resume() {
        if (state != 2) {
            throw new IllegalStateException();
        }
        state = 1;
        new ArrayList<>(remainingHandlers).forEach(handler -> {
            handler.accept(this);
            remainingHandlers.remove(handler);
            if (state != 1) {
                return;
            }
        });
        complete();
    }

    @Override
    public void cancel(String reason) {
        if (state >= 3) {
            throw new IllegalStateException();
        }
        state = 3;
        getCancelConsumer().accept(this, reason);
        UltimateCore.get().getTeleportService().getUnfinishedTeleportations().remove(this);
    }

    @Override
    public void complete() {
        state = 4;
        Transform<World> t = target.get();
        getEntities().forEach(en -> {
            if (safe) {
                en.setLocationSafely(t.getLocation());
            } else {
                en.setLocation(t.getLocation());
            }
            en.setRotation(t.getRotation());
            en.setScale(t.getScale());
        });
        getCompleteConsumer().accept(this);
        UltimateCore.get().getTeleportService().getUnfinishedTeleportations().remove(this);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.ofNullable(source);
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
    public Supplier<Transform<World>> getTarget() {
        return target;
    }

    @Override
    public Consumer<Teleportation> getCompleteConsumer() {
        return complete;
    }

    @Override
    public BiConsumer<Teleportation, String> getCancelConsumer() {
        return cancel;
    }

    @Override
    public boolean isSafe() {
        return safe;
    }

    @Override
    public boolean isInstant() {
        return instant;
    }

    @Override
    public Long getCreationTime() {
        return createTime;
    }
}
