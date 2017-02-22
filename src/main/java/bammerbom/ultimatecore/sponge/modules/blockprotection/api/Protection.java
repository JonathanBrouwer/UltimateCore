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
package bammerbom.ultimatecore.sponge.modules.blockprotection.api;

import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.modules.blockprotection.BlockprotectionModule;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.locktype.LockType;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.locktype.LockTypeRegistry;
import bammerbom.ultimatecore.sponge.modules.blockprotection.api.locktype.ValueLockType;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class Protection {
    private Set<Location<World>> locations;
    private UUID owner;
    private List<UUID> players;
    private LockType locktype;

    public Protection(Set<Location<World>> locations, UUID owner, List<UUID> players, LockType locktype) {
        this.locations = locations;
        this.owner = owner;
        this.players = players;
        this.locktype = locktype;
    }

    public Set<Location<World>> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location<World>> locations) {
        this.locations = locations;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public LockType getLocktype() {
        return locktype;
    }

    public void setLocktype(LockType locktype) {
        this.locktype = locktype;
    }

    public static class ProtectionSerializer implements TypeSerializer<Protection> {
        @Override
        public Protection deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            LockTypeRegistry registry = ((BlockprotectionModule) Modules.BLOCKPROTECTION.get()).getLockTypeRegistry();

            HashSet<Location<World>> locations = new HashSet<>();
            locations.addAll(node.getNode("locations").getList(new TypeToken<Location<World>>() {
            }));
            UUID owner = UUID.fromString(node.getNode("owner").getString());
            List<UUID> players = node.getNode("players").getList(TypeToken.of(String.class)).stream().map(UUID::fromString).collect(Collectors.toList());
            LockType ltype = registry.getById(node.getNode("locktype").getString()).orElseThrow(() -> new ObjectMappingException("Invalid locktype"));
            if (ltype instanceof ValueLockType) {
                ((ValueLockType) ltype).setValue(node.getNode("locktype-value").getString());
            }

            return new Protection(locations, owner, players, ltype);
        }

        @Override
        public void serialize(TypeToken<?> type, Protection prot, ConfigurationNode node) throws ObjectMappingException {
            node.getNode("locations").setValue(new TypeToken<List<Location>>() {
            }, new ArrayList<>(prot.getLocations()));
            node.getNode("owner").setValue(prot.getOwner().toString());
            node.getNode("players").setValue(prot.getPlayers().stream().map(UUID::toString).collect(Collectors.toList()));
            node.getNode("locktype").setValue(prot.getLocktype().getId());
            if (prot.getLocktype() instanceof ValueLockType) {
                node.getNode("locktype-value", ((ValueLockType) prot.getLocktype()).getValue());
            }
        }
    }
}
