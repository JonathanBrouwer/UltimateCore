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
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.living.player.PlayerChatEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ChatListener {

    static HashMap<String, Text.Literal> lastChatMessage = new HashMap<>();
    static HashMap<String, Integer> lastChatMessageTimes = new HashMap<>();
    static HashMap<String, Integer> spamTime = new HashMap<>();
    static HashMap<String, Integer> swearAmount = new HashMap<>();
    static Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    static Pattern domainPattern = Pattern.compile("((?:(?:https?)://)?[\\\\www\\\\.]{2,})\\\\.([a-zA-Z]{2,3}(?:/\\\\S+)?)");

    public static void start() {
        ChatListener list = new ChatListener();
        list.spamTask();
        r.getGame().getEventManager().registerListeners(r.getUC(), new ChatListener());
    }

    private static ChatSet testMessage(Text.Literal mr, Player p) {
        ChatSet set = new ChatSet(mr);
        if (r.perm(p, "uc.chat", false, false)) {
            return set;
        }
        //Anti REPEAT
        if (!r.perm(p, "uc.chat.repeat", false, false)) {
            if (r.getCnfg().getBoolean("Chat.RepeatFilter")) {
                Text.Literal lastmessage = Texts.of("");
                Integer lastmessageTimes = 0;
                if (lastChatMessage.get(p.getName()) != null) {
                    lastmessage = lastChatMessage.get(p.getName());
                    lastmessageTimes = lastChatMessageTimes.get(p.getName());
                }
                lastChatMessage.put(p.getName(), mr);
                lastChatMessageTimes.put(p.getName(), lastmessageTimes + 1);
                if (lastmessage.getContent().equalsIgnoreCase(mr.getContent())) {
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
                        r.getGame().getCommandDispatcher().process(r.getGame().getServer().getConsole(), "mute " + p.getName() + " 5m");
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
                    if (mr.getContent().toLowerCase().contains(sw.toLowerCase())) {
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
                                r.getGame().getCommandDispatcher().process(r.getGame().getServer().getConsole(), "mute " + p.getName() + " 5m");
                                set.setCancelled(true);
                            }
                        }
                        HashMap<String, String> map = new HashMap<>();
                        map.put("(?i)" + sw, "****");
                        set.setMessage((Text.Literal) Texts.format(set.getMessage(), map));
                    }
                }
            }
        }
        //Anti CAPS
        if (!r.perm(p, "uc.chat.caps", false, false)) {
            if (r.getCnfg().get("Chat.CapsFilter") == null || r.getCnfg().getBoolean("Chat.CapsFilter")) {
                double msglength = set.getMessage().getContent().toCharArray().length;
                double capsCountt = 0.0D;
                if (msglength > 8.0) {
                    for (char c : set.getMessage().getContent().toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            capsCountt += 1.0D;
                        }
                        if (!Character.isLetterOrDigit(c)) {
                            msglength -= 1.0D;
                        }
                    }
                }
                if ((capsCountt / msglength * 100) > 60.0) {
                    set.setMessage(Texts.of(StringUtil.firstUpperCase(set.getMessage().getContent().toLowerCase())));
                }
            }
        }
        //Anti IP
        if (!r.perm(p, "uc.chat.ip", false, false)) {
            if (r.getCnfg().getBoolean("Chat.IpFilter")) {
                if (ipPattern.matcher(set.getMessage().getContent()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatIp");
                }
            }
        }
        //Anti URL
        if (!r.perm(p, "uc.chat.url", false, false)) {
            if (r.getCnfg().getBoolean("Chat.UrlFilter")) {
                if (domainPattern.matcher(set.getMessage().getContent()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatUrl");
                }
            }
        }
        return set;
    }

    @Listener(order = Order.EARLY)
    public void ChatListener(PlayerChatEvent e) {
        if (!e.isCancelled() && !UC.getPlayer(e.getEntity()).isMuted()) {
            Player p = e.getEntity();
            Text.Literal m = (Text.Literal) e.getUnformattedMessage();
            if (r.perm(e.getUser(), "uc.coloredchat", false, false)) {
                m = (Text.Literal) r.translateAlternateColorCodes('&', m);
            }
            ChatSet set = testMessage(m, e.getUser());
            if (set.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            m = set.getMessage();
            e.setUnformattedMessage(m);
            if (!r.getCnfg().getBoolean("Chat.EnableCustomChat")) {
                e.getUser().offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
                return;
            }
            if (r.getCnfg().getBoolean("Chat.Groups.Enabled")) {
                if (r.getPermission() != null) {
                    String group = p.getParents().get(0).getIdentifier();
                    if (!(group == null) && !group.equalsIgnoreCase("") && r.getCnfg().get("Chat.Groups." + group) != null) {
                        String f = r.getCnfg().getString("Chat.Groups." + group);
                        String prefix = r.getPrefix(p);
                        String suffix = r.getSuffix(p);
                        if (!f.contains("\\+Name")) {
                            e.getUser().offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
                        } else {
                            e.getUser().offer(Keys.DISPLAY_NAME, Texts.of(p.getName()));
                        }
                        f = r(f, "\\+Group", r.perm(p, "uc.chat.rainbow", false, false) ? group.replaceAll("&y", r.getRandomTextColor() + "") : group);
                        f = r(f, "\\+Prefix", r.perm(p, "uc.chat.rainbow", false, false) ? prefix.replaceAll("&y", r.getRandomTextColor() + "") : prefix);
                        f = r(f, "\\+Suffix", r.perm(p, "uc.chat.rainbow", false, false) ? suffix.replaceAll("&y", r.getRandomTextColor() + "") : suffix);
                        f = r(f, "\\+Name", "\\%1\\$s");
                        f = r(f, "\\+Displayname", "\\%1\\$s");
                        f = r(f, "\\+WorldAlias", p.getWorld().getName().charAt(0) + "");
                        f = r(f, "\\+World", p.getWorld().getName());
                        f = r(f, "\\+Faction", r.getFaction(p));
                        f = r(f, "\\+Town", r.getTown(p));
                        f = r.translateAlternateColorCodes('&', f);
                        if (r.perm(p, "uc.chat.rainbow", false, false)) {
                            f = r(f, "&y", r.getRandomTextColor() + "");
                        }
                        f = r(f, "\\+Message", "\\%2\\$s");
                        e.setUnformattedMessage(m);
                        e.setNewMessage(Texts.of(f));
                        return;
                    }
                }
            }
            String f = r.getCnfg().getString("Chat.Format");
            String group = "";
            String prefix = "";
            String suffix = "";
            if (r.getPermission() != null) {
                group = p.getParents().get(0).getIdentifier();
                prefix = r.getPrefix(p);
                suffix = r.getSuffix(p);
            }
            if (!f.contains("\\+Name")) {
                e.getUser().offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
            } else {
                e.getUser().offer(Keys.DISPLAY_NAME, Texts.of(p.getName()));
            }
            f = r(f, "\\+Group", r.perm(p, "uc.chat.rainbow", false, false) ? group.replaceAll("&y", r.getRandomTextColor() + "") : group);
            f = r(f, "\\+Prefix", r.perm(p, "uc.chat.rainbow", false, false) ? prefix.replaceAll("&y", r.getRandomTextColor() + "") : prefix);
            f = r(f, "\\+Suffix", r.perm(p, "uc.chat.rainbow", false, false) ? suffix.replaceAll("&y", r.getRandomTextColor() + "") : suffix);
            f = r(f, "\\+Name", "\\%1\\$s");
            f = r(f, "\\+Displayname", "\\%1\\$s");
            f = r(f, "\\+WorldAlias", p.getWorld().getName().charAt(0) + "");
            f = r(f, "\\+World", p.getWorld().getName());
            f = r(f, "\\+Faction", r.getFaction(p));
            f = r(f, "\\+Town", r.getTown(p));
            f = r.translateAlternateColorCodes('&', f);
            if (r.perm(p, "uc.chat.rainbow", false, false)) {
                f = r(f, "&y", r.getRandomTextColor() + "");
            }
            f = r(f, "\\+Message", "\\%2\\$s");
            e.setUnformattedMessage(m);
            e.setNewMessage(Texts.of(f));
        }
    }

    private void spamTask() {
        r.getGame().getScheduler().createTaskBuilder().delayTicks(70L).intervalTicks(70L).name("UC: Spam task").execute(new Runnable() {
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
        }).submit(r.getUC());
        r.getGame().getScheduler().createTaskBuilder().delayTicks(140L).intervalTicks(140L).name("UC: Swear task").execute(new Runnable() {
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
        }).submit(r.getUC());
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
    Text.Literal message;

    public ChatSet(Text.Literal mes) {
        cancelled = false;
        message = mes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean can) {
        cancelled = can;
    }

    public Text.Literal getMessage() {
        return message;
    }

    public void setMessage(Text.Literal msg) {
        message = msg;
    }
}
