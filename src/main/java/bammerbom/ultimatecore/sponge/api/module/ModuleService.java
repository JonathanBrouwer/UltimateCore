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
package bammerbom.ultimatecore.sponge.api.module;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface ModuleService {

    /**
     * Retrieves list of all registered modules
     *
     * @return the list
     */
    List<Module> getRegisteredModules();

    /**
     * Retrieves a specific module by id
     *
     * @param id the id to search for
     * @return the module, or Optional.empty() if not found
     */
    Optional<Module> getModule(String id);

    /**
     * Registers a new module
     *
     * @param module
     * @return
     */
    boolean registerModule(Module module);

    /**
     * Unregisters a module
     *
     * @param id The identifier of the module
     * @return Whether the module was found
     */
    boolean unregisterModule(String id);

    /**
     * Unregisters a module
     *
     * @param module The instance of the module
     * @return Whether the module was found
     */
    boolean unregisterModule(Module module);

    /**
     * This loads module, registers it at the ModuleService and calls the onRegister() method for the module.
     *
     * @param file The .jar file for the module
     * @return The module, or Optional.empty() if not found
     */
    Optional<Module> load(File file);
}
