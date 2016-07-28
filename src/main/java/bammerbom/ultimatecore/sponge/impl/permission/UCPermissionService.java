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
package bammerbom.ultimatecore.sponge.impl.permission;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UCPermissionService implements PermissionService {

    List<Permission> permissions = new ArrayList<>();

    /**
     * Get a list of all registered permissions.
     *
     * @return The list
     */
    @Override
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Get a {@link Permission} from the provided id.
     *
     * @param id The id to search for
     * @return The permission, or {@link Optional#empty()} when not found.
     */
    @Override
    public Optional<Permission> get(String id) {
        for (Permission perm : permissions) {
            if (perm.get().equalsIgnoreCase(id)) {
                return Optional.of(perm);
            }
        }
        return Optional.empty();
    }

    /**
     * Register a new permission
     *
     * @param perm The permission to register
     */
    @Override
    public void register(Permission perm) {
        permissions.add(perm);
    }

    /**
     * Unregister a certain permission
     *
     * @param perm The permission to unregister
     * @return Whether the permission was unregister successfully
     */
    @Override
    public boolean unregister(Permission perm) {
        return permissions.remove(perm);
    }

    /**
     * Search for a certain permission, and unregister it.
     * <p>
     * This is the same as calling:
     * unregister(get(id).get())
     *
     * @param id
     * @return Whether the permission was found and unregistered successfully
     */
    @Override
    public boolean unregister(String id) {
        Optional<Permission> perm = get(id);
        if (!perm.isPresent()) return false;
        return unregister(perm.get());
    }
}
