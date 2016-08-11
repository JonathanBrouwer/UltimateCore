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
package bammerbom.ultimatecore.sponge.modules.heal.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class HealPermissions {
    public static Permission UC_HEAL = Permission.create("uc.heal", "heal", PermissionLevel.ADMIN, "heal", Text.of("Allows you to use the heal command."));
    public static Permission UC_HEAL_OTHERS = Permission.create("uc.heal.others", "heal", PermissionLevel.ADMIN, "heal", Text.of("Allows you to use the heal command on other players."));
    public static Permission UC_SETHEALTH = Permission.create("uc.sethealth", "heal", PermissionLevel.ADMIN, "sethealth", Text.of("Allows you to use the sethealth command."));
    public static Permission UC_SETHEALTH_OTHERS = Permission.create("uc.sethealth.others", "heal", PermissionLevel.ADMIN, "sethealth", Text.of("Allows you to use the sethealth " +
            "command on other players."));
    public static Permission UC_SETMAXHEALTH = Permission.create("uc.setmaxhealth", "heal", PermissionLevel.ADMIN, "setmaxhealth", Text.of("Allows you to use the setmaxhealth command on "
            + "other players."));
    public static Permission UC_SETMAXHEALTH_OTHERS = Permission.create("uc.setmaxhealth.others", "heal", PermissionLevel.ADMIN, "setmaxhealth", Text.of("Allows you to use the " +
            "setmaxhealth command on other players."));

}
