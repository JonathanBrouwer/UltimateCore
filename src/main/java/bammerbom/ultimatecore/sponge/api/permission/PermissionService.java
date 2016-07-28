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
package bammerbom.ultimatecore.sponge.api.permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {

    /**
     * Get a list of all registered permissions.
     *
     * @return The list
     */
    List<Permission> getPermissions();

    /**
     * Get a {@link Permission} from the provided id.
     *
     * @param id The id to search for
     * @return The permission, or {@link Optional#empty()} when not found.
     */
    Optional<Permission> get(String id);

    /**
     * Register a new permission
     *
     * @param perm The permission to register
     */
    void register(Permission perm);

    /**
     * Unregister a certain permission
     *
     * @param perm The permission to unregister
     * @return Whether the permission was unregister successfully
     */
    boolean unregister(Permission perm);

    /**
     * Search for a certain permission, and unregister it.
     * <p>
     * This is the same as calling:
     * unregister(get(id).get())
     *
     * @param id
     * @return Whether the permission was found and unregistered successfully
     */
    boolean unregister(String id);
}
