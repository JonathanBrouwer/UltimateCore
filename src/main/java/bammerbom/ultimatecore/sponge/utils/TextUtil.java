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
    public static List<LiteralText> getFormattedChars(Text text) {
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

    public static Text replaceFirst(Text text, String find, Text replace) {
        int index = text.toPlain().indexOf(find);
        Text front = subtext(text, 0, index);
        Text after = subtext(text, index + find.length(), text.toPlain().length());
        return Text.of(front, replace, after);
    }

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
