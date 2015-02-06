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
package bammerbom.ultimatecore.bukkit;

import bammerbom.ultimatecore.bukkit.UltimateUpdater.UpdateResult;
import bammerbom.ultimatecore.bukkit.UltimateUpdater.UpdateType;
import bammerbom.ultimatecore.bukkit.configuration.Config;
import bammerbom.ultimatecore.bukkit.resources.classes.ErrorLogger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class r {

    static {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vault = new r().new Vault();
        }
    }

    //Colors
    public static ChatColor negative = ChatColor.RED;
    public static ChatColor neutral = ChatColor.AQUA;
    public static ChatColor positive = ChatColor.DARK_AQUA;

    //Updater
    public static UltimateUpdater updater;
    //Updater end
    //Metrics
    public static UltimateMetrics metrics;
    public static ResourceBundle en = null;
    public static ResourceBundle cu = null;
    public static Random ra = new Random();
    //Vault end
    //Methods
    static UltimateCore uc = UltimateCore.getInstance();
    static boolean debug = false;
    //Config end
    //Vault
    private static Vault vault;

    public static UltimateUpdater getUpdater() {
        return updater;
    }

    public static void runUpdater() {
        if (!r.getCnfg().getBoolean("Updater.check")) {
            return;
        }
        Boolean dl = r.getCnfg().getBoolean("Updater.download");
        updater = new UltimateUpdater(r.getUC(), 66979, UltimateCore.getPluginFile(), dl ? UpdateType.DEFAULT : UpdateType.NO_DOWNLOAD, true);
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                updater.waitForThread();
                try {
                    if (updater != null && updater.getResult() != null && updater.getResult().equals(UpdateResult.UPDATE_AVAILABLE)) {
                        r.log("There is an update available for UltimateCore.");
                        r.log("Use /uc update to update UltimateCore.");
                    }
                } catch (Exception ex) {
                    ErrorLogger.log(ex, "Update check failed.");
                }
            }
        });
        thr.setName("UltimateUpdater");
        thr.start();
    }

    public static void runMetrics() {
        if (!r.getCnfg().getBoolean("Metrics")) {
            return;
        }
        try {
            metrics = new UltimateMetrics(getUC());
            metrics.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Metrics end
    //Config
    public static Config getCnfg() {
        if (!new File(r.getUC().getDataFolder(), "config.yml").exists()) {
            if (new File(r.getUC().getDataFolder(), "config_CORRUPT.yml").exists()) {
                String filename = "config_CORRUPT.yml";
                Integer i = 1;
                while (new File(r.getUC().getDataFolder(), filename).exists()) {
                    i++;
                    filename = "config_CORRUPT" + i + ".yml";
                }
                i--;
                if (!(i == 0)) {
                    filename = "config_CORRUPT" + i + ".yml";
                }
                r.log(ChatColor.GOLD + "---------------------------------------------------");
                r.log(ChatColor.AQUA + "Config file failed to load, creating a new file...");
                r.log(ChatColor.AQUA + "Corrupt file saved as " + ChatColor.YELLOW + filename);
                r.log(ChatColor.GOLD + "----------------------------------------------------");
            } else {
                r.log(ChatColor.GOLD + "-------------------------------------------------");
                r.log(ChatColor.AQUA + "Config file doesn't exist, creating a new file...");
                r.log(ChatColor.GOLD + "-------------------------------------------------");
            }
            UltimateFileLoader.Enable();
        }
        return new Config(new File(r.getUC().getDataFolder(), "config.yml"));
    }

    public static Vault getVault() {
        return vault;
    }

    public static UltimateCore getUC() {
        if (uc == null) {
            uc = UltimateCore.getInstance();
        }
        return uc;
    }

    protected static void removeUC() {
        uc = null;
        updater = null;
        metrics = null;
        vault = null;
    }

    public static boolean isPlayer(CommandSender cs) {
        if (cs instanceof Player) {
            return true;
        }
        r.sendMes(cs, "notPlayer");
        return false;
    }

    public static boolean perm(CommandSender cs, String perm, Boolean def, Boolean message) {
        if (!(cs instanceof Player)) {
            return true;
        }
        Player pl = (Player) cs;
        Boolean hasperm = perm(pl, perm, def);
        if (hasperm == false && message == true) {
            r.sendMes(cs, "noPermissions");
        }
        return hasperm;
    }

    private static boolean perm(Player p, String perm, Boolean def) {
        if (p.isOp()) {
            return true;
        }
        if (r.getVault() != null && r.getVault().getPermission() != null && !r.getVault().getPermission().getName().equals("SuperPerms")) {
            return r.getVault().getPermission().has(p, perm);
        } else {
            if (def == true) {
                return true;
            }
            return p.hasPermission(perm);
        }
    }

    public static boolean checkArgs(Object[] args, Integer numb) {
        try {
            args[numb].equals("Ritja");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getFinalArg(String[] args, int start) {
        StringBuilder bldr = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }
        return bldr.toString();
    }

    public static void EnableMES() {
        try {
            FileInputStream ENs = new FileInputStream(UltimateFileLoader.ENf);
            FileInputStream CUs = new FileInputStream(UltimateFileLoader.LANGf);
            en = new PropertyResourceBundle(ENs);
            cu = new PropertyResourceBundle(CUs);
            ENs.close();
            CUs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String mes(String padMessage, Object... repl) {
        if (cu.containsKey(padMessage)) {
            String a = r.positive + ChatColor.translateAlternateColorCodes('&', cu.getString(padMessage)
                    .replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("\\\\n", "\n"));
            String repA = null;
            for (Object s : repl) {
                if (repA == null) {
                    repA = s.toString();
                } else {
                    a = a.replace(repA, s.toString());
                    repA = null;
                }
            }
            return a;
        }
        if (en.containsKey(padMessage)) {
            String b = r.positive + ChatColor.translateAlternateColorCodes('&', en.getString(padMessage)
                    .replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("\\\\n", "\n"));
            String repB = null;
            for (Object s : repl) {
                if (repB == null) {
                    repB = s.toString();
                } else {
                    b = b.replace(repB, s.toString());
                    repB = null;
                }
            }
            return b;
        }
        r.log(r.negative + "Failed to find " + padMessage + " in Messages file.");
        return "";
    }

    public static void sendMes(CommandSender cs, String padMessage, Object... repl) {
        String mes = mes(padMessage, repl);
        cs.sendMessage(mes);
    }

    public static void log(Object message) {
        if (message == null) {
            r.log("null");
            return;
        }
        String logo = ChatColor.translateAlternateColorCodes('&', "&9[&bUC&9]&r");
        Bukkit.getConsoleSender().sendMessage(logo + " " + ChatColor.YELLOW + message.toString());
        //
    }

    public static void debug(Object message) {
        if (!debug) {
            return;
        }
        log(ChatColor.WHITE + message.toString());
        //
    }

    @SuppressWarnings("deprecation")
    public static Player[] getOnlinePlayers() {
        List<Player> plz = (List<Player>) Bukkit.getOnlinePlayers();
        return plz.toArray(new Player[plz.size()]);
    }

    public static List<Player> getOnlinePlayersL() {
        return (List<Player>) Bukkit.getOnlinePlayers();
    }

    public static OfflinePlayer[] getOfflinePlayers() {
        List<OfflinePlayer> plz = Arrays.asList(Bukkit.getOfflinePlayers());
        return plz.toArray(new OfflinePlayer[plz.size()]);
    }

    public static List<OfflinePlayer> getOfflinePlayersL() {
        return (List<OfflinePlayer>) Arrays.asList(Bukkit.getOfflinePlayers());
    }

    public static Player searchPlayer(String s) {
        Player found = null;
        String lowerName = s.toLowerCase();
        {
            int delta = 2147483647;
            for (Player player : getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(lowerName)) {
                    int curDelta = player.getName().length() - lowerName.length();
                    if (curDelta < delta) {
                        found = player;
                        delta = curDelta;
                    }
                    if (curDelta == 0) {
                        break;
                    }
                }
            }
        }
        return found;
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer searchOfflinePlayer(String s) {
        return Bukkit.getOfflinePlayer(s);
    }

    public static Player searchPlayer(UUID u) {
        return Bukkit.getPlayer(u);
    }

    public static OfflinePlayer searchOfflinePlayer(UUID u) {
        return Bukkit.getOfflinePlayer(u);
    }

    //
    public static boolean isInt(String check) {
        try {
            Integer.parseInt(check);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    //
    public static Integer normalize(Integer a, Integer b, Integer c) {
        if (a < b) {
            a = b;
        }
        if (a > c) {
            a = c;
        }
        return a;
    }

    public static Double normalize(Double a, Double b, Double c) {
        if (a < b) {
            a = b;
        }
        if (a > c) {
            a = c;
        }
        return a;
    }

    public static ChatColor getRandomChatColor() {
        ArrayList<ChatColor> values = new ArrayList<>();
        for (ChatColor c : ChatColor.values()) {
            if (!c.isFormat()) {
                values.add(c);
            }
        }
        return values.get(ra.nextInt(values.size()));
    }

    public class Vault {

        private Permission permission = null;
        private Chat chat = null;
        private Economy economy = null;

        public Vault() {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null && Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                //Permissions
                RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
                if (permissionProvider != null) {
                    permission = permissionProvider.getProvider();
                }
                //Chat
                RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
                if (chatProvider != null) {
                    chat = chatProvider.getProvider();
                }
                RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null) {
                    economy = economyProvider.getProvider();
                }
            }
        }

        public Permission getPermission() {
            return permission;
        }

        public Chat getChat() {
            return chat;
        }

        public Economy getEconomy() {
            return economy;
        }
    }
    //
}
