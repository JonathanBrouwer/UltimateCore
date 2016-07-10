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
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.TextColorUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.scoreboard.Visibilities;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabListener {

    static String defaultFormat = TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.Tab.DefaultFormat"));
    static HashMap<String, String> tabFormats = new HashMap<>();
    static String afkFormat = TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.Tab.AfkFormat"));
    static boolean headerFooterEnabled = r.getCnfg().getBoolean("Chat.Tab.HeaderFooterEnabled");
    static String header = TextColorUtil.translateAlternate(r.getCnfg().isList("Chat.Tab.Header") ? StringUtil.join("\n", r.getCnfg().getStringList("Chat.Tab.Header")) : r.getCnfg()
            .getString("Chat.Tab.Header"));
    static String footer = TextColorUtil.translateAlternate(r.getCnfg().isList("Chat.Tab.Footer") ? StringUtil.join("\n", r.getCnfg().getStringList("Chat.Tab.Footer")) : r.getCnfg()
            .getString("Chat.Tab.Footer"));
    static Boolean abovehead = r.getCnfg().getBoolean("Chat.Nametag.Enabled");
    static String defaultPrefix = r.getCnfg().getString("Chat.Nametag.DefaultPrefix");
    static String defaultSuffix = r.getCnfg().getString("Chat.Nametag.DefaultSuffix");
    static HashMap<String, String> prefixes = new HashMap<>();
    static HashMap<String, String> suffixes = new HashMap<>();

    static Scoreboard board;

    public static void start() {
        if (!r.getCnfg().getBoolean("Chat.Tab.Enabled")) {
            return;
        }
        for (String key : r.getCnfg().getConfigurationSection("Chat.Tab.Groups").getValues(false).keySet()) {
            tabFormats.put(key, TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.Tab.Groups." + key)));
        }
        for (String key : r.getCnfg().getConfigurationSection("Chat.Nametag.Groups").getValues(false).keySet()) {
            prefixes.put(key, TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.Nametag.Groups." + key + ".Prefix")));
            suffixes.put(key, TextColorUtil.translateAlternate(r.getCnfg().getString("Chat.Nametag.Groups." + key + ".Suffix")));
        }


        Sponge.getEventManager().registerListeners(r.getUC(), new TabListener());
        final List<TabListEntry> entryList = new ArrayList<>();
        Sponge.getScheduler().createTaskBuilder().delayTicks(100L).name("UC tab task").execute(new Runnable() {
            @Override
            public void run() {
                for (Player p : r.getOnlinePlayers()) {
                    //HEADER AND FOOTER
                    if (headerFooterEnabled) {
                        p.getTabList().setHeaderAndFooter(Text.of(replaceVariables(header, p)), Text.of(replaceVariables(footer, p)));
                    }

                    //PLAYER NAMES
                    Subject group = r.getPrimaryGroup(p);

                    //
                    String base = (!group.getIdentifier().isEmpty() && tabFormats.containsKey(group.getIdentifier())) ? tabFormats.get(group.getIdentifier()) : defaultFormat;
                    base = replaceVariables(base, p);
                    if (UC.getPlayer(p).isAfk()) {
                        base = afkFormat.replace("+Original", base);
                    }
                    entryList.add(TabListEntry.builder().from(p.getTabList().getEntry(p.getUniqueId()).orElse(TabListEntry.builder().build())).displayName(Text.of(base)).build());
                    //SCOREBOARD
                    if (abovehead) {
                        if (board == null) {
                            board = Scoreboard.builder().build();
                        }
                        Team team = board.getTeam(p.getName()).orElse(null);
                        if (team == null) {
                            team = Team.builder().name(p.getName()).build();
                            board.registerTeam(team);
                        }
                        String prefix = defaultPrefix;
                        if (prefixes.containsKey(group)) {
                            prefix = prefixes.get(group);
                        }
                        String suffix = defaultSuffix;
                        if (suffixes.containsKey(group)) {
                            suffix = suffixes.get(group);
                        }
                        team.addMember(p.getTeamRepresentation());
                        prefix = replaceVariables(prefix, p);
                        suffix = replaceVariables(suffix, p);
                        if (prefix.length() > 16) {
                            prefix = prefix.substring(0, 15);
                        }
                        if (suffix.length() > 16) {
                            suffix = suffix.substring(0, 15);
                        }
                        team.setPrefix(Text.of(prefix));
                        team.setSuffix(Text.of(suffix));
                        team.setNameTagVisibility(Visibilities.ALL);
                    }
                }
                for (Player p : r.getOnlinePlayers()) {
                    //TODO best way to apply?
                    for (TabListEntry en : entryList) {

                    }
                }
                if (abovehead) {
                    for (Player pl : r.getOnlinePlayers()) {
                        pl.setScoreboard(board);
                    }
                }
            }
        }).submit(r.getUC());
    }

    public static String replaceVariables(String base, Player p) {
        String playerip = p.getConnection().getAddress().toString().split("/")[1].split(":")[0];
        String name = p.getName();
        String money = "";
        if (Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
            EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();
            if (service.getOrCreateAccount(p.getUniqueId()).isPresent()) {
                money = service.getOrCreateAccount(p.getUniqueId()).get().getBalance(service.getDefaultCurrency()).toString();
            }
        }
        String ip = playerip;
        String version = Sponge.getPlatform().getMinecraftVersion().getName();
        int maxplayers = Sponge.getServer().getMaxPlayers();
        int onlineplayers = r.getOnlinePlayers().length;
        String servername = Sponge.getServer().getServerName();

        String group = "";
        String prefix = p.getOption("prefix").orElse("");
        String suffix = p.getOption("suffix").orElse("");
        String displayname = UC.getPlayer(p).getDisplayName().toPlain();
        String worldalias = p.getWorld().getName().charAt(0) + "";
        String world = p.getWorld().getName();
        //String faction = r.getFaction(p) != null ? r.getFaction(p) : "";
        //String town = r.getTown(p) != null ? r.getTown(p) : "";

        base = base.replace("+Group", group);
        base = base.replace("+Prefix", prefix);
        base = base.replace("+Suffix", suffix);
        base = base.replace("+Name", name);
        base = base.replace("+Displayname", displayname);
        base = base.replace("+World", world);
        base = base.replace("+WorldAlias", worldalias);
        //base = base.replace("+Town", town);
        //base = base.replace("+Faction", faction);
        base = base.replace("+Ip", ip);
        base = base.replace("+Money", money);
        base = base.replace("+Version", version);
        base = base.replace("+Maxplayers", maxplayers + "");
        base = base.replace("+Onlineplayers", onlineplayers + "");
        base = base.replace("+Servername", servername);
        base = base.replace("+Uptime", TextColorUtil.strip(DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
        base = TextColorUtil.translateAlternate(base);
        return base;
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        //Format
        Player p = e.getTargetEntity();
        //Header and footer
        if (headerFooterEnabled) {
            p.getTabList().setHeaderAndFooter(Text.of(replaceVariables(header, p)), Text.of(replaceVariables(footer, p)));
        }
    }
}
