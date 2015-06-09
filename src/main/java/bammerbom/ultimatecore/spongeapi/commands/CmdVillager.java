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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.ReflectionUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.VillagerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSource;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CmdVillager implements UltimateCommand {

    public static ArrayList<UUID> usedCommand = new ArrayList<>();
    public static HashMap<UUID, Villager> currentVillager = new HashMap<>();
    public static HashMap<UUID, Inventory> editInv = new HashMap<>();
    public static HashMap<UUID, Integer> editing = new HashMap<>();
    public static HashMap<UUID, Integer> pages = new HashMap<>();
    static boolean blockClose = false;

    public static boolean confirm(HumanEntity p, Villager v, Integer page) {
        r.debug("Confirm " + page);
        if (!usedCommand.contains(p.getUniqueId())) {
            return false;
        }
        usedCommand.remove(p.getUniqueId());

        final int opage = page;
        pages.put(p.getUniqueId(), page);
        page--;
        page = page * 9;

        Inventory inv = Bukkit.createInventory(p, 54, "Villager Editor (Page " + ((page / 9) + 1) + ")");

        ItemStack paper_buying1 = new ItemStack(Material.PAPER);
        ItemMeta paper_buying1_meta = paper_buying1.getItemMeta();
        paper_buying1_meta.setDisplayName(ChatColor.AQUA + "Buying item 1");
        paper_buying1.setItemMeta(paper_buying1_meta);
        inv.setItem(0, paper_buying1);
        inv.setItem(5, paper_buying1);

        ItemStack paper_buying2 = new ItemStack(Material.PAPER);
        ItemMeta paper_buying2_meta = paper_buying2.getItemMeta();
        paper_buying2_meta.setDisplayName(ChatColor.AQUA + "Buying item 2");
        paper_buying2.setItemMeta(paper_buying2_meta);
        inv.setItem(1, paper_buying2);
        inv.setItem(6, paper_buying2);

        ItemStack paper_selling = new ItemStack(Material.PAPER);
        ItemMeta paper_selling_meta = paper_selling.getItemMeta();
        paper_selling_meta.setDisplayName(ChatColor.AQUA + "Selling item");
        paper_selling.setItemMeta(paper_selling_meta);
        inv.setItem(2, paper_selling);
        inv.setItem(7, paper_selling);

        ItemStack greenglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
        ItemMeta greenglass_meta = greenglass.getItemMeta();
        greenglass_meta.setDisplayName(" ");
        greenglass.setItemMeta(greenglass_meta);
        inv.setItem(3, greenglass);
        inv.setItem(4, greenglass);
        inv.setItem(8, greenglass);
        inv.setItem(13, greenglass);
        inv.setItem(22, greenglass);
        inv.setItem(31, greenglass);
        inv.setItem(40, greenglass);
        inv.setItem(49, greenglass);
        inv.setItem(53, greenglass);

        Integer s1 = 9, s2 = 18, s3 = 27, s4 = 36, s5 = 45, s6 = 14, s7 = 23, s8 = 32, s9 = 41;
        int opages = 1;
        for (VillagerUtil.VillagerTrade trade : VillagerUtil.listTrades(v)) {
            if (page != 0) {
                page--;
                continue;
            }
            inv = addTradeToInv(inv, trade, Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9), p);
            if (s1 != null) {
                s1 = null;
            } else if (s2 != null) {
                s2 = null;
            } else if (s3 != null) {
                s3 = null;
            } else if (s4 != null) {
                s4 = null;
            } else if (s5 != null) {
                s5 = null;
            } else if (s6 != null) {
                s6 = null;
            } else if (s7 != null) {
                s7 = null;
            } else if (s8 != null) {
                s8 = null;
            } else if (s9 != null) {
                s9 = null;
                opages++;
            }
        }

        ItemStack feather_previous = new ItemStack(Material.FEATHER);
        ItemMeta feather_previous_meta = feather_previous.getItemMeta();
        feather_previous_meta.setDisplayName(ChatColor.DARK_RED + "Previous page");
        if (opage == 1) {
            feather_previous_meta.setLore(Arrays.asList(ChatColor.RED + "Not available"));
        }
        feather_previous.setItemMeta(feather_previous_meta);
        inv.setItem(50, feather_previous);

        ItemStack feather_next = new ItemStack(Material.FEATHER);
        ItemMeta feather_next_meta = feather_next.getItemMeta();
        feather_next_meta.setDisplayName(ChatColor.GREEN + "Next page");
        r.debug(opage + " " + opages);
        if (opages <= opage) {
            feather_next_meta.setLore(Arrays.asList(ChatColor.RED + "Not available"));
        }
        feather_next.setItemMeta(feather_next_meta);
        inv.setItem(51, feather_next);

        blockClose = true;
        p.openInventory(inv);
        blockClose = false;

        currentVillager.put(p.getUniqueId(), v);
        return true;
    }

    static Inventory addTradeToInv(Inventory inv, VillagerUtil.VillagerTrade trade, List<Integer> s, HumanEntity p) {
        ItemStack barrier = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta barrier_meta = barrier.getItemMeta();
        barrier_meta.setDisplayName(ChatColor.DARK_AQUA + "Edit");
        barrier_meta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + trade.getUses(), ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + trade
                .getMaxUses(), ChatColor.DARK_AQUA + "Reward exp: " + ChatColor.AQUA + trade.getRewardExp()));
        barrier.setItemMeta(barrier_meta);
        Integer s1 = s.get(0), s2 = s.get(1), s3 = s.get(2), s4 = s.get(3), s5 = s.get(4), s6 = s.get(5), s7 = s.get(6), s8 = s.get(7), s9 = s.get(8);
        if (s1 != null) {
            inv.setItem(s1, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s1 + 1, trade.getItem2());
            }
            inv.setItem(s1 + 2, trade.getRewardItem());
            inv.setItem(s1 + 3, barrier);
            s1 = null;
        } else if (s2 != null) {
            inv.setItem(s2, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s2 + 1, trade.getItem2());
            }
            inv.setItem(s2 + 2, trade.getRewardItem());
            inv.setItem(s2 + 3, barrier);
            s2 = null;
        } else if (s3 != null) {
            inv.setItem(s3, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s3 + 1, trade.getItem2());
            }
            inv.setItem(s3 + 2, trade.getRewardItem());
            inv.setItem(s3 + 3, barrier);
            s3 = null;
        } else if (s4 != null) {
            inv.setItem(s4, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s4 + 1, trade.getItem2());
            }
            inv.setItem(s4 + 2, trade.getRewardItem());
            inv.setItem(s4 + 3, barrier);
            s4 = null;
        } else if (s5 != null) {
            inv.setItem(s5, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s5 + 1, trade.getItem2());
            }
            inv.setItem(s5 + 2, trade.getRewardItem());
            inv.setItem(s5 + 3, barrier);
            s5 = null;
        } else if (s6 != null) {
            inv.setItem(s6, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s6 + 1, trade.getItem2());
            }
            inv.setItem(s6 + 2, trade.getRewardItem());
            inv.setItem(s6 + 3, barrier);
            s6 = null;
        } else if (s7 != null) {
            inv.setItem(s7, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s7 + 1, trade.getItem2());
            }
            inv.setItem(s7 + 2, trade.getRewardItem());
            inv.setItem(s7 + 3, barrier);
            s7 = null;
        } else if (s8 != null) {
            inv.setItem(s8, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s8 + 1, trade.getItem2());
            }
            inv.setItem(s8 + 2, trade.getRewardItem());
            inv.setItem(s8 + 3, barrier);
            s8 = null;
        } else if (s9 != null) {
            inv.setItem(s9, trade.getItem1());
            if (trade.hasItem2()) {
                inv.setItem(s9 + 1, trade.getItem2());
            }
            inv.setItem(s9 + 2, trade.getRewardItem());
            inv.setItem(s9 + 3, barrier);
            s9 = null;
        } else {
            return inv;
        }
        return inv;
    }

    public static boolean clickButton(final InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase("Villager Editor (Settings)")) {
            if (e.getClickedInventory() == null || e.getInventory() == null || !e.getClickedInventory().equals(e.getInventory())) {
                return true;
            }
            if (e.getSlot() % 9 == 0 && e.getClickedInventory().getItem(e.getSlot()).getDurability() == (short) 14) {
                ItemStack s = e.getClickedInventory().getItem(e.getSlot());
                ItemStack paper = e.getClickedInventory().getItem(28);
                Integer cur = Integer.parseInt(ChatColor.stripColor(paper.getItemMeta().getDisplayName()).replaceFirst("Uses: ", ""));
                cur = cur - s.getAmount();
                if (cur <= 0) {
                    cur = 0;
                }
                ItemMeta meta = paper.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + cur);
                paper.setItemMeta(meta);
                e.getInventory().setItem(28, paper);
            }
            if ((e.getSlot() - 2) % 9 == 0 && e.getClickedInventory().getItem(e.getSlot()).getDurability() == (short) 13) {
                ItemStack s = e.getClickedInventory().getItem(e.getSlot());
                ItemStack paper = e.getClickedInventory().getItem(28);
                Integer cur = Integer.parseInt(ChatColor.stripColor(paper.getItemMeta().getDisplayName()).replaceFirst("Uses: ", ""));
                cur = cur + s.getAmount();
                ItemMeta meta = paper.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + cur);
                paper.setItemMeta(meta);
                e.getInventory().setItem(28, paper);
            }
            if ((e.getSlot() - 6) % 9 == 0 && e.getClickedInventory().getItem(e.getSlot()).getDurability() == (short) 14) {
                ItemStack s = e.getClickedInventory().getItem(e.getSlot());
                ItemStack paper = e.getClickedInventory().getItem(34);
                Integer cur = Integer.parseInt(ChatColor.stripColor(paper.getItemMeta().getDisplayName()).replaceFirst("Max uses: ", ""));
                cur = cur - s.getAmount();
                if (cur <= 0) {
                    cur = 0;
                }
                ItemMeta meta = paper.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + cur);
                paper.setItemMeta(meta);
                e.getInventory().setItem(34, paper);
            }
            if ((e.getSlot() - 8) % 9 == 0 && e.getClickedInventory().getItem(e.getSlot()).getDurability() == (short) 13) {
                ItemStack s = e.getClickedInventory().getItem(e.getSlot());
                ItemStack paper = e.getClickedInventory().getItem(34);
                Integer cur = Integer.parseInt(ChatColor.stripColor(paper.getItemMeta().getDisplayName()).replaceFirst("Max uses: ", ""));
                cur = cur + s.getAmount();
                ItemMeta meta = paper.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + cur);
                paper.setItemMeta(meta);
                e.getInventory().setItem(34, paper);
            }
            if (e.getSlot() == 22) {
                Boolean rewardExp = e.getInventory().getItem(22).getDurability() == (short) 13;
                ItemStack clay = new ItemStack(Material.STAINED_CLAY, 1, rewardExp ? (byte) 14 : (byte) 13);
                ItemMeta clay_meta = clay.getItemMeta();
                clay_meta.setDisplayName(ChatColor.DARK_AQUA + "Generate exp: " + ChatColor.AQUA + !rewardExp);
                clay.setItemMeta(clay_meta);
                e.getInventory().setItem(22, clay);
            }
            if (e.getSlot() == 40) {
                Inventory inv = editInv.get(e.getWhoClicked().getUniqueId());
                Inventory oinv = e.getInventory();
                Integer iedit = editing.get(e.getWhoClicked().getUniqueId());

                ItemStack stack_uses = oinv.getItem(28);
                Integer uses = Integer.parseInt(ChatColor.stripColor(stack_uses.getItemMeta().getDisplayName()).replaceFirst("Uses: ", ""));

                ItemStack stack_max_uses = oinv.getItem(34);
                Integer max_uses = Integer.parseInt(ChatColor.stripColor(stack_max_uses.getItemMeta().getDisplayName()).replaceFirst("Max uses: ", ""));

                ItemStack stack_generateExp = oinv.getItem(22);
                Boolean generateExp = stack_generateExp.getDurability() == (short) 13;

                ItemStack barrier = new ItemStack(Material.LAPIS_BLOCK);
                ItemMeta barrier_meta = barrier.getItemMeta();
                barrier_meta.setDisplayName(ChatColor.DARK_AQUA + "Edit");
                barrier_meta.setLore(Arrays
                        .asList(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + uses, ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + max_uses, ChatColor.DARK_AQUA + "Reward exp: " + ChatColor.AQUA + generateExp));
                barrier.setItemMeta(barrier_meta);

                inv.setItem(iedit, barrier);

                editInv.remove(e.getWhoClicked().getUniqueId());
                editing.remove(e.getWhoClicked().getUniqueId());

                e.getWhoClicked().openInventory(inv);
            }
            return true;
        }
        if (e.getSlot() == 50 && !e.getClickedInventory().getItem(50).getItemMeta().hasLore()) {
            if (!(pages.get(e.getWhoClicked().getUniqueId()) == 1)) {
                r.debug("OPEN " + (pages.get(e.getWhoClicked().getUniqueId()) - 1));
                savePage(e.getWhoClicked(), e.getClickedInventory(), pages.get(e.getWhoClicked().getUniqueId()));
                closeInv(e.getWhoClicked(), e.getClickedInventory());
                usedCommand.add(e.getWhoClicked().getUniqueId());
                confirm(e.getWhoClicked(), currentVillager.get(e.getWhoClicked().getUniqueId()), pages.get(e.getWhoClicked().getUniqueId()) - 1);
                r.debug("OPENED " + (pages.get(e.getWhoClicked().getUniqueId())));
            }
        }
        if (e.getSlot() == 51 && !e.getClickedInventory().getItem(51).getItemMeta().hasLore()) {
            r.debug("OPEN " + (pages.get(e.getWhoClicked().getUniqueId()) + 1));
            closeInv(e.getWhoClicked(), e.getClickedInventory());
            usedCommand.add(e.getWhoClicked().getUniqueId());
            confirm(e.getWhoClicked(), currentVillager.get(e.getWhoClicked().getUniqueId()), pages.get(e.getWhoClicked().getUniqueId()) + 1);
            r.debug("OPENED " + (pages.get(e.getWhoClicked().getUniqueId())));
        }
        final ItemStack barrier = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta barrier_meta = barrier.getItemMeta();
        barrier_meta.setDisplayName(ChatColor.DARK_AQUA + "Edit");
        barrier_meta.setLore(Arrays
                .asList(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + "0", ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + "7", ChatColor.DARK_AQUA + "Reward exp: " + ChatColor.AQUA + "true"));
        barrier.setItemMeta(barrier_meta);

        final ItemStack redglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 11);
        ItemMeta redglass_meta = redglass.getItemMeta();
        redglass_meta.setDisplayName(ChatColor.DARK_AQUA + "Edit");
        redglass_meta.setLore(Arrays.asList(ChatColor.RED + "Invalid trade"));
        redglass.setItemMeta(redglass_meta);

        final Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();
        if (e.getClickedInventory() == e.getWhoClicked().getInventory()) {
            return false;
        }
        int slot = e.getSlot();
        while (isEditableSlot(slot - 1)) {
            slot--;
        }
        final int fslot = slot;
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                Boolean all = true;
                if (inv == null) {
                    return;
                }
                for (int i : Arrays.asList(9, 18, 27, 36, 45, 14, 23, 32, 41)) {
                    if (inv.getItem(i) != null && inv.getItem(i + 2) != null) {
                        inv.setItem(i + 3, barrier);
                    } else if (inv.getItem(i) != null || inv.getItem(i + 1) != null || inv.getItem(i + 2) != null) {
                        inv.setItem(i + 3, redglass);
                        all = false;
                    } else {
                        inv.setItem(i + 3, new ItemStack(Material.AIR));
                        all = false;
                    }
                }
                if (all) {
                    ItemStack feather_previous = new ItemStack(Material.FEATHER);
                    ItemMeta feather_previous_meta = feather_previous.getItemMeta();
                    feather_previous_meta.setDisplayName(ChatColor.DARK_RED + "Previous page");
                    if (pages.get(e.getWhoClicked().getUniqueId()) == 1) {
                        feather_previous_meta.setLore(Arrays.asList(ChatColor.RED + "Not available"));
                    }
                    feather_previous.setItemMeta(feather_previous_meta);
                    inv.setItem(50, feather_previous);

                    ItemStack feather_next = new ItemStack(Material.FEATHER);
                    ItemMeta feather_next_meta = feather_next.getItemMeta();
                    feather_next_meta.setDisplayName(ChatColor.GREEN + "Next page");
                    feather_next.setItemMeta(feather_next_meta);
                    inv.setItem(51, feather_next);
                }
            }
        }, 3L);
        if (isEditableSlot(e.getSlot())) {
            return false;

        }
        if (item != null && !inv.getTitle().equalsIgnoreCase("Villager Editor (Settings)")) {
            //Edit
            if (e.getSlot() == 12 || e.getSlot() == 21 || e.getSlot() == 30 || e.getSlot() == 39 || e.getSlot() == 48 || e.getSlot() == 17 || e.getSlot() == 26 || e.getSlot() == 35 || e
                    .getSlot() == 44) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                    @Override
                    public void run() {
                        if (e.getClickedInventory().getItem(e.getSlot()) == null || e.getClickedInventory().getItem(e.getSlot()).getType().equals(Material.AIR)) {
                            return;
                        }
                        editInv.put(e.getWhoClicked().getUniqueId(), inv);
                        editing.put(e.getWhoClicked().getUniqueId(), e.getSlot());
                        Inventory settings = Bukkit.createInventory(e.getWhoClicked(), 54, "Villager Editor (Settings)");

                        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 11);
                        ItemMeta pane_meta = pane.getItemMeta();
                        pane_meta.setDisplayName(" ");
                        pane.setItemMeta(pane_meta);

                        settings.setItem(0, pane);
                        settings.setItem(1, pane);
                        settings.setItem(2, pane);

                        settings.setItem(3, inv.getItem(fslot));
                        settings.setItem(4, inv.getItem(fslot + 1));
                        settings.setItem(5, inv.getItem(fslot + 2));

                        settings.setItem(6, pane);
                        settings.setItem(7, pane);
                        settings.setItem(8, pane);

                        settings.setItem(10, pane);
                        settings.setItem(19, pane);
                        settings.setItem(37, pane);
                        settings.setItem(46, pane);

                        settings.setItem(12, pane);
                        settings.setItem(21, pane);
                        settings.setItem(30, pane);
                        settings.setItem(39, pane);
                        settings.setItem(48, pane);

                        settings.setItem(13, pane);
                        settings.setItem(31, pane);
                        settings.setItem(49, pane);

                        settings.setItem(14, pane);
                        settings.setItem(23, pane);
                        settings.setItem(32, pane);
                        settings.setItem(41, pane);
                        settings.setItem(50, pane);

                        settings.setItem(16, pane);
                        settings.setItem(25, pane);
                        settings.setItem(43, pane);
                        settings.setItem(52, pane);

                        Integer uses = 0;
                        Integer maxUses = 7;
                        Boolean rewardExp = true;
                        ItemStack lapis = e.getInventory().getItem(fslot + 3);
                        if (lapis != null && lapis.getType().equals(Material.LAPIS_BLOCK)) {
                            for (String s : lapis.getItemMeta().getLore()) {
                                if (ChatColor.stripColor(s).startsWith("Uses: ")) {
                                    uses = Integer.parseInt(ChatColor.stripColor(s).replaceFirst("Uses: ", ""));
                                } else if (ChatColor.stripColor(s).startsWith("Max uses: ")) {
                                    maxUses = Integer.parseInt(ChatColor.stripColor(s).replaceFirst("Max uses: ", ""));
                                } else if (ChatColor.stripColor(s).startsWith("Reward exp: ")) {
                                    rewardExp = Boolean.parseBoolean(ChatColor.stripColor(s).replaceFirst("Reward exp: ", ""));
                                } else {
                                    r.debug("Failed to parse info String: " + s);
                                }
                            }
                        }
                        ItemStack paper = new ItemStack(Material.PAPER);
                        ItemMeta paper_meta = paper.getItemMeta();

                        paper_meta.setDisplayName(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + uses);
                        paper.setItemMeta(paper_meta);
                        settings.setItem(28, paper);

                        ItemStack clay = new ItemStack(Material.STAINED_CLAY, 1, rewardExp ? (byte) 13 : (byte) 14);
                        ItemMeta clay_meta = clay.getItemMeta();
                        clay_meta.setDisplayName(ChatColor.DARK_AQUA + "Generate exp: " + ChatColor.AQUA + rewardExp);
                        clay.setItemMeta(clay_meta);
                        settings.setItem(22, clay);

                        paper_meta.setDisplayName(ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + maxUses);
                        paper.setItemMeta(paper_meta);
                        settings.setItem(34, paper);

                        ItemStack plus = new ItemStack(Material.STAINED_GLASS_PANE, 0, (byte) 13);
                        ItemMeta plus_meta = plus.getItemMeta();

                        plus.setAmount(1);
                        plus_meta.setDisplayName(ChatColor.DARK_GREEN + "Add 1");
                        plus.setItemMeta(plus_meta);
                        settings.setItem(47, plus);
                        settings.setItem(53, plus);

                        plus.setAmount(5);
                        plus_meta.setDisplayName(ChatColor.DARK_GREEN + "Add 5");
                        plus.setItemMeta(plus_meta);
                        settings.setItem(38, plus);
                        settings.setItem(44, plus);

                        plus.setAmount(10);
                        plus_meta.setDisplayName(ChatColor.DARK_GREEN + "Add 10");
                        plus.setItemMeta(plus_meta);
                        settings.setItem(29, plus);
                        settings.setItem(35, plus);

                        plus.setAmount(20);
                        plus_meta.setDisplayName(ChatColor.DARK_GREEN + "Add 20");
                        plus.setItemMeta(plus_meta);
                        settings.setItem(20, plus);
                        settings.setItem(26, plus);

                        plus.setAmount(50);
                        plus_meta.setDisplayName(ChatColor.DARK_GREEN + "Add 50");
                        plus.setItemMeta(plus_meta);
                        settings.setItem(11, plus);
                        settings.setItem(17, plus);

                        ItemStack min = new ItemStack(Material.STAINED_GLASS_PANE, 0, (byte) 14);
                        ItemMeta min_meta = plus.getItemMeta();

                        min.setAmount(1);
                        min_meta.setDisplayName(ChatColor.DARK_RED + "Remove 1");
                        min.setItemMeta(min_meta);
                        settings.setItem(45, min);
                        settings.setItem(51, min);

                        min.setAmount(5);
                        min_meta.setDisplayName(ChatColor.DARK_RED + "Remove 5");
                        min.setItemMeta(min_meta);
                        settings.setItem(36, min);
                        settings.setItem(42, min);

                        min.setAmount(10);
                        min_meta.setDisplayName(ChatColor.DARK_RED + "Remove 10");
                        min.setItemMeta(min_meta);
                        settings.setItem(27, min);
                        settings.setItem(33, min);

                        min.setAmount(20);
                        min_meta.setDisplayName(ChatColor.DARK_RED + "Remove 20");
                        min.setItemMeta(min_meta);
                        settings.setItem(18, min);
                        settings.setItem(24, min);

                        min.setAmount(50);
                        min_meta.setDisplayName(ChatColor.DARK_RED + "Remove 50");
                        min.setItemMeta(min_meta);
                        settings.setItem(9, min);
                        settings.setItem(15, min);

                        ItemStack close = new ItemStack(Material.BARRIER);
                        ItemMeta close_meta = close.getItemMeta();
                        close_meta.setDisplayName(ChatColor.DARK_RED + "Close");
                        close.setItemMeta(close_meta);
                        settings.setItem(40, close);

                        e.getWhoClicked().closeInventory();
                        e.getWhoClicked().openInventory(settings);
                    }
                }, 1L);
            }
        }

        return true;
    }

    public static void closeInv(HumanEntity p, Inventory inv) {
        if (blockClose) {
            return;
        }
        if (!currentVillager.containsKey(p.getUniqueId())) {
            return;
        }
        if (inv.getTitle().equalsIgnoreCase("Villager Editor (Settings)")) {
            //closeInv(p, editInv.get(p.getUniqueId()));
            return;
        }
        savePage(p, inv, pages.get(p.getUniqueId()));
    }

    static void savePage(HumanEntity p, Inventory inv, Integer page) {
        r.debug("Save page " + page);
        List<VillagerUtil.VillagerTrade> trades = VillagerUtil.listTrades(currentVillager.get(p.getUniqueId()));
        r.debug("[1] " + trades);
        int cur = ((page - 1) * 9);
        if (cur < 0) {
            cur = 0;
        }
        r.debug("Cur " + cur);
        try {
            Integer times = 1;
            while (times <= 9) {
                r.debug("Remove " + cur + " (" + times + ")");
                trades.remove(cur);
                times++;
            }
        } catch (Exception ex) {
        }
        r.debug("[2] " + trades);

        for (int i : Arrays.asList(9, 18, 27, 36, 45, 14, 23, 32, 41)) {
            ItemStack lapis = inv.getItem(i + 3);
            if (lapis != null && lapis.getType().equals(Material.LAPIS_BLOCK)) {
                Integer maxUses = 7;
                Integer uses = 0;
                Boolean rewardExp = true;
                for (String s : lapis.getItemMeta().getLore()) {
                    if (ChatColor.stripColor(s).startsWith("Uses: ")) {
                        uses = Integer.parseInt(ChatColor.stripColor(s).replaceFirst("Uses: ", ""));
                    } else if (ChatColor.stripColor(s).startsWith("Max uses: ")) {
                        maxUses = Integer.parseInt(ChatColor.stripColor(s).replaceFirst("Max uses: ", ""));
                    } else if (ChatColor.stripColor(s).startsWith("Reward exp: ")) {
                        rewardExp = Boolean.parseBoolean(ChatColor.stripColor(s).replaceFirst("Reward exp: ", ""));
                    } else {
                        r.debug("Failed to parse info String: " + s);
                    }
                }

                if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR || inv.getItem(i + 2) == null || inv.getItem(i + 2).getType() == Material.AIR) {
                    continue;
                }
                r.debug("Slot " + i + " count " + inv.getItem(i).getAmount());
                if (inv.getItem(i + 1) != null && inv.getItem(i + 1).getType() != Material.AIR) {
                    trades.add(cur, new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 1), inv.getItem(i + 2), maxUses, uses, rewardExp));
                } else {
                    trades.add(cur, new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 2), maxUses, uses, rewardExp));
                }
                cur++;
            }
        }
        r.debug("[3] " + trades);
        new ReflectionUtil.ReflectionObject(currentVillager.get(p.getUniqueId())).invoke("getHandle").set("tradingPlayer", null);
        VillagerUtil.setTrades(currentVillager.get(p.getUniqueId()), trades);
    }

    static boolean isEditableSlot(int slot) {
        Integer s1 = 9, s2 = 18, s3 = 27, s4 = 36, s5 = 45, s6 = 14, s7 = 23, s8 = 32, s9 = 41;
        if (slot >= s1 && slot <= (s1 + 2)) {
            return true;
        }
        if (slot >= s2 && slot <= (s2 + 2)) {
            return true;
        }
        if (slot >= s3 && slot <= (s3 + 2)) {
            return true;
        }
        if (slot >= s4 && slot <= (s4 + 2)) {
            return true;
        }
        if (slot >= s5 && slot <= (s5 + 2)) {
            return true;
        }
        if (slot >= s6 && slot <= (s6 + 2)) {
            return true;
        }
        if (slot >= s7 && slot <= (s7 + 2)) {
            return true;
        }
        if (slot >= s8 && slot <= (s8 + 2)) {
            return true;
        }
        if (slot >= s9 && slot <= (s9 + 2)) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "villager";
    }

    @Override
    public String getPermission() {
        return "uc.villager";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("editvillager");
    }

    @Override
    public void run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.villager", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        usedCommand.add(p.getUniqueId());
        r.sendMes(p, "villagerMessage");
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
