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
package bammerbom.ultimatecore.sponge.modules.afk.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class AfkPermissions {
    public static Permission UC_AFK = Permission.create("uc.afk", "afk", PermissionLevel.EVERYONE, "afk", Text.of("Allows you to toggle your own afk status."));
    public static Permission UC_AFK_MESSAGE = Permission.create("uc.afk.message", "afk", PermissionLevel.EVERYONE, "afk", Text.of("Allows you to provide a reason for being afk."));
    public static Permission UC_AFK_OTHERS = Permission.create("uc.afk.others", "afk", PermissionLevel.MOD, "afk", Text.of("Allows you to toggle another player's afk status."));
    public static Permission UC_AFK_OTHERS_MESSAGE = Permission.create("uc.afk.others.message", "afk", PermissionLevel.MOD, "afk", Text.of("Allows you to provide an message while " +
            "toggling another player's afk status."));
    public static Permission UC_AFK_EXEMPT = Permission.create("uc.afk.exempt", "afk", PermissionLevel.VIP, null, Text.of("When you have this permission you can't be kicked for being " +
            "afk."));
}