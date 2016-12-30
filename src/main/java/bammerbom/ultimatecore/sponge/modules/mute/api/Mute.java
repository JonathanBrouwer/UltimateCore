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

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class Mute {

    UUID muted;
    UUID muter;
    Long endtime;
    Long starttime;
    Text reason;

    public Mute(UUID muted, UUID muter, Long endtime, Long starttime, Text reason) {
        this.muted = muted;
        this.muter = muter;
        this.endtime = endtime;
        this.starttime = starttime;
        this.reason = reason;
    }

    public UUID getMuted() {
        return muted;
    }

    public UUID getMuter() {
        return muter;
    }

    public Long getEndtime() {
        return endtime;
    }

    public Long getStarttime() {
        return starttime;
    }

    public Text getReason() {
        return reason;
    }

    public static class MuteSerializer implements TypeSerializer<Mute> {
        @Override
        public Mute deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            UUID muted = node.getNode("muted").getValue(TypeToken.of(UUID.class));
            UUID muter = node.getNode("muter").getValue(TypeToken.of(UUID.class));
            Long endtime = node.getNode("endtime").getLong();
            Long starttime = node.getNode("starttime").getLong();
            Text reason = node.getNode("reason").getValue(TypeToken.of(Text.class));
            return new Mute(muted, muter, endtime, starttime, reason);
        }

        @Override
        public void serialize(TypeToken<?> type, Mute mute, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("muted").setValue(TypeToken.of(UUID.class), mute.getMuted());
            node.getNode("muter").setValue(TypeToken.of(UUID.class), mute.getMuter());
            node.getNode("endtime").setValue(mute.getEndtime());
            node.getNode("starttime").setValue(mute.getStarttime());
            node.getNode("reason").setValue(TypeToken.of(Text.class), mute.getReason());
        }
    }
}
