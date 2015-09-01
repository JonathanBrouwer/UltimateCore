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
package bammerbom.ultimatecore.bukkit.listeners;

import bammerbom.ultimatecore.bukkit.api.UC;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    static HashMap<String, String> lastChatMessage = new HashMap<>();
    static HashMap<String, Integer> lastChatMessageTimes = new HashMap<>();
    static HashMap<String, Integer> spamTime = new HashMap<>();
    static HashMap<String, Integer> swearAmount = new HashMap<>();
    static Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    static Pattern domainPattern = Pattern.compile("(?<!'|\\w)(?:(?:(?:(?!\\w)www.*|.*http.*|[a-zA-Z0-9]|\\s*dash\\s*){1,63}){1,2}(?<!'|\\w)(a+(\\W|_)*c+|a+(\\W|_)*d+|a+(\\W|_)*e+|a+(\\W|_)" +
            "*f+|a+(\\W|_)*g+|a+(\\W|_)*i+|a+(\\W|_)*l+|a+(\\W|_)*m+|a+(\\W|_)*n+|a+(\\W|_)*o+|a+(\\W|_)*q+|a+(\\W|_)*r+|a+(\\W|_)*r+(\\W|_)*p+(\\W|_)*a+|a+(\\W|_)*s+|a+(\\W|_)*t+|a+(\\W|_)*u+|a+" +
            "(\\W|_)*w+|a+(\\W|_)*x+|a+(\\W|_)*z+|b+(\\W|_)*a+|b+(\\W|_)*b+|b+(\\W|_)*d+|b+(\\W|_)*e+|b+(\\W|_)*f+|b+(\\W|_)*g+|b+(\\W|_)*h+|b+(\\W|_)*i+|b+(\\W|_)*i+(\\W|_)*z+|b+(\\W|_)*j+|b+" +
            "(\\W|_)*m+|b+(\\W|_)*n+|b+(\\W|_)*o+|b+(\\W|_)*r+|b+(\\W|_)*s+|b+(\\W|_)*t+|b+(\\W|_)*v+|b+(\\W|_)*w+|b+(\\W|_)*y+|b+(\\W|_)*z+|c+(\\W|_)*a+|c+(\\W|_)*a+(\\W|_)*t+|c+(\\W|_)*c+|c+" +
            "(\\W|_)*d+|c+(\\W|_)*f+|c+(\\W|_)*g+|c+(\\W|_)*h+|c+(\\W|_)*i+|c+(\\W|_)*k+|c+(\\W|_)*l+|c+(\\W|_)*m+|c+(\\W|_)*n+|c+(\\W|_)*o+|c+(\\W|_)*o+(\\W|_)*m+|c+(\\W|_)*o+(\\W|_)*o+(\\W|_)" +
            "*p+|c+(\\W|_)*r+|c+(\\W|_)*u+|c+(\\W|_)*v+|c+(\\W|_)*w+|c+(\\W|_)*x+|c+(\\W|_)*y+|c+(\\W|_)*z+|d+(\\W|_)*e+(?!-)|d+(\\W|_)*j+|d+(\\W|_)*k+|d+(\\W|_)*m+|d+(\\W|_)*o+|d+(\\W|_)*z+|e+" +
            "(\\W|_)*c+|e+(\\W|_)*d+(\\W|_)*u+|e+(\\W|_)*e+|e+(\\W|_)*g+|e+(\\W|_)*r+|e+(\\W|_)*s+|e+(\\W|_)*t+|e+(\\W|_)*u+|f+(\\W|_)*i+|f+(\\W|_)*j+|f+(\\W|_)*k+|f+(\\W|_)*m+|f+(\\W|_)*o+|f+" +
            "(\\W|_)*r+|g+(\\W|_)*a+|g+(\\W|_)*b+|g+(\\W|_)*d+|g+(\\W|_)*e+|g+(\\W|_)*f+|g+(\\W|_)*g+|g+(\\W|_)*h+|g+(\\W|_)*i+|g+(\\W|_)*l+|g+(\\W|_)*m+|g+(\\W|_)*n+|g+(\\W|_)*o+(\\W|_)*v+|g+" +
            "(\\W|_)*p+|g+(\\W|_)*q+|g+(\\W|_)*r+|g+(\\W|_)*s+|g+(\\W|_)*t+|g+(\\W|_)*u+|g+(\\W|_)*w+|g+(\\W|_)*y+|h+(\\W|_)*k+|h+(\\W|_)*m+|h+(\\W|_)*n+|h+(\\W|_)*r+|h+(\\W|_)*t+|h+(\\W|_)*u+|i+" +
            "(\\W|_)*d+|i+(\\W|_)*e+|i+(\\W|_)*l+|i+(\\W|_)*m+|i+(\\W|_)*n+|i+(\\W|_)*n+(\\W|_)*f+(\\W|_)*o+|i+(\\W|_)*n+(\\W|_)*t+|i+(\\W|_)*o+|i+(\\W|_)*q+|i+(\\W|_)*r+|i+(\\W|_)*s+|i+(\\W|_)" +
            "*t+|j+(\\W|_)*e+|j+(\\W|_)*m+|j+(\\W|_)*o+|j+(\\W|_)*p+|k+(\\W|_)*e+|k+(\\W|_)*g+|k+(\\W|_)*h+|k+(\\W|_)*i+|k+(\\W|_)*m+|k+(\\W|_)*n+|k+(\\W|_)*p+|k+(\\W|_)*r+|k+(\\W|_)*w+|k+(\\W|_)" +
            "*y+|k+(\\W|_)*z+|l+(\\W|_)*a+|l+(\\W|_)*b+|l+(\\W|_)*c+|l+(\\W|_)*i+|l+(\\W|_)*k+|l+(\\W|_)*r+|l+(\\W|_)*s+|l+(\\W|_)*t+|l+(\\W|_)*u+|l+(\\W|_)*v+|l+(\\W|_)*y+|m+(\\W|_)*a+|m+(\\W|_)" +
            "*c+|m+(\\W|_)*d+|m+(\\W|_)*e+|m+(\\W|_)*g+|m+(\\W|_)*h+|m+(\\W|_)*i+(\\W|_)*l+|m+(\\W|_)*k+|m+(\\W|_)*l+|m+(\\W|_)*m+|m+(\\W|_)*n+|m+(\\W|_)*o+|m+(\\W|_)*o+(\\W|_)*b+(\\W|_)*i+|m+" +
            "(\\W|_)*p+|m+(\\W|_)*q+|m+(\\W|_)*r+|m+(\\W|_)*s+|m+(\\W|_)*t+|m+(\\W|_)*u+|m+(\\W|_)*v+|m+(\\W|_)*w+|m+(\\W|_)*x+|m+(\\W|_)*y+|m+(\\W|_)*z+|n+(\\W|_)*a+|n+(\\W|_)*c+|n+(\\W|_)*e+|n+" +
            "(\\W|_)*e+(\\W|_)*t+|n+(\\W|_)*f+|n+(\\W|_)*g+|n+(\\W|_)*i+|n+(\\W|_)*l+|n+(\\W|_)*o+|n+(\\W|_)*p+|n+(\\W|_)*r+|n+(\\W|_)*u+|n+(\\W|_)*z+|o+(\\W|_)*m+|o+(\\W|_)*r+(\\W|_)*g+|p+(\\W|_)" +
            "*a+|p+(\\W|_)*e+|p+(\\W|_)*f+|p+(\\W|_)*g+|p+(\\W|_)*h+|p+(\\W|_)*k+|p+(\\W|_)*l+|p+(\\W|_)*m+|p+(\\W|_)*n+|p+(\\W|_)*r+|p+(\\W|_)*r+(\\W|_)*o+|p+(\\W|_)*s+|p+(\\W|_)*t+|p+(\\W|_)" +
            "*w+|p+(\\W|_)*y+|q+(\\W|_)*a+|r+(\\W|_)*e+|r+(\\W|_)*o+|r+(\\W|_)*s+|r+(\\W|_)*u+|r+(\\W|_)*w+|s+(\\W|_)*a+|s+(\\W|_)*b+|s+(\\W|_)*c+|s+(\\W|_)*d+|s+(\\W|_)*e+|s+(\\W|_)*g+|s+(\\W|_)" +
            "*h+|s+(\\W|_)*i+|s+(\\W|_)*j+|s+(\\W|_)*k+|s+(\\W|_)*l+|s+(\\W|_)*m+|s+(\\W|_)*n+|s+(\\W|_)*o+|s+(\\W|_)*r+|s+(\\W|_)*t+|s+(\\W|_)*u+|s+(\\W|_)*v+|s+(\\W|_)*x+|s+(\\W|_)*y+|s+(\\W|_)" +
            "*z+|t+(\\W|_)*c+|t+(\\W|_)*d+|t+(\\W|_)*e+(\\W|_)*l+|t+(\\W|_)*f+|t+(\\W|_)*g+|t+(\\W|_)*h+|t+(\\W|_)*j+|t+(\\W|_)*k+|t+(\\W|_)*l+|t+(\\W|_)*m+|t+(\\W|_)*n+|t+(\\W|_)*o+|t+(\\W|_)" +
            "*p+|t+(\\W|_)*r+|t+(\\W|_)*t+|t+(\\W|_)*v+|t+(\\W|_)*w+|t+(\\W|_)*z+|u+(\\W|_)*a+|u+(\\W|_)*g+|u+(\\W|_)*k+|u+(\\W|_)*s+|u+(\\W|_)*y+|u+(\\W|_)*z+|v+(\\W|_)*a+|v+(\\W|_)*c+|v+(\\W|_)" +
            "*e+|v+(\\W|_)*g+|v+(\\W|_)*i+|v+(\\W|_)*n+|v+(\\W|_)*u+|w+(\\W|_)*f+|w+(\\W|_)*s+|x+(\\W|_)*n+|x+(\\W|_)*x+(\\W|_)*x+|y+(\\W|_)*e+|y+(\\W|_)*t+|z+(\\W|_)*a+|z+(\\W|_)*m+|z+(\\W|_)*w+)" +
            ":*\\d*(?!\\w)(?!\\w|')(?!\\w|,|')/*.*)\n");

    public static void start() {
        ChatListener list = new ChatListener();
        list.spamTask();
        Bukkit.getPluginManager().registerEvents(list, r.getUC());
    }

    private static ChatSet testMessage(String mr, Player p) {
        ChatSet set = new ChatSet(mr);
        if (r.perm(p, "uc.chat", false, false)) {
            return set;
        }
        //Anti REPEAT
        if (!r.perm(p, "uc.chat.repeat", false, false)) {
            if (r.getCnfg().getBoolean("Chat.RepeatFilter")) {
                String lastmessage = "";
                Integer lastmessageTimes = 0;
                if (lastChatMessage.get(p.getName()) != null) {
                    lastmessage = lastChatMessage.get(p.getName());
                    lastmessageTimes = lastChatMessageTimes.get(p.getName());
                }
                lastChatMessage.put(p.getName(), mr);
                lastChatMessageTimes.put(p.getName(), lastmessageTimes + 1);
                if (lastmessage.equalsIgnoreCase(mr)) {
                    if (lastmessageTimes + 1 == 3) {
                        r.sendMes(p, "chatRepeat");
                        set.setCancelled(true);
                    }
                    if (lastmessageTimes + 1 == 4) {
                        r.sendMes(p, "chatRepeat");
                        set.setCancelled(true);
                    }
                    if (lastmessageTimes + 1 == 5) {
                        UC.getPlayer(p).setMuted(true, 60000L);
                        set.setCancelled(true);
                    }
                } else {
                    lastChatMessageTimes.put(p.getName(), 1);
                }
            }
        }
        //Anti SPAM
        if (!r.perm(p, "uc.chat.spam", false, false)) {
            if (r.getCnfg().getBoolean("Chat.SpamFilter")) {
                if (spamTime.containsKey(p.getName())) {
                    Integer amount = spamTime.get(p.getName());
                    spamTime.put(p.getName(), amount + 1);
                    if (amount >= 4) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mute " + p.getName() + " 5m");
                        set.setCancelled(true);
                    } else if (amount >= 3) {
                        r.sendMes(p, "chatSpam");
                    }
                } else {
                    spamTime.put(p.getName(), 1);
                }
            }
        }
        //Anti SWEAR
        if (!r.perm(p, "uc.chat.swear", false, false)) {
            if (r.getCnfg().getBoolean("Chat.SwearFilter") || r.getCnfg().getBoolean("Chat.SwearFiler")) {
                Boolean stop = false;
                for (String sw : r.getCnfg().getStringList("SwearWords")) {
                    if (mr.toLowerCase().contains(sw.toLowerCase())) {
                        //set.setCancelled(true);
                        if (!stop) {
                            stop = true;
                            Integer s = swearAmount.get(p.getName());
                            if (s == null) {
                                s = 0;
                            }
                            s++;
                            swearAmount.put(p.getName(), s);
                            r.sendMes(p, "chatSwear");
                            if (s >= 3) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mute " + p.getName() +
                                        " 5m");
                                set.setCancelled(true);
                            }
                        }
                        set.setMessage(set.getMessage().replaceAll("(?i)" + sw, "****"));
                    }
                }
            }
        }
        //Anti CAPS
        if (!r.perm(p, "uc.chat.caps", false, false)) {
            if (r.getCnfg().get("Chat.CapsFilter") == null || r.getCnfg().getBoolean("Chat.CapsFilter")) {
                double msglength = set.getMessage().toCharArray().length;
                double capsCountt = 0.0D;
                if (msglength > 9.0) {
                    for (char c : set.getMessage().toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            capsCountt += 1.0D;
                        }
                        if (!Character.isLetterOrDigit(c)) {
                            msglength -= 1.0D;
                        }
                    }
                }
                if ((capsCountt / msglength * 100) > 60.0) {
                    set.setMessage(StringUtil.firstUpperCase(set.getMessage().toLowerCase()));
                }
            }
        }
        //Anti IP
        if (!r.perm(p, "uc.chat.ip", false, false)) {
            if (r.getCnfg().getBoolean("Chat.IpFilter")) {
                if (ipPattern.matcher(set.getMessage()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatIp");
                }
            }
        }
        //Anti URL
        if (!r.perm(p, "uc.chat.url", false, false)) {
            if (r.getCnfg().getBoolean("Chat.UrlFilter")) {
                if (domainPattern.matcher(set.getMessage()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatUrl");
                }
            }
        }
        return set;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void ChatListener(AsyncPlayerChatEvent e) {
        if (!e.isCancelled() && !UC.getPlayer(e.getPlayer()).isMuted()) {
            String m = e.getMessage();
            if (r.perm(e.getPlayer(), "uc.coloredchat", false, false)) {
                m = ChatColor.translateAlternateColorCodes('&', m);
            }
            ChatSet set = testMessage(m, e.getPlayer());
            if (set.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            m = set.getMessage();
            e.setMessage(m);
            if (!r.getCnfg().getBoolean("Chat.EnableCustomChat")) {
                e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
                return;
            }
            if ((Bukkit.getPluginManager().getPlugin("EssentialsChat") != null && Bukkit.getPluginManager().getPlugin("EssentialsChat").isEnabled()) || (Bukkit.getPluginManager()
                    .getPlugin("Essentials") != null && Bukkit.getPluginManager().isPluginEnabled("Essentials"))) {
                if (!ChatColor.stripColor(e.getFormat()).equalsIgnoreCase("<%1$s> %2$s")) {
                    e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
                    return;
                }
            }
            if (r.getCnfg().getBoolean("Chat.Groups.Enabled")) {
                if (r.getVault() != null && r.getVault().getPermission() != null) {
                    String group = r.getVault().getPermission().getPrimaryGroup(e.getPlayer());
                    if (!(group == null) && !group.equalsIgnoreCase("") && r.getCnfg().get("Chat.Groups." + group) != null) {
                        String f = r.getCnfg().getString("Chat.Groups." + group);
                        String prefix = "";
                        String suffix = "";
                        if (r.getVault().getChat() != null) {
                            prefix = r.getVault().getChat().getGroupPrefix(e.getPlayer().getWorld(), r.getVault().getPermission().getPrimaryGroup(e.getPlayer()));
                            suffix = r.getVault().getChat().getGroupSuffix(e.getPlayer().getWorld(), r.getVault().getPermission().getPrimaryGroup(e.getPlayer()));
                            if ((r.getVault().getChat().getPlayerPrefix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerPrefix(e.getPlayer()).isEmpty()) {
                                prefix = r.getVault().getChat().getPlayerPrefix(e.getPlayer());
                            }
                            if ((r.getVault().getChat().getPlayerSuffix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerSuffix(e.getPlayer()).isEmpty()) {
                                suffix = r.getVault().getChat().getPlayerSuffix(e.getPlayer());
                            }
                        }
                        if (!f.contains("\\+Name")) {
                            e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
                        } else {
                            e.getPlayer().setDisplayName(e.getPlayer().getName());
                        }
                        f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? group.replaceAll("&y", r.getRandomChatColor() + "") : group);
                        f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : prefix);
                        f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : suffix);
                        f = r(f, "\\+Name", "\\%1\\$s");
                        f = r(f, "\\+Displayname", "\\%1\\$s");
                        f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
                        f = r(f, "\\+World", e.getPlayer().getWorld().getName());
                        f = r(f, "\\+Faction", r.getFaction(e.getPlayer()));
                        f = r(f, "\\+Town", r.getTown(e.getPlayer()));
                        f = ChatColor.translateAlternateColorCodes('&', f);
                        if (r.perm(e.getPlayer(), "uc.chat.rainbow", false, false)) {
                            f = r(f, "&y", r.getRandomChatColor() + "");
                        }
                        f = r(f, "\\+Message", "\\%2\\$s");
                        synchronized (f) {
                            e.setMessage(m);
                            e.setFormat(f);
                        }
                        return;
                    }
                }
            }
            String f = r.getCnfg().getString("Chat.Format");
            String group = "";
            String prefix = "";
            String suffix = "";
            if (r.getVault() != null && r.getVault().getPermission() != null && r.getVault().getChat() != null) {
                group = r.getVault().getPermission().getPrimaryGroup(e.getPlayer());
                prefix = r.getVault().getChat().getGroupPrefix(e.getPlayer().getWorld(), r.getVault().getPermission().getPrimaryGroup(e.getPlayer()));
                suffix = r.getVault().getChat().getGroupSuffix(e.getPlayer().getWorld(), r.getVault().getPermission().getPrimaryGroup(e.getPlayer()));
            }
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerPrefix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerPrefix(e.getPlayer())
                    .equalsIgnoreCase("")) {
                prefix = r.getVault().getChat().getPlayerPrefix(e.getPlayer());
            }
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerSuffix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerSuffix(e.getPlayer())
                    .equalsIgnoreCase("")) {
                suffix = r.getVault().getChat().getPlayerSuffix(e.getPlayer());
            }
            if (!f.contains("\\+Name")) {
                e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
            } else {
                e.getPlayer().setDisplayName(e.getPlayer().getName());
            }
            f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (group != null ? group.replaceAll("&y", r.getRandomChatColor() + "") : "") : (group != null ? group : ""));
            f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (prefix != null ? prefix
                    .replaceAll("&y", r.getRandomChatColor() + "") : "") : (prefix != null ? prefix : ""));
            f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (suffix != null ? suffix
                    .replaceAll("&y", r.getRandomChatColor() + "") : "") : (suffix != null ? suffix : ""));
            f = r(f, "\\+Name", "\\%1\\$s");
            f = r(f, "\\+Displayname", "\\%1\\$s");
            f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
            f = r(f, "\\+World", e.getPlayer().getWorld().getName());
            f = r(f, "\\+Faction", r.getFaction(e.getPlayer()));
            f = r(f, "\\+Town", r.getTown(e.getPlayer()));
            f = ChatColor.translateAlternateColorCodes('&', f);
            if (r.perm(e.getPlayer(), "uc.chat.rainbow", false, false)) {
                f = r(f, "&y", r.getRandomChatColor() + "");
            }
            f = r(f, "\\+Message", "\\%2\\$s");
            synchronized (f) {
                e.setMessage(m);
                e.setFormat(f);
            }
        }
    }

    private void spamTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                ArrayList<String> spamtime_remove = new ArrayList<>();
                if (!spamTime.isEmpty()) {
                    for (String key : spamTime.keySet()) {
                        Integer value = spamTime.get(key);
                        value--;
                        if (value == 0) {
                            spamtime_remove.add(key);
                        } else {
                            spamTime.put(key, value);
                        }
                    }
                }
                for (String str : spamtime_remove) {
                    spamTime.remove(str);
                }
            }
        }, 70L, 70L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                ArrayList<String> spamtime_remove = new ArrayList<>();
                if (!swearAmount.isEmpty()) {
                    for (String key : swearAmount.keySet()) {
                        Integer value = swearAmount.get(key);
                        value--;
                        if (value == 0) {
                            spamtime_remove.add(key);
                        } else {
                            swearAmount.put(key, value);
                        }
                    }
                    for (String str : spamtime_remove) {
                        swearAmount.remove(str);
                    }
                }
            }
        }, 160L, 160L);
    }

    public String r(String str, String str2, String str3) {
        if (str == null || str2 == null) {
            return str;
        }
        if (str3 == null) {
            return str.replaceAll(str2, "");
        }
        return str.replaceAll(str2, str3);
    }
}

class ChatSet {

    Boolean cancelled;
    String message;

    public ChatSet(String mes) {
        cancelled = false;
        message = mes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean can) {
        cancelled = can;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        message = msg;
    }
}
