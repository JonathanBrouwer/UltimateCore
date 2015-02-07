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
package bammerbom.ultimatecore.bukkit.resources.utils;

import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public class FormatUtil {

    public static final Pattern IPPATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    static final transient Pattern VANILLA_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "+[0-9A-FK-ORa-fk-or]?");
    static final transient Pattern VANILLA_COLOR_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "+[0-9A-Fa-f]");
    static final transient Pattern VANILLA_MAGIC_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "+[Kk]");
    static final transient Pattern VANILLA_FORMAT_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "+[L-ORl-or]");
    static final transient Pattern REPLACE_ALL_PATTERN = Pattern.compile("(?<!&)&([0-9a-fk-orA-FK-OR])");
    static final transient Pattern REPLACE_COLOR_PATTERN = Pattern.compile("(?<!&)&([0-9a-fA-F])");
    static final transient Pattern REPLACE_MAGIC_PATTERN = Pattern.compile("(?<!&)&([Kk])");
    static final transient Pattern REPLACE_FORMAT_PATTERN = Pattern.compile("(?<!&)&([l-orL-OR])");
    static final transient Pattern REPLACE_PATTERN = Pattern.compile("&&(?=[0-9a-fk-orA-FK-OR])");
    static final transient Pattern LOGCOLOR_PATTERN = Pattern.compile("\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]");
    static final transient Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_\\.]{2,})\\.([a-zA-Z]{2,3}(?:/\\S+)?)");

    public static String stripFormat(String input) {
        if (input == null) {
            return null;
        }
        return stripColor(input, VANILLA_PATTERN);
    }

    public static String stripEssentialsFormat(String input) {
        if (input == null) {
            return null;
        }
        return stripColor(input, REPLACE_ALL_PATTERN);
    }

    public static String replaceFormat(String input) {
        if (input == null) {
            return null;
        }
        return replaceColor(input, REPLACE_ALL_PATTERN);
    }

    static String replaceColor(String input, Pattern pattern) {
        return REPLACE_PATTERN.matcher(pattern.matcher(input).replaceAll(ChatColor.COLOR_CHAR + "")).replaceAll("&");
    }

    public static String stripLogColorFormat(String input) {
        if (input == null) {
            return null;
        }
        return stripColor(input, LOGCOLOR_PATTERN);
    }

    static String stripColor(String input, Pattern pattern) {
        return pattern.matcher(input).replaceAll("");
    }

    public static String lastCode(String input) {
        int pos = input.lastIndexOf(ChatColor.COLOR_CHAR);
        if ((pos == -1) || (pos + 1 == input.length())) {
            return "";
        }
        return input.substring(pos, pos + 2);
    }

    static String blockURL(String input) {
        if (input == null) {
            return null;
        }
        String text = URL_PATTERN.matcher(input).replaceAll("$1 $2");
        while (URL_PATTERN.matcher(text).find()) {
            text = URL_PATTERN.matcher(text).replaceAll("$1 $2");
        }
        return text;
    }

    public static boolean validIP(String ipAddress) {
        return IPPATTERN.matcher(ipAddress).matches();
    }
}
