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
import bammerbom.ultimatecore.spongeapi.resources.utils.LocationUtil;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CmdEditsign implements UltimateCommand {

    @Override
    public String getName() {
        return "editsign";
    }

    @Override
    public String getPermission() {
        return "uc.editsign";
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
        return Arrays.asList("signedit");
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.editsign", true)) {
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "editsignUsage");
            return CommandResult.empty();
        }
        Optional<Location> b = LocationUtil.getAbsoluteTarget(p).getBlock();
        if (!b.isPresent() || !b.get().getTileEntity().isPresent() || !(b.get().getTileEntity().get() instanceof Sign)) {
            if (b != null && b.getState() != null) {
                r.sendMes(cs, "editsignNoSignA", "%Block", ItemUtil.getName(new ItemStack(b.getType(), b.getData())));
            } else {
                r.sendMes(cs, "editsignNoSignB");
            }
            return CommandResult.empty();
        }
        Sign s = (Sign) b.getState();
        int lineNumber;
        try {
            lineNumber = Integer.parseInt(args[0]);
            lineNumber--;
        } catch (NumberFormatException e) {
            r.sendMes(cs, "editsignUsage");
            return CommandResult.empty();
        }
        if ((lineNumber < 0) || (lineNumber > 3)) {
            r.sendMes(cs, "editsignUsage");
            return CommandResult.empty();
        }
        String text = args.length < 2 ? "" : r.getFinalArg(args, 1);
        String l1 = lineNumber == 0 ? text : s.getLine(0);
        String l2 = lineNumber == 1 ? text : s.getLine(1);
        String l3 = lineNumber == 2 ? text : s.getLine(2);
        String l4 = lineNumber == 3 ? text : s.getLine(3);
        SignChangeEvent ev = new SignChangeEvent(b, p, new String[]{l1, l2, l3, l4});
        Bukkit.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            r.sendMes(cs, "editsignNoAccess");
            return CommandResult.empty();
        }
        if (args.length < 2) {
            s.setLine(lineNumber, "");
            s.update();
            r.sendMes(cs, "editsignClear", "%Line", lineNumber + 1);
            return CommandResult.empty();
        }
        if (r.perm(p, "uc.sign.colored", false, false)) {
            s.setLine(0, TextColorUtil.translateAlternate(l1));
            s.setLine(1, TextColorUtil.translateAlternate(l2));
            s.setLine(2, TextColorUtil.translateAlternate(l3));
            s.setLine(3, TextColorUtil.translateAlternate(l4));
            text = TextColorUtil.translateAlternate(text);
        } else {
            s.setLine(lineNumber, text);
        }
        s.update();
        r.sendMes(cs, "editsignSet", "%Line", lineNumber + 1, "%Text", text);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
//    @Override
//    public List<String> getAliases() {
//        return Arrays.asList("signedit");
//    }
//
//    @Override
//    public void run(final CommandSource cs, String label, String[] args) {
//        if (!r.perm(cs, "uc.editsign", false, true)) {
//            return CommandResult.empty();
//        }
//        if (!r.isPlayer(cs)) {
//            return CommandResult.empty();
//        }
//        Player p = (Player) cs;
//        if (!r.checkArgs(args, 0)) {
//            r.sendMes(cs, "editsignUsage");
//            return CommandResult.empty();
//        }
//        Block b = LocationUtil.getAbsoluteTarget(p).getBlock();
//        if ((b == null) || b.getState() == null || (!(b.getState() instanceof Sign))) {
//            if (b != null && b.getState() != null) {
//                r.sendMes(cs, "editsignNoSignA", "%Block", ItemUtil.getName(new ItemStack(b.getType(), b.getData())));
//            } else {
//                r.sendMes(cs, "editsignNoSignB");
//            }
//            return CommandResult.empty();
//        }
//        Sign s = (Sign) b.getState();
//        int lineNumber;
//        try {
//            lineNumber = Integer.parseInt(args[0]);
//            lineNumber--;
//        } catch (NumberFormatException e) {
//            r.sendMes(cs, "editsignUsage");
//            return CommandResult.empty();
//        }
//        if ((lineNumber < 0) || (lineNumber > 3)) {
//            r.sendMes(cs, "editsignUsage");
//            return CommandResult.empty();
//        }
//        String text = args.length < 2 ? "" : r.getFinalArg(args, 1);
//        String l1 = lineNumber == 0 ? text : s.getLine(0);
//        String l2 = lineNumber == 1 ? text : s.getLine(1);
//        String l3 = lineNumber == 2 ? text : s.getLine(2);
//        String l4 = lineNumber == 3 ? text : s.getLine(3);
//        SignChangeEvent ev = new SignChangeEvent(b, p, new String[]{l1, l2, l3, l4});
//        Bukkit.getPluginManager().callEvent(ev);
//        if (ev.isCancelled()) {
//            r.sendMes(cs, "editsignNoAccess");
//            return CommandResult.empty();
//        }
//        if (args.length < 2) {
//            s.setLine(lineNumber, "");
//            s.update();
//            r.sendMes(cs, "editsignClear", "%Line", lineNumber + 1);
//            return CommandResult.empty();
//        }
//        if (r.perm(p, "uc.sign.colored", false, false)) {
//            s.setLine(0, TextColorUtil.translateAlternate(l1));
//            s.setLine(1, TextColorUtil.translateAlternate(l2));
//            s.setLine(2, TextColorUtil.translateAlternate(l3));
//            s.setLine(3, TextColorUtil.translateAlternate(l4));
//            text = TextColorUtil.translateAlternate(text);
//        } else {
//            s.setLine(lineNumber, text);
//        }
//        s.update();
//        r.sendMes(cs, "editsignSet", "%Line", lineNumber + 1, "%Text", text);
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSource cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
//        if (curn == 0) {
//            return Arrays.asList("1", "2", "3", "4");
//        } else {
//            return new ArrayList<>();
//        }
//    }
}
