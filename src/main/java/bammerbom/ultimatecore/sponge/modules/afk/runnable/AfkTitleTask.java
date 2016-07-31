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
package bammerbom.ultimatecore.sponge.modules.afk.runnable;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TimeUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

public class AfkTitleTask implements Runnable {
    @Override
    public void run() {
        CommentedConfigurationNode config = Modules.AFK.get().getConfig().get().get();
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            UltimateUser user = UltimateCore.get().getUserService().getUser(player);
            if (user.get(AfkKeys.IS_AFK).get()) {
                Text title = Messages.getFormatted("afk.title.title");
                Text subtitle;
                long timediff = (config.getNode("time", "kicktime").getInt() * 1000) - (System.currentTimeMillis() - user.get(AfkKeys.AFK_TIME).get());
                if (config.getNode("time", "kicktime").getInt() != -1 && timediff <= 0 && !player.hasPermission(AfkPermissions.UC_AFK_EXEMPT.get())) {
                    player.kick(Messages.getFormatted("afk.kick.reason"));
                }
                if (player.hasPermission(AfkPermissions.UC_AFK_EXEMPT.get()) || config.getNode("time", "kicktime").getInt() == -1) {
                    subtitle = Messages.getFormatted("afk.title.subtitle.exempt");
                } else if (config.getNode("title", "subtitle-show-seconds").getBoolean(true)) {
                    subtitle = Messages.getFormatted("afk.title.subtitle.kick", "%time%", TimeUtil.format(timediff, 3, null));
                } else {
                    subtitle = Messages.getFormatted("afk.title.subtitle.kick", "%time%", TimeUtil.format(timediff, 3, 11));
                }
                if (config.getNode("title", "subtitle").getBoolean(false) && (!player.hasPermission(AfkPermissions.UC_AFK_EXEMPT.get()) || config.getNode("title", "subtitle-exempt")
                        .getBoolean(false))) {
                    //Show subtitle
                    player.sendTitle(Title.builder().title(title).subtitle(subtitle).fadeIn(0).fadeOut(20).stay(config.getNode("title", "subtitle-refresh").getInt()).build());
                } else {
                    //Don't show subtitle
                    //TODO refresh?
                    player.sendTitle(Title.builder().title(title).fadeIn(0).fadeOut(20).stay(config.getNode("title", "subtitle-refresh").getInt()).build());
                }
            }
        }
    }
}
