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
package bammerbom.ultimatecore.sponge.modules.jail.api;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;

import java.util.UUID;

/**
 * JailData is the class attached to a player when the player is jailed.
 * This contains information like who jailed him, for how long, etc.
 */
public class JailData {
    UUID jaild;
    UUID jailr;
    Long endtime;
    Long starttime;
    Text reason;
    String jail;

    public JailData(UUID jaild, UUID jailr, Long endtime, Long starttime, Text reason, String jail) {
        this.jaild = jaild;
        this.jailr = jailr;
        this.endtime = endtime;
        this.starttime = starttime;
        this.reason = reason;
        this.jail = jail;
    }

    public UUID getJailed() {
        return jaild;
    }

    public UUID getJailer() {
        return jailr;
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

    public String getJail() {
        return jail;
    }

    public static class JailDataSerializer implements TypeSerializer<JailData> {
        @Override
        public JailData deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            UUID jaild = node.getNode("jaild").getValue(TypeToken.of(UUID.class));
            UUID jailr = node.getNode("jailr").getValue(TypeToken.of(UUID.class));
            Long endtime = node.getNode("endtime").getLong();
            Long starttime = node.getNode("starttime").getLong();
            Text reason = node.getNode("reason").getValue(TypeToken.of(Text.class));
            String jail = node.getNode("jail").getString();
            return new JailData(jaild, jailr, endtime, starttime, reason, jail);
        }

        @Override
        public void serialize(TypeToken<?> type, JailData jail, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("jaild").setValue(TypeToken.of(UUID.class), jail.getJailed());
            node.getNode("jailr").setValue(TypeToken.of(UUID.class), jail.getJailer());
            node.getNode("endtime").setValue(jail.getEndtime());
            node.getNode("starttime").setValue(jail.getStarttime());
            node.getNode("reason").setValue(TypeToken.of(Text.class), jail.getReason());
            node.getNode("jail").setValue(jail.getJail());
        }
    }
}
