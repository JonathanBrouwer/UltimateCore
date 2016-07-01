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
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CmdInventory implements UltimateCommand {

    public static void disable() {
        for (Player pl : UC.getServer().getInOnlineInventoryOnlinePlayers()) {
            UC.getPlayer(pl).setInOnlineInventory(null);
            pl.closeInventory(Cause.builder().build());
        }
        for (Player pl : UC.getServer().getInOfflineInventoryOnlinePlayers()) {
            UC.getPlayer(pl).setInOfflineInventory(null);
            pl.closeInventory(Cause.builder().build());
        }
    }

    @Override
    public String getName() {
        return "inventory";
    }

    @Override
    public String getPermission() {
        return "uc.inventory";
    }

    @Override
    public String getUsage() {
        return "/<command> <Player>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Open a players inventory.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("inv", "invsee");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!(r.isPlayer(cs))) {
            return CommandResult.empty();
        }
        if (!r.perm(cs, "uc.inventory", true)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (r.checkArgs(args, 0)) {
            Player t = r.searchPlayer(args[0]).orElse(null);
            if (t != null) {
                if (r.checkArgs(args, 1) && args[1].equalsIgnoreCase("armor")) {
                    Inventory inv = Bukkit.getServer().createInventory(p, 9, "Equipped");
                    inv.setContents(t.getInventory().getArmorContents());
                    p.openInventory(inv);
                    UC.getPlayer(p).setInOfflineInventory(t);
                } else {
                    p.openInventory(t.getInventory());
                    UC.getPlayer(p).setInOnlineInventory(t);
                }
            } else {
                OfflinePlayer t2 = r.searchGameProfile(args[0]);
                if (t2 == null || (!t2.hasPlayedBefore() && !t2.isOnline())) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }
                if (UC.getPlayer(t2).getLastInventory() == null) {
                    r.sendMes(cs, "playerNotFound", "%Player", args[0]);
                    return CommandResult.empty();
                }

                p.openInventory(UC.getPlayer(t2).getLastInventory());
                UC.getPlayer(p).setInOfflineInventory(t2);
            }
        } else {
            r.sendMes(cs, "inventoryUsage");
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
//    @Override
//    public List<String> getAliases() {
//
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {

//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        return null;
//    }
}
