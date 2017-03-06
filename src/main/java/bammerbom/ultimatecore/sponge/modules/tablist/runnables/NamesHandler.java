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
package bammerbom.ultimatecore.sponge.modules.tablist.runnables;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.data_old.user.UltimateUser;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.tablist.api.TablistPermissions;
import bammerbom.ultimatecore.sponge.utils.Tuples;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;
import java.util.stream.Collectors;

public class NamesHandler {

    public LinkedHashMap<UUID, Tuples.Tri<Text, Text, Text>> names = new LinkedHashMap<>();
    public Scoreboard board = null;
    public int teamcount = 0;
    public HashMap<UUID, Team> teams = new HashMap<>();


    public void update() {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        boolean enablenames = config.get().getNode("names", "enable").getBoolean();
        boolean compatibility = config.get().getNode("names", "compatibility-mode").getBoolean();
        boolean updated = false;
        HeaderFooterHandler.handleHeaderFooter();
        if (!enablenames) {
            return;
        }

        //Add new players
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (!this.names.containsKey(p.getUniqueId())) {
                this.names.put(p.getUniqueId(), getDetails(p));
                updated = true;
            }
        }

        //Remove old players
        List<UUID> removenames = this.names.keySet().stream().filter(uuid -> !Sponge.getServer().getPlayer(uuid).isPresent()).collect(Collectors.toList());
        removenames.forEach(this::removeCache);

        //Resort map if needed
        if (updated) {
            resortNamesList();
        }

        //Update tablists
        if (compatibility) {
            refreshCompat();
        } else {
            refreshNormal();
        }
    }

    public void refreshNormal() {
        //TODO don't rewrite everything?
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            TabList list = p.getTabList();
            new ArrayList<>(list.getEntries()).forEach(entry -> list.removeEntry(entry.getProfile().getUniqueId()));
            this.names.forEach((uuid, name) -> {
                Player player = Sponge.getServer().getPlayer(uuid).get();
                Text fullname = Text.of(name.getFirst(), name.getSecond(), name.getThird());
                list.addEntry(TabListEntry.builder().displayName(fullname).gameMode(player.gameMode().get()).latency(player.getConnection().getLatency()).list(list).profile(player.getProfile()).build());
            });
        }
    }

    public void refreshCompat() {
        //Generate teams
        if (this.board == null) {
            this.board = Scoreboard.builder().build();
        }
        boolean updated = false;
        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            if (!this.teams.containsKey(p.getUniqueId())) {
                updated = true;
                String teamname = "uc_" + this.teamcount++;
                //Team might be present if it has been used earlier
                if (!this.board.getTeam(teamname).isPresent()) {
                    this.board.registerTeam(Team.builder().name(teamname).build());
                }
                Team team = this.board.getTeam(teamname).get();
                team.addMember(p.getTeamRepresentation());
                team.setPrefix(this.names.get(p.getUniqueId()).getFirst());
                team.setSuffix(this.names.get(p.getUniqueId()).getThird());
                this.teams.put(p.getUniqueId(), team);
            }
        }
        if (updated) {
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                p.setScoreboard(this.board);
            }
        }
    }

    public void resortNamesList() {
        //Resort
        LinkedHashMap<UUID, Tuples.Tri<Text, Text, Text>> tempnames = (LinkedHashMap<UUID, Tuples.Tri<Text, Text, Text>>) this.names.clone();
        this.names.clear();

        //Sort map by weight
        List<UUID> nameslist = new ArrayList<>(tempnames.keySet());
        nameslist.sort((u1, u2) -> {
            Player p1 = Sponge.getServer().getPlayer(u1).get();
            Player p2 = Sponge.getServer().getPlayer(u2).get();
            Integer weight1 = TablistPermissions.UC_TABLIST_WEIGHT.getIntFor(p1);
            Integer weight2 = TablistPermissions.UC_TABLIST_WEIGHT.getIntFor(p2);
            return weight1.compareTo(weight2);
        });

        //Add to global list
        nameslist.forEach(name -> this.names.put(name, tempnames.get(name)));
    }

    //Prefix, name, suffix
    private Tuples.Tri<Text, Text, Text> getDetails(Player p) {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        CommentedConfigurationNode node = config.get();
        Text prefix = Messages.toText(node.getNode("names", "default", "prefix").getString(""));
        Text suffix = Messages.toText(node.getNode("names", "default", "suffix").getString(""));
        Text name = Messages.toText(node.getNode("names", "default", "format").getString(""));

        //Check if the uc.tablist.group property is set, in that case override name.
        String group = TablistPermissions.UC_TABLIST_GROUP.getFor(p);
        if (group != null && !node.getNode("names", "groups", group).isVirtual()) {
            CommentedConfigurationNode subnode = node.getNode("names", "groups", group);
            name = Messages.toText(subnode.getNode("format").getString(""));
            prefix = Messages.toText(subnode.getNode("prefix").getString(""));
            suffix = Messages.toText(subnode.getNode("suffix").getString(""));
        }

        //Afk suffix
        if (Modules.AFK.isPresent()) {
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            if (up.get(AfkKeys.IS_AFK).get()) {
                Text afksuffix = Messages.toText(config.get().getNode("afk-suffix").getString());
                suffix = Text.of(suffix, afksuffix);
            }
        }

        prefix = VariableUtil.replaceVariables(prefix, p);
        suffix = VariableUtil.replaceVariables(suffix, p);
        name = VariableUtil.replaceVariables(name, p);

        //Max length check for prefix & suffix
        if (TextSerializers.FORMATTING_CODE.serialize(name).length() > 16) {
            name = TextSerializers.FORMATTING_CODE.deserialize(TextSerializers.FORMATTING_CODE.serialize(name).substring(0, 16));
        }
        if (TextSerializers.FORMATTING_CODE.serialize(prefix).length() > 16) {
            prefix = TextSerializers.FORMATTING_CODE.deserialize(TextSerializers.FORMATTING_CODE.serialize(prefix).substring(0, 16));
        }
        if (TextSerializers.FORMATTING_CODE.serialize(suffix).length() > 16) {
            suffix = TextSerializers.FORMATTING_CODE.deserialize(TextSerializers.FORMATTING_CODE.serialize(suffix).substring(0, 16));
        }

        return Tuples.of(prefix, name, suffix);
    }

    public void removeCache(UUID uuid) {
        if (this.board != null) {
            this.board.getTeams().remove(this.teams.get(uuid));
        }
        this.names.remove(uuid);
        this.teams.remove(uuid);
    }

    public void clearCache() {
        this.board = Scoreboard.builder().build();
        this.names.clear();
        this.teamcount = 0;
        this.teams.clear();
        update();
    }
}
