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
package bammerbom.ultimatecore.sponge.modules.automessage.api;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColor;
import org.spongepowered.api.boss.BossBarOverlay;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.title.Title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Automessage {
    //General
    public boolean enable;
    public int time;
    public String random;
    public ImmutableList<Text> messages;

    //Chat
    public boolean chat;

    //Actionbar
    public boolean actionbar;
    public int actionbar_stay;

    //Bossbar
    public boolean bossbar;
    public int bossbar_stay;
    public boolean bossbar_decrease;
    public BossBarColor bossbar_color;
    public BossBarOverlay bossbar_style;

    //Title
    public boolean title;
    public int title_fadein;
    public int title_stay;
    public int title_fadeout;

    //INTERNAL VALUES
    //Contains all messages excluding the already send messages
    List<Integer> tempmessages = new ArrayList<>();
    static Random rand = new Random();

    public Automessage() {
    }

    public void start() {
        if (!enable || messages.isEmpty()) {
            return;
        }
        Sponge.getScheduler().createTaskBuilder().interval(time, TimeUnit.SECONDS).delay(time, TimeUnit.SECONDS).execute(this::run).name("UC: Automessage Task").submit(UltimateCore.get());
    }

    public void run() {
        //Get the message
        Text message;
        if (random.equalsIgnoreCase("random")) {
            message = messages.get(rand.nextInt(messages.size()));
        } else if (random.equalsIgnoreCase("order")) {
            if (tempmessages.isEmpty()) {
                tempmessages = new ArrayList<>();
                int i = 0;
                for (Text mes : messages) {
                    tempmessages.add(i);
                    i++;
                }
            }
            message = messages.get(tempmessages.get(0));
            tempmessages.remove(0);
        } else if (random.equalsIgnoreCase("randomorder")) {
            if (tempmessages.isEmpty()) {
                tempmessages = new ArrayList<>();
                int i = 0;
                for (Text mes : messages) {
                    tempmessages.add(i);
                    i++;
                }
                Collections.shuffle(tempmessages);
            }
            message = messages.get(tempmessages.get(0));
            tempmessages.remove(0);
        } else {
            Messages.log("Invalid random type in automessage config: " + random);
            return;
        }

        //Remove weird character from message
        message = TextUtil.replace(message, "\r", Text.of());

        //Chat
        if (chat) {
            Sponge.getServer().getBroadcastChannel().send(message);
        }

        //Actionbar
        if (actionbar) {
            MutableMessageChannel channel = Sponge.getServer().getBroadcastChannel().asMutable();
            channel.removeMember(Sponge.getServer().getConsole());
            //First send
            Sponge.getServer().getBroadcastChannel().send(TextUtil.split(message, "\n").get(0), ChatTypes.ACTION_BAR);
            //Last send
            final Text finalmessage = message;
            Sponge.getScheduler().createTaskBuilder().delay(actionbar_stay, TimeUnit.SECONDS).execute(new Runnable() {
                @Override
                public void run() {
                    channel.send(TextUtil.split(finalmessage, "\n").get(0), ChatTypes.ACTION_BAR);
                }
            }).name("UC: Automessage actionbar delay task 1").submit(UltimateCore.get());
            //Repeating send
            int duration = actionbar_stay;
            while (duration > 2) {
                duration -= 2;
                Sponge.getScheduler().createTaskBuilder().delay(duration, TimeUnit.SECONDS).execute(new Runnable() {
                    @Override
                    public void run() {
                        channel.send(TextUtil.split(finalmessage, "\n").get(0), ChatTypes.ACTION_BAR);
                    }
                }).name("UC: Automessage actionbar delay task 2").submit(UltimateCore.get());
            }
        }

        //Bossbar
        if (bossbar) {
            //First send
            ServerBossBar bar = ServerBossBar.builder().color(bossbar_color).overlay(bossbar_style).name(TextUtil.split(message, "\n").get(0)).percent(1.0f).build();
            Sponge.getServer().getOnlinePlayers().forEach(bar::addPlayer);
            Sponge.getScheduler().createTaskBuilder().interval(1, TimeUnit.SECONDS).execute(new Consumer<Task>() {
                int duration = bossbar_stay;

                @Override
                public void accept(Task task) {
                    if (duration < 0) {
                        task.cancel();
                        bar.setVisible(false);
                        return;
                    }
                    bar.setPercent(duration / bossbar_stay);
                    duration--;
                }
            }).name("UC: Automessage bossbar task").submit(UltimateCore.get());
        }

        //Title
        if (title) {
            Text uptitle;
            Text subtitle;
            if (message.toPlain().contains("\n")) {
                List<Text> split = TextUtil.split(message, "\n");
                uptitle = split.get(0);
                subtitle = split.get(1);
            } else {
                uptitle = message;
                subtitle = Text.of();
            }
            Title title = Title.builder().title(uptitle).subtitle(subtitle).fadeIn(title_fadein * 20).stay(title_stay * 20).fadeOut(title_fadeout * 20).build();
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                p.sendTitle(title);
            }
        }
    }
}
