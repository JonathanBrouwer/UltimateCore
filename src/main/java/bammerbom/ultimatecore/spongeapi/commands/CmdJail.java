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
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CmdJail implements UltimateCommand {

    static Random ra = new Random();

    @Override
    public String getName() {
        return "jail";
    }

    @Override
    public String getPermission() {
        return "uc.jail";
    }

    @Override
    public String getUsage() {
        return "/<command> ";
    }

    @Override
    public Text getDescription() {
        return Text.of("Description");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
//    @Override
//    public List<String> getAliases() {
//        return Arrays.asList();
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.perm(cs, "uc.jail", false, true)) {
//            return CommandResult.empty();
//        }
//        if (!r.checkArgs(args, 0)) {
//            HashMap<String, Location> jails = UC.getServer().getJails();
//            if (jails.isEmpty()) {
//                r.sendMes(cs, "jailNone");
//                return CommandResult.empty();
//            }
//            StringBuilder b = new StringBuilder();
//            for (String j : jails.keySet()) {
//                if (b.length() != 0) {
//                    b.append(j + ", ");
//                }
//                b.append(j);
//            }
//            r.sendMes(cs, "jailList", "%Jails", b.toString());
//        } else {
//            if (!r.checkArgs(args, 0)) {
//                r.sendMes(cs, "jailUsage");
//                return CommandResult.empty();
//            }
//            OfflinePlayer pl = r.searchGameProfile(args[0]);
//            if (pl == null || (!pl.hasPlayedBefore() && !pl.isOnline())) {
//                r.sendMes(cs, "playerNotFound", "%Player", args[0]);
//                return CommandResult.empty();
//            }
//            UPlayer pu = UC.getPlayer(pl);
//            if (pu.isJailed()) {
//                r.sendMes(cs, "jailAlreadyJailed", "%Player", pl.getName());
//                return CommandResult.empty();
//            }
//            HashMap<String, Location> jailsA = UC.getServer().getJails();
//            if (jailsA.isEmpty()) {
//                r.sendMes(cs, "jailNone");
//                return CommandResult.empty();
//            }
//            String jail;
//            Long time = -1L;
//            if (r.checkArgs(args, 1) && DateUtil.parseDateDiff(args[1]) != -1) {
//                time = DateUtil.parseDateDiff(args[1]);
//            }
//            if (r.checkArgs(args, 2)) {
//                jail = args[2];
//            } else if (r.checkArgs(args, 1) && time == -1) {
//                jail = args[1];
//            } else {
//                ArrayList<String> jails = UC.getServer().getJailsL();
//                jail = jails.get(ra.nextInt(jails.size()));
//            }
//            if (UC.getServer().getJail(jail) == null) {
//                r.sendMes(cs, "jailNotFound", "%Jail", jail);
//                return CommandResult.empty();
//            }
//            Location loc = UC.getServer().getJail(jail);
//            if (pl.isOnline()) {
//                LocationUtil.teleportUnsafe(pl.getPlayer(), loc, TeleportCause.PLUGIN, false);
//                r.sendMes(pl.getPlayer(), "jailTarget", "%Time", time != -1L ? DateUtil.format(time) : r.mes("jailEver"));
//            }
//            pu.jail(jail, time);
//            r.sendMes(cs, "jailSender", "%Jail", jail, "%Player", pl.getName(), "%Time", time != -1L ? DateUtil.format(time) : r.mes("jailEver"));
//        }
//
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        if (curn == 0) {
//            return null;
//        } else if (curn == 1) {
//            return UC.getServer().getJailsL();
//        } else {
//            return new ArrayList<>();
//        }
//    }
}
