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
package bammerbom.ultimatecore.sponge.api.event.sign;

import bammerbom.ultimatecore.sponge.api.sign.UCSign;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * A {@link bammerbom.ultimatecore.sponge.api.event.sign.SignEvent} is an event where a {@link UCSign} is involved.
 */
public class SignEvent implements Event, Cancellable {
    private Cause cause;
    private UCSign sign;
    private boolean cancelled = false;

    public SignEvent(UCSign sign, Cause cause) {
        this.sign = sign;
        this.cause = cause;
    }

    /**
     * Get the cause for the event.
     *
     * @return The last cause
     */
    @Override
    public Cause getCause() {
        return cause;
    }

    /**
     * Get the {@link UCSign} for the event
     *
     * @return The sign
     */
    public UCSign getSign() {
        return sign;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static class Locatable extends SignEvent {
        private Location<World> location;

        public Locatable(UCSign sign, Location<World> location, Cause cause) {
            super(sign, cause);
            this.location = location;
        }

        /**
         * Get the {@link Location} where the event happened
         */
        public Location<World> getLocation() {
            return location;
        }
    }
}