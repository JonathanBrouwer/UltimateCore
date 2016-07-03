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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CmdSave implements UltimateCommand {

    Boolean autosaveMessage = r.getCnfg().getBoolean("Command.Save.autosaveMessage");

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getPermission() {
        return "uc.save";
    }

    @Override
    public String getUsage() {
        return "/<command> [World]";
    }

    @Override
    public Text getDescription() {
        return Text.of("Save all worlds to disk, or save a specific world.");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList();
    }

    @Override
    public CommandResult run(final CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.save", true)) {
            return CommandResult.empty();
        }
        if (r.checkArgs(args, 0)) {
            World w = Sponge.getServer().getWorld(args[0]).orElse(null);
            if (w == null) {
                r.sendMes(cs, "worldNotFound", "%World", args[0]);
                return CommandResult.empty();
            }
            if (autosaveMessage) {
                Sponge.getServer().getBroadcastChannel().send(r.mes("saveStart"));
            } else {
                r.sendMes(cs, "saveStart");
            }
            try {
                w.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (autosaveMessage) {
                Sponge.getServer().getBroadcastChannel().send(r.mes("saveStart"));
            } else {
                r.sendMes(cs, "saveStart");
            }
            for (World w : Sponge.getServer().getWorlds()) {
                try {
                    w.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (autosaveMessage) {
            Sponge.getServer().getBroadcastChannel().send(r.mes("saveFinish"));
        } else {
            r.sendMes(cs, "saveFinish");
        }
        return CommandResult.success();
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
