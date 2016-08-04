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
package bammerbom.ultimatecore.sponge.modules.gamemode.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class GamemodePermissions {
    public static Permission UC_GAMEMODE = Permission.create("uc.gamemode", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change your own gamemode"));
    public static Permission UC_GAMEMODE_SURVIVAL = Permission.create("uc.gamemode.survival", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change your own " +
            "gamemode to survival"));
    public static Permission UC_GAMEMODE_CREATIVE = Permission.create("uc.gamemode.creative", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change your own " +
            "gamemode to creative"));
    public static Permission UC_GAMEMODE_ADVENTURE = Permission.create("uc.gamemode.adventure", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change your own " +
            "gamemode to adventure"));
    public static Permission UC_GAMEMODE_SPECTATOR = Permission.create("uc.gamemode.spectator", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change your own " +
            "gamemode to spectator"));
    public static Permission UC_GAMEMODE_OTHERS = Permission.create("uc.gamemode.others", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change a player's gamemode"));
    public static Permission UC_GAMEMODE_OTHERS_SURVIVAL = Permission.create("uc.gamemode.others.survival", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change a"
            + " player's gamemode to survival"));
    public static Permission UC_GAMEMODE_OTHERS_CREATIVE = Permission.create("uc.gamemode.others.creative", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change a"
            + " player's gamemode to creative"));
    public static Permission UC_GAMEMODE_OTHERS_ADVENTURE = Permission.create("uc.gamemode.others.adventure", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change"
            + " a player's gamemode to adventure"));
    public static Permission UC_GAMEMODE_OTHERS_SPECTATOR = Permission.create("uc.gamemode.others.spectator", "gamemode", PermissionLevel.ADMIN, "gamemode", Text.of("Allows you to change"
            + " a player's gamemode to spectator"));
}
