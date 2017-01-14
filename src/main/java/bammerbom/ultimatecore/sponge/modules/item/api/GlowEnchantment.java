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
package bammerbom.ultimatecore.sponge.modules.item.api;

import bammerbom.ultimatecore.sponge.utils.Messages;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.translation.Translation;

import java.util.Locale;

public class GlowEnchantment implements Enchantment {
    @Override
    public String getId() {
        return "ultimatecore:glowing";
    }

    @Override
    public String getName() {
        return Messages.getFormatted("item.command.itemglow.ench.name").toPlain();
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public int getMinimumLevel() {
        return 1;
    }

    @Override
    public int getMaximumLevel() {
        return 1;
    }

    @Override
    public int getMinimumEnchantabilityForLevel(int level) {
        return 0;
    }

    @Override
    public int getMaximumEnchantabilityForLevel(int level) {
        return 0;
    }

    @Override
    public boolean canBeAppliedToStack(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canBeAppliedByTable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isCompatibleWith(Enchantment ench) {
        return true;
    }

    @Override
    public Translation getTranslation() {
        return new Translation() {
            @Override
            public String getId() {
                return "glowing";
            }

            @Override
            public String get(Locale locale) {
                return Messages.getFormatted("item.command.itemglow.ench.name").toPlain();
            }

            @Override
            public String get(Locale locale, Object... args) {
                return Messages.getFormatted("item.command.itemglow.ench.name").toPlain();
            }
        };
    }
}
