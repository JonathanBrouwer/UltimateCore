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
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.config.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.modules.afk.api.AfkKeys;
import bammerbom.ultimatecore.sponge.modules.tablist.api.TablistPermissions;
import bammerbom.ultimatecore.sponge.utils.*;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TablistRunnable implements Runnable {

    @Override
    public void run() {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        boolean enablehf = config.get().getNode("headerfooter", "enable").getBoolean();
        boolean enablenames = config.get().getNode("names", "enable").getBoolean();
        boolean compatibility = config.get().getNode("names", "compatibility-mode").getBoolean();
        if (!enablehf && !enablenames) {
            return;
        }

        LinkedHashMap<Player, Tuples.Tri<Text, Text, Text>> names = new LinkedHashMap<>();
        if (enablenames) {
            //Get all values and put them in a temporary map
            LinkedHashMap<Player, Tuples.Tri<Text, Text, Text>> tempnames = new LinkedHashMap<>();
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                tempnames.put(p, getDetails(p));
            }

            //Sort map by weight
            List<Player> nameslist = new ArrayList<>(tempnames.keySet());
            nameslist.sort((p1, p2) -> {
                Integer weight1 = TablistPermissions.UC_TABLIST_WEIGHT.getIntFor(p1);
                Integer weight2 = TablistPermissions.UC_TABLIST_WEIGHT.getIntFor(p2);
                return weight1.compareTo(weight2);
            });

            //Add to global list
            nameslist.forEach(name -> names.put(name, tempnames.get(name)));
        }

        String header;
        String footer;
        try {
            header = StringUtil.join("\n", config.get().getNode("headerfooter", "header").getList(TypeToken.of(String.class)));
            footer = StringUtil.join("\n", config.get().getNode("headerfooter", "footer").getList(TypeToken.of(String.class)));
        } catch (ObjectMappingException e) {
            ErrorLogger.log(e, "Failed to load header and footer for tablist.");
            return;
        }

        for (Player p : Sponge.getServer().getOnlinePlayers()) {
            TabList list = p.getTabList();

            //Header and footer
            if (enablehf) {
                list.setHeaderAndFooter(VariableUtil.replaceVariables(Messages.toText(header), p), VariableUtil.replaceVariables(Messages.toText(footer), p));
            }

            //Names
            if (enablenames) {
                if (!compatibility) {
                    //No compatibility, just update the tablist
                    new ArrayList<>(list.getEntries()).forEach(entry -> list.removeEntry(entry.getProfile().getUniqueId()));
                    names.forEach((player, name) -> list.addEntry(TabListEntry.builder().displayName(Text.of(name.getFirst(), name.getSecond(), name.getThird())).gameMode(player.gameMode().get()).latency(player.getConnection().getLatency()).list(list).profile(player.getProfile()).build()));
                } else {
                    //Compatibility, use scoreboard instead
                    Scoreboard board = p.getScoreboard();
                    int teamcount = 0;
                    for (Player t : Sponge.getServer().getOnlinePlayers()) {
                        String teamname = "uc_" + teamcount++;
                        if (!board.getTeam(teamname).isPresent()) {
                            board.registerTeam(Team.builder().name(teamname).build());
                        }
                        Team team = board.getTeam(teamname).get();
                        team.addMember(t.getTeamRepresentation());
                        team.setPrefix(names.get(t).getFirst());
                        team.setSuffix(names.get(t).getThird());
                    }
                    p.setScoreboard(board);
                }
            }
        }
    }

    //Prefix, name, suffix
    private Tuples.Tri<Text, Text, Text> getDetails(Player p) {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        CommentedConfigurationNode node = config.get();
        Text prefix = Messages.toText(node.getNode("names", "default", "prefix").getString());
        Text suffix = Messages.toText(node.getNode("names", "default", "suffix").getString());
        Text name = Messages.toText(node.getNode("names", "default", "format").getString());

        //Check if the uc.tablist.group property is set, in that case override name.
        String group = TablistPermissions.UC_TABLIST_GROUP.getFor(p);
        if (group != null && !node.getNode("names", "groups", group).isVirtual()) {
            CommentedConfigurationNode subnode = node.getNode("names", "groups", group);
            name = Messages.toText(subnode.getNode("format").getString());
            prefix = Messages.toText(subnode.getNode("prefix").getString());
            suffix = Messages.toText(subnode.getNode("suffix").getString());
        }

        //Afk suffix
        if (Modules.AFK.isPresent()) {
            UltimateUser up = UltimateCore.get().getUserService().getUser(p);
            if (up.get(AfkKeys.IS_AFK).get()) {
                Text afksuffix = Messages.toText(config.get().getNode("afk-suffix").getString());
                suffix = Text.of(suffix, afksuffix);
            }
        }

        //Max length check for prefix & suffix
        if (TextSerializers.FORMATTING_CODE.serialize(prefix).length() > 16) {
            prefix = TextSerializers.FORMATTING_CODE.deserialize(TextSerializers.FORMATTING_CODE.serialize(prefix).substring(0, 16));
        }
        if (TextSerializers.FORMATTING_CODE.serialize(suffix).length() > 16) {
            suffix = TextSerializers.FORMATTING_CODE.deserialize(TextSerializers.FORMATTING_CODE.serialize(suffix).substring(0, 16));
        }

        prefix = VariableUtil.replaceVariables(prefix, p);
        suffix = VariableUtil.replaceVariables(suffix, p);
        name = VariableUtil.replaceVariables(name, p);
        return Tuples.of(prefix, name, suffix);
    }
}
