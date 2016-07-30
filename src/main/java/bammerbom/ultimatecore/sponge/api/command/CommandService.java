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
package bammerbom.ultimatecore.sponge.api.command;

import java.util.List;
import java.util.Optional;

public interface CommandService {

    /**
     * Get a list of all registered {@link Command}s.
     *
     * @return The list of all commands.
     */
    List<Command> getCommands();

    /**
     * Search a command by the provided identifier.
     * This will search for the id first, and then for an alias.
     *
     * @param id The id to search for
     * @return The command, or {@link Optional#empty()} if no results are found
     */
    Optional<Command> get(String id);

    /**
     * Register a new {@link Command}.
     *
     * @param command The command to register
     * @return Whether the command was successfully registered
     */
    boolean register(Command command);

    /**
     * Unregisters the given {@link Command}.
     * This also unregisters it from the sponge service.
     *
     * @param command The {@link Command} to unregister
     * @return Whether the command was found
     */
    boolean unregister(Command command);

    /**
     * Unregisters the given {@link Command}.
     * This also unregisters it from the sponge service.
     * <p>
     * This is the same as calling unregister(get(id).get())
     *
     * @param id The {@link Command} to unregister
     * @return Whether the command was found
     */
    boolean unregister(String id);
}
