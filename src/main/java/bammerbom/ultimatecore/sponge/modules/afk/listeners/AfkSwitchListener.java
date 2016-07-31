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
package bammerbom.ultimatecore.sponge.modules.afk.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.event.data.DataOfferEvent;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.utils.ExtendedLocation;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public class AfkSwitchListener {
    @Listener(order = Order.LAST)
    public void onAfkSwitch(DataOfferEvent<Boolean> event) {
        if (event.getKey().equals(AfkKeys.IS_AFK) && event.getValue().isPresent()) {
            Player player = event.getCause().first(Player.class).orElse(null);
            if (player == null) {
                Messages.log("Invalid DataOfferEvent! (No player found)");
                return;
            }
            UltimateUser user = UltimateCore.get().getUserService().getUser(player);
            CommentedConfigurationNode config = Modules.AFK.get().getConfig().get().get();
            if (config.getNode("title", "enabled").getBoolean(true)) {
                if (event.getValue().get()) {
                    //Player went afk
                    Text title = Messages.getFormatted("afk.title.title");
                    Text subtitle;
                    if (player.hasPermission(AfkPermissions.UC_AFK_EXEMPT.get())) {
                        subtitle = Messages.getFormatted("afk.title.subtitle.exempt");
                    } else if (config.getNode("title", "subtitle-show-seconds").getBoolean(true)) {
                        subtitle = Messages.getFormatted("afk.title.subtitle.kick", "%time%", TimeUtil.format(config.getNode("time", "kicktime").getInt() * 1000, 3, null));
                    } else {
                        subtitle = Messages.getFormatted("afk.title.subtitle.kick", "%time%", TimeUtil.format(config.getNode("time", "kicktime").getInt() * 1000, 3, 11));
                    }
                    if (config.getNode("title", "subtitle").getBoolean(false) && (!player.hasPermission(AfkPermissions.UC_AFK_EXEMPT.get()) || config.getNode("title", "subtitle-exempt")
                            .getBoolean(false))) {
                        //Show subtitle
                        player.sendTitle(Title.builder().title(title).subtitle(subtitle).fadeIn(20).fadeOut(20).stay(config.getNode("title", "subtitle-refresh").getInt()).build());
                    } else {
                        //Don't show subtitle
                        //TODO refresh?
                        player.sendTitle(Title.builder().title(title).fadeIn(20).fadeOut(20).stay(config.getNode("title", "subtitle-refresh").getInt()).build());
                    }
                    //Make sure the player is not un-afked instantly
                    AfkDetectionListener.afktime.put(player.getUniqueId(), 0L);
                    user.offer(AfkKeys.LAST_LOCATION, new ExtendedLocation(player.getLocation(), player.getRotation()));
                } else {
                    //Player is no longer afk
                    player.sendTitle(Title.builder().clear().build());
                    //Make sure the player is not afked instantly
                    AfkDetectionListener.afktime.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }


}
