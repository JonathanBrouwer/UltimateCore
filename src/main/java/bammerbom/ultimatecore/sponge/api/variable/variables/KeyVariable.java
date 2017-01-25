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
 * furnished to do so, Player to the following conditions:
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
package bammerbom.ultimatecore.sponge.api.variable.variables;

import bammerbom.ultimatecore.sponge.api.variable.DynamicVariable;
import bammerbom.ultimatecore.sponge.utils.ArgumentUtil;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.TextUtil;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;

public class KeyVariable implements DynamicVariable {
    //Format: %option:prefix%
    @Override
    public Text replace(Text text, @Nullable Object player) {
        if (player instanceof Player) {
            Player p = (Player) player;
            for (String var : TextUtil.getVariables(text)) {
                if (var.startsWith("%key:")) {
                    try {
                        String keyid = var.replace("%", "").split(":")[1];
                        //Error will get handled in catch
                        Key key = ArgumentUtil.getKeyById(keyid).get();
                        String value = p.get(key).get().toString();
                        text = TextUtil.replace(text, var, Messages.toText(value));
                    } catch (Exception ex) {
                        text = TextUtil.replace(text, var, Text.of());
                    }
                }
            }
        }
        return text;
    }
}
