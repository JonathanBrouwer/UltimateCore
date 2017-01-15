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

import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.config.ModuleConfig;
import bammerbom.ultimatecore.sponge.modules.tablist.api.TablistPermissions;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.StringUtil;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TablistRunnable implements Runnable {

    @Override
    public void run() {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        boolean enablehf = config.get().getNode("headerfooter", "enable").getBoolean();
        boolean enablenames = config.get().getNode("names", "enable").getBoolean();
        if (!enablehf && !enablenames) {
            return;
        }

        HashMap<Player, Text> names = new HashMap<>();
        if (enablenames) {
            for (Player p : Sponge.getServer().getOnlinePlayers()) {
                names.put(p, getName(p));
            }
        }

        String header;
        String footer;
        try {
            header = StringUtil.join("\n", config.get().getNode("headerfooter", "header").getList(TypeToken.of(String.class)));
            footer = StringUtil.join("\n", config.get().getNode("headerfooter", "footer").getList(TypeToken.of(String.class)));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
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
                new ArrayList<>(list.getEntries()).forEach(entry -> list.removeEntry(entry.getProfile().getUniqueId()));
                names.forEach((player, name) -> list.addEntry(TabListEntry.builder().displayName(name).gameMode(player.gameMode().get()).latency(player.getConnection().getLatency()).list(list).profile(player.getProfile()).build()));
            }
        }
    }

    private Text getName(Player p) {
        ModuleConfig config = Modules.TABLIST.get().getConfig().get();
        CommentedConfigurationNode node = config.get();
        Text name = Messages.toText(node.getNode("names", "default", "format").getString());

        //Check if the uc.tablist.group property is set, in that case override name.
        String group = TablistPermissions.UC_TABLIST_GROUP.getFor(p);
        if (group != null && !node.getNode("names", "groups", group).isVirtual()) {
            CommentedConfigurationNode subnode = node.getNode("names", "groups", group);
            name = Messages.toText(subnode.getNode("format").getString());
        }

        return VariableUtil.replaceVariables(name, p);
    }
}
