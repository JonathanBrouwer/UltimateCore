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
package bammerbom.ultimatecore.sponge.modules.teleport.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class TeleportPermissions {
    public static Permission UC_TELEPORT = Permission.create("uc.teleport", "teleport", PermissionLevel.ADMIN, "teleport", Text.of("Allows you to use the teleport command to teleport " +
            "yourself to someone else."));
    public static Permission UC_TELEPORT_OTHERS = Permission.create("uc.teleport.others", "teleport", PermissionLevel.ADMIN, "teleport", Text.of("Allows you to use the teleport command "
            + "to teleport someone else to someone else."));
    public static Permission UC_TELEPORT_COORDINATES = Permission.create("uc.teleport.coordinates", "teleport", PermissionLevel.ADMIN, "teleport", Text.of("Allows you to use the teleport"
            + " command to teleport yourself to certain coordinates."));
    public static Permission UC_TELEPORT_COORDINATES_OTHERS = Permission.create("uc.teleport.coordinates.others", "teleport", PermissionLevel.ADMIN, "teleport", Text.of("Allows you to " +
            "use the teleport command to teleport someone else to certain coordinates."));

    public static Permission UC_TELEPORTASK = Permission.create("uc.teleportask", "teleport", PermissionLevel.EVERYONE, "teleportask", Text.of("Allows you to use the teleportask command"));
    public static Permission UC_TELEPORTACCEPT = Permission.create("uc.teleportaccept", "teleport", PermissionLevel.EVERYONE, "teleportaccept", Text.of("Allows you to use the " +
            "teleportaccept command"));
    public static Permission UC_TELEPORTDENY = Permission.create("uc.teleportdeny", "teleport", PermissionLevel.EVERYONE, "teleportdeny", Text.of("Allows you to use the teleportdeny " +
            "command"));
}
