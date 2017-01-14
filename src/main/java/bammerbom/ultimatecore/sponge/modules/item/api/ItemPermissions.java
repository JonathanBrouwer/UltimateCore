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
package bammerbom.ultimatecore.sponge.modules.item.api;

import bammerbom.ultimatecore.sponge.api.permission.Permission;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import org.spongepowered.api.text.Text;

public class ItemPermissions {
    public static Permission UC_ITEM_MORE_BASE = Permission.create("uc.item.more.base", "item", PermissionLevel.ADMIN, "more", Text.of("Allows you to use the more command."));
    public static Permission UC_ITEM_REPAIR_BASE = Permission.create("uc.item.repair.base", "item", PermissionLevel.ADMIN, "repair", Text.of("Allows you to use the repair command for both all and one item."));
    public static Permission UC_ITEM_REPAIR_ALL = Permission.create("uc.item.repair.all", "item", PermissionLevel.ADMIN, "repair", Text.of("Allows you to use the repair command for all items."));
    public static Permission UC_ITEM_REPAIR_ONE = Permission.create("uc.item.repair.one", "item", PermissionLevel.ADMIN, "repair", Text.of("Allows you to use the repair command for one item."));

    public static Permission UC_ITEM_ITEMNAME_BASE = Permission.create("uc.item.itemname.base", "item", PermissionLevel.ADMIN, "itemname", Text.of("Allows you to use the itemname command."));
    public static Permission UC_ITEM_ITEMLORE_BASE = Permission.create("uc.item.itemlore.base", "item", PermissionLevel.ADMIN, "itemlore", Text.of("Allows you to use the itemlore command."));
    public static Permission UC_ITEM_ITEMQUANTITY_BASE = Permission.create("uc.item.itemquantity.base", "item", PermissionLevel.ADMIN, "itemquantity", Text.of("Allows you to use the itemquantity command."));
    public static Permission UC_ITEM_ITEMDURABILITY_BASE = Permission.create("uc.item.itemdurability.base", "item", PermissionLevel.ADMIN, "itemdurability", Text.of("Allows you to use the itemdurability command."));
    public static Permission UC_ITEM_ITEMUNBREAKABLE_BASE = Permission.create("uc.item.itemunbreakable.base", "item", PermissionLevel.ADMIN, "itemunbreakable", Text.of("Allows you to use the itemunbreakable command."));
    //public static Permission UC_ITEM_ITEMGLOW_BASE = Permission.create("uc.item.itemglow.base", "item", PermissionLevel.ADMIN, "itemglow", Text.of("Allows you to use the itemglow command."));
    public static Permission UC_ITEM_ITEMCANPLACEON_BASE = Permission.create("uc.item.itemcanplaceon.base", "item", PermissionLevel.ADMIN, "itemcanplaceon", Text.of("Allows you to use the itemcanplaceon command."));
    public static Permission UC_ITEM_ITEMCANBREAK_BASE = Permission.create("uc.item.itemcanbreak.base", "item", PermissionLevel.ADMIN, "itemcanbreak", Text.of("Allows you to use the itemcanbreak command."));
    public static Permission UC_ITEM_ITEMHIDETAGS_BASE = Permission.create("uc.item.itemhidetags.base", "item", PermissionLevel.ADMIN, "itemhidetags", Text.of("Allows you to use the itemhidetags command."));
    public static Permission UC_ITEM_ITEMENCHANT_BASE = Permission.create("uc.item.itemenchant.base", "item", PermissionLevel.ADMIN, "itemenchant", Text.of("Allows you to use the itemenchant command."));

    public static Permission UC_ITEM_ITEMMAXHEALTH_BASE = Permission.create("uc.item.itemmaxhealth.base", "item", PermissionLevel.ADMIN, "itemmaxhealth", Text.of("Allows you to use the itemmaxhealth command."));
    public static Permission UC_ITEM_ITEMDAMAGE_BASE = Permission.create("uc.item.itemdamage.base", "item", PermissionLevel.ADMIN, "itemdamage", Text.of("Allows you to use the itemdamage command."));
    public static Permission UC_ITEM_ITEMSPEED_BASE = Permission.create("uc.item.itemspeed.base", "item", PermissionLevel.ADMIN, "itemspeed", Text.of("Allows you to use the itemspeed command."));
    public static Permission UC_ITEM_ITEMKNOCKBACKRESISTANCE_BASE = Permission.create("uc.item.itemknockbackresistance.base", "item", PermissionLevel.ADMIN, "itemknockbackresistance", Text.of("Allows you to use the itemknockbackresistance command."));

    public static Permission UC_ITEM_SKULL_BASE = Permission.create("uc.item.skull.base", "item", PermissionLevel.ADMIN, "skull", Text.of("Allows you to use the skull command."));
    public static Permission UC_ITEM_BOOKAUTHOR_BASE = Permission.create("uc.item.bookauthor.base", "item", PermissionLevel.ADMIN, "bookauthor", Text.of("Allows you to use the bookauthor command."));
    public static Permission UC_ITEM_BOOKTITLE_BASE = Permission.create("uc.item.booktitle.base", "item", PermissionLevel.ADMIN, "booktitle", Text.of("Allows you to use the booktitle command."));
    public static Permission UC_ITEM_BOOKEDIT_BASE = Permission.create("uc.item.bookedit.base", "item", PermissionLevel.ADMIN, "bookedit", Text.of("Allows you to use the bookedit command."));
    public static Permission UC_ITEM_FIREWORK_BASE = Permission.create("uc.item.firework.base", "item", PermissionLevel.ADMIN, "firework", Text.of("Allows you to use the firework command."));
    public static Permission UC_ITEM_LEATHERARMORCOLOR_BASE = Permission.create("uc.item.leatherarmorcolor.base", "item", PermissionLevel.ADMIN, "leatherarmorcolor", Text.of("Allows you to use the leatherarmorcolor command."));
}
