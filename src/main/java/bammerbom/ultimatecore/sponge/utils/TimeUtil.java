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

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    public static long parseDateDiff(String time) {
        if (time.equalsIgnoreCase("-1")) {
            return -1;
        }
        try {
            Long mil = -1L;
            Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);
            while (matcher.find()) {
                String s = matcher.group();
                Long numb = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
                String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
                switch (type) {
                    case "s":
                    case "S":
                        mil = mil + (numb * 1000);
                        break;
                    case "m":
                        mil = mil + (numb * 1000 * 60);
                        break;
                    case "h":
                    case "H":
                        mil = mil + (numb * 1000 * 60 * 60);
                        break;
                    case "d":
                    case "D":
                        mil = mil + (numb * 1000 * 60 * 60 * 24);
                        break;
                    case "w":
                    case "W":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 7);
                        break;
                    case "M":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 30);
                        break;
                    case "y":
                    case "Y":
                        mil = mil + (numb * 1000 * 60 * 60 * 24 * 365);
                        break;
                    default:
                        break;
                }
            }
            if (isInt(time)) {
                mil = mil + (Long.parseLong(time) * 1000 * 60 * 60);
            }
            return mil;
        } catch (Exception ex) {
            return -1L;
        }
    }

    static boolean isInt(String time) {
        try {
            Integer.parseInt(time);
            return true;
        } catch (Exception ex) {
            return false;
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
        return formatDateDiff(date, 3, null);
    }

    public static String formatDateDiff(long date, int maxacc, Integer disable) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c, maxacc, disable);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        return formatDateDiff(fromDate, toDate, 3, null);
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate, int maxacc, @Nullable Integer disable) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return Messages.getColored("core.time.now");
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = {1, 2, 5, 11, 12, 13};

        String[] names = {Messages.getColored("core.time.years"), Messages.getColored("core.time.year"), Messages.getColored("core.time.months"), Messages.getColored("core.time.month"),
                Messages.getColored("core.time.days"), Messages.getColored("core.time.day"), Messages.getColored("core.time.hours"), Messages.getColored("core.time.hour"), Messages
                .getColored("core.time.minutes"), Messages.getColored("core.time.minute"), Messages.getColored("core.time.seconds"), Messages.getColored("core.time.second")};

        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy >= maxacc) {
                break;
            }
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                int name = ((i * 2) + (diff == 1 ? 1 : 0));
                if (disable != null && name >= disable) {
                    continue;
                }
                sb.append(" ").append(diff).append(" ").append(names[name]);
            }
        }
        if (sb.length() == 0) {
            return Messages.getColored("core.time.now");
        }
        return sb.toString().trim();
    }

    public static String format(long date) {
        return format(date, 3, null);
    }

    public static String format(long date, int maxacc, Integer disable) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date + System.currentTimeMillis());
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c, maxacc, disable);
    }

}