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

import bammerbom.ultimatecore.spongeapi.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookUtil {

    public static boolean bookExists(String title) {
        title = title.toLowerCase();
        File booksfile = UltimateFileLoader.Dbooks;
        List<String> alllines = FileUtil.getLines(booksfile);
        return alllines.contains("#" + title);
    }

    public static List<String> readBook(String title) {
        title = title.toLowerCase();
        File booksfile = UltimateFileLoader.Dbooks;
        List<String> alllines = FileUtil.getLines(booksfile);
        if (!alllines.contains("#" + title)) {
            return null;
        }
        List<String> booklines = new ArrayList<>();
        boolean inBook = false;
        for (String s : alllines) {
            if (inBook && s.startsWith("#")) {
                break;
            }
            if (s.equalsIgnoreCase("#" + title)) {
                inBook = true;
                continue;
            }
            if (inBook) {
                booklines.add(s.replace('&', '§').replace("§§", "&"));
            }
        }
        List<String> bookchapters = new ArrayList<>();
        StringBuilder chapter = new StringBuilder();
        for (String s : booklines) {
            if (s.equalsIgnoreCase("[NEXTPAGE]")) {
                bookchapters.add(chapter.toString());
                chapter = new StringBuilder();
            } else {
                if (chapter.length() != 0) {
                    chapter.append("\n");
                }
                chapter.append(s);
            }
        }
        bookchapters.add(chapter.toString());
        return bookchapters;
    }

    public static void writeBook(String title, List<String> bookchapters) {
        title = title.toLowerCase();
        File booksfile = UltimateFileLoader.Dbooks;
        List<String> alllines = FileUtil.getLines(booksfile);
        if (bookExists(title)) {
            List<String> booklines = new ArrayList<>();
            boolean inBook = false;
            for (String s : alllines) {
                if (inBook && s.startsWith("#")) {
                    break;
                }
                if (s.equalsIgnoreCase("#" + title)) {
                    inBook = true;
                    continue;
                }
                if (inBook) {
                    booklines.add(s);
                }
            }
            alllines.removeAll(booklines);
        }
        alllines.add("#" + title);
        boolean first = true;
        for (String chapter : bookchapters) {
            if (!first) {
                alllines.add("[NEXTPAGE]");
            }
            first = false;

            if (chapter.contains("\n")) {
                for (String line : chapter.split("\n")) {
                    alllines.add(line);
                }
            } else {
                alllines.add(chapter);
            }
        }
        try {
            FileUtil.writeFile(booksfile, alllines);
        } catch (IOException e) {
            ErrorLogger.log(e, "Failed to write books file.");
        }
    }
}