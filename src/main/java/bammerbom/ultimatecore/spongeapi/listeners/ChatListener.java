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
package bammerbom.ultimatecore.spongeapi.listeners;

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    static HashMap<String, String> lastChatMessage = new HashMap<>();
    static HashMap<String, Integer> lastChatMessageTimes = new HashMap<>();
    static HashMap<String, Integer> spamTime = new HashMap<>();
    static HashMap<String, Integer> swearAmount = new HashMap<>();

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
                if (msglength > 3.0) {
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
            if (!r.getCnfg().contains("Chat.IpFilter") || r.getCnfg().getBoolean("Chat.IpFilter")) {
                String ipPattern = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
                if (Pattern.compile(ipPattern).matcher(set.getMessage()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatIp");
                }
            }
        }
        //Anti URL
        if (!r.perm(p, "uc.chat.url", false, false)) {
            if (!r.getCnfg().contains("Chat.UrlFilter") || r.getCnfg().getBoolean("Chat.UrlFilter")) {
                final Pattern domainPattern = Pattern.compile("((?:(?:https?)://)?[\\w-_\\.]{2,})\\.([a-zA-Z]{2,3}" + "(?:/\\S+)?)");
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
            if (r.getCnfg().getBoolean("Chat.EnableCustomChat") == false) {
                e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
                return;
            }
            if ((Bukkit.getPluginManager().getPlugin("EssentialsChat") != null && Bukkit.getPluginManager().getPlugin("EssentialsChat").isEnabled()) || (Bukkit.getPluginManager().getPlugin("Essentials") != null && Bukkit.getPluginManager().isPluginEnabled("Essentials"))) {
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
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerPrefix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerPrefix(e.getPlayer()).equalsIgnoreCase("")) {
                prefix = r.getVault().getChat().getPlayerPrefix(e.getPlayer());
            }
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerSuffix(e.getPlayer()) != null) && !r.getVault().getChat().getPlayerSuffix(e.getPlayer()).equalsIgnoreCase("")) {
                prefix = r.getVault().getChat().getPlayerSuffix(e.getPlayer());
            }
            if (!f.contains("\\+Name")) {
                e.getPlayer().setDisplayName(UC.getPlayer(e.getPlayer()).getDisplayName());
            } else {
                e.getPlayer().setDisplayName(e.getPlayer().getName());
            }
            f = r(f, "\\+Group", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (group != null ? group.replaceAll("&y", r.getRandomChatColor() + "") : "") : (group != null ? group : ""));
            f = r(f, "\\+Prefix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (prefix != null ? prefix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (prefix != null ? prefix : ""));
            f = r(f, "\\+Suffix", r.perm(e.getPlayer(), "uc.chat.rainbow", false, false) ? (suffix != null ? suffix.replaceAll("&y", r.getRandomChatColor() + "") : "") : (suffix != null ? suffix : ""));
            f = r(f, "\\+Name", "\\%1\\$s");
            f = r(f, "\\+Displayname", "\\%1\\$s");
            f = r(f, "\\+WorldAlias", e.getPlayer().getWorld().getName().charAt(0) + "");
            f = r(f, "\\+World", e.getPlayer().getWorld().getName());
            f = r(f, "\\+Faction", r.getFaction(e.getPlayer()));
            f = r(f, "\\+Town", r.getTown(e.getPlayer()));
            f = ChatColor.translateAlternateColorCodes('&', f);
            ChatColor value = Arrays.asList(ChatColor.values()).get(r.ra.nextInt(Arrays.asList(ChatColor.values()).size()));
            if (r.perm(e.getPlayer(), "uc.chat.rainbow", false, false)) {
                f = r(f, "&y", value + "");
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
