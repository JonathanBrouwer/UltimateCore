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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.ItemUtil;
import bammerbom.ultimatecore.bukkit.resources.utils.VillagerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CmdVillager implements UltimateCommand {

    /**
     * Contains a player when he used /villager, but didn't click a villager yet
     */
    public static ArrayList<UUID> usedCommand = new ArrayList<>();

    /**
     * Contains the player's uuid, and the uuid villager he is editing
     */
    public static HashMap<UUID, UUID> villager = new HashMap<>();

    /**
     * Contains the player's uuid, and the list of trades of the villager he is editing with <Index, Trade>
     */
    public static HashMap<UUID, HashMap<Integer, VillagerUtil.VillagerTrade>> trades = new HashMap<>();

    /**
     * Contain's the player's uuid, and the totalIndex of the settings the player is editing
     */
    public static HashMap<UUID, Integer> inSettings = new HashMap<>();


    /**
     * Whether close inventory events should be ignored
     */
    public static boolean ignoreClose = false;

    //----------------------------------------------------------------------------------------------

    /**
     * Called when a player right clicks a villager
     *
     * @return whether the event should be cancelled
     */
    public static boolean confirm(Player p, Villager v) {
        if (!usedCommand.contains(p.getUniqueId())) {
            return false;
        }
        usedCommand.remove(p.getUniqueId());

        //Put information in hashmaps
        villager.put(p.getUniqueId(), v.getUniqueId());
        HashMap<Integer, VillagerUtil.VillagerTrade> tlist = new HashMap<>();
        int i = 0;
        for (VillagerUtil.VillagerTrade trade : VillagerUtil.listTrades(v)) {
            tlist.put(i, trade);
            i++;
        }
        trades.put(p.getUniqueId(), tlist);

        //Open page 1
        openPage(p, 1);

        //Return
        return true;
    }

    /**
     * Open a specific page for a player.
     *
     * @return The inventory, which will already be opened by this method
     */
    public static Inventory openPage(Player p, int page) {
        //Create inventory
        Inventory inv = Bukkit.createInventory(p, 54, "Villager Editor (Page " + page + ")");

        //Put green glass in the inventory
        ItemStack green = ItemUtil.createItem(Material.STAINED_GLASS_PANE, 5, "", new ArrayList<String>());
        inv = ItemUtil.putInInventory(inv, green, 3, 4, 8, 13, 22, 31, 40, 49, 52, 53);

        //Put paper with instructions in the inventory
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.PAPER, 0, ChatColor.AQUA + "Buying item 1", new ArrayList<String>()), 0, 5);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.PAPER, 0, ChatColor.AQUA + "Buying item 2", new ArrayList<String>()), 1, 6);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.PAPER, 0, ChatColor.AQUA + "Selling item", new ArrayList<String>()), 2, 7);
        //Put the trades in the inventory
        for (int i = 1; i <= 9; i++) {
            inv = addTradeToInv(p, inv, page, i);
        }
        //Put page forward & backward arrows in inventory
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.FEATHER, 0, ChatColor.RED + "Previous page", new ArrayList<String>()), 50);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.FEATHER, 0, ChatColor.GREEN + "Next page", new ArrayList<String>()), 51);

        //Open Inventory
        ignoreClose = true;
        p.openInventory(inv);
        ignoreClose = false;
        return inv;
    }

    /**
     * Open a specific settings page for a player.
     *
     * @return The inventory, which will already be opened by this method
     */
    public static Inventory openSettings(Player p, int totalIndex) {
        //Update hashmaps
        inSettings.put(p.getUniqueId(), totalIndex);
        //Create inventory
        Inventory inv = Bukkit.createInventory(p, 54, "Villager Trade Settings");

        //Put the trade in the inventory
        VillagerUtil.VillagerTrade trade = trades.get(p.getUniqueId()).get(totalIndex);
        inv.setItem(3, trade.getItem1());
        inv.setItem(4, trade.getItem2());
        inv.setItem(5, trade.getRewardItem());
        //Put blue (neutral) glass in inventory
        ItemStack blue = ItemUtil.createItem(Material.STAINED_GLASS_PANE, 11, "", new ArrayList<String>());
        inv = ItemUtil.putInInventory(inv, blue, 0, 1, 2, 6, 7, 8, 10, 12, 13, 14, 16, 19, 21, 23, 25, 30, 31, 32, 37, 39, 41, 43, 46, 48, 49, 50, 52);
        //Put red (remove) glass in inventory
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 14, ChatColor.RED + "Remove 1", new ArrayList<String>()), 45, 51);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 5, 14, ChatColor.RED + "Remove 5", new ArrayList<String>()), 36, 42);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 10, 14, ChatColor.RED + "Remove 10", new ArrayList<String>()), 27, 33);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 20, 14, ChatColor.RED + "Remove 20", new ArrayList<String>()), 18, 24);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 50, 14, ChatColor.RED + "Remove 50", new ArrayList<String>()), 9, 15);
        //Put green (add) glass in inventory
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 13, ChatColor.GREEN + "Add 1", new ArrayList<String>()), 47, 53);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 5, 13, ChatColor.GREEN + "Add 5", new ArrayList<String>()), 38, 44);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 10, 13, ChatColor.GREEN + "Add 10", new ArrayList<String>()), 29, 35);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 20, 13, ChatColor.GREEN + "Add 20", new ArrayList<String>()), 20, 26);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_GLASS_PANE, 50, 13, ChatColor.GREEN + "Add 50", new ArrayList<String>()), 11, 17);
        //Put rest in inventory
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.PAPER, 0, ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + trade.getUses(), new ArrayList<String>()), 28);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.PAPER, 0, ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + trade.getMaxUses(), new ArrayList<String>()), 34);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_CLAY, trade.getRewardExp() ? 13 : 14, ChatColor.DARK_AQUA + "Generate experience: " + ChatColor.AQUA +
                trade.getRewardExp(), new ArrayList<String>()), 22);
        inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.BARRIER, 0, ChatColor.DARK_RED + "Close", new ArrayList<String>()), 40);
        //Open inventory
        p.openInventory(inv);
        return inv;
    }

    /**
     * Puts a certain trade in the inventory
     *
     * @param tradeIndex the index on the page, so the first item on page 3 has tradeIndex 1
     * @return
     */
    public static Inventory addTradeToInv(Player p, Inventory inv, int page, int tradeIndex) {
        //Calculate indexes
        //totalIndex = index over all pages
        int totalIndex = ((page - 1) * 9) + (tradeIndex - 1);
        if (trades.get(p.getUniqueId()).size() < totalIndex + 1) {
            return inv;
        }
        int[] slotList = {9, 18, 27, 36, 45, 14, 23, 32, 41};
        //startSlot = slot to put the trade
        int startSlot = slotList[tradeIndex - 1];

        //Get trade
        VillagerUtil.VillagerTrade trade = trades.get(p.getUniqueId()).get(totalIndex);

        //Put items in inventory
        inv.setItem(startSlot, trade.getItem1());
        inv.setItem(startSlot + 1, trade.getItem2());
        inv.setItem(startSlot + 2, trade.getRewardItem());

        //Create edit item & put it in inventory
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + trade.getUses());
        lore.add(ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + trade.getMaxUses());
        lore.add(ChatColor.DARK_AQUA + "Reward xp: " + ChatColor.AQUA + trade.getRewardExp());
        ItemStack edit = ItemUtil.createItem(Material.LAPIS_BLOCK, 0, ChatColor.DARK_AQUA + "Click to edit", lore);
        inv.setItem(startSlot + 3, edit);
        return inv;
    }

    /**
     * Called when a player clicks on an item in the inventory
     *
     * @return whether the event should be cancelled
     */
    public static boolean clickButton(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (!villager.containsKey(p.getUniqueId())) {
            return false;
        }
        Inventory inv = e.getClickedInventory();
        if (inv == null || inv.getName() == null) {
            return false;
        }
        //If in main villager editor screen
        if (inv.getName().startsWith("Villager Editor (Page ")) {
            int slot = e.getSlot();
            final int page = Integer.parseInt(inv.getName().split("Villager Editor \\(Page ")[1].split("\\)")[0]);
            int relIndex = 0;
            for (int i : new int[]{9, 18, 27, 36, 45, 14, 23, 32, 41}) {
                relIndex++;
                if (slot == i || slot - 1 == i || slot - 2 == i) {
                    //PLAYER CLICKED ON 1, 2, OR 3
                    updateInventory(p, inv, page);
                    final Inventory newinv = inv;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
                        @Override
                        public void run() {
                            updateInventory(p, newinv, page);
                        }
                    }, 5L);
                    return false;
                }
                if (slot - 3 == i && inv.getItem(slot) != null && inv.getItem(slot).getType() == Material.LAPIS_BLOCK) {
                    //PLAYER CLICKED EDIT BUTTON
                    saveInventory(p, inv, true);
                    int totalIndex = ((page - 1) * 9) + (relIndex - 1);
                    openSettings(p, totalIndex);
                }
            }
            final Inventory newinv = inv;
            return true;
        }

        //If in trade settings screen
        else if (inv.getName().equals("Villager Trade Settings")) {
            //On click add of remove
            if (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                boolean isAdd = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).startsWith("Add");
                if (Arrays.asList(new Integer[]{9, 18, 27, 36, 45, 11, 20, 29, 38, 47}).contains(Integer.valueOf(e.getSlot()))) {
                    int current = Integer.parseInt(ChatColor.stripColor(inv.getItem(28).getItemMeta().getDisplayName()).split("Uses: ")[1]);
                    int add = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[1]) * (isAdd ? 1 : -1);
                    inv.setItem(28, ItemUtil.createItem(Material.PAPER, 0, ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + (current + add), new ArrayList<String>()));
                } else if (Arrays.asList(new Integer[]{15, 24, 33, 42, 51, 17, 26, 35, 44, 53}).contains(Integer.valueOf(e.getSlot()))) {
                    int current = Integer.parseInt(ChatColor.stripColor(inv.getItem(34).getItemMeta().getDisplayName()).split("Max uses: ")[1]);
                    int add = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).split(" ")[1]) * (isAdd ? 1 : -1);
                    inv.setItem(34, ItemUtil.createItem(Material.PAPER, 0, ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + (current + add), new ArrayList<String>()));
                }
            }
            if (e.getSlot() == 22) {
                boolean exp = !Boolean.parseBoolean(ChatColor.stripColor(inv.getItem(22).getItemMeta().getDisplayName()).split("Generate experience: ")[1]);
                inv = ItemUtil.putInInventory(inv, ItemUtil.createItem(Material.STAINED_CLAY, exp ? 13 : 14, ChatColor.DARK_AQUA + "Generate experience: " + ChatColor.AQUA +
                        exp, new ArrayList<String>()), 22);
            }
            if (e.getSlot() == 40) {
                closeInv(p, inv);
            }
            return true;
        }

        //Else?
        else {
            //Idk what happened, just return & don't cancel
            return false;
        }
    }

    /**
     * Save the current inventory to the hashmaps
     */
    public static void updateInventory(Player p, Inventory inv, int page) {
        //Loop trough all buying item 1 slots
        int relIndex = 1;
        for (int i : new int[]{9, 18, 27, 36, 45, 14, 23, 32, 41}) {
            //Get trade
            int totalIndex = ((page - 1) * 9) + (relIndex - 1);
            relIndex++;
            VillagerUtil.VillagerTrade trade = trades.containsKey(p.getUniqueId()) ? trades.get(p.getUniqueId()).get(totalIndex) : null;

            //True if at least buying item 1 & selling item
            boolean valid = true;
            //True if at least one slot contains an item
            boolean one = false;
            //Check if there is an item in buying item 1
            if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
                valid = false;
            } else {
                one = true;
            }
            //Check if there is an item in buying item 2
            if (inv.getItem(i + 1) != null && inv.getItem(i + 1).getType() != Material.AIR) {
                one = true;
            }
            //Check if there is an item in selling item
            if (inv.getItem(i + 2) == null || inv.getItem(i + 2).getType() == Material.AIR) {
                valid = false;
            } else {
                one = true;
            }

            //Get the trade
            if (valid) {
                if (trade == null) {
                    if (inv.getItem(i + 1) != null && inv.getItem(i + 1).getType() != Material.AIR) {
                        trade = new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 1), inv.getItem(i + 2), 7, 0, true);
                    } else {
                        trade = new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 2), 7, 0, true);
                    }
                } else {
                    if (inv.getItem(i + 1) != null && inv.getItem(i + 1).getType() != Material.AIR) {
                        trade = new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 1), inv.getItem(i + 2), trade.getMaxUses(), trade.getUses(), trade.getRewardExp());
                    } else {
                        trade = new VillagerUtil.VillagerTrade(inv.getItem(i), inv.getItem(i + 2), trade.getMaxUses(), trade.getUses(), trade.getRewardExp());
                    }
                }
                //Create edit item & put it in inventory
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.DARK_AQUA + "Uses: " + ChatColor.AQUA + trade.getUses());
                lore.add(ChatColor.DARK_AQUA + "Max uses: " + ChatColor.AQUA + trade.getMaxUses());
                lore.add(ChatColor.DARK_AQUA + "Reward xp: " + ChatColor.AQUA + trade.getRewardExp());
                ItemStack edit = ItemUtil.createItem(Material.LAPIS_BLOCK, 0, ChatColor.DARK_AQUA + "Click to edit", lore);
                inv.setItem(i + 3, edit);
                //Add/update trade to list
                HashMap<Integer, VillagerUtil.VillagerTrade> tlist = trades.get(p.getUniqueId());
                tlist.put(totalIndex, trade);
                trades.put(p.getUniqueId(), tlist);
            } else if (one) {
                //Set item
                ItemStack edit = ItemUtil.createItem(Material.STAINED_GLASS_PANE, 14, ChatColor.DARK_AQUA + "Click to edit", Arrays.asList(ChatColor.RED + "Invalid trade."));
                inv.setItem(i + 3, edit);
                //Remove trade from list
                HashMap<Integer, VillagerUtil.VillagerTrade> tlist = trades.get(p.getUniqueId());
                tlist.remove(totalIndex);
                trades.put(p.getUniqueId(), tlist);
            } else {
                //Set item
                inv.setItem(i + 3, null);
                //Remove trade from list
                HashMap<Integer, VillagerUtil.VillagerTrade> tlist = trades.get(p.getUniqueId());
                tlist.remove(totalIndex);
                trades.put(p.getUniqueId(), tlist);
            }
        }
    }

    /**
     * Save the current inventory to the villager
     */
    public static void saveInventory(Player p, Inventory inv, boolean main) {
        if (main) {
            updateInventory(p, inv, Integer.parseInt(inv.getName().split("Villager Editor \\(Page ")[1].split("\\)")[0]));
            HashMap<Integer, VillagerUtil.VillagerTrade> tlist = trades.get(p.getUniqueId());
            //Get the villager instance
            Villager v = null;
            for (Entity en : p.getWorld().getEntities()) {
                if (en instanceof Villager && en.getUniqueId().equals(villager.get(p.getUniqueId()))) {
                    v = (Villager) en;
                }
            }
            if (v == null) {
                p.sendMessage(ChatColor.RED + "Invalid villager, is the villager dead?");
                return;
            }
            //Save trades
            ArrayList<VillagerUtil.VillagerTrade> tlist2 = new ArrayList<>();
            for (VillagerUtil.VillagerTrade trade_ : tlist.values()) {
                tlist2.add(trade_);
            }
            VillagerUtil.setTrades(v, tlist2);
        } else {
            //Get data
            boolean exp = Boolean.parseBoolean(ChatColor.stripColor(inv.getItem(22).getItemMeta().getDisplayName()).split("Generate experience: ")[1]);
            int uses = Integer.parseInt(ChatColor.stripColor(inv.getItem(28).getItemMeta().getDisplayName()).split("Uses: ")[1]);
            int maxuses = Integer.parseInt(ChatColor.stripColor(inv.getItem(34).getItemMeta().getDisplayName()).split("Max uses: ")[1]);

            //Get trade
            int totalIndex = inSettings.get(p.getUniqueId());
            VillagerUtil.VillagerTrade trade = trades.get(p.getUniqueId()).get(totalIndex);
            VillagerUtil.VillagerTrade newtrade = new VillagerUtil.VillagerTrade(trade.getItem1(), trade.getItem2(), trade.getRewardItem(), maxuses, uses, exp);

            //Save trade to hashmap
            HashMap<Integer, VillagerUtil.VillagerTrade> tlist = trades.get(p.getUniqueId());
            tlist.put(totalIndex, newtrade);
            trades.put(p.getUniqueId(), tlist);
        }
    }

    /**
     * Called when a player closes an inventory
     */
    public static void closeInv(Player p, Inventory inv) {
        if (!villager.containsKey(p.getUniqueId()) || ignoreClose) {
            return;
        }
        //If in main villager editor screen
        if (inv.getTitle().startsWith("Villager Editor (Page ") && !inSettings.containsKey(p.getUniqueId())) {
            saveInventory(p, inv, true);
            villager.remove(p.getUniqueId());
            trades.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GREEN + "Villager edit completed succesfully.");
        }

        //If in trade settings screen
        else if (inv.getName().startsWith("Villager Trade Settings")) {
            saveInventory(p, inv, false);
            int totalIndex = inSettings.get(p.getUniqueId());
            int page = 1;
            while (totalIndex >= 9) {
                page++;
                totalIndex -= 9;
            }
            openPage(p, page);
            inSettings.remove(p.getUniqueId());
            return;
        }
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
    public void run(final CommandSender cs, String label, String[] args) {
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
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
