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
package bammerbom.ultimatecore.sponge.modules.kit.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class KitPermissions {
    public static Permission UC_KIT_KIT_BASE = Permission.create("uc.kit.kit.base", "kit", PermissionLevel.EVERYONE, "kit", Text.of("Allows you to use the kit command."));
    public static Permission UC_KIT_KIT_KIT = Permission.create("uc.kit.kit.[KIT]", "kit", PermissionLevel.EVERYONE, "kit", Text.of("Allows you to use the kit command for a specific kit."));
    public static Permission UC_KIT_KITLIST_BASE = Permission.create("uc.kitlist.base", "kit", PermissionLevel.EVERYONE, "kit", Text.of("Allows you to see a list of all kits."));
    public static Permission UC_KIT_CREATEKIT_BASE = Permission.create("uc.kit.createkit.base", "kit", PermissionLevel.ADMIN, "createkit", Text.of("Allows you to use the createkit command."));
    public static Permission UC_KIT_REMOVEKIT_BASE = Permission.create("uc.kit.removekit.base", "kit", PermissionLevel.ADMIN, "removekit", Text.of("Allows you to use the removekit command."));
    public static Permission UC_KIT_COOLDOWN_EXEMPT = Permission.create("uc.kit.cooldown.exempt", "kit", PermissionLevel.ADMIN, "kit", Text.of("Allows you to bypass a kit's cooldown."));
}
