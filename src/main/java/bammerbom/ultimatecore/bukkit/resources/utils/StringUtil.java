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

import java.util.*;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

public class StringUtil {

    public static final char CHAT_STYLE_CHAR = '�';
    public static final int SPACE_WIDTH = getWidth(' ');
    public static final String[] EMPTY_ARRAY = new String[0];
    private static final Pattern INVALIDFILECHARS = Pattern.compile("[^a-z0-9-]");
    private static final char[] CHAT_CODES;
    static {
        ChatColor[] styles = ChatColor.values();
        LinkedHashSet<Character> chars = new LinkedHashSet<>(styles.length * 2);
        for (int i = 0; i < styles.length; i++) {
            chars.add(Character.valueOf(Character.toLowerCase(styles[i].getChar())));
            chars.add(Character.valueOf(Character.toUpperCase(styles[i].getChar())));
        }
        CHAT_CODES = new char[chars.size()];
        int i = 0;
        for (Character c : chars) {
            CHAT_CODES[i] = c.charValue();
            i++;
        }
    }

    public static String joinList(Object[] list) {
        return joinList(", ", list);
    }

    public static String consolidateStrings(String[] args, int start) {
        String ret = args[start];
        if (args.length > start + 1) {
            for (int i = start + 1; i < args.length; i++) {
                ret = ret + " " + args[i];
            }
        }
        return ret;
    }

    public static boolean containsIgnoreCase(List<? extends String> l, String s) {
        for (String c : l) {
            if (c.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static String sanitizeFileName(String name) {
        return INVALIDFILECHARS.matcher(name.toLowerCase(Locale.ENGLISH)).replaceAll("_");
    }

    @SuppressWarnings("rawtypes")
    public static String joinList(String seperator, Object[] list) {
        StringBuilder buf = new StringBuilder();
        for (Object each : list) {
            if (buf.length() > 0) {
                buf.append(seperator);
            }

            if ((each instanceof Collection)) {
                buf.append(joinList(seperator, ((Collection) each).toArray()));
            } else {
                try {
                    buf.append(each.toString());
                } catch (Exception e) {
                }
            }
        }
        return buf.toString();
    }

    public static String joinList(@SuppressWarnings("rawtypes") Collection c) {
        return joinList(c.toArray());
    }

    public static String firstUpperCase(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "[a-zA-Z0-9]";
        return s.matches(pattern);
    }

    public static String blockToString(Block block) {
        return block.getWorld().getName() + "_" + block.getX() + "_" + block.getY() + "_" + block.getZ();
    }

    public static Block stringToBlock(String str) {
        try {
            String[] s = str.split("_");

            if (s.length < 4) {
                return null;
            }

            int x = Integer.parseInt(s[(s.length - 3)]);
            int y = Integer.parseInt(s[(s.length - 2)]);
            int z = Integer.parseInt(s[(s.length - 1)]);

            StringBuilder worldName = new StringBuilder(12);
            for (int i = 0; i < s.length - 3; i++) {
                if (i != 0) {
                    worldName.append('_');
                }
                worldName.append(s[i]);
            }

            World world = Bukkit.getServer().getWorld(worldName.toString());
            if (world == null) {
                return null;
            }
            return world.getBlockAt(x, y, z);
        } catch (Exception e) {
        }
        return null;
    }

    public static int getWidth(String[] text) {
        int width = 0;
        for (String part : text) {
            for (int i = 0; i < part.length(); i++) {
                char character = part.charAt(i);
                if (character != '\n') {
                    if (character == '�') {
                        i++;
                    } else if (character == ' ') {
                        width += SPACE_WIDTH;
                    } else {
                        MapFont.CharacterSprite charsprite = MinecraftFont.Font.getChar(character);
                        if (charsprite != null) {
                            width += charsprite.getWidth();
                        }
                    }
                }
            }
        }
        return width;
    }

    public static int getWidth(char character) {
        return MinecraftFont.Font.getChar(character).getWidth();
    }

    public static int firstIndexOf(String text, char[] values) {
        for (int i = 0; i < text.length(); i++) {
            if (containsChar(text.charAt(i), values)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean containsChar(char value, CharSequence sequence) {
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsChar(char value, char[] values) {
        for (char v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

    public static int firstIndexOf(String text, String[] values) {
        return firstIndexOf(text, 0, values);
    }

    public static int firstIndexOf(String text, int startindex, String[] values) {
        int i = -1;

        for (String value : values) {
            int index;
            if (((index = text.indexOf(value, startindex)) != -1) && ((i == -1) || (index < i))) {
                i = index;
            }
        }
        return i;
    }

    public static String getFilledString(String text, int n) {
        StringBuffer outputBuffer = new StringBuffer(text.length() * n);
        for (int i = 0; i < n; i++) {
            outputBuffer.append(text);
        }
        return outputBuffer.toString();
    }

    public static String getBefore(String text, String delimiter) {
        int index = text.indexOf(delimiter);
        return index >= 0 ? text.substring(0, index) : "";
    }

    public static String getAfter(String text, String delimiter) {
        int index = text.indexOf(delimiter);
        return index >= 0 ? text.substring(index + delimiter.length()) : "";
    }

    public static String getLastBefore(String text, String delimiter) {
        int index = text.lastIndexOf(delimiter);
        return index >= 0 ? text.substring(0, index) : "";
    }

    public static String getLastAfter(String text, String delimiter) {
        int index = text.lastIndexOf(delimiter);
        return index >= 0 ? text.substring(index + delimiter.length()) : "";
    }

    public static String replace(String text, int startIndex, int endIndex, String replacement) {
        StringBuilder builder = new StringBuilder(text);
        builder.replace(startIndex, endIndex, replacement);
        return builder.toString();
    }

    public static String trimEnd(String text, String[] textToTrim) {
        for (String trim : textToTrim) {
            if (text.endsWith(trim)) {
                return text.substring(0, text.length() - trim.length());
            }
        }
        return text;
    }

    public static String trimStart(String text, String[] textToTrim) {
        for (String trim : textToTrim) {
            if (text.startsWith(trim)) {
                return text.substring(trim.length());
            }
        }
        return text;
    }

    public static String trimStart(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') {
                return text.substring(i);
            }
        }
        return "";
    }

    public static String trimEnd(String text) {
        for (int i = text.length() - 1; i >= 0; i--) {
            if (text.charAt(i) != ' ') {
                return text.substring(0, i + 1);
            }
        }
        return "";
    }

    public static String[] remove(String[] input, int index) {
        if ((index < 0) || (index >= input.length)) {
            return input;
        }
        String[] rval = new String[input.length - 1];
        System.arraycopy(input, 0, rval, 0, index);
        System.arraycopy(input, index + 1, rval, index, input.length - index - 1);
        return rval;
    }

    public static String combineNames(Collection<Object> items) {
        if ((items == null) || (items.isEmpty())) {
            return "";
        }

        if (items.size() == 1) {
            Object item = items.iterator().next();
            return item == null ? "" : item.toString();
        }

        StringBuilder rval = new StringBuilder();
        int i = 0;
        for (Iterator<Object> i$ = items.iterator(); i$.hasNext();) {
            Object item = i$.next();
            if (i == items.size() - 1) {
                rval.append(" and ");
            } else if (i > 0) {
                rval.append(", ");
            }
            if (item != null) {
                rval.append(item);
            }
            i++;
        }
        return rval.toString();
    }

    public static String combineNames(String[] items) {
        return combineNames(new HashSet<Object>(Arrays.asList(items)));
    }

    @Deprecated
    public static String combine(String separator, String[] parts) {
        return join(separator, parts);
    }

    @Deprecated
    public static String combine(String separator, Collection<String> parts) {
        return join(separator, parts);
    }

    public static String join(String separator, String[] parts) {
        return join(separator, Arrays.asList(parts));
    }

    public static String join(String separator, Collection<String> parts) {
        StringBuilder builder = new StringBuilder(parts.size() * 16);
        boolean first = true;
        for (String line : parts) {
            if (!first) {
                builder.append(separator);
            }
            if (line != null) {
                builder.append(line);
            }
            first = false;
        }
        return builder.toString();
    }

    public static String[] convertArgs(String[] args) {
        ArrayList<Object> tmpargs = new ArrayList<>(args.length);
        boolean isCommenting = false;
        for (String arg : args) {
            if ((!isCommenting) && ((arg.startsWith("\"")) || (arg.startsWith("'")))) {
                if ((arg.endsWith("\"")) && (arg.length() > 1)) {
                    tmpargs.add(arg.substring(1, arg.length() - 1));
                } else {
                    isCommenting = true;
                    tmpargs.add(arg.substring(1));
                }
            } else if ((isCommenting) && ((arg.endsWith("\"")) || (arg.endsWith("'")))) {
                arg = arg.substring(0, arg.length() - 1);
                arg = tmpargs.get(tmpargs.size() - 1) + " " + arg;
                tmpargs.set(tmpargs.size() - 1, arg);
                isCommenting = false;
            } else if (isCommenting) {
                arg = tmpargs.get(tmpargs.size() - 1) + " " + arg;
                tmpargs.set(tmpargs.size() - 1, arg);
            } else {
                tmpargs.add(arg);
            }
        }
        return tmpargs.toArray(new String[0]);
    }

    public static boolean isChatCode(char character) {
        return containsChar(character, CHAT_CODES);
    }

    public static int getSuccessiveCharCount(String value, char character) {
        return getSuccessiveCharCount(value, character, 0, value.length() - 1);
    }

    public static int getSuccessiveCharCount(String value, char character, int startindex) {
        return getSuccessiveCharCount(value, character, startindex, value.length() - startindex - 1);
    }

    public static int getSuccessiveCharCount(String value, char character, int startindex, int endindex) {
        int count = 0;
        for (int i = startindex; (i <= endindex)
                && (value.charAt(i) == character); i++) {
            count++;
        }

        return count;
    }

    public static void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);

            index += to.length();
            index = builder.indexOf(from, index);
        }
    }

    public static ChatColor getColor(char code, ChatColor def) {
        for (ChatColor color : ChatColor.values()) {
            if (code == color.toString().charAt(1)) {
                return color;
            }
        }
        return def;
    }

    public static String ampToColor(String line) {
        return swapColorCodes(line, '&', '�');
    }

    public static String colorToAmp(String line) {
        return swapColorCodes(line, '�', '&');
    }

    public static String swapColorCodes(String line, char fromCode, char toCode) {
        StringBuilder builder = new StringBuilder(line);
        for (int i = 0; i < builder.length() - 1; i++) {
            if ((builder.charAt(i) == fromCode) && (isChatCode(builder.charAt(i + 1)))) {
                builder.setCharAt(i, toCode);
                i++;
            }
        }
        return builder.toString();
    }


    public static boolean nullOrEmpty(Map<?, ?> map) {
        return (map == null) || (map.isEmpty());
    }

    public static boolean nullOrEmpty(Collection<?> collection) {
        return (collection == null) || (collection.isEmpty());
    }

    public static boolean nullOrEmpty(String text) {
        return (text == null) || (text.isEmpty());
    }

    public static boolean nullOrEmpty(Object[] array) {
        return (array == null) || (array.length == 0);
    }

}
