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
package bammerbom.ultimatecore.sponge.modules.spy.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class SpyPermissions {
    public static Permission UC_SPY_COMMANDSPY_BASE = Permission.create("uc.spy.commandspy.base", "spy", PermissionLevel.MOD, "commandspy", Text.of("Allows you to toggle whether your commandspy is enabled."));
    public static Permission UC_SPY_MESSAGESPY_BASE = Permission.create("uc.spy.messagespy.base", "spy", PermissionLevel.MOD, "messagespy", Text.of("Allows you to toggle whether your messagespy is enabled."));
    public static Permission UC_SPY_COMMANDSPY_OTHERS = Permission.create("uc.spy.commandspy.others", "spy", PermissionLevel.MOD, "commandspy", Text.of("Allows you to toggle whether someones commandspy is enabled."));
    public static Permission UC_SPY_MESSAGESPY_OTHERS = Permission.create("uc.spy.messagespy.others", "spy", PermissionLevel.MOD, "messagespy", Text.of("Allows you to toggle whether someones messagespy is enabled."));
    public static Permission UC_SPY_COMMANDSPY_SEE = Permission.create("uc.spy.commandspy.see", "spy", PermissionLevel.EVERYONE, "commandspy", Text.of("Allows you to see commandspy messages if your commandspy is toggled on."));
    public static Permission UC_SPY_MESSAGESPY_SEE = Permission.create("uc.spy.messagespy.see", "spy", PermissionLevel.EVERYONE, "messagespy", Text.of("Allows you to see messagespy messages if your messagespy is toggled on."));
}