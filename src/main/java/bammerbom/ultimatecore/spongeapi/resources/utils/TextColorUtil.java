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
package bammerbom.ultimatecore.spongeapi.resources.utils;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class TextColorUtil {
    static HashMap<TextColor, Character> colors = new HashMap<>();
    static HashMap<TextStyle, Character> styles = new HashMap<>();

    {
        colors.put(TextColors.BLACK, '0');
        colors.put(TextColors.DARK_BLUE, '1');
        colors.put(TextColors.DARK_GREEN, '2');
        colors.put(TextColors.DARK_AQUA, '3');
        colors.put(TextColors.DARK_RED, '4');
        colors.put(TextColors.DARK_PURPLE, '5');
        colors.put(TextColors.GOLD, '6');
        colors.put(TextColors.GRAY, '7');
        colors.put(TextColors.DARK_GREEN, '8');
        colors.put(TextColors.BLUE, '9');

        colors.put(TextColors.GREEN, 'a');
        colors.put(TextColors.AQUA, 'b');
        colors.put(TextColors.RED, 'c');
        colors.put(TextColors.LIGHT_PURPLE, 'd');
        colors.put(TextColors.YELLOW, 'e');
        colors.put(TextColors.WHITE, 'f');

        styles.put(TextStyles.OBFUSCATED, 'k');
        styles.put(TextStyles.BOLD, 'l');
        styles.put(TextStyles.STRIKETHROUGH, 'm');
        styles.put(TextStyles.UNDERLINE, 'n');
        styles.put(TextStyles.ITALIC, 'o');
        styles.put(TextStyles.RESET, 'r');
    }

    public static Optional<Object> getByChar(char code) {
        for (TextColor color : colors.keySet()) {
            if (colors.get(color).equals(code)) {
                return Optional.of(color);
            }
        }
        for (TextStyle style : styles.keySet()) {
            if (styles.get(style).equals(code)) {
                return Optional.of(style);
            }
        }
        return Optional.empty();
    }

    public static Optional<TextColor> getColorByChar(char code) {
        for (TextColor color : colors.keySet()) {
            if (colors.get(color).equals(code)) {
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }

    public static String translateAlternate(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if ((b[i] == '&') && (i == 0 || b[i - 1] != '&') && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1)) {
                b[i] = '§';
                b[(i + 1)] = Character.toLowerCase(b[(i + 1)]);
            }
        }
        return new String(b);
    }

    public static String strip(String input) {
        if (input == null) {
            return null;
        }
        return Pattern.compile("(?i)" + String.valueOf('�') + "[0-9A-FK-OR]").matcher(input).replaceAll("");
    }
}
