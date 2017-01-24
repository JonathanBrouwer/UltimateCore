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
package bammerbom.ultimatecore.sponge.api.variable.impl;

import bammerbom.ultimatecore.sponge.api.variable.DynamicVariable;
import bammerbom.ultimatecore.sponge.api.variable.StaticVariable;
import bammerbom.ultimatecore.sponge.api.variable.VariableService;
import bammerbom.ultimatecore.sponge.api.variable.variables.*;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UCVariableService implements VariableService {
    private ArrayList<StaticVariable> staticVariables = new ArrayList<>();
    private ArrayList<DynamicVariable> dynamicVariables = new ArrayList<>();

    public void init() {
        staticVariables.add(new DisplaynameVariable());
        staticVariables.add(new IpVariable());
        staticVariables.add(new MaxplayersVariable());
        staticVariables.add(new MoneyVariable());
        staticVariables.add(new NameVariable());
        staticVariables.add(new OnlineplayersVariable());
        staticVariables.add(new PlayersVariable());
        staticVariables.add(new PlayerVariable());
        staticVariables.add(new PrefixVariable());
        staticVariables.add(new SuffixVariable());
        staticVariables.add(new UuidVariable());
        staticVariables.add(new VersionVariable());
        staticVariables.add(new WorldaliasVariable());
        staticVariables.add(new WorldVariable());
        staticVariables.add(new DeathsVariable());
        staticVariables.add(new KillsVariable());
        staticVariables.add(new PlayerkillsVariable());

        dynamicVariables.add(new OptionVariable());
    }

    @Override
    public void register(StaticVariable var) {
        staticVariables.add(var);
    }

    @Override
    public void unregister(StaticVariable var) {
        staticVariables.remove(var);
    }

    @Override
    public void register(DynamicVariable var) {
        dynamicVariables.add(var);
    }

    @Override
    public void unregister(DynamicVariable var) {
        dynamicVariables.add(var);
    }

    @Override
    public List<StaticVariable> getStaticVariables() {
        return staticVariables;
    }

    @Override
    public List<DynamicVariable> getDynamicVariables() {
        return dynamicVariables;
    }

    @Override
    public Text replace(Text text, @Nullable Object player) {
        String plain = text.toPlain();
        for (StaticVariable var : staticVariables) {
            if (plain.contains(var.getKey())) {
                text = TextUtil.replace(text, var.getKey(), var.getValue(player).orElse(Text.of()));
            }
        }
        for (DynamicVariable var : dynamicVariables) {
            text = var.replace(text, player);
        }
        return text;
    }
}
