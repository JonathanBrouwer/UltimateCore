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
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;

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
    public String getUsage() {
        return "/<command> <Gamemode> [Player]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Change a player's gamemode.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("gm", "s", "survival", "c", "creative", "a", "adventure", "spec", "spectator");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        //Survival
        if (label.equalsIgnoreCase("s") || label.equalsIgnoreCase("survival")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.survival", false) && !r.perm(cs, "uc.gamemode", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                p.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeSurvival"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.survival", false) && !r.perm(cs, "uc.gamemode.others", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                t.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeSurvival"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeSurvival"), "%Player", r.getDisplayName(cs));
            }
            return CommandResult.empty();
        }
        //Creative
        if (label.equalsIgnoreCase("c") || label.equalsIgnoreCase("creative")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.creative", false) && !r.perm(cs, "uc.gamemode", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                p.offer(Keys.GAME_MODE, GameModes.CREATIVE);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeCreative"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.creative", false) && !r.perm(cs, "uc.gamemode.others", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                t.offer(Keys.GAME_MODE, GameModes.CREATIVE);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeCreative"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeCreative"), "%Player", r.getDisplayName(cs));
            }
            return CommandResult.empty();
        }
        //Adventure
        if (label.equalsIgnoreCase("a") || label.equalsIgnoreCase("adventure")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.adventure", false) && !r.perm(cs, "uc.gamemode", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                p.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeAdventure"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.adventure", false) && !r.perm(cs, "uc.gamemode.others", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                t.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeAdventure"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeAdventure"), "%Player", r.getDisplayName(cs));
            }
            return CommandResult.empty();
        }
        if (label.equalsIgnoreCase("spec") || label.equalsIgnoreCase("spectator")) {

            if (!r.checkArgs(args, 0)) {
                if (!r.perm(cs, "uc.gamemode.spectator", false) && !r.perm(cs, "uc.gamemode", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                Player p = (Player) cs;
                p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemodeSpectator"));
            } else {
                if (!r.perm(cs, "uc.gamemode.others.spectator", false) && !r.perm(cs, "uc.gamemode.others", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                Player t = r.searchPlayer(args[0]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                t.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemodeSpectator"), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemodeSpectator"), "%Player", r.getDisplayName(cs));
            }
            return CommandResult.empty();
        }
        //Gamemode
        if (label.equalsIgnoreCase("gm") || label.equalsIgnoreCase("gamemode")) {
            GameMode mode;
            if (!r.checkArgs(args, 0)) {
                r.sendMes(cs, "gamemodeUsage");
                return CommandResult.empty();
            }
            switch (args[0].toLowerCase()) {
                case "survival":
                case "s":
                case "surv":
                case "0":
                    mode = GameModes.SURVIVAL;
                    break;
                case "creative":
                case "c":
                case "crea":
                case "1":
                    mode = GameModes.CREATIVE;
                    break;
                case "adventure:":
                case "a":
                case "adven":
                case "2":
                    mode = GameModes.ADVENTURE;
                    break;
                case "sp":
                case "spec":
                case "spectate":
                case "spectator":
                case "3":
                    mode = GameModes.SPECTATOR;
                    break;
                default:
                    r.sendMes(cs, "gamemodeUsage");
                    return CommandResult.empty();
            }

            if (r.checkArgs(args, 1)) {
                //Permissions
                if (!r.perm(cs, "uc.gamemode.others." + mode.toString().toLowerCase(), false) && !r.perm(cs, "uc.gamemode.others", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                //
                Player t = r.searchPlayer(args[1]).orElse(null);
                if (t == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[1]);
                    return CommandResult.empty();
                }
                t.offer(Keys.GAME_MODE, mode);
                r.sendMes(cs, "gamemodeOthersSelf", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())), "%Player", t.getName());
                r.sendMes(t, "gamemodeOthersOther", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())), "%Player", r.getDisplayName(cs));
            } else {
                if (!r.isPlayer(cs)) {
                    return CommandResult.empty();
                }
                //Permissions
                if (!r.perm(cs, "uc.gamemode." + mode.toString().toLowerCase(), false) && !r.perm(cs, "uc.gamemode", false)) {
                    r.sendMes(cs, "noPermissions");
                    return CommandResult.empty();
                }
                //
                Player p = (Player) cs;
                p.offer(Keys.GAME_MODE, mode);
                r.sendMes(cs, "gamemodeSelf", "%Gamemode", r.mes("gamemode" + StringUtil.firstUpperCase(mode.toString().toLowerCase())));
            }
        }

        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        ArrayList<String> rtrn = new ArrayList<>();
        if (alias.equalsIgnoreCase("gm") || alias.equalsIgnoreCase("gamemode")) {
            if (curn == 0) {
                for (GameMode g : Sponge.getRegistry().getAllOf(CatalogTypes.GAME_MODE)) {
                    rtrn.add(g.getName().toLowerCase());
                }
                return rtrn;
            }
        }
        return null;
    }
}
