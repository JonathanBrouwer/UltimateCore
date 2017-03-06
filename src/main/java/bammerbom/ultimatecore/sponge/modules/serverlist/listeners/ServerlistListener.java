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
package bammerbom.ultimatecore.sponge.modules.serverlist.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.datafiles.GlobalDataFile;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.error.utils.ErrorLogger;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.serverlist.handlers.FaviconHandler;
import com.google.common.reflect.TypeToken;
import net.minecrell.statusprotocol.StatusProtocol;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class ServerlistListener {
    static Random random = new Random();

    @Listener(order = Order.LATE)
    public void onJoin(ClientConnectionEvent.Join event) {
        try {
            Player p = event.getTargetEntity();
            ModuleConfig config = Modules.SERVERLIST.get().getConfig().get();
            //Join motd
            if (config.get().getNode("joinmessage", "enable").getBoolean()) {
                List<String> joinmsgs = config.get().getNode("joinmessage", "joinmessages").getList(TypeToken.of(String.class));
                Text joinmsg = VariableUtil.replaceVariables(Messages.toText(joinmsgs.get(random.nextInt(joinmsgs.size()))), p);
                p.sendMessage(joinmsg);
            }
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to construct join message.");
        }
    }

    @Listener
    public void onMotdSend(ClientPingServerEvent event) {
        try {
            String ip = event.getClient().getAddress().getAddress().toString().replace("/", "");
            GlobalDataFile file = new GlobalDataFile("ipcache");
            ModuleConfig config = Modules.SERVERLIST.get().getConfig().get();
            if (file.get().getChildrenMap().keySet().contains(ip)) {
                //Player
                UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
                User p = service.get(UUID.fromString(file.get().getNode(ip, "uuid").getString())).orElse(null);
                if (p == null) return;
                UltimateUser up = UltimateCore.get().getUserService().getUser(p);

                //Motd
                if (config.get().getNode("player", "motd", "enable").getBoolean()) {
                    List<String> motds = config.get().getNode("player", "motd", "motds").getList(TypeToken.of(String.class));
                    Text motd = VariableUtil.replaceVariables(Messages.toText(motds.get(random.nextInt(motds.size()))), p);
                    event.getResponse().setDescription(motd);
                }

                //Version info
                if (config.get().getNode("player", "playercount", "enable").getBoolean()) {
                    List<String> formats = config.get().getNode("player", "playercount", "counter").getList(TypeToken.of(String.class));
                    Text format = VariableUtil.replaceVariables(Messages.toText(formats.get(random.nextInt(formats.size()))), p);
                    StatusProtocol.setVersion(event.getResponse(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(format), 1);
                }

                //Playercounter hover
                if (config.get().getNode("player", "playerhover", "enable").getBoolean()) {
                    List<String> hovers = config.get().getNode("player", "playerhover", "hover").getList(TypeToken.of(String.class));
                    Text hover = VariableUtil.replaceVariables(Messages.toText(hovers.get(random.nextInt(hovers.size()))), p);
                    if (event.getResponse().getPlayers().isPresent()) {
                        ClientPingServerEvent.Response.Players players = event.getResponse().getPlayers().get();
                        players.getProfiles().clear();
                        players.getProfiles().add(GameProfile.of(UUID.randomUUID(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(hover)));
                    }
                }

                //Favicon
                if (config.get().getNode("player", "favicon", "enable").getBoolean()) {
                    List<String> rawlist = config.get().getNode("player", "favicon", "favicons").getList(TypeToken.of(String.class));
                    Optional<Favicon> fav = FaviconHandler.randomFavicon(rawlist, event.getResponse(), p);
                    fav.ifPresent(favicon -> event.getResponse().setFavicon(favicon));
                }
            } else {
                //Unknown

                //Motd
                if (config.get().getNode("unknown", "motd", "enable").getBoolean()) {
                    List<String> motds = config.get().getNode("unknown", "motd", "motds").getList(TypeToken.of(String.class));
                    Text motd = VariableUtil.replaceVariables(Messages.toText(motds.get(random.nextInt(motds.size()))), null);
                    event.getResponse().setDescription(motd);
                }

                //Version info
                if (config.get().getNode("unknown", "playercount", "enable").getBoolean()) {
                    List<String> formats = config.get().getNode("unknown", "playercount", "counter").getList(TypeToken.of(String.class));
                    Text format = VariableUtil.replaceVariables(Messages.toText(formats.get(random.nextInt(formats.size()))), null);
                    StatusProtocol.setVersion(event.getResponse(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(format), 1);
                }

                //Playercounter hover
                if (config.get().getNode("unknown", "playerhover", "enable").getBoolean()) {
                    List<String> hovers = config.get().getNode("unknown", "playerhover", "hover").getList(TypeToken.of(String.class));
                    Text hover = VariableUtil.replaceVariables(Messages.toText(hovers.get(random.nextInt(hovers.size()))), null);
                    if (event.getResponse().getPlayers().isPresent()) {
                        ClientPingServerEvent.Response.Players players = event.getResponse().getPlayers().get();
                        players.getProfiles().clear();
                        players.getProfiles().add(GameProfile.of(UUID.randomUUID(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(hover)));
                    }
                }

                //Favicon
                if (config.get().getNode("unknown", "favicon", "enable").getBoolean()) {
                    List<String> rawlist = config.get().getNode("unknown", "favicon", "favicons").getList(TypeToken.of(String.class));
                    Optional<Favicon> fav = FaviconHandler.randomFavicon(rawlist, event.getResponse(), null);
                    fav.ifPresent(favicon -> event.getResponse().setFavicon(favicon));
                }
            }
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to send server list info.");
        }
    }
}
