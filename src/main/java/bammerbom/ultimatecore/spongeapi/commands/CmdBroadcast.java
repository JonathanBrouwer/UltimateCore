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
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdBroadcast implements UltimateCommand {

    String format = r.getCnfg().getString("Chat.BroadcastFormat");

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public String getPermission() {
        return "uc.broadcast";
    }

    @Override
    public String getUsage() {
        return "/<command> <Message>";
    }

    @Override
    public String getDescription() {
        return "Broadcasts a message.";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("bc", "bcast", "shout");
    }

    @Override
    public void run(CommandSource cs, String label, String[] args) {
        if (!r.perm(cs, "uc.broadcast", false, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "broadcastUsage");
            return;
        }
        String message = r.getFinalArg(args, 0);
        message = format.replace("%Message", message);
        Sponge.getGame().getServer().getBroadcastSink()
                .sendMessage(Texts.of(r.translateAlternateColorCodes('&', message).replace("@1", r.positive + "").replace("@2", r.neutral + "").replace("@3", r.negative + "").replace("\\\\n", "\n")));
    }

    @Override
    public List<String> onTabComplete(CommandSource cs, String[] args, String label, String curs, Integer curn) {
        return new ArrayList<>();
    }
}
