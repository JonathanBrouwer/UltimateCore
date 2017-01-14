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
package bammerbom.ultimatecore.sponge.modules.mute.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import org.spongepowered.api.text.Text;

public class MutePermissions {
    public static Permission UC_MUTE_MUTE_BASE = Permission.create("uc.mute.mute.base", "mute", PermissionLevel.MOD, "mute", Text.of("Allows you to mute someone."));
    public static Permission UC_MUTE_UNMUTE_BASE = Permission.create("uc.mute.unmute.base", "mute", PermissionLevel.MOD, "unmute", Text.of("Allows you to unmute someone."));

    public static PermissionOption UC_MUTE_EXEMPTPOWER = PermissionOption.create("uc.mute.exemptpower", "mute", "mute", "0", Text.of("The amount of exemptpower the player has. If a player's mutepower is higher than or equal to the targets exemptpower he can mute the target."));
    public static PermissionOption UC_MUTE_POWER = PermissionOption.create("uc.mute.power", "mute", "mute", "0", Text.of("The amount of mutepower the player has. If a player's mutepower is higher than or equal to the targets exemptpower he can mute the target."));
}
