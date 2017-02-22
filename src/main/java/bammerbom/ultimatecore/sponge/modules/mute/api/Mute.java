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
package bammerbom.ultimatecore.sponge.modules.mute.api;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;

import java.util.UUID;

@ConfigSerializable
public class Mute {

    @Setting
    UUID muted;
    @Setting
    UUID muter;
    @Setting
    Long endtime;
    @Setting
    Long starttime;
    @Setting
    Text reason;

    public Mute() {

    }

    public Mute(UUID muted, UUID muter, Long endtime, Long starttime, Text reason) {
        this.muted = muted;
        this.muter = muter;
        this.endtime = endtime;
        this.starttime = starttime;
        this.reason = reason;
    }

    public UUID getMuted() {
        return this.muted;
    }

    public UUID getMuter() {
        return this.muter;
    }

    public Long getEndtime() {
        return this.endtime;
    }

    public Long getStarttime() {
        return this.starttime;
    }

    public Text getReason() {
        return this.reason;
    }
}
