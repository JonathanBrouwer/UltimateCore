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

import bammerbom.ultimatecore.spongeapi.resources.databases.ItemDatabase;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemUtil {

    public static ItemStack setName(ItemStack item, Text name) {
        item.offer(Keys.DISPLAY_NAME, name);
        return item;
    }

    public static ItemStack setLore(ItemStack item, List<Text> lore) {
        item.offer(Keys.ITEM_LORE, lore);
        return item;
    }

    public static ItemStack searchItem(String str) {
        return ItemDatabase.getItem(str);
    }

    public static Optional<JSONObject> serialize(ItemStack item) {
        try {
            return Optional.of((JSONObject) new JSONParser().parse(new GsonBuilder().create().toJson(item.toContainer().getValues(true))));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<ItemStack> deserialize(String json) {
        try {
            StringReader source = new StringReader(json);
            GsonConfigurationLoader loader = GsonConfigurationLoader.builder().setSource(() -> new BufferedReader(source)).build();
            ConfigurationNode node = loader.load();
            return Optional.of(node.getValue(TypeToken.of(ItemStack.class)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static List<JSONObject> serialize(List<ItemStack> items) {
        List<JSONObject> rtrn = new ArrayList<>();
        for (ItemStack stack : items) {
            Optional<JSONObject> obj = serialize(stack);
            if (obj.isPresent()) {
                rtrn.add(obj.get());
            }
        }
        return rtrn;
    }

    public static List<ItemStack> deserialize(List<String> jsons) {
        List<ItemStack> rtrn = new ArrayList<>();
        for (String json : jsons) {
            Optional<ItemStack> item = deserialize(json);
            if (item.isPresent()) {
                rtrn.add(item.get());
            }
        }
        return rtrn;
    }
}