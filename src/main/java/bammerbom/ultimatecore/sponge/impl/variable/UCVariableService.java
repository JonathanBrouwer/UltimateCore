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
package bammerbom.ultimatecore.sponge.impl.variable;

import bammerbom.ultimatecore.sponge.api.variable.Variable;
import bammerbom.ultimatecore.sponge.api.variable.VariableService;
import bammerbom.ultimatecore.sponge.impl.variable.variables.*;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class UCVariableService implements VariableService {
    public ArrayList<Variable> variables = new ArrayList<>();

    public void init() {
        variables.add(new DisplaynameVariable());
        variables.add(new IpVariable());
        variables.add(new MaxplayersVariable());
        variables.add(new MoneyVariable());
        variables.add(new NameVariable());
        variables.add(new OnlineplayersVariable());
        variables.add(new PlayersVariable());
        variables.add(new PlayerVariable());
        variables.add(new PrefixVariable());
        variables.add(new SuffixVariable());
        variables.add(new VersionVariable());
        variables.add(new WorldaliasVariable());
        variables.add(new WorldVariable());
    }

    @Override
    public void register(Variable var) {
        variables.add(var);
    }

    @Override
    public void unregister(Variable var) {
        variables.remove(var);
    }

    @Override
    public List<Variable> getVariables() {
        return variables;
    }

    @Override
    public Text replace(Text text) {
        String plain = text.toPlain();
        for (Variable var : variables) {
            if (plain.contains(var.getKey())) {
                text = TextUtil.replace(text, var.getKey(), var.getValue(null).orElse(Text.of()));
            }
        }
        return text;
    }

    @Override
    public Text replaceUser(Text text, User user) {
        if (user instanceof Player) {
            return replacePlayer(text, (Player) user);
        }
        String plain = text.toPlain();
        for (Variable var : variables) {
            if (plain.contains(var.getKey())) {
                text = TextUtil.replace(text, var.getKey(), var.getValue(user).orElse(Text.of()));
            }
        }
        return text;
    }

    @Override
    public Text replaceSource(Text text, CommandSource source) {
        if (source instanceof Player) {
            return replacePlayer(text, (Player) source);
        }
        String plain = text.toPlain();
        for (Variable var : variables) {
            if (plain.contains(var.getKey())) {
                text = TextUtil.replace(text, var.getKey(), var.getValue(source).orElse(Text.of()));
            }
        }
        return text;
    }

    @Override
    public Text replacePlayer(Text text, Player player) {
        String plain = text.toPlain();
        for (Variable var : variables) {
            if (plain.contains(var.getKey())) {
                text = TextUtil.replace(text, var.getKey(), var.getValue(player).orElse(Text.of()));
            }
        }
        return text;
    }
}
