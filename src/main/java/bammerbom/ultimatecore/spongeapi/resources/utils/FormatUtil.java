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

import java.util.regex.Pattern;

public class FormatUtil {

    public static final Pattern IPPATTERN = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    static final transient Pattern URL_PATTERN = Pattern.compile("((?:(?:https?)://)?[\\w-_\\.]{2,})\\.([a-zA-Z]{2,3}" + "(?:/\\S+)?)");

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
