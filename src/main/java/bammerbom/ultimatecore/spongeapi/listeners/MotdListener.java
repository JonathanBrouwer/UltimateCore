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

import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.api.UEconomy;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

public class MotdListener {

    static boolean enableColors = true;
    static boolean enableVariables = true;
    static boolean enableBanMotd = true;
    static String banMotd = "&4&lYou are banned! &c(+Bantime left)\n&4&lReason: &c+Banreason";

    public static void start() {
        enableColors = r.getCnfg().getBoolean("Motd.EnableColors");
        enableVariables = r.getCnfg().getBoolean("Motd.EnableVariables");
        enableBanMotd = r.getCnfg().getBoolean("Motd.EnableBanMotd");
        banMotd = r.getCnfg().getString("Motd.BanMotd");
        Sponge.getEventManager().registerListeners(r.getUC(), new MotdListener());
    }

    @Listener
    public void motdHandler(ClientPingServerEvent e) {
        if (enableColors) {
            e.getResponse().setDescription(Text.of(TextColorUtil.translateAlternate(e.getResponse().getDescription().toString())));
        }
        if (enableVariables) {
            String playerip = e.getClient().getAddress().getAddress().toString().split("/")[1].split(":")[0];
            GameProfile p = null;
            for (GameProfile pl : r.getGameProfiles()) {
                if (UC.getPlayer(pl).getLastIp() == null) {
                    continue;
                }
                if (UC.getPlayer(pl).getLastIp().equalsIgnoreCase(playerip)) {
                    p = pl;
                }
            }
            String name = "";
            String money = "";
            if (p != null) {
                name = p.getName().orElse("");
                if (r.getVault() != null) {
                    if (r.getVault().getEconomy() != null) {
                        money = r.getVault().getEconomy().format(r.getVault().getEconomy().getBalance(p));
                    }
                }
            } else {
                name = TextColorUtil.strip(r.mes("motdDefaultName"));
                if (r.getVault() != null) {
                    if (r.getVault().getEconomy() != null) {
                        if (r.getVault().getEconomy() instanceof UEconomy) {
                            UEconomy ec = (UEconomy) r.getVault().getEconomy();
                            money = ec.format(ec.getStartingMoney());
                        }
                    }
                }
            }
            String ip = playerip;
            String version = Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
            int maxplayers = e.getMaxPlayers();
            int onlineplayers = e.getNumPlayers();
            String servername = Bukkit.getServerName();

            String motd = e.getMotd();
            motd = motd.replace("+Player", name);
            motd = motd.replace("+Ip", ip);
            motd = motd.replace("+Money", money);
            motd = motd.replace("+Version", version);
            motd = motd.replace("+Maxplayers", maxplayers + "");
            motd = motd.replace("+Onlineplayers", onlineplayers + "");
            motd = motd.replace("+Servername", servername);
            e.setMotd(motd);
        }
        if (enableBanMotd) {
            String playerip = e.getAddress().toString().split("/")[1].split(":")[0];
            OfflinePlayer p = null;
            for (OfflinePlayer pl : r.getGameProfiles()) {
                if (UC.getPlayer(pl) == null || UC.getPlayer(pl).getLastIp() == null) {
                    continue;
                }
                if (UC.getPlayer(pl).getLastIp().equalsIgnoreCase(playerip)) {
                    p = pl;
                }
            }
            if (p != null && UC.getPlayer(p).isBanned()) {
                String name = p.getName();
                String money = "";
                if (r.getVault() != null) {
                    if (r.getVault().getEconomy() != null) {
                        money = r.getVault().getEconomy().format(r.getVault().getEconomy().getBalance(p));
                    }
                }

                String ip = playerip;
                String version = Bukkit.getServer().getVersion().split("\\(MC: ")[1].split("\\)")[0];
                int maxplayers = e.getMaxPlayers();
                int onlineplayers = e.getNumPlayers();
                String servername = Bukkit.getServerName();

                String bantime = bammerbom.ultimatecore.bukkit.api.UC.getPlayer(p).getBanTime() <= 1 ? bammerbom.ultimatecore.bukkit.r.mes("banForever") : TextColorUtil.strip(DateUtil
                        .formatDateDiff(UC.getPlayer(p).getBanTime(), 2));
                String banreason = UC.getPlayer(p).getBanReason();

                String motd = banMotd;
                motd = TextColorUtil.translateAlternate(motd);
                motd = motd.replace("+Name", name);
                motd = motd.replace("+Ip", ip);
                motd = motd.replace("+Money", money);
                motd = motd.replace("+Version", version);
                motd = motd.replace("+Maxplayers", maxplayers + "");
                motd = motd.replace("+Onlineplayers", onlineplayers + "");
                motd = motd.replace("+Servername", servername);
                motd = motd.replace("+Bantime", TextColorUtil.strip(bantime));
                motd = motd.replace("+Banreason", banreason);
                e.setMotd(motd);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void joinMSG(final PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(r.getUC(), new Runnable() {
            @Override
            public void run() {
                if (!UC.getServer().getMotd().isEmpty()) {
                    e.getPlayer().sendMessage(UC.getServer().getMotd(e.getPlayer()));
                }
            }
        });
    }
}
