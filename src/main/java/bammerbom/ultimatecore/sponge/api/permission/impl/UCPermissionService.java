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
package bammerbom.ultimatecore.sponge.api.permission.impl;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import bammerbom.ultimatecore.sponge.api.permission.PermissionService;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UCPermissionService implements PermissionService {

    List<Permission> permissions = new ArrayList<>();
    List<PermissionOption> permissionoptions = new ArrayList<>();

    @Override
    public List<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public Optional<Permission> get(String id) {
        for (Permission perm : permissions) {
            if (perm.get().equalsIgnoreCase(id)) {
                return Optional.of(perm);
            }
        }
        return Optional.empty();
    }

    @Override
    public void register(Permission perm) {
        permissions.add(perm);
        org.spongepowered.api.service.permission.PermissionService service = Sponge.getServiceManager().provide(org.spongepowered.api.service.permission.PermissionService.class).get();
        service.newDescriptionBuilder(UltimateCore.get()).get().id(perm.get()).description(perm.getDescription()).register();
    }

    @Override
    public boolean unregister(Permission perm) {
        return permissions.remove(perm);
    }

    @Override
    public boolean unregister(String id) {
        Optional<Permission> perm = get(id);
        if (!perm.isPresent()) return false;
        return unregister(perm.get());
    }

    @Override
    public List<PermissionOption> getPermissionOptions() {
        return permissionoptions;
    }

    @Override
    public Optional<PermissionOption> getOption(String id) {
        for (PermissionOption perm : permissionoptions) {
            if (perm.get().equalsIgnoreCase(id)) {
                return Optional.of(perm);
            }
        }
        return Optional.empty();
    }

    @Override
    public void registerOption(PermissionOption perm) {
        permissionoptions.add(perm);
        //TODO do I have to register it somewhere in sponge?
    }

    @Override
    public boolean unregisterOption(PermissionOption perm) {
        return permissionoptions.remove(perm);
    }

    @Override
    public boolean unregisterOption(String id) {
        Optional<PermissionOption> perm = getOption(id);
        if (!perm.isPresent()) return false;
        return unregisterOption(perm.get());
    }
}
