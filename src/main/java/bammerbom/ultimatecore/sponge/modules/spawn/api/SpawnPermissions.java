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
package bammerbom.ultimatecore.sponge.modules.spawn.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import org.spongepowered.api.text.Text;

public class SpawnPermissions {
    //Spawn self
    public static Permission UC_SPAWN_SPAWN_BASE = Permission.create("uc.spawn.spawn.base", "spawn", PermissionLevel.EVERYONE, "spawn", Text.of("Allows you to use the spawn command."));
    public static Permission UC_SPAWN_GLOBALSPAWN_BASE = Permission.create("uc.spawn.globalspawn.base", "spawn", PermissionLevel.ADMIN, "globalspawn", Text.of("Allows you to use the globalspawn " + "command."));
    public static Permission UC_SPAWN_GROUPSPAWN_BASE = Permission.create("uc.spawn.groupspawn.base", "spawn", PermissionLevel.ADMIN, "groupspawn", Text.of("Allows you to use the groupspawn command."));
    public static Permission UC_SPAWN_GROUPSPAWN_GROUP = Permission.create("uc.spawn.groupspawn.group.<Group>", "spawn", PermissionLevel.ADMIN, "groupspawn", Text.of("Allows you to use the groupspawn command for a certain group."));
    public static Permission UC_SPAWN_FIRSTSPAWN_BASE = Permission.create("uc.spawn.firstspawn.base", "spawn", PermissionLevel.ADMIN, "firstspawn", Text.of("Allows you to use the firstspawn command."));

    //Spawn others
    public static Permission UC_SPAWN_SPAWN_OTHERS = Permission.create("uc.spawn.spawn.others", "spawn", PermissionLevel.ADMIN, "spawn", Text.of("Allows you to use the spawn command for other players."));
    public static Permission UC_SPAWN_GLOBALSPAWN_OTHERS = Permission.create("uc.spawn.globalspawn.others", "spawn", PermissionLevel.ADMIN, "globalspawn", Text.of("Allows you to use the globalspawn command for other players."));
    public static Permission UC_SPAWN_GROUPSPAWN_OTHERS_BASE = Permission.create("uc.spawn.groupspawn.others.base", "spawn", PermissionLevel.ADMIN, "groupspawn", Text.of("Allows you to use the groupspawn command for other players."));
    public static Permission UC_SPAWN_GROUPSPAWN_OTHERS_GROUP = Permission.create("uc.spawn.groupspawn.others.group.<Group>", "spawn", PermissionLevel.ADMIN, "groupspawn", Text.of("Allows you to use the groupspawn command for a certain group for other players."));
    public static Permission UC_SPAWN_FIRSTSPAWN_OTHERS = Permission.create("uc.spawn.firstspawn.others", "spawn", PermissionLevel.ADMIN, "firstspawn", Text.of("Allows you to use the firstspawn command for other players."));

    //Setspawn
    public static Permission UC_SPAWN_SETGLOBALSPAWN_BASE = Permission.create("uc.spawn.setglobalspawn.base", "spawn", PermissionLevel.ADMIN, "setglobalspawn", Text.of("Allows you to set the globalspawn command."));
    public static Permission UC_SPAWN_SETGROUPSPAWN_BASE = Permission.create("uc.spawn.setgroupspawn.base", "spawn", PermissionLevel.ADMIN, "setgroupspawn", Text.of("Allows you to set the groupspawn command."));
    public static Permission UC_SPAWN_SETFIRSTSPAWN_BASE = Permission.create("uc.spawn.setfirstspawn.base", "spawn", PermissionLevel.ADMIN, "setfirstspawn", Text.of("Allows you to set the firstspawn command."));

    //Delspawn
    public static Permission UC_SPAWN_DELGLOBALSPAWN_BASE = Permission.create("uc.spawn.delglobalspawn.base", "spawn", PermissionLevel.ADMIN, "delglobalspawn", Text.of("Allows you to delete the globalspawn command."));
    public static Permission UC_SPAWN_DELGROUPSPAWN_BASE = Permission.create("uc.spawn.delgroupspawn.base", "spawn", PermissionLevel.ADMIN, "delgroupspawn", Text.of("Allows you to delete the groupspawn command."));
    public static Permission UC_SPAWN_DELFIRSTSPAWN_BASE = Permission.create("uc.spawn.delfirstspawn.base", "spawn", PermissionLevel.ADMIN, "delfirstspawn", Text.of("Allows you to delete the firstspawn command."));

    //Groupspawn option
    public static PermissionOption UC_SPAWN_GROUPSPAWN = PermissionOption.create("uc.spawn.groupspawn", "spawn", "spawn", null, Text.of("The name of the group spawn the player should spawn at."));
}
