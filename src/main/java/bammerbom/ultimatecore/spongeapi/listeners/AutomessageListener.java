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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutomessageListener implements Listener {

    public static ArrayList<String> messages = new ArrayList<>();
    public static String currentmessage = "";
    public static Integer id = -1;
    public static boolean decrease = true;
    public static Random random;

    public static void start() {
        File file = new File(bammerbom.ultimatecore.spongeapi.r.getUC().getDataFolder(), "messages.txt");
        if (!file.exists()) {
            bammerbom.ultimatecore.spongeapi.r.getUC().saveResource("messages.txt", true);
        }
        messages = bammerbom.ultimatecore.spongeapi.resources.utils.FileUtil.getLines(file);
        decrease = bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Decrease");
        random = new Random();
        if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledchat") == false && bammerbom.ultimatecore.spongeapi.r.getCnfg()
                .getBoolean("Messages" + ".Enabledbossbar") == false && bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledactionbar") == false) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new AutomessageListener(), bammerbom.ultimatecore.spongeapi.r.getUC());
        ArrayList<String> messgs = messages;
        Integer length = messgs.size();
        if (length != 0) {
            timer(messgs);
        }
    }

    public static void timer(final List<String> messgs) {
        final Integer time = bammerbom.ultimatecore.spongeapi.r.getCnfg().getInt("Messages.Time");
        final Boolean ur = bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Randomise");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(bammerbom.ultimatecore.spongeapi.r.getUC(), new Runnable() {
            @Override
            public void run() {
                String mess = ur ? messgs.get(random.nextInt(messgs.size())) : "";
                if (!ur) {
                    id++;
                    if ((messgs.size() - 1) < id) {
                        mess = messgs.get(0);
                        id = 0;
                    } else {
                        mess = messgs.get(id);
                    }
                }
                mess = mess.replace("\\n", "\n");
                currentmessage = ChatColor.translateAlternateColorCodes('&', mess);
                for (Player p : bammerbom.ultimatecore.spongeapi.r.getOnlinePlayers()) {
                    if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledbossbar") == true) {
                        if (decrease) {
                            bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil.setMessage(p, ChatColor.translateAlternateColorCodes('&', mess).replace("\n", " "), time);
                        } else {
                            bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil.setMessage(p, ChatColor.translateAlternateColorCodes('&', mess).replace("\n", " "));
                        }
                    }
                    if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledactionbar") == true) {
                        bammerbom.ultimatecore.spongeapi.resources.utils.TitleUtil.sendActionBar(p, ChatColor.translateAlternateColorCodes('&', mess).replace("\n", " "));
                    }
                    if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledchat") == true) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', mess));
                    }

                }
            }
        }, 0, time * 20);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(final PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(bammerbom.ultimatecore.spongeapi.r.getUC(), new Runnable() {
            @Override
            public void run() {
                ArrayList<String> messgs = messages;
                Integer length = messgs.size();
                if (length == 0) {
                    return;
                }
                if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledbossbar") == true) {
                    bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil.setMessage(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', currentmessage.replace("\n", " ")));
                }
                if (bammerbom.ultimatecore.spongeapi.r.getCnfg().getBoolean("Messages.Enabledactionbar") == true) {
                    bammerbom.ultimatecore.spongeapi.resources.utils.TitleUtil.sendActionBar(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', currentmessage.replace("\n", " ")));
                }
            }
        }, 100L);
    }
}
