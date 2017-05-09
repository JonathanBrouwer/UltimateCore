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
package bammerbom.ultimatecore.sponge.api.language.utils;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.command.exceptions.ErrorMessageException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;

public class Messages {

    public static void send(CommandSource p, String key, Object... vars) {
        p.sendMessage(getFormatted(p, key, vars));
    }

    public static ErrorMessageException error(CommandSource p, String key, Object... vars) {
        return new ErrorMessageException(getFormatted(p, key, vars));
    }

    /**
     * Get a string with all available formatting, including click and hover actions.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The formatted message
     */
    public static Text getFormatted(@Nullable CommandSource p, String key, Object... vars) {
        return UltimateCore.get().getLanguageService().getMessage(p, key, vars);
    }

    /**
     * Get a string with all available formatting, including click and hover actions.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The formatted message
     */
    public static Text getFormatted(String key, Object... vars) {
        return UltimateCore.get().getLanguageService().getMessage(null, key, vars);
    }

    /**
     * Get a string which is formatted with Ampersands.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The ampersand-formatted message
     */
    public static String getColored(@Nullable CommandSource p, String key, Object... vars) {
        return TextSerializers.FORMATTING_CODE.serialize(UltimateCore.get().getLanguageService().getMessage(p, key, vars));
    }

    /**
     * Get a string which is formatted with Ampersands.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The ampersand-formatted message
     */
    public static String getColored(String key, Object... vars) {
        return getColored(null, key, vars);
    }

    /**
     * Get the pure text without any formatting from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The plain-text message
     */
    public static String getPlain(@Nullable CommandSource p, String key, Object... vars) {
        return getFormatted(p, key, vars).toPlain();
    }

    /**
     * Get the pure text without any formatting from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The plain-text message
     */
    public static String getPlain(String key, Object... vars) {
        return getPlain(null, key, vars);
    }

    /**
     * Get a raw message from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The raw message, or null when not found
     */
    public static String get(@Nullable CommandSource p, String key, Object... vars) {
        return UltimateCore.get().getLanguageService().getRaw(p, key, vars);
    }

    /**
     * Get a raw message from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The raw message, or null when not found
     */
    public static String get(String key, Object... vars) {
        return get(null, key, vars);
    }

    /**
     * Print the message to the console, with the UC prefix in front of it.
     *
     * @param message The message to print
     */
    public static void log(Object message) {
        Text logo = Text.of("[UC] ");
        if (message == null) {
            log("null");
            return;
        }
        Sponge.getServer().getConsole().sendMessage(logo.toBuilder().append(Text.of(message)).build());
    }

    /**
     * Does exactly the same as .log(), but can be found using ctrl + f so I can remove them later
     */
    public static void debug(Object message) {
        log(message);
    }

    /**
     * Convert a String to a Text object
     */
    public static Text toText(String raw) {
        if (raw == null) return null;
        try {
            return TextSerializers.JSON.deserialize(raw);
        } catch (Exception ex) {
            try {
                return TextSerializers.FORMATTING_CODE.deserialize(raw);
            } catch (Exception ex3) {
                return Text.of(raw);
            }
        }
    }
}
