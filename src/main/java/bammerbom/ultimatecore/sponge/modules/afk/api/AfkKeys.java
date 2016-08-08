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
package bammerbom.ultimatecore.sponge.modules.afk.api;

import bammerbom.ultimatecore.sponge.api.data.Key;
import bammerbom.ultimatecore.sponge.api.data.KeyProvider;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.modules.afk.listeners.AfkDetectionListener;
import bammerbom.ultimatecore.sponge.utils.ExtendedLocation;
import org.spongepowered.api.text.title.Title;

public class AfkKeys {
    public static Key.User.Online<Long> AFK_TIME = new Key.User.Online<>("afk_time");
    public static Key.User.Online<String> AFK_MESSAGE = new Key.User.Online<>("afk_message");
    public static Key.User.Online<ExtendedLocation> LAST_LOCATION = new Key.User.Online<>("afk_lastlocation");
    public static Key.User.Online<Boolean> IS_AFK = new Key.User.Online<>("afk", new KeyProvider.User<Boolean>() {
        @Override
        public Boolean load(UltimateUser user) {
            return false;
        }

        @Override
        public void save(UltimateUser user, Boolean data) {
            if (data) {
                //Make sure the player is not un-afked instantly
                AfkDetectionListener.afktime.put(user.getIdentifier(), 0L);
                if (user.getPlayer().isPresent()) {
                    user.offer(AfkKeys.LAST_LOCATION, new ExtendedLocation(user.getPlayer().get().getLocation(), user.getPlayer().get().getRotation()));
                }
            } else {
                //Player is no longer afk
                if (user.getPlayer().isPresent()) {
                    user.getPlayer().get().sendTitle(Title.builder().clear().build());
                }
                //Make sure the player is not afked instantly
                AfkDetectionListener.afktime.put(user.getIdentifier(), System.currentTimeMillis());
            }
        }
    });
}
