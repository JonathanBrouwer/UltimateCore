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
package bammerbom.ultimatecore.sponge.modules.kick.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import org.spongepowered.api.text.Text;

public class KickPermissions {
    public static Permission UC_KICK_KICK_BASE = Permission.create("uc.kick.kick.base", "kick", PermissionLevel.MOD, "kick", Text.of("Allows you to use the kick command."));
    public static Permission UC_KICK_KICKALL_BASE = Permission.create("uc.kick.kickall.base", "kick", PermissionLevel.ADMIN, "kickall", Text.of("Allows you to use the kickall command."));

    public static PermissionOption UC_KICK_EXEMPTPOWER = PermissionOption.create("uc.kick.exemptpower", "kick", "kick", "0", Text.of("The amount of exemptpower the player has. If a player's kickpower is higher than or equal to the targets exemptpower he can kick the target."));
    public static PermissionOption UC_KICK_POWER = PermissionOption.create("uc.kick.power", "kick", "kick", "0", Text.of("The amount of kickpower the player has. If a player's kickpower is higher than or equal to the targets exemptpower he can kick the target."));
}
