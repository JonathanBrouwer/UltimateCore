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
package bammerbom.ultimatecore.spongeapi.api;

import bammerbom.ultimatecore.spongeapi.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi.configuration.Config;
import bammerbom.ultimatecore.spongeapi.configuration.ConfigSection;
import bammerbom.ultimatecore.spongeapi.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class representing a kit.
 */
public class UKit {

    private static final Config kits = new Config(UltimateFileLoader.Dkits);
    private final String name;
    private final String description;
    private final List<ItemStack> items;
    private final long cooldown;
    private final String cooldowns;
    private final boolean firstjoin;
    private ConfigSection kit;

    public UKit(String name) {
        for (String s : new Config(UltimateFileLoader.Dkits).getKeys(false)) {
            if (s.equalsIgnoreCase(name)) {
                name = s;
            }
        }
        this.name = name;
        this.kit = kits.getConfigurationSection(name);
        this.items = getItemStacks(kit.getStringList("items"));
        this.cooldown = DateUtil.parseDateDiff(kit.getString("cooldown", "0"));
        this.cooldowns = kit.getString("cooldown", "0");
        this.description = TextColorUtil.translateAlternate(kit.getString("description", ""));
        this.firstjoin = kit.getBoolean("firstjoin", false);
    }

    public UKit(String name, final String cooldown, final boolean firstjoin, final String description, final List<ItemStack> items) {
        for (String s : new Config(UltimateFileLoader.Dkits).getKeys(false)) {
            if (s.equalsIgnoreCase(name)) {
                name = s;
            }
        }
        this.name = name;
        this.kit = null;
        this.items = items;
        this.cooldown = DateUtil.parseDateDiff(cooldown);
        this.cooldowns = cooldown;
        this.description = TextColorUtil.translateAlternate(description);
        this.firstjoin = firstjoin;
    }

    /**
     * Saves this kit to the config file
     */
    public void save() {
        List<JSONObject> jsons = ItemUtil.serialize(items);
        List<String> strings = jsons.stream().map(JSONAware::toJSONString).collect(Collectors.toList());
        kits.set(name + ".description", description);
        kits.set(name + ".cooldown", cooldowns);
        kits.set(name + ".firstjoin", firstjoin);
        kits.set(name + ".items", strings);
        kits.save();
        this.kit = kits.getConfigurationSection(name);
    }

    /**
     * Gets if the kit is given to the player on first join.
     *
     * @return If the kit is given to the player on first join.
     */
    public boolean firstJoin() {
        return firstjoin;
    }

    /**
     * Gets an ItemStack from the given node
     *
     * @param item Node representing an ItemStack
     * @return ItemStack of null
     */
    private ItemStack getItemStack(String item) {
        try {
            return ItemUtil.deserialize(item).get();
        } catch (Exception ex) {
            r.log("Kit " + name + " has an invalid item: " + item);
            return ItemStack.builder().itemType(ItemTypes.NONE).build();
        }
    }

    /**
     * Gets all of the ItemStacks represented by the list of nodes.
     *
     * @param items List of nodes representing items
     * @return List of ItemStacks, never null
     */
    private List<ItemStack> getItemStacks(List<String> items) {
        final List<ItemStack> itemStacks = new ArrayList<>();
        for (String obj : items) {
            final ItemStack is = this.getItemStack(obj);
            if (is == null) {
                continue;
            }
            itemStacks.add(is);
        }
        return itemStacks;
    }

    /**
     * Gets the amount of seconds this kit's cooldown is.
     *
     * @return Seconds
     */
    public long getCooldown() {
        return this.cooldown;
    }

    /**
     * Gets the timestamp in milliseconds for when the cooldown for the given player will expire.
     *
     * @param p Player
     * @return Cooldown expiration timestamp in milliseconds
     */
    public long getCooldownFor(final Player p) {
        final long lastUsed = this.getLastUsed(p);
        return (this.getCooldown()) + lastUsed;
    }

    /**
     * Gets the description of this kit, suitable for display to players.
     *
     * @return Description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets a cloned list of items contained in this kit.
     *
     * @return Cloned list
     */
    public List<ItemStack> getItems() {
        return new ArrayList<>(this.items);
    }

    /**
     * Gets the timestamp in milliseconds that the player last used this kit.
     *
     * @param p Player
     * @return Timestamp in milliseconds
     */
    public long getLastUsed(final Player p) {
        Long l = UC.getPlayer(p).getPlayerConfig().getLong("kits." + this.getName() + ".lastused");
        return l == null ? 0L : l;
    }

    /**
     * Gets the name of this kit.
     *
     * @return Name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks to see if the cooldown time has passed for the player using this kit. If this returns
     * true, the player can use the kit, if not, he can't.
     *
     * @param p RPlayer using kit
     * @return If the player can use the kit
     */
    public boolean hasCooldownPassedFor(final Player p) {
        final long lastUsed = this.getLastUsed(p);
        if (getCooldown() == -1L && lastUsed != 0L) {
            return false;
        }
        return (this.getCooldownFor(p) < System.currentTimeMillis());
    }

    /**
     * Sets the last time that the player used this kit.
     *
     * @param p        Player using kit
     * @param lastUsed Timestamp in milliseconds
     */
    public void setLastUsed(final Player p, final long lastUsed) {
        if (this.getCooldown() == 0L) {
            return;
        }
        JsonConfig conf = UC.getPlayer(p).getPlayerConfig();
        conf.set("kits." + this.getName() + ".lastused", lastUsed);
        conf.save();
    }
}
