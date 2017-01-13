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
package bammerbom.ultimatecore.sponge.modules.deaf.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.api.permission.PermissionOption;
import org.spongepowered.api.text.Text;

public class DeafPermissions {
    public static Permission UC_DEAF_DEAF = Permission.create("uc.deaf.deaf", "deaf", PermissionLevel.MOD, "deaf", Text.of("Allows you to deaf someone."));
    public static Permission UC_DEAF_UNDEAF = Permission.create("uc.deaf.undeaf", "deaf", PermissionLevel.MOD, "undeaf", Text.of("Allows you to undeaf someone."));

    public static PermissionOption UC_DEAF_EXEMPTPOWER = PermissionOption.create("uc.deaf.exemptpower", "deaf", "deaf", "0", Text.of("The amount of exemptpower the player has. If a player's deafpower is higher than or equal to the targets exemptpower he can deaf the target."));
    public static PermissionOption UC_DEAF_POWER = PermissionOption.create("uc.deaf.power", "deaf", "deaf", "0", Text.of("The amount of deafpower the player has. If a player's deafpower is higher than or equal to the targets exemptpower he can deaf the target."));
}
