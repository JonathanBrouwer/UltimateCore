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

import bammerbom.ultimatecore.bukkit.r;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.Map.Entry;

public class InventoryUtil {

    public static void addItem(Inventory inv, ItemStack item) {
        Integer amount = item.getAmount();
        while (item.getAmount() >= 64 && !isFullInventory(inv)) {
            ItemStack item2 = item.clone();
            item2.setAmount(64);
            inv.addItem(item2);
            item.setAmount(item.getAmount() - 64);
            amount = amount - 64;
        }
        if (!isFullInventory(inv)) {
            if (amount == 0) {
                return;
            }
            inv.addItem(item);
        }

    }

    public static void clearInventory(final Player p) {
        p.setItemInHand(new ItemStack(Material.AIR));
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        InventoryView view = p.getOpenInventory();
        if (view != null) {
            view.setCursor(null);
            Inventory i = view.getTopInventory();
            if (i != null) {
                i.clear();
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                p.updateInventory();

            }
        }, 1L);
    }

    public static void clearHandler(CommandSender cs, Player player, String[] args, int offset, boolean showExtended) {
        short data = -1;
        int type = -1;
        int amount = -1;

        if ((args.length > offset + 1) && (r.isInt(args[(offset + 1)]))) {
            amount = Integer.parseInt(args[(offset + 1)]);
        }
        if (args.length > offset) {
            if (args[offset].equalsIgnoreCase("**")) {
                type = -2;
            } else if (!args[offset].equalsIgnoreCase("*")) {
                String[] split = args[offset].split(":");
                ItemStack item = ItemUtil.searchItem(split[0]);
                if (item == null) {
                    r.sendMes(cs, "clearItemNotFound", "%Item", split[0]);
                    return;
                }
                type = item.getTypeId();
                if ((split.length > 1) && (r.isInt(split[1]))) {
                    data = Short.parseShort(split[1]);
                } else {
                    data = item.getDurability();
                }
            }
        }
        if (type == -1 || type == -2) {
            if (showExtended) {
                if (cs.equals(player)) {
                    r.sendMes(cs, "clearSelfAll");
                } else {
                    r.sendMes(cs, "clearOtherAllSender", "%Player", player.getName());
                    r.sendMes(player, "clearOtherAllPlayer", "%Player", r.getDisplayName(cs));
                }
            }
            player.getInventory().clear();
        } else if (data == -1) {
            ItemStack stack = new ItemStack(type);
            if (showExtended) {
                if (cs.equals(player)) {
                    r.sendMes(cs, "clearSelfItem", "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", r.mes("clearAll"));
                } else {
                    r.sendMes(cs, "clearOtherItemSender", "%Player", player.getName(), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", r.mes("clearAll"));
                    r.sendMes(player, "clearOtherItemPlayer", "%Player", r.getDisplayName(cs), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", r.mes("clearAll"));
                }
            }
            player.getInventory().clear(type, data);
        } else if (amount == -1) {
            ItemStack stack = new ItemStack(type, 100000, data);
            ItemStack removedStack = player.getInventory().removeItem(new ItemStack[]{stack}).get(Integer.valueOf(0));
            int removedAmount = 100000 - removedStack.getAmount();
            if (removedAmount == 0) {
                r.sendMes(cs, "clearNoItems", "%Player", player.getName(), "%Item", ItemUtil.getName(stack));
            } else if ((removedAmount > 0) || (showExtended)) {
                if (cs.equals(player)) {
                    r.sendMes(cs, "clearSelfItem", "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", removedAmount);
                } else {
                    r.sendMes(cs, "clearOtherItemSender", "%Player", player.getName(), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", removedAmount);
                    r.sendMes(player, "clearOtherItemPlayer", "%Player", r.getDisplayName(cs), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", removedAmount);
                }
            }
        } else {
            if (amount < 0) {
                amount = 1;
            }
            ItemStack stack = new ItemStack(type, amount, data);
            if (player.getInventory().containsAtLeast(stack, amount)) {
                if (cs.equals(player)) {
                    r.sendMes(cs, "clearSelfItem", "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", amount);
                } else {
                    r.sendMes(cs, "clearOtherItemSender", "%Player", player.getName(), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", amount);
                    r.sendMes(player, "clearOtherItemPlayer", "%Player", r.getDisplayName(cs), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", amount);
                }
                player.getInventory().removeItem(new ItemStack[]{stack});
            } else if (showExtended) {
                r.sendMes(cs, "clearNotEnoughItems", "%Player", player.getName(), "%Item", ItemUtil.getName(stack).toLowerCase(), "%Amount", amount);
            }
        }
    }

    public static boolean isFullInventory(Inventory inv) {
        ItemStack[] inventory = inv.getContents();

        for (ItemStack stack : inventory) {
            if (stack == null || stack.getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public static boolean isEmptyInventory(PlayerInventory inv) {
        ItemStack[] inventory = inv.getContents();
        ItemStack[] armor = inv.getArmorContents();

        for (ItemStack stack : inventory) {
            if (stack != null) {
                return false;
            }
        }

        for (ItemStack stack : armor) {
            if (stack.getTypeId() != 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public static String convertInventoryToString(Inventory invInventory) {
        String serialization = invInventory.getSize() + ";";
        for (int i = 0;
             i < invInventory.getSize();
             i++) {
            ItemStack is = invInventory.getItem(i);
            if (is != null) {
                String serializedItemStack = "";

                String isType = String.valueOf(ItemUtil.getID(is.getType()));
                serializedItemStack += "t@" + isType;

                if (is.getDurability() != 0) {
                    String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack += ":d@" + isDurability;
                }

                if (is.getAmount() != 1) {
                    String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack += ":a@" + isAmount;
                }

                try {
                    Map<Enchantment, Integer> isEnch = is.getEnchantments();
                    if (isEnch.size() > 0) {
                        for (Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                            serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
                        }
                    }
                } catch (Exception ex) {
                    //Ignore invalid enchantments
                }

                serialization += i + "#" + serializedItemStack + ";";
            }
        }
        return serialization;
    }

    @SuppressWarnings("deprecation")
    public static Inventory convertStringToInventory(String invString, String name) {
        String[] serializedBlocks = invString.split(";");
        String invInfo = serializedBlocks[0];
        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo), (name.length() >= 32) ? name.substring(0, 31) : name);

        for (int i = 1;
             i < serializedBlocks.length;
             i++) {
            String[] serializedBlock = serializedBlocks[i].split("#");
            int stackPosition = Integer.valueOf(serializedBlock[0]);

            if (stackPosition >= deserializedInventory.getSize()) {
                continue;
            }

            ItemStack is = null;
            Boolean createdItemStack = false;

            String[] serializedItemStack = serializedBlock[1].split(":");
            for (String itemInfo : serializedItemStack) {
                String[] itemAttribute = itemInfo.split("@");
                if (itemAttribute[0].equals("t")) {
                    is = new ItemStack(r.isInt(itemAttribute[1]) ? Material.getMaterial(Integer.parseInt(itemAttribute[1])) : Material.getMaterial(itemAttribute[1]));
                    createdItemStack = true;
                } else if (itemAttribute[0].equals("d") && createdItemStack) {
                    is.setDurability(Short.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("a") && createdItemStack) {
                    is.setAmount(Integer.valueOf(itemAttribute[1]));
                } else if (itemAttribute[0].equals("e") && createdItemStack) {
                    is.addUnsafeEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
                }
            }
            deserializedInventory.setItem(stackPosition, is);
        }

        return deserializedInventory;
    }

}
