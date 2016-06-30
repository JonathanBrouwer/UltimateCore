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
import bammerbom.ultimatecore.spongeapi.api.UKit;
import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.utils.DateUtil;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdCreatekit implements UltimateCommand {

    @Override
    public String getName() {
        return "createkit";
    }

    @Override
    public String getPermission() {
        return "uc.createkit";
    }

    @Override
    public String getUsage() {
        return "/<command> <Name> <Time> <Description...>";
    }

    @Override
    public Text getDescription() {
        return Text.of("Create a new kit with the items in your inventory.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.createkit", true)) {
            return CommandResult.empty();
        }
        if (!r.checkArgs(args, 2)) {
            r.sendMes(cs, "createkitUsage");
            return CommandResult.empty();
        }
        if (!r.isPlayer(cs)) {
            return CommandResult.empty();
        }
        Player p = (Player) cs;
        if (DateUtil.parseDateDiff(args[1]) == -1) {
            r.sendMes(cs, "createkitUsage");
            return CommandResult.empty();
        }

        String kitname = args[0].toLowerCase();
        String cooldown = args[1];
        String description = r.getFinalArg(args, 2);

        List<ItemStack> list = new ArrayList<>();
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.getType() != null && is.getType() != Material.AIR) {
                list.add(is);
            }
        }

        UKit kit = new UKit(kitname, cooldown, false, description, list);
        kit.save();
        r.sendMes(cs, "createkitCreated", "%Name", kitname);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }

}
