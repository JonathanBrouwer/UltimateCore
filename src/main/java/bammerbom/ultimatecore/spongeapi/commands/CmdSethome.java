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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CmdSethome implements UltimateCommand {

    @Override
    public String getName() {
        return "sethome";
    }

    @Override
    public String getPermission() {
        return "uc.selhome";
    }

    @Override
    public String getUsage() {
        return "/<command> [Player:][Name]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Set or move a home.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.perm(p, "uc.sethome", true)) {
            return CommandResult.empty();
        }
        if (r.checkArgs(args, 0) && args[0].contains(":")) {
            if (!r.perm(p, "uc.sethome.others", true)) {
                return CommandResult.empty();
            }
            if (!r.checkArgs(args[0].split(":"), 1) || args[0].split(":")[0].isEmpty() || args[0].split(":")[1].isEmpty()) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                return CommandResult.empty();
            }
            GameProfile t = r.searchGameProfile(args[0].split(":")[0]).orElse(null);
            if (t == null) {
                r.sendMes(cs, "playerNotFound", "%Player", args[0].split(":")[0]);
                return CommandResult.empty();
            }
            List<String> homes = UC.getPlayer(t).getHomeNames();
            if (homes.contains(args[0])) {
                r.sendMes(cs, "sethomeMoved", "%Home", args[0]);
            } else {
                r.sendMes(cs, "sethomeSet", "%Home", args[0]);
            }
            if (!homes.contains(args[0].toLowerCase().split(":")[1])) {
                homes.add(args[0].toLowerCase().split(":")[1]);
            }
            UC.getPlayer(t).addHome(args[0].toLowerCase().split(":")[1], p.getLocation());
            return CommandResult.empty();
        }
        Set<String> multihomes = r.getCnfg().getConfigurationSection("Command.HomeLimits").getKeys(false);
        Integer limit = 1;
        if (multihomes != null) {
            for (String s : multihomes) {
                if (r.perm(cs, "uc.sethome." + s.toLowerCase(), false)) {
                    if (limit < r.getCnfg().getInt("Command.HomeLimits." + s)) {
                        limit = r.getCnfg().getInt("Command.HomeLimits." + s);
                    }
                }
            }
        }
        if (r.perm(cs, "uc.sethome.unlimited", false)) {
            limit = 999999;
        }
        List<String> homes = UC.getPlayer(p).getHomeNames();
        String name = r.checkArgs(args, 0) ? args[0] : "home";
        if (homes.size() >= limit && !homes.contains(name.toLowerCase())) {
            r.sendMes(cs, "sethomeMax", "%Limit", limit);
            return CommandResult.empty();
        }
        if (homes.contains(name)) {
            r.sendMes(cs, "sethomeMoved", "%Home", name);
        } else {
            r.sendMes(cs, "sethomeSet", "%Home", name);
        }
        if (!homes.contains(name.toLowerCase())) {
            homes.add(name.toLowerCase());
        }
        UC.getPlayer(p).addHome(name.toLowerCase(), p.getLocation());
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
