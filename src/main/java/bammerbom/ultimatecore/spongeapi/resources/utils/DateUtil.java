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

import bammerbom.ultimatecore.spongeapi.r;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    public static long parseDateDiff(String time) {
        try {
            Long mil = 999L;
            Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);
            while (matcher.find()) {
                String s = matcher.group();
                Long numb = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
                String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
                switch (type) {
                    case "s":
                        mil = mil + (numb * 1000);
                        break;
                    case "m":
                        mil = mil + (numb * 1000 * 60);
                        break;
                    case "h":
                        mil = mil + (numb * 1000 * 60 * 60);
                        break;
                    case "d":
                        mil = mil + (numb * 1000 * 60 * 60 * 24);
                        break;
                    case "w":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 7);
                        break;
                    case "M":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 30);
                        break;
                    case "y":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 365);
                        break;
                }
            }
            if (mil == 999) {
                return -1L;
            }
            return mil;
        } catch (Exception ex) {
            return -1L;
        }
    }

    static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while (((future) && (!fromDate.after(toDate))) || ((!future) && (!fromDate.before(toDate)))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return r.mes("now");
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = {1, 2, 5, 11, 12, 13};

        String[] names = {r.mes("years"), r.mes("year"), r.mes("months"), r.mes("month"), r.mes("days"), r.mes("day"), r.mes("hours"), r.mes("hour"), r.mes("minutes"), r.mes("minute"), r.mes("seconds"), r.mes("second")};

        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[((i * 2) + (diff == 1 ? 1 : 0))]);
            }
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString().trim();
    }

    public static String format(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date + System.currentTimeMillis());
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

}
