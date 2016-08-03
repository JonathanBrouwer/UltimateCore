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
package bammerbom.ultimatecore.sponge.api.event.data;

import bammerbom.ultimatecore.sponge.api.data.Key;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An event where a {@link Key} and it's value is involved.
 */
public class DataEvent<C> implements Event {
    protected Key<C> key;
    protected C orgvalue;
    protected C value;
    protected Cause cause;

    public DataEvent(Key<C> key, @Nullable C value, Cause cause) {
        this.key = key;
        this.orgvalue = value;
        this.value = value;
        this.cause = cause;
    }

    public Key getKey() {
        return key;
    }

    public Optional<C> getOriginalValue() {
        return Optional.ofNullable(orgvalue);
    }

    public Optional<C> getValue() {
        return Optional.ofNullable(value);
    }

    /**
     * Set the value, this will not actually change the value associated with the key.
     *
     * @param value The value to set it to
     */
    public void setValue(C value) {
        this.value = value;
    }

    @Override
    public Cause getCause() {
        return cause;
    }
}
