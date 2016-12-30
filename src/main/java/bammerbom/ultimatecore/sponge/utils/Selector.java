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
package bammerbom.ultimatecore.sponge.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.selector.Selector;
import org.spongepowered.api.world.Locatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Selector {
    /**
     * Returns a list of all entities which satisfy the string.
     * This can be empty if no entities are found.
     *
     * @param s The string to search for
     * @return All entities which statisfy the string
     */
    public static List<Entity> multipleEntities(CommandSource source, String s) {
        List<Entity> matches = new ArrayList<>();

        Optional<Player> match = Sponge.getServer().getPlayer(s);
        match.ifPresent(matches::add);

        try {
            UUID uuid = UUID.fromString(s);
            if (source instanceof Locatable) {
                Locatable loc = (Locatable) source;
                Optional<Entity> en = loc.getWorld().getEntity(uuid);
                en.ifPresent(matches::add);
            }
        } catch (IllegalArgumentException ignored) {
        }

        if (s.startsWith("@")) {
            org.spongepowered.api.text.selector.Selector selector = org.spongepowered.api.text.selector.Selector.parse(s);
            selector.resolve(source).forEach(matches::add);
        }

        return matches;
    }

    /**
     * Returns a list of all players which satisfy the string.
     * This can be empty if no players are found.
     *
     * @param s The string to search for
     * @return All players which statisfy the string
     */
    public static List<Player> multiple(CommandSource source, String s) {
        List<Player> matches = new ArrayList<>();
        multipleEntities(source, s).forEach(en -> {
            if (en instanceof Player) {
                matches.add((Player) en);
            }
        });
        return matches;
    }

    /**
     * Returns the entity which most satisfies the string.
     *
     * @param s The string to search for
     * @return The entity which most satisfies the string
     */
    public static Optional<Entity> oneEntity(CommandSource source, String s) {
        List<Entity> entities = multipleEntities(source, s);
        return entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0));
    }

    /**
     * Returns the player which most satisfies the string.
     *
     * @param s The string to search for
     * @return The player which most satisfies the string
     */
    public static Optional<Player> one(CommandSource source, String s) {
        List<Player> players = multiple(source, s);
        return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
    }
}
