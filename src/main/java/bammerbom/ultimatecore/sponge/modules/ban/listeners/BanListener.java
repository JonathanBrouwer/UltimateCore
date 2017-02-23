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
package bammerbom.ultimatecore.sponge.modules.ban.listeners;

import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.language.utils.TextUtil;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.variable.utils.TimeUtil;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.BanService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.ban.Ban;

import java.net.InetAddress;
import java.util.UUID;

public class BanListener {
    @Listener
    @IsCancelled(value = Tristate.UNDEFINED)
    public void onConnect(ClientConnectionEvent.Login event) {
        BanService service = Sponge.getServiceManager().provide(BanService.class).get();
        if (service.isBanned(event.getProfile())) {
            Ban ban = service.getBanFor(event.getProfile()).get();
            Long time = ban.getExpirationDate().map(date -> (date.toEpochMilli() - System.currentTimeMillis())).orElse(-1L);
            event.setCancelled(true);
            event.setMessage(Messages.getFormatted("ban.banned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", ban.getReason().orElse(Messages.getFormatted("ban.command.ban.defaultreason"))));
        } else if (service.isBanned(event.getConnection().getAddress().getAddress())) {
            Ban ban = service.getBanFor(event.getConnection().getAddress().getAddress()).get();
            Long time = ban.getExpirationDate().map(date -> (date.toEpochMilli() - System.currentTimeMillis())).orElse(-1L);
            event.setCancelled(true);
            event.setMessage(Messages.getFormatted("ban.ipbanned", "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : TimeUtil.format(time)), "%reason%", ban.getReason().orElse(Messages.getFormatted("ban.command.ban.defaultreason"))));
        }

    }

    @Listener(order = Order.LATE)
    public void onMotd(ClientPingServerEvent event) {
        try {
            ModuleConfig config = Modules.BAN.get().getConfig().get();
            if (!config.get().getNode("ban-motd", "enabled").getBoolean()) return;

            String ip = event.getClient().getAddress().getAddress().toString().replace("/", "");
            GlobalDataFile file = new GlobalDataFile("ipcache");
            if (file.get().getChildrenMap().keySet().contains(ip)) {
                //Player
                GameProfile profile = Sponge.getServer().getGameProfileManager().get(UUID.fromString(file.get().getNode(ip, "uuid").getString())).get();
                InetAddress address = InetAddress.getByName(ip);

                //Check if banned
                BanService bs = Sponge.getServiceManager().provide(BanService.class).get();
                UserStorageService us = Sponge.getServiceManager().provide(UserStorageService.class).get();
                if (bs.isBanned(profile) || bs.isBanned(address)) {
                    Text motd = VariableUtil.replaceVariables(Messages.toText(config.get().getNode("ban-motd", "text").getString()), us.get(profile.getUniqueId()).orElse(null));

                    //Replace ban vars
                    Ban ban = bs.isBanned(profile) ? bs.getBanFor(profile).get() : bs.getBanFor(address).get();
                    Long time = ban.getExpirationDate().map(date -> (date.toEpochMilli() - System.currentTimeMillis())).orElse(-1L);
                    motd = TextUtil.replace(motd, "%time%", (time == -1L ? Messages.getFormatted("core.time.ever") : Text.of(TimeUtil.format(time))));
                    motd = TextUtil.replace(motd, "%reason%", ban.getReason().orElse(Messages.getFormatted("ban.command.ban.defaultreason")));

                    event.getResponse().setDescription(motd);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
