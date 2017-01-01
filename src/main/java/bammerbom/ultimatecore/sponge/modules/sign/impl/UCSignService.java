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
package bammerbom.ultimatecore.sponge.modules.sign.impl;


import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.event.sign.SignRegisterEvent;
import bammerbom.ultimatecore.sponge.api.event.sign.SignUnregisterEvent;
import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.sign.UCSign;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UCSignService implements SignService {
    Set<UCSign> signs = new HashSet<>();
    HashMap<String, PermissionTriset> perms = new HashMap<>();

    /**
     * Retrieves list of all registered signs
     *
     * @return the list
     */
    @Override
    public Set<UCSign> getRegisteredSigns() {
        return signs;
    }

    /**
     * Retrieves a specific sign by id
     *
     * @param id the id to search for
     * @return the sign, or Optional.empty() if not found
     */
    @Override
    public Optional<UCSign> getSign(String id) {
        for (UCSign sign : signs) {
            if (sign.getIdentifier().equalsIgnoreCase(id)) {
                return Optional.of(sign);
            }
        }
        return Optional.empty();
    }

    /**
     * Registers a new sign
     *
     * @param sign The sign to register
     * @return Whether the sign was successfully registered
     */
    @Override
    public boolean registerSign(UCSign sign) {
        SignRegisterEvent cevent = new SignRegisterEvent(sign, Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(cevent);
        if (cevent.isCancelled()) {
            return false;
        }
        signs.add(sign);
        Permission use = Permission.create("uc.sign." + sign.getIdentifier() + ".use", sign.getModule(), PermissionLevel.EVERYONE, null, Text.of("Permission to use " + sign.getIdentifier() + "signs" +
                "."));
        Permission create = Permission.create("uc.sign." + sign.getIdentifier() + ".create", sign.getModule(), PermissionLevel.ADMIN, null, Text.of("Permission to create " + sign.getIdentifier() +
                "signs."));
        Permission destroy = Permission.create("uc.sign." + sign.getIdentifier() + ".destroy", sign.getModule(), PermissionLevel.ADMIN, null, Text.of("Permission to destroy " + sign.getIdentifier()
                + "signs."));
        perms.put(sign.getIdentifier(), new PermissionTriset(use, create, destroy));
        return true;
    }

    /**
     * Unregisters a sign
     *
     * @param id The identifier of the sign
     * @return Whether the sign was found
     */
    @Override
    public boolean unregisterSign(String id) {
        return getSign(id).isPresent() ? unregisterSign(getSign(id).get()) : false;
    }

    /**
     * Unregisters a sign
     *
     * @param sign The instance of the sign
     * @return Whether the sign was found
     */
    @Override
    public boolean unregisterSign(UCSign sign) {
        SignUnregisterEvent cevent = new SignUnregisterEvent(sign, Cause.builder().owner(UltimateCore.get()).build());
        Sponge.getEventManager().post(cevent);
        if (cevent.isCancelled()) {
            return false;
        }
        perms.remove(sign.getIdentifier());
        return signs.remove(sign);
    }

    @Override
    public Permission getDefaultUsePermission(UCSign sign) {
        return perms.get(sign.getIdentifier()).getUse();
    }

    @Override
    public Permission getDefaultCreatePermission(UCSign sign) {
        return perms.get(sign.getIdentifier()).getCreate();
    }

    @Override
    public Permission getDefaultDestroyPermission(UCSign sign) {
        return perms.get(sign.getIdentifier()).getDestroy();
    }

    public static class PermissionTriset {
        Permission use;
        Permission create;
        Permission destroy;

        public PermissionTriset(Permission use, Permission create, Permission destroy) {
            this.use = use;
            this.create = create;
            this.destroy = destroy;
        }

        public Permission getCreate() {
            return create;
        }

        public Permission getUse() {
            return use;
        }

        public Permission getDestroy() {
            return destroy;
        }
    }
}
