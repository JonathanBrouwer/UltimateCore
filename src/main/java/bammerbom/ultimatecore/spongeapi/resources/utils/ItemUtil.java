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
package bammerbom.ultimatecore.spongeapi.resources.utils;

import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.databases.ItemDatabase;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.data.manipulators.items.DurabilityData;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map.Entry;

@SuppressWarnings({"deprecation", "unchecked"})
public class ItemUtil {

    static HashMap<ItemType, String> ids = new HashMap<>();

    public static void start() {
        //ids
        try {
            for (ItemType type : CatalogTypes.ITEM_TYPE.getEnumConstants()) {
                ids.put(type, type.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ItemStack addGlow(ItemStack item) {
        //EnchantGlow.addGlow(item);
        return item;
    }

    public static ItemStack searchItem(String str) {
        return ItemDatabase.getItem(str);
    }

    public static String getName(ItemStack stack) {
        try {
            return stack.getItem().getName();
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to get item name.");
            return "NAME";
        }
    }

    public static boolean isRepairable(ItemStack stack) {
        return stack.isCompatible(DurabilityData.class);
    }

    public static ItemType getItemTypeFromId(String id) {
        for (Entry<ItemType, String> en : ids.entrySet()) {
            if (en.getValue().equalsIgnoreCase(id)) {
                return en.getKey();
            }
        }
        return null;
    }
}