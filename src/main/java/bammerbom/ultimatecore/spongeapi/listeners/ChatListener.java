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
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ChatListener {

    static HashMap<String, Text> lastChatMessage = new HashMap<>();
    static HashMap<String, Integer> lastChatMessageTimes = new HashMap<>();
    static HashMap<String, Integer> spamTime = new HashMap<>();
    static HashMap<String, Integer> swearAmount = new HashMap<>();
    static Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");

    public static void start() {
        ChatListener list = new ChatListener();
        list.spamTask();
        Sponge.getEventManager().registerListeners(r.getUC(), list);
    }

    private static ChatSet testMessage(Text mr, Player p) {
        ChatSet set = new ChatSet(mr);
        if (r.perm(p, "uc.chat", false)) {
            return set;
        }
        //Anti REPEAT
        if (!r.perm(p, "uc.chat.repeat", false)) {
            if (r.getCnfg().getBoolean("Chat.RepeatFilter")) {
                Text lastmessage = Text.of();
                Integer lastmessageTimes = 0;
                if (lastChatMessage.get(p.getName()) != null) {
                    lastmessage = lastChatMessage.get(p.getName());
                    lastmessageTimes = lastChatMessageTimes.get(p.getName());
                }
                lastChatMessage.put(p.getName(), mr);
                lastChatMessageTimes.put(p.getName(), lastmessageTimes + 1);
                if (lastmessage.equals(mr)) {
                    if (lastmessageTimes + 1 == 3) {
                        r.sendMes(p, "chatRepeat");
                        set.setCancelled(true);
                    }
                    if (lastmessageTimes + 1 == 4) {
                        r.sendMes(p, "chatRepeat");
                        set.setCancelled(true);
                    }
                    if (lastmessageTimes + 1 == 5) {
                        UC.getPlayer(p).setMuted(true, 60000L, r.mes("chatRepeat").toPlain());
                        set.setCancelled(true);
                    }
                } else {
                    lastChatMessageTimes.put(p.getName(), 1);
                }
            }
        }
        //Anti SPAM
        if (!r.perm(p, "uc.chat.spam", false)) {
            if (r.getCnfg().getBoolean("Chat.SpamFilter")) {
                if (spamTime.containsKey(p.getName())) {
                    Integer amount = spamTime.get(p.getName());
                    spamTime.put(p.getName(), amount + 1);
                    if (amount >= 4) {
                        UC.getPlayer(p).setMuted(true, 60000L, r.mes("chatSpam").toPlain());
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
        if (!r.perm(p, "uc.chat.swear", false)) {
            if (r.getCnfg().getBoolean("Chat.SwearFilter") || r.getCnfg().getBoolean("Chat.SwearFiler")) {
                Boolean stop = false;
                for (String sw : r.getCnfg().getStringList("SwearWords")) {
                    if (mr.toPlain().toLowerCase().contains(sw.toLowerCase())) {
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
                                UC.getPlayer(p).setMuted(true, 60000L, r.mes("chatSwear").toPlain());
                                set.setCancelled(true);
                            }
                        }
                        set.setMessage(Text.of(set.getMessage().toString().replaceAll("(?i)" + sw, "****")));
                    }
                }
            }
        }
        //Anti CAPS
        if (!r.perm(p, "uc.chat.caps", false)) {
            if (r.getCnfg().get("Chat.CapsFilter") == null || r.getCnfg().getBoolean("Chat.CapsFilter")) {
                double msglength = set.getMessage().toPlain().toCharArray().length;
                double capsCountt = 0.0D;
                if (msglength > 9.0) {
                    for (char c : set.getMessage().toPlain().toCharArray()) {
                        if (Character.isUpperCase(c)) {
                            capsCountt += 1.0D;
                        }
                        if (!Character.isLetterOrDigit(c)) {
                            msglength -= 1.0D;
                        }
                    }
                }
                if ((capsCountt / msglength * 100) > 60.0) {
                    set.setMessage(Text.of(StringUtil.firstUpperCase(set.getMessage().toString().toLowerCase())));
                }
            }
        }
        //Anti IP
        if (!r.perm(p, "uc.chat.ip", false)) {
            if (r.getCnfg().getBoolean("Chat.IpFilter")) {
                if (ipPattern.matcher(set.getMessage().toPlain()).find()) {
                    set.setCancelled(true);
                    r.sendMes(p, "chatIp");
                }
            }
        }
        //Anti URL
        if (!r.perm(p, "uc.chat.url", false)) {
            if (r.getCnfg().getBoolean("Chat.UrlFilter")) {
                if (domainPattern.matcher(set.getMessage()).find()) { //TODO find better domain pattern
                    set.setCancelled(true);
                    r.sendMes(p, "chatUrl");
                }
            }
        }
        return set;
    }

    @Listener(order = Order.DEFAULT)
    public void ChatListener(MessageChannelEvent.Chat e) {
        Player p = e.getCause().first(Player.class).get();
        if (!e.isCancelled() && !UC.getPlayer(p).isMuted()) {
            Text m = e.getRawMessage();
            if (r.perm(p, "uc.coloredchat", false)) {
                m = Text.of(TextColorUtil.translateAlternate(m.toString()));
            }
            ChatSet set = testMessage(m, p);
            if (set.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            m = set.getMessage();
            e.setMessage(Text.of(m));
            if (!r.getCnfg().getBoolean("Chat.EnableCustomChat")) {
                p.offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
                return;
            }
            if (r.getCnfg().getBoolean("Chat.Groups.Enabled")) {
                if (Sponge.getServiceManager().provide(PermissionService.class).isPresent()) {
                    PermissionService service = Sponge.getServiceManager().provide(PermissionService.class).get();
                    Subject group = r.getPrimaryGroup(p);
                    if (!(group == null) && !group.equalsIgnoreCase("") && r.getCnfg().get("Chat.Groups." + group) != null) {
                        String f = r.getCnfg().getString("Chat.Groups." + group);
                        String prefix = service.getGroupSubjects();
                        String suffix = "";
                        if (r.getVault().getChat() != null) { //TODO prefix & suffix
                            prefix = r.getVault().getChat().getGroupPrefix(p.getWorld(), r.getPrimaryGroup(p));
                            suffix = r.getVault().getChat().getGroupSuffix(p.getWorld(), r.getPrimaryGroup(p));
                            if ((r.getVault().getChat().getPlayerPrefix(p) != null) && !r.getVault().getChat().getPlayerPrefix(p).isEmpty()) {
                                prefix = r.getVault().getChat().getPlayerPrefix(p);
                            }
                            if ((r.getVault().getChat().getPlayerSuffix(p) != null) && !r.getVault().getChat().getPlayerSuffix(p).isEmpty()) {
                                suffix = r.getVault().getChat().getPlayerSuffix(p);
                            }
                        }
                        if (!f.contains("\\+Name")) {
                            p.offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
                        } else {
                            p.offer(Keys.DISPLAY_NAME, Text.of(p.getName()));
                        }
                        f = TabListener.replaceVariables(f, p);
                        f = TextColorUtil.translateAlternate(f);
                        if (r.perm(p, "uc.chat.rainbow", false)) {
                            f = r(f, "&y", r.getRandomTextColor() + "");
                        }
                        e.setMessage(m);

                        return;
                    }
                }
            }
            String f = r.getCnfg().getString("Chat.Format");
            String group = "";
            String prefix = "";
            String suffix = "";
            if (r.getVault() != null && r.getVault().getPermission() != null && r.getVault().getChat() != null) {
                group = r.getPrimaryGroup(p);
                prefix = r.getVault().getChat().getGroupPrefix(p.getWorld(), r.getPrimaryGroup(p));
                suffix = r.getVault().getChat().getGroupSuffix(p.getWorld(), r.getPrimaryGroup(p));
            }
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerPrefix(p) != null) && !r.getVault().getChat().getPlayerPrefix(e.getPlayer())
                    .equalsIgnoreCase("")) {
                prefix = r.getVault().getChat().getPlayerPrefix(p);
            }
            if (r.getVault() != null && r.getVault().getChat() != null && (r.getVault().getChat().getPlayerSuffix(p) != null) && !r.getVault().getChat().getPlayerSuffix(e.getPlayer())
                    .equalsIgnoreCase("")) {
                suffix = r.getVault().getChat().getPlayerSuffix(p);
            }
            if (!f.contains("\\+Name")) {
                p.offer(Keys.DISPLAY_NAME, UC.getPlayer(p).getDisplayName());
            } else {
                p.offer(Keys.DISPLAY_NAME, Text.of(p.getName()));
            }
            f = r(f, "\\+Group", r.perm(p, "uc.chat.rainbow", false) ? (group != null ? group.replaceAll("&y", r.getRandomTextColor() + "") : "") : (group != null ? group : ""));
            f = r(f, "\\+Prefix", r.perm(p, "uc.chat.rainbow", false) ? (prefix != null ? prefix.replaceAll("&y", r.getRandomTextColor() + "") : "") : (prefix != null ? prefix : ""));
            f = r(f, "\\+Suffix", r.perm(p, "uc.chat.rainbow", false) ? (suffix != null ? suffix.replaceAll("&y", r.getRandomTextColor() + "") : "") : (suffix != null ? suffix : ""));
            f = r(f, "\\+Name", "\\%1\\$s");
            f = r(f, "\\+Displayname", "\\%1\\$s");
            f = r(f, "\\+WorldAlias", p.getWorld().getName().charAt(0) + "");
            f = r(f, "\\+World", p.getWorld().getName());
//            f = r(f, "\\+Faction", r.getFaction(p));
//            f = r(f, "\\+Town", r.getTown(p));
            f = TextColorUtil.translateAlternate(f);
            if (r.perm(p, "uc.chat.rainbow", false)) {
                f = r(f, "&y", r.getRandomTextColor() + "");
            }
            f = r(f, "\\+Message", "\\%2\\$s");
            e.setMessage(m);

        }
    }

    private void spamTask() {
        Sponge.getScheduler().createTaskBuilder().intervalTicks(70L).delayTicks(70L).execute(new Runnable() {
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
        }).name("UC spam task").submit(r.getUC());
        Sponge.getScheduler().createTaskBuilder().intervalTicks(160L).delayTicks(160L).execute(new Runnable() {
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
        }).name("UC swear task").submit(r.getUC());
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
    Text message;

    public ChatSet(Text mes) {
        cancelled = false;
        message = mes;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean can) {
        cancelled = can;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text msg) {
        message = msg;
    }
}
