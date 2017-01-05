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
package bammerbom.ultimatecore.sponge.impl.variable.variables;

import bammerbom.ultimatecore.sponge.api.variable.Variable;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

public class NameVariable implements Variable {
    @Override
    public String getKey() {
        return "%name%";
    }

    @Override
    public Text replace(Text text) {
        return TextUtil.replace(text, "%name%", Text.of());
    }

    @Override
    public Text replaceUser(Text text, User p) {
        return TextUtil.replace(text, "%name%", Text.of(p.getName()));
    }

    @Override
    public Text replaceSource(Text text, CommandSource p) {
        return TextUtil.replace(text, "%name%", Text.of(p.getName()));
    }

    @Override
    public Text replacePlayer(Text text, Player p) {
        return TextUtil.replace(text, "%name%", Text.of(p.getName()));
    }
}
