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
package bammerbom.ultimatecore.sponge.modules.votifier.api;

import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import com.vexsoftware.votifier.model.Vote;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class VotifierScheme implements BiConsumer<Vote, Player> {

    private boolean enable;
    private double chance;
    private String permission;
    private List<String> commands;
    private List<Text> messages;

    public VotifierScheme(boolean enable, double chance, String permission, List<String> commands, List<Text> messages) {
        this.enable = enable;
        this.chance = chance;
        this.permission = permission;
        this.commands = commands;
        this.messages = messages;
    }

    @Override
    public void accept(Vote vote, Player p) {
        if (!enable) {
            return;
        }

        //Check permission
        if (!p.hasPermission(permission)) {
            return;
        }

        //If a random number between 0-1 is higher than the chance/100, return
        if (Math.random() > (chance / 100)) {
            return;
        }

        //Execute commands
        for (String cmd : commands) {
            if (cmd.startsWith("/")) cmd = cmd.replaceFirst("/", "");
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), VariableUtil.replaceVariables(Text.of(cmd.replace("%player%", p.getName())), p).toPlain());
        }

        //Send one message randomly
        if (!messages.isEmpty()) {
            Text msg = messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
            Sponge.getServer().getBroadcastChannel().send(VariableUtil.replaceVariables(msg, p));
        }
    }

    public boolean isEnabled() {
        return enable;
    }

    public double getChance() {
        return chance;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<Text> getMessages() {
        return messages;
    }
}
