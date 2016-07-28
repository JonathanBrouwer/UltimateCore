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
package bammerbom.ultimatecore.sponge.api.user;

import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class UCUser {

    private List<UserData> datas = new ArrayList<>();
    private UUID uuid; //TODO uuid or user?

    public UCUser(User user) {
        this.uuid = user.getUniqueId();
    }

    public User getUser(){
        if(getPlayer().isPresent()){
            return getPlayer().get();
        }
        return Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid).orElse(null);
    }

    public Optional<Player> getPlayer(){
        return Sponge.getServer().getPlayer(uuid);
    }

    public <D extends UserData> Optional<D> get(Class<D> cl) {
        for (UserData dat : datas) {
            if (dat.getClass().equals(cl)) {
                return Optional.of((D) dat);
            }
        }
        try {
            UserData ndat = (UserData) cl.getConstructors()[0].newInstance(getUser());
            datas.add(ndat);
            return Optional.of((D) ndat);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean isCompatible(Class<UserData> cl) {
        return Sponge.getServer().getPlayer(uuid).isPresent() || !cl.equals(PlayerData.class);
    }

    public boolean offer(UserData data) {
        if (!Sponge.getServer().getPlayer(uuid).isPresent() && data instanceof PlayerData) {
            return false;
        }
        datas.removeAll(datas.stream().filter(dat -> dat.getClass().equals(data.getClass())).collect(Collectors.toList()));
        return datas.add(data);
    }

    public boolean remove(Class<UserData> cl) {
        List<UserData> data = datas.stream().filter(dat -> dat.getClass().equals(cl)).collect(Collectors.toList());
        if (data.isEmpty()) {
            return false;
        }
        return datas.removeAll(data);
    }
}

