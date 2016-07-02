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
package bammerbom.ultimatecore.spongeapi.commands;

import bammerbom.ultimatecore.spongeapi.UltimateCommand;
import bammerbom.ultimatecore.spongeapi.api.UC;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;

import java.util.*;

public class CmdList implements UltimateCommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "uc.list";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("View a list of online players.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("players", "online", "who");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.list", true)) {
            return CommandResult.empty();
        }
        if (!Sponge.getServiceManager().provide(PermissionService.class).isPresent()) {
            StringBuilder online = new StringBuilder();
            Player[] players = r.getOnlinePlayers();
            Integer i = 0;

            for (Player player : players) {
                if ((!(cs instanceof Player)) || (((Player) cs).canSee(player))) {
                    if (online.length() > 0) {
                        online.append(", ");
                    }
                    i++;
                    online.append(UC.getPlayer(player).getDisplayName());
                }
            }
            r.sendMes(cs, "listList", "%Online", i, "%Max", Sponge.getServer().getMaxPlayers(), "%List", online.toString());
        } else {
            PermissionService pserv = Sponge.getServiceManager().provide(PermissionService.class).get();
            StringBuilder online = new StringBuilder();
            List<Player> plz = new ArrayList<>();
            plz.addAll(Sponge.getServer().getOnlinePlayers());
            Boolean first2 = true;

            HashMap<String, List<String>> players = new HashMap<>();
            List<String> nopl = new ArrayList<>();
            for (Player p : r.getOnlinePlayers()) {
                Subject maingroup = p.getSubjectData().getParents(new HashSet<>()).get(0);
                if (maingroup == null) {
                    nopl.add(p.getName());
                    continue;
                }
                String mname = maingroup.getIdentifier();
                List<String> pl = players.getOrDefault(mname, new ArrayList<>());
                pl.add(p.getName());
                players.put(mname, pl);
            }
            for (String gn : players.keySet()) {
                List<String> pls = players.get(gn);
                if (first2) {
                    first2 = false;
                    online.append(r.positive + StringUtil.firstUpperCase(gn) + ": ");
                } else {
                    online.append("\n" + r.positive + StringUtil.firstUpperCase(gn) + ": ");
                }
                online.append(StringUtil.joinList(pls));
            }
            if (!plz.isEmpty()) {
                online.append("\n" + r.positive + "No group: ");
                for (Player pl : plz) {
                    online.append(r.neutral + pl.getName());
                }
            }
            r.sendMes(cs, "listList", "%Online", Sponge.getServer().getOnlinePlayers().size(), "%Max", Sponge.getServer().getMaxPlayers(), "%List", online.toString());
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
