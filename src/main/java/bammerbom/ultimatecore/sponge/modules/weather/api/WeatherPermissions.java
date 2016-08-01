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
package bammerbom.ultimatecore.sponge.modules.weather.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class WeatherPermissions {
    public static Permission UC_WEATHER = Permission.create("uc.weather", "weather", PermissionLevel.ADMIN, "weather", Text.of("Allows you to change the weather to anything."));
    public static Permission UC_WEATHER_SUN = Permission.create("uc.weather.sun", "weather", PermissionLevel.ADMIN, "weather", Text.of("Allows you to change the weather to sun."));
    public static Permission UC_WEATHER_RAIN = Permission.create("uc.weather.rain", "weather", PermissionLevel.ADMIN, "weather", Text.of("Allows you to change the weather to rain."));
    public static Permission UC_WEATHER_THUNDER = Permission.create("uc.weather.thunder", "weather", PermissionLevel.ADMIN, "weather", Text.of("Allows you to change the weather to " +
            "thunder."));
}
