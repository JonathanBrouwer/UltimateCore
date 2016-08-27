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
package bammerbom.ultimatecore.sponge.utils;

import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TextUtil {
    /**
     * Get a list of Text objects, all containing a char from the text.
     * This is the same as {@link String#toCharArray()}, but keeps formatting.
     *
     * @param text The text to get the characters for
     * @return A list of formatted chars
     */
    public static List<LiteralText> getFormattedChars(Text text) {
        Messages.debug(Text.of("getformattedchars ", text));
        //Split the text in a list of chars
        List<LiteralText> chars = new ArrayList<>();
        //Get all children
        List<Text> texts = new ArrayList<>();
        if (text.getChildren().isEmpty()) {
            texts.add(text);
        } else {
            texts.addAll(text.getChildren());
        }
        //Get all chars
        for (Text tex : texts) {
            for (char c : tex.toPlain().toCharArray()) {
                LiteralText.Builder builder = LiteralText.builder(c).format(tex.getFormat()).onClick(tex.getClickAction().orElse(null)).onHover(tex.getHoverAction().orElse(null))
                        .onShiftClick(tex.getShiftClickAction().orElse(null));
                chars.add(builder.build());
            }
        }
        return chars;
    }

    /**
     * This will get a specific piece of text, starting at {@code start} and ending at {@code end - 1}.
     * This is the same as {@link String#substring(int, int)}, but keeps formatting.
     *
     * @param text  The text to get the subtext for
     * @param start The beginning index, inclusive
     * @param end   The ending index, exclusive
     * @return The subtext
     */
    public static LiteralText subtext(Text text, int start, int end) {
        Messages.debug(Text.of("subtext ", text, " " + start + " " + end));
        if (start == end) {
            return Text.of("");
        }
        List<LiteralText> chars = getFormattedChars(text);
        //Get the chars needed
        LiteralText.Builder sub = LiteralText.builder("");
        for (Text tex : Arrays.copyOfRange(chars.toArray(new Text[chars.size()]), start, end)) {
            sub.append(tex);
        }
        return sub.build();
    }

    /**
     * This will replace all literal matches of {@code find} with {@code replace}.
     * This is the same as {@link String#replace(CharSequence, CharSequence)}, but keeps formatting.
     * This does NOT support regex.
     *
     * @param text    The text to search & replace in.
     * @param find    The string to search for.
     * @param replace The text to replace the string with.
     * @return The text, where every match has been replaced.
     */
    public static Text replace(Text text, String find, Text replace) {
        int index = text.toPlain().indexOf(find);
        while (index != -1) {
            Text front = subtext(text, 0, index);
            Text after = subtext(text, index + find.length(), text.toPlain().length());
            text = Text.of(front, replace, after);
            index = text.toPlain().indexOf(find);
        }
        return text;
    }

    /**
     * This will replace the first literal match of {@code find} with {@code replace}.
     * This is the same as {@link String#replaceFirst(String, String)}, but keeps formatting.
     * This does NOT support regex.
     *
     * @param text    The text to search & replace in.
     * @param find    The string to search for.
     * @param replace The text to replace the string with.
     * @return The text, where the first match has been replaced.
     */
    public static Text replaceFirst(Text text, String find, Text replace) {
        int index = text.toPlain().indexOf(find);
        Text front = subtext(text, 0, index);
        Text after = subtext(text, index + find.length(), text.toPlain().length());
        return Text.of(front, replace, after);
    }

    /**
     * Get a list of all literal matches of {@code split}.
     * The integer is the index of the first character of the found match.
     * This does NOT support regex.
     *
     * @param text  The text to search in
     * @param split The string to search for
     * @return The list of found indexes, empty if none are found
     */
    public static List<Integer> indexesOf(Text text, String split) {
        Messages.debug(Text.of("indexesOf ", text, " " + split));
        String string = text.toPlain();
        HashSet<Integer> results = new HashSet<>();

        int i = 0;
        while (i < string.length()) {
            int indexof = string.indexOf(split, i);
            if (indexof >= 0) {
                results.add(indexof);
            }
            i++;
        }

        return new ArrayList<>(results);
    }

    /**
     * Split the text on {@code split}, and return all
     * This is the same as {@link String#split(String)}, but keeps formatting.
     * This does NOT support regex.
     *
     * @param text  The text to split.
     * @param split The string to split on.
     * @return All results of the split, or only the {@code text} if the {@code split} was not found.
     */
    public static List<Text> split(Text text, String split) {
        Messages.debug(Text.of("split ", text, " " + split));
        List<Integer> found = indexesOf(text, split);
        if (found.isEmpty()) {
            return Arrays.asList(text);
        }
        //Actually split the text instance
        //0 - firstmatch
        //firstmatch+split - secondmatch
        //secondmatch+split - thirdmatch
        //...
        Messages.debug(Text.of("split found: " + found));
        List<Text> results = new ArrayList<>();
        results.add(subtext(text, 0, found.get(0)));
        int cur = 0;
        for (Integer res : found) {
            Integer next = found.size() > (cur + 1) ? found.get(cur + 1) : null;
            Messages.debug("Split for " + (res + split.length()) + " - " + (next == null ? text.toPlain().length() : next));
            results.add(subtext(text, res + split.length(), next == null ? text.toPlain().length() : next));
            cur++;
        }
        return results;
    }

}
