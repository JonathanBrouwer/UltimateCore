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
import bammerbom.ultimatecore.spongeapi.resources.classes.MetaItemStack;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.ItemUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * A class representing a kit.
 */
public class UKit {

    private static final Config kits = new Config(UltimateFileLoader.DFkits);
    private final ConfigSection kit;
    private final String name;
    private final String description;
    private final List<ItemStack> items;
    private final long cooldown;

    public UKit(final String name) {
        this.name = name;
        this.kit = kits.getConfigurationSection(name);
        this.items = getItemStacks(kit.getMapList("items"));
        this.cooldown = DateUtil.parseDateDiff(kit.getString("cooldown", "0"));
        this.description = ChatColor.translateAlternateColorCodes('&', kit.getString("description", ""));
    }

    /**
     * Adds a list of represented enchantments to an ItemStack.
     *
     * @param is ItemStack to add enchantments to
     * @param enchantments List of nodes representing enchantments
     * @return ItemStack with enchantments applied
     */
    private ItemStack addEnchantments(final ItemStack is, final List<ConfigSection> enchantments) {
        for (final ConfigSection enchantment : enchantments) {
            final Enchantment realEnchantment = this.getEnchantment(enchantment);
            if (realEnchantment == null) {
                continue;
            }
            is.addUnsafeEnchantment(realEnchantment, enchantment.getInt("level", 1));
        }
        return is;
    }

    /**
     * Gets an enchantment from a node representing it.
     *
     * @param enchantment Node
     * @return Enchantment (may be null)
     */
    private Enchantment getEnchantment(final ConfigSection enchantment) {
        return Enchantment.getByName(enchantment.getString("type", "").toUpperCase());
    }

    /**
     * Gets an ItemStack from the given node
     *
     * @param item Node representing an ItemStack
     * @return ItemStack of null
     */
    private ItemStack getItemStack(final Map<String, Object> item) {
        final ItemStack is = ItemUtil.searchItem((String) item.get("type"));
        if (is == null) {
            return null;
        }
        if (item.containsKey("amount")) {
            is.setAmount((int) item.get("amount"));
        }
        if (item.containsKey("damage")) {
            is.setDurability(((Number) item.get("damage")).shortValue());
        }
        MetaItemStack ism = new MetaItemStack(is);
        for (String s : item.keySet()) {
            if (s.equalsIgnoreCase("amount") || s.equalsIgnoreCase("type") || s.equalsIgnoreCase("damage")) {
                continue;
            }
            try {
                ism.addStringMeta(null, true, s + ":" + item.get(s).toString().replaceAll(" ", "_"));
            } catch (Exception ex) {
                continue;
            }
        }
        return ism.getItemStack();
    }

    /**
     * Gets all of the ItemStacks represented by the list of nodes.
     *
     * @param items List of nodes representing items
     * @return List of ItemStacks, never null
     */
    private List<ItemStack> getItemStacks(final List<Map<?, ?>> items) {
        final List<ItemStack> itemStacks = new ArrayList<>();
        for (final Map<?, ?> item : items) {
            HashMap<String, Object> itemc = new HashMap<>();
            itemc.putAll((Map<? extends String, ? extends Object>) item);
            final ItemStack is = this.getItemStack(itemc);
            if (is == null) {
                continue;
            }
            itemStacks.add(is);
        }
        return itemStacks;
    }

    /**
     * Gets the lore for the given item.
     *
     * @param item Item to get lore for
     * @return List of strings, never null
     */
    private List<String> getLore(final ConfigSection item) {
        final List<String> lore = new ArrayList<>();
        if (item == null) {
            return lore;
        }
        for (final String loreItem : item.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', loreItem));
        }
        return lore;
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
     * Gets the timestamp in milliseconds for when the cooldown for the given
     * player will expire.
     *
     * @param p Player
     * @return Cooldown expiration timestamp in milliseconds
     */
    public long getCooldownFor(final Player p) {
        final long lastUsed = this.getLastUsed(p);
        return (this.getCooldown() * 1000L) + lastUsed;
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
        return UC.getPlayer(p).getPlayerConfig().getLong("kits." + this.getName() + ".lastused", 0L);
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
     * Checks to see if the cooldown time has passed for the player using this
     * kit. If this returns true, the player can use the kit, if not, he can't.
     *
     * @param p RPlayer using kit
     * @return If the player can use the kit
     */
    public boolean hasCooldownPassedFor(final Player p) {
        final long lastUsed = this.getLastUsed(p);
        return !(this.getCooldown() == -1L && lastUsed != 0L) && lastUsed + (this.getCooldown() * 1000L) < System.currentTimeMillis();
    }

    /**
     * Sets the last time that the player used this kit.
     *
     * @param p Player using kit
     * @param lastUsed Timestamp in milliseconds
     */
    public void setLastUsed(final Player p, final long lastUsed) {
        if (this.getCooldown() == 0L) {
            return;
        }
        Config conf = UC.getPlayer(p).getPlayerConfig();
        conf.set("kits." + this.getName() + ".lastused", lastUsed);
        conf.save();
    }
}
