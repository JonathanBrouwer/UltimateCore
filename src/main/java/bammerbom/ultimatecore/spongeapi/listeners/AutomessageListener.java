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

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.ActionBarUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.BossbarUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
        File file = new File(r.getUC().getDataFolder(), "messages.txt");
        if (!file.exists()) {
            r.getUC().saveResource("messages.txt", true);
        }
        messages = FileUtil.getLines(file);
        decrease = r.getCnfg().getBoolean("Messages.Decrease");
        random = new Random();
        if (!r.getCnfg().getBoolean("Messages.Enabledchat") && !r.getCnfg().getBoolean("Messages" + ".Enabledbossbar") && !r.getCnfg().getBoolean("Messages.Enabledactionbar")) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new AutomessageListener(), r.getUC());
        ArrayList<String> messgs = messages;
        Integer length = messgs.size();
        if (length != 0) {
            timer(messgs);
        }
    }

    public static void timer(final List<String> messgs) {
        final Integer time = r.getCnfg().getInt("Messages.Time");
        final Integer timestay = r.getCnfg().getInt("Messages.Stay");
        final Boolean ur = r.getCnfg().getBoolean("Messages.Randomise");
        final BarColor color = BarColor.valueOf(r.getCnfg().getString("Messages.Color").toUpperCase());
        final BarStyle style = BarStyle.valueOf(r.getCnfg().getString("Messages.Style").toUpperCase());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(r.getUC(), new Runnable() {
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
                currentmessage = TextColorUtil.translateAlternate(mess);
                for (Player p : r.getOnlinePlayers()) {
                    if (r.getCnfg().getBoolean("Messages.Enabledbossbar")) {
                        if (decrease) {
                            BossbarUtil.setMessage(p, TextColorUtil.translateAlternate(mess).replace("\n", " "), timestay, color, style);
                        } else {
                            BossbarUtil.setMessage(p, TextColorUtil.translateAlternate(mess).replace("\n", " "), color, style);
                        }
                    }
                    if (r.getCnfg().getBoolean("Messages.Enabledactionbar")) {
                        ActionBarUtil.sendActionBar(p, TextColorUtil.translateAlternate(mess).replace("\n", " "), timestay * 20);
                    }
                    if (r.getCnfg().getBoolean("Messages.Enabledchat")) {
                        p.sendMessage(TextColorUtil.translateAlternate(mess));
                    }

                }
            }
        }, 0, time * 20);
    }
}
