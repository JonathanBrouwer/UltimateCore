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
package bammerbom.ultimatecore.sponge.modules.time.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class TimePermissions {
    public static Permission UC_TIME = Permission.create("uc.time", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to use all subcommands of the time command."));
    public static Permission UC_TIME_DAY = Permission.create("uc.time.day", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to set the time to day."));
    public static Permission UC_TIME_NIGHT = Permission.create("uc.time.night", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to set the time to night."));
    public static Permission UC_TIME_TICKS = Permission.create("uc.time.ticks", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to set the time to a certain amount of ticks."));
    public static Permission UC_TIME_ADD = Permission.create("uc.time.add", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to add a certain amount of ticks to the time"));
    public static Permission UC_TIME_QUERY = Permission.create("uc.time.query", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to use all types of time query commands."));
    public static Permission UC_TIME_QUERY_DAYS = Permission.create("uc.time.query.days", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to query the amount of days that have passes in the server."));
    public static Permission UC_TIME_QUERY_DAYTIME = Permission.create("uc.time.query.daytime", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to query how many ticks have passed since the last day change."));
    public static Permission UC_TIME_QUERY_GAMETIME = Permission.create("uc.time.query.gametime", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to use query how many ticks have passed in the server."));
    public static Permission UC_TIME_QUERY_FORMATTED = Permission.create("uc.time.query.formatted", "time", PermissionLevel.EVERYONE, "time", Text.of("Allows you to see the formatted time."));
    public static Permission UC_TIME_ENABLE = Permission.create("uc.time.enable", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to enable the daylight cycle."));
    public static Permission UC_TIME_DISABLE = Permission.create("uc.time.disable", "time", PermissionLevel.ADMIN, "time", Text.of("Allows you to disable the daylight cycle."));
}
