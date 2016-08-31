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
        //Split the text in a list of chars
        List<LiteralText> chars = new ArrayList<>();
        //Get all children
        List<Text> children = getAllChildren(text);
        //Get all chars
        for (Text child : children) {
            for (char c : getContent(child).toCharArray()) {
                LiteralText.Builder builder = LiteralText.builder(c).format(child.getFormat()).onClick(child.getClickAction().orElse(null)).onHover(child.getHoverAction().orElse(null))
                        .onShiftClick(child.getShiftClickAction().orElse(null));
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
            //This will make sure the replacement get formatted correctly (see merge method)
            Text charr = getChar(text, index);
            Text replacenew = merge(replace, charr);

            //Get the text before and after the found text, and put the replacement in
            Text front = subtext(text, 0, index);
            Text after = subtext(text, index + find.length(), text.toPlain().length());
            text = Text.of(front, replacenew, after);
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

        //This will make sure the replacement get formatted correctly (see merge method)
        Text charr = getChar(text, index);
        Text replacenew = merge(replace, charr);

        //Get the text before and after the found text, and put the replacement in
        Text front = subtext(text, 0, index);
        Text after = subtext(text, index + find.length(), text.toPlain().length());
        return Text.of(front, replacenew, after);
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
     * This method gets all children of a text, including the children of the children of the children, etc....
     *
     * @param parent The {@link Text} to get the children from
     * @return A list of all the children the text has
     */
    public static List<Text> getAllChildren(Text parent) {
        ArrayList<Text> list = new ArrayList<>();
        parent.withChildren().forEach(list::add);
        return list;
    }

    /**
     * Get the content of a Text.
     * This is the toPlain() without the text of any children.
     * <p>
     * //TODO Wait for: https://github.com/SpongePowered/SpongeAPI/issues/1347
     *
     * @param text The text to get the content of
     * @return The plain content of the text
     */
    public static String getContent(Text text) {
        return text.toBuilder().removeAll().build().toPlain();
    }

    /**
     * Get the char at the specified index.
     *
     * @param text  The text to get the char in
     * @param index The index the char is at
     * @return The char
     */
    public static Text getChar(Text text, int index) {
        return getFormattedChars(text).get(index);
    }

    /**
     * Merge two texts to one Text
     * This might not do what you expect it does, read below:
     * <p>
     * This will take the text of the first, plus any formatting and actions it has.
     * It will ignore any text the second argument has, and then add formatting if the first argument has none, and add all actions the first argument doesn't have.
     * Repeat until all texts have been merged.
     * </p>
     */
    public static Text merge(Text... rawtexts) {
        //Make a modifyable list of all texts
        List<Text> texts = new ArrayList<>(Arrays.<Text>asList(rawtexts));

        if (texts.isEmpty()) {
            throw new IllegalArgumentException("Can not have zero arguments for merge.");
        }

        Text.Builder start = texts.get(0).toBuilder();
        texts.remove(0);

        while (!texts.isEmpty()) {
            Text merge = texts.get(0);

            if (start.getFormat().isEmpty()) {
                start.format(merge.getFormat());
            }
            if (!start.getClickAction().isPresent()) {
                start.onClick(merge.getClickAction().orElse(null));
            }
            if (!start.getHoverAction().isPresent()) {
                start.onHover(merge.getHoverAction().orElse(null));
            }
            if (!start.getShiftClickAction().isPresent()) {
                start.onShiftClick(merge.getShiftClickAction().orElse(null));
            }

            texts.remove(0);
        }

        return start.build();
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
        List<Integer> found = indexesOf(text, split);
        if (found.isEmpty()) {
            return Arrays.asList(text);
        }
        //Actually split the text instance
        //0 - firstmatch
        //firstmatch+split - secondmatch
        //secondmatch+split - thirdmatch
        //...
        List<Text> results = new ArrayList<>();
        results.add(subtext(text, 0, found.get(0)));
        int cur = 0;
        for (Integer res : found) {
            Integer next = found.size() > (cur + 1) ? found.get(cur + 1) : null;
            results.add(subtext(text, res + split.length(), next == null ? text.toPlain().length() : next));
            cur++;
        }
        return results;
    }

}
