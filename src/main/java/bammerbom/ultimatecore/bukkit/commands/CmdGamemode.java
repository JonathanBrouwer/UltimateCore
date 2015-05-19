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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.UltimateCommand;
import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.StringUtil;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdGamemode implements UltimateCommand {

    @Override
    public String getName() {
        return "gamemode";
    }

    @Override
    public String getPermission() {
        return "uc.gamemode";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("gm", "s", "survival", "c", "creative", "a", "adventure", "spec", "spectator");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        //Survival
        if (label.equalsIgnoreCase("s") || label.equalsIgnoreCase("survival")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.survival", false, false) && !r.perm(cs, "uc.gamemode", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                p.setGameMode(GameMode.SURVIVAL);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeSurvival"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.survival", false, false) && !r.perm(cs, "uc.gamemode.others", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                Player t = r.searchPlayer(args[0]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                t.setGameMode(GameMode.SURVIVAL);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeSurvival"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeSurvival"), "%Player", r.getDisplayName(cs));
            }
            return;
        }
        //Creative
        if (label.equalsIgnoreCase("c") || label.equalsIgnoreCase("creative")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.creative", false, false) && !r.perm(cs, "uc.gamemode", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                p.setGameMode(GameMode.CREATIVE);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeCreative"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.creative", false, false) && !r.perm(cs, "uc.gamemode.others", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                Player t = r.searchPlayer(args[0]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                t.setGameMode(GameMode.CREATIVE);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeCreative"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeCreative"), "%Player", r.getDisplayName(cs));
            }
            return;
        }
        //Adventure
        if (label.equalsIgnoreCase("a") || label.equalsIgnoreCase("adventure")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.adventure", false, false) && !r.perm(cs, "uc.gamemode", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                p.setGameMode(GameMode.ADVENTURE);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeAdventure"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.adventure", false, false) && !r.perm(cs, "uc.gamemode.others", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                Player t = r.searchPlayer(args[0]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                t.setGameMode(GameMode.ADVENTURE);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeAdventure"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeAdventure"), "%Player", r.getDisplayName(cs));
            }
            return;
        }
        if (label.equalsIgnoreCase("spec") || label.equalsIgnoreCase("spectator")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.spectator", false, false) && !r.perm(cs, "uc.gamemode", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                if (!r.isPlayer(cs)) {
                    return;
                }
                Player p = (Player) cs;
                p.setGameMode(GameMode.SPECTATOR);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeSpectator"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.spectator", false, false) && !r.perm(cs, "uc.gamemode.others", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                Player t = r.searchPlayer(args[0]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return;
                }
                t.setGameMode(GameMode.SPECTATOR);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeSpectator"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeSpectator"), "%Player", r.getDisplayName(cs));
            }
            return;
        }
        //Gamemode
        if (label.equalsIgnoreCase("gm") || label.equalsIgnoreCase("gamemode")) {
            GameMode mode;
            if (!r.checkArgs(args, 0)) {
                r.sendMes(cs, "gamemodeUsage");
                return;
            }
            switch (args[0].toLowerCase()) {
                case "survival":
                case "s":
                case "surv":
                case "0":
                    mode = GameMode.SURVIVAL;
                    break;
                case "creative":
                case "c":
                case "crea":
                case "1":
                    mode = GameMode.CREATIVE;
                    break;
                case "adventure:":
                case "a":
                case "adven":
                case "2":
                    mode = GameMode.ADVENTURE;
                    break;
                case "sp":
                case "spec":
                case "spectate":
                case "spectator":
                case "3":
                    mode = GameMode.SPECTATOR;
                    break;
                default:
                    r.sendMes(cs, "gamemodeUsage");
                    return;
            }

            if (r.checkArgs(args, 1)) {
                //Permissions
                if (!r.perm(cs, "uc.gamemode.others." + mode.toString().toLowerCase(), false, false) && !r.perm(cs, "uc.gamemode.others", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                //
                Player t = r.searchPlayer(args[1]);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return;
                }
                t.setGameMode(mode);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())), "%Player", r.getDisplayName(cs));
            } else {
                if (!r.isPlayer(cs)) {
                    return;
                }
                //Permissions
                if (!r.perm(cs, "uc.gamemode." + mode.toString().toLowerCase(), false, false) && !r.perm(cs, "uc.gamemode", false, false)) {
                    r.sendMes(cs, "noPermissions");
                    return;
                }
                //
                Player p = (Player) cs;
                p.setGameMode(mode);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())));
            }
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> rtrn = new ArrayList<>();
        if (alias.equalsIgnoreCase("gm") || alias.equalsIgnoreCase("gamemode")) {
            if (curn == 0) {
                for (GameMode g : GameMode.values()) {
                    rtrn.add(g.name().toLowerCase());
                }
                return rtrn;
            }
        }
        return null;
    }
}
