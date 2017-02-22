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
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

@ConfigSerializable
public class VotifierScheme implements BiConsumer<Vote, Player> {

    @Setting
    private boolean enable;
    @Setting
    private double chance;
    @Setting
    private String permission;
    @Setting
    private List<String> commands;
    @Setting
    private List<Text> broadcast;

    public VotifierScheme() {

    }

    public VotifierScheme(boolean enable, double chance, String permission, List<String> commands, List<Text> broadcast) {
        this.enable = enable;
        this.chance = chance;
        this.permission = permission;
        this.commands = commands;
        this.broadcast = broadcast;
    }

    @Override
    public void accept(Vote vote, Player p) {
        if (!this.enable) {
            return;
        }

        //Check permission
        if (!p.hasPermission(this.permission)) {
            return;
        }

        //If a random number between 0-1 is higher than the chance/100, return
        if (Math.random() > (this.chance / 100)) {
            return;
        }

        //Execute commands
        for (String cmd : this.commands) {
            if (cmd.startsWith("/")) cmd = cmd.replaceFirst("/", "");
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), VariableUtil.replaceVariables(Text.of(cmd.replace("%player%", p.getName())), p).toPlain());
        }

        //Send one message randomly
        if (!this.broadcast.isEmpty()) {
            Text msg = this.broadcast.get(ThreadLocalRandom.current().nextInt(this.broadcast.size()));
            Sponge.getServer().getBroadcastChannel().send(VariableUtil.replaceVariables(msg, p));
        }
    }

    public boolean isEnabled() {
        return this.enable;
    }

    public double getChance() {
        return this.chance;
    }

    public String getPermission() {
        return this.permission;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public List<Text> getBroadcast() {
        return this.broadcast;
    }
}
