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
import bammerbom.ultimatecore.spongeapi.resources.utils.FileUtil;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutomessageListener {

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
        if (r.getCnfg().getBoolean("Messages.Enabledchat") == false && r.getCnfg().getBoolean("Messages" + ".Enabledbossbar") == false && r.getCnfg().getBoolean("Messages.Enabledactionbar") == false) {
            return;
        }
        r.getGame().getEventManager().register(r.getUC(), new AutomessageListener());
        ArrayList<String> messgs = messages;
        Integer length = messgs.size();
        if (length != 0) {
            timer(messgs);
        }
    }

    public static void timer(final List<String> messgs) {
        final Integer time = r.getCnfg().getInt("Messages.Time");
        final Boolean ur = r.getCnfg().getBoolean("Messages.Randomise");
        r.getGame().getSyncScheduler().runRepeatingTask(r.getUC(), new Runnable() {
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
                currentmessage = r.translateAlternateColorCodes('&', mess);
                for (Player p : r.getOnlinePlayers()) {
                    if (r.getCnfg().getBoolean("Messages.Enabledactionbar") == true) {
                        p.sendMessage(ChatTypes.ACTION_BAR, r.translateAlternateColorCodes('&', mess).replace("\n", " "));
                    }
                    if (r.getCnfg().getBoolean("Messages.Enabledchat") == true) {
                        p.sendMessage(Texts.of(r.translateAlternateColorCodes('&', mess)));
                    }

                }
            }
        }, time * 20);
    }

    @Subscribe(order = Order.EARLY)
    public void onJoin(final PlayerJoinEvent e) {
        ArrayList<String> messgs = messages;
        Integer length = messgs.size();
        if (length == 0) {
            return;
        }
        if (r.getCnfg().getBoolean("Messages.Enabledactionbar") == true) {
            e.getPlayer().sendMessage(ChatTypes.ACTION_BAR, r.translateAlternateColorCodes('&', currentmessage).replace("\n", " "));
        }
    }
}
