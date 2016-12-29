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
import bammerbom.ultimatecore.sponge.api.teleport.TeleportRequest;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UCTeleportRequest implements TeleportRequest {

    //0 = stopped, 1 = executing, 2 = paused / waiting, 3 = cancelled, 4 = completed
    private int state = 0;
    private List<UUID> entities;
    private Supplier<Transform<World>> target;
    private Consumer<TeleportRequest> cancel;
    private Consumer<TeleportRequest> complete;
    private List<Consumer<TeleportRequest>> remainingHandlers;

    public UCTeleportRequest(List<Entity> entities, Supplier<Transform<World>> target, Consumer<TeleportRequest> complete, Consumer<TeleportRequest> cancel) {
        this.entities = new ArrayList<>();
        entities.forEach(en -> this.entities.add(en.getUniqueId()));
        this.target = target;
        this.cancel = cancel;
        this.complete = complete;
    }

    @Override
    public void start() {
        if (state != 0 && state != 3) {
            throw new IllegalStateException();
        }
        state = 1;
        remainingHandlers = UltimateCore.get().getTeleportService().getHandlers();
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
    public void cancel() {
        if (state >= 3) {
            throw new IllegalStateException();
        }
        state = 3;
        getCancelConsumer().accept(this);
    }

    @Override
    public void complete() {
        state = 4;
        Transform<World> t = target.get();
        getEntities().forEach(en -> {
            en.setLocationSafely(t.getLocation());
            en.setRotation(t.getRotation());
            en.setScale(t.getScale());
        });
        getCompleteConsumer().accept(this);
    }

    @Override
    public int getState() {
        return state;
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
    public Consumer<TeleportRequest> getCompleteConsumer() {
        return complete;
    }

    @Override
    public Consumer<TeleportRequest> getCancelConsumer() {
        return cancel;
    }
}
