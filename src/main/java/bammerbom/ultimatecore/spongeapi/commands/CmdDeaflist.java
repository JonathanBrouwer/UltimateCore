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
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdDeaflist implements UltimateCommand {

    @Override
    public String getName() {
        return "deaflist";
    }

    @Override
    public String getPermission() {
        return "uc.deaflist";
    }

    @Override
    public String getUsage() {
        return "/<command>";
    }

    @Override
    public Text getDescription() {
        return Text.of("View a list of all current deaf players.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.deaflist", true)) {
            return CommandResult.empty();
        }
        List<GameProfile> deafs = UC.getServer().getDeafGameProfiles();
        if (deafs == null || deafs.isEmpty()) {
            r.sendMes(cs, "deaflistNoDeafsFound");
            return CommandResult.empty();
        }
        StringBuilder deaflist = new StringBuilder();
        Integer cur = 0;
        String result;
        for (int i = 0;
             i < deafs.size();
             i++) {
            deaflist.append(deafs.get(cur).getName() + ", ");
            cur++;

        }
        result = deaflist.substring(0, deaflist.length() - 2);
        r.sendMes(cs, "deaflistDeafs", "%Deaflist", result);
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        List<String> deafs = new ArrayList<>();
        for (GameProfile pl : UC.getServer().getDeafGameProfiles()) {
            deafs.add(pl.getName().get());
        }
        return deafs;
    }
}
