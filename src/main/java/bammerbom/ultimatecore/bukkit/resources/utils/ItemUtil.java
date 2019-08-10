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
package bammerbom.ultimatecore.bukkit.resources.utils;

import bammerbom.ultimatecore.bukkit.resources.databases.ItemDatabase;
import bammerbom.ultimatecore.bukkit.resources.utils.ReflectionUtil.ReflectionStatic;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.Map.Entry;

public class ItemUtil {

    static HashMap<Material, String> ids = new HashMap<>();

    public static ItemStack setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack setLore(ItemStack item, String... lore) {
        return setLore(item, Arrays.asList(lore));
    }

    public static ItemStack setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack searchItem(String str) {
        return ItemDatabase.getItem(str);
    }

    public static String getName(ItemStack stack) {
        try {
            return ReflectionUtil.execute("getName()", ReflectionUtil.executeStatic("asNMSCopy({1})", ReflectionStatic.fromOBC("inventory.CraftItemStack"), stack).fetch()).fetch()
                    .toString();
        } catch (Exception ex) {
            return getTypeName(stack);
        }
    }

    public static String getTypeName(ItemStack stack) {
        if (stack == null || stack.getType() == null || stack.getType().equals(Material.AIR)) {
            return "hand";
        }
        return StringUtil.firstUpperCase(stack.getType().name().replace("_", " ").toLowerCase());
    }

    public static boolean isRepairable(ItemStack stack) {
        return stack.getItemMeta() instanceof Damageable;
    }

    public static String getID(Material mat) {
        return ids.get(mat);
    }

    public static Material getMaterialFromId(String id) {
        for (Entry<Material, String> en : ids.entrySet()) {
            if (en.getValue().equalsIgnoreCase(id)) {
                return en.getKey();
            }
        }
        return null;
    }

    public static List<HashMap<String, Object>> serialize(List<ItemStack> is) {
        List<HashMap<String, Object>> items = new ArrayList<>();
        for (ItemStack stack : is) {
            items.add(serialize(stack));
        }
        return items;
    }

    public static HashMap<String, Object> serialize(ItemStack is) {
        HashMap<String, Object> item = new HashMap<>();
        String mat = ItemUtil.getID(is.getType());
        if (is.getData().getData() != 0) {
            mat = mat + ":" + is.getData().getData();
        }
        item.put("type", mat);

        int quantity = is.getAmount();
        item.put("quantity", quantity);

        if (is.hasItemMeta()) {
            ItemMeta meta = is.getItemMeta();
            if (meta.hasDisplayName()) {
                item.put("name", meta.getDisplayName().replaceAll(" ", "_"));
            }

            if (meta.hasLore()) {
                StringBuilder lore = new StringBuilder();
                boolean first = true;
                for (String s : meta.getLore()) {
                    if (!first) {
                        lore.append("|");
                    }
                    first = false;
                    lore.append(s.replaceAll(" ", "_"));
                }
                item.put("lore", lore);
            }

            if (meta.hasEnchants()) {
                for (Enchantment e : meta.getEnchants().keySet()) {
                    item.put(e.getName().toLowerCase(), meta.getEnchantLevel(e));
                }
            }

            ArrayList<String> flags = new ArrayList<>();
            for (ItemFlag flag : meta.getItemFlags()) {
                flags.add(flag.name().toLowerCase().replace("_", ""));
            }
            if (!flags.isEmpty()) {
                String flagstring = StringUtil.joinList(",", flags.toArray(new String[flags.size()]));
                item.put("hideflags", flagstring);
            }
            //TODO canplaceon and candestroy

        }

        switch (is.getType()) {
            case WRITTEN_BOOK:
                BookMeta bookMeta = (BookMeta) is.getItemMeta();
                if (bookMeta.hasPages()) {
                    List<String> pages = bookMeta.getPages();
                    String name = "autogenerated_" + UUID.randomUUID();
                    while (BookUtil.bookExists(name)) {
                        name = "autogenerated_" + UUID.randomUUID();
                    }
                    BookUtil.writeBook(name, pages);
                    item.put("book", name);
                }
                if (bookMeta.hasTitle()) {
                    item.put("title", bookMeta.getTitle());
                }
                if (bookMeta.hasAuthor()) {
                    item.put("author", bookMeta.getAuthor());
                }
                break;
            case ENCHANTED_BOOK:
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) is.getItemMeta();
                for (Enchantment e : enchantmentStorageMeta.getStoredEnchants().keySet()) {
                    item.put(e.getName().toLowerCase(), enchantmentStorageMeta.getStoredEnchantLevel(e));
                }
                break;
            case FIREWORK_ROCKET:
                FireworkMeta fireworkMeta = (FireworkMeta) is.getItemMeta();
                if (fireworkMeta.hasEffects()) {
                    for (FireworkEffect effect : fireworkMeta.getEffects()) {
                        if (effect.getColors() != null && !effect.getColors().isEmpty()) {
                            StringBuilder colors = new StringBuilder();
                            boolean first = true;
                            for (Color c : effect.getColors()) {
                                if (!first) {
                                    colors.append(",");
                                }
                                colors.append(c.toString());
                                first = false;
                            }
                            item.put("color", colors);
                        }

                        item.put("shape", effect.getType().name());
                        if (effect.getFadeColors() != null && !effect.getFadeColors().isEmpty()) {
                            StringBuilder fade = new StringBuilder();
                            boolean first = true;
                            for (Color c : effect.getFadeColors()) {
                                if (!first) {
                                    fade.append(",");
                                }
                                fade.append(c.toString());
                                first = false;
                            }
                            item.put("fade", fade);
                        }
                    }
                    item.put("power", fireworkMeta.getPower());
                }
                break;
            case POTION:
                Potion potion = Potion.fromItemStack(is);
                for (PotionEffect e : potion.getEffects()) {
                    item.put("splash", potion.isSplash());
                    item.put("effect", e.getType().getName().toLowerCase());
                    item.put("power", e.getAmplifier());
                    item.put("duration", e.getDuration() / 20);
                }
                break;
            case PLAYER_HEAD:
                SkullMeta skullMeta = (SkullMeta) is.getItemMeta();
                if (skullMeta != null && skullMeta.hasOwner()) {
                    item.put("player", skullMeta.getOwner());
                }
                break;
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) is.getItemMeta();
                int rgb = leatherArmorMeta.getColor().asRGB();
                item.put("color", rgb);
                break;
            case WHITE_BANNER:
                BannerMeta bannerMeta = (BannerMeta) is.getItemMeta();
                if (bannerMeta != null) {
                    int basecolor = bannerMeta.getBaseColor().getColor().asRGB();
                    item.put("basecolor", basecolor);
                    for (org.bukkit.block.banner.Pattern p : bannerMeta.getPatterns()) {
                        String type = p.getPattern().getIdentifier();
                        int color = p.getColor().getColor().asRGB();
                        item.put(type, color);
                    }
                }
                break;
        }

        return item;
    }

    public static ItemStack createItem(Material mat, int data, String name, List<String> lore) {
        ItemStack stack = new ItemStack(mat);
        stack.setDurability((short) data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack createItem(Material mat, int quantity, int data, String name, List<String> lore) {
        ItemStack stack = new ItemStack(mat);
        stack.setDurability((short) data);
        stack.setAmount(quantity);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static Inventory putInInventory(Inventory inv, ItemStack stack, Integer... slots) {
        for (Integer slot : slots) {
            inv.setItem(slot, stack);
        }
        return inv;
    }

}
