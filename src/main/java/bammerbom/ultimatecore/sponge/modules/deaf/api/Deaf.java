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
package bammerbom.ultimatecore.sponge.modules.deaf.api;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class Deaf {

    UUID deafd;
    UUID deafr;
    Long endtime;
    Long starttime;
    Text reason;

    public Deaf(UUID deafd, UUID deafr, Long endtime, Long starttime, Text reason) {
        this.deafd = deafd;
        this.deafr = deafr;
        this.endtime = endtime;
        this.starttime = starttime;
        this.reason = reason;
    }

    public UUID getDeafed() {
        return deafd;
    }

    public UUID getDeafer() {
        return deafr;
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

    public static class DeafSerializer implements TypeSerializer<Deaf> {
        @Override
        public Deaf deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            UUID deafd = node.getNode("deafd").getValue(TypeToken.of(UUID.class));
            UUID deafr = node.getNode("deafr").getValue(TypeToken.of(UUID.class));
            Long endtime = node.getNode("endtime").getLong();
            Long starttime = node.getNode("starttime").getLong();
            Text reason = node.getNode("reason").getValue(TypeToken.of(Text.class));
            return new Deaf(deafd, deafr, endtime, starttime, reason);
        }

        @Override
        public void serialize(TypeToken<?> type, Deaf deaf, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("deafd").setValue(TypeToken.of(UUID.class), deaf.getDeafed());
            node.getNode("deafr").setValue(TypeToken.of(UUID.class), deaf.getDeafer());
            node.getNode("endtime").setValue(deaf.getEndtime());
            node.getNode("starttime").setValue(deaf.getStarttime());
            node.getNode("reason").setValue(TypeToken.of(Text.class), deaf.getReason());
        }
    }
}
