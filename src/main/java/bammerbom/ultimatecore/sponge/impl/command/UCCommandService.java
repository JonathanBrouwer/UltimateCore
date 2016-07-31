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
package bammerbom.ultimatecore.sponge.impl.command;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.Command;
import bammerbom.ultimatecore.sponge.api.command.CommandService;
import bammerbom.ultimatecore.sponge.config.CommandsConfig;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UCCommandService implements CommandService {
    private List<Command> commands = new ArrayList<>();

    /**
     * Get a list of all registered {@link Command}s.
     *
     * @return The list of all commands.
     */
    @Override
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Register a new {@link Command}.
     * This also registers it to the sponge command manager.
     *
     * @param command The command to register
     * @return Whether the command was successfully registered
     */
    @Override
    public boolean register(Command command) {
        if (!CommandsConfig.get().getNode("commands", command.getIdentifier(), "enabled").getBoolean(true)) {
            return false;
        }
        commands.add(command);
        Sponge.getCommandManager().register(UltimateCore.get(), new UCCommandCallable(command), command.getAliases());
        return true;
    }

    /**
     * Unregisters the given {@link Command}.
     * This also unregisters it from the sponge command manager.
     *
     * @param command The {@link Command} to unregister
     * @return Whether the command was found
     */
    @Override
    public boolean unregister(Command command) {
        return commands.remove(command);
    }

    /**
     * Unregisters the given {@link Command}.
     * This also unregisters it from the sponge service.
     * <p>
     * This is the same as calling unregister(get(id).get())
     *
     * @param id The {@link Command} to unregister
     * @return Whether the command was found
     */
    @Override
    public boolean unregister(String id) {
        Optional<Command> cmd = get(id);
        if (!cmd.isPresent()) return false;
        return unregister(cmd.get());
    }

    /**
     * Search a command by the provided identifier.
     * This will search for the id first, and then for an alias.
     *
     * @param id The id to search for
     * @return The command, or {@link Optional#empty()} if no results are found
     */
    @Override
    public Optional<Command> get(String id) {
        List<Command> matches = commands.stream().filter(cmd -> cmd.getIdentifier().equalsIgnoreCase(id)).collect(Collectors.toList());
        if (matches.isEmpty()) {
            matches = commands.stream().filter(cmd -> cmd.getAliases().contains(id)).collect(Collectors.toList());
        }
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }
}
