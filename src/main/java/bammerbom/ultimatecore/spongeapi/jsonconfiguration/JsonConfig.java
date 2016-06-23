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
package bammerbom.ultimatecore.spongeapi.jsonconfiguration;

import bammerbom.ultimatecore.spongeapi.r;
import bammerbom.ultimatecore.spongeapi.resources.classes.ErrorLogger;
import bammerbom.ultimatecore.spongeapi.resources.utils.FileUtil;
import bammerbom.ultimatecore.spongeapi.resources.utils.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class JsonConfig implements Cloneable {

    private final File file;
    private final Map<String, Object> map;

    public JsonConfig(File file2) {
        file = file2;
        List<String> list = FileUtil.getLines(file2);
        String list2 = StringUtil.joinList(list);
        Object ob = JSONValue.parse(list2);
        if (ob instanceof JSONObject) {
            map = (JSONObject) ob;
        } else {
            if (ob != null) {
                r.log("Warning: Config was invalid json format. (List)");
                r.log(file2.getName() + " - " + ob);
            }
            map = new HashMap<>();
        }
    }

    public void set(String s, Object o) {
        if (o == null) {
            ArrayList<String> remv = new ArrayList<>();
            remv.add(s);
            for (String key : map.keySet()) {
                if (key.startsWith(s)) {
                    remv.add(key);
                }
            }
            for (String st : remv) {
                map.remove(st);
            }
        } else {
            map.put(s, o);
        }
    }

    public Object get(String s) {
        return map.get(s);
    }

    public Object get(String s, Object def) {
        if (!map.containsKey(s)) {
            return def;
        }
        return map.get(s);
    }

    public boolean contains(String s) {
        for (String st : map.keySet()) {
            if (st.equals(s) || st.startsWith(s + ".")) {
                return true;
            }
        }
        return false;
    }

    public String getString(String s) {
        return getString(s, "");
    }

    public String getString(String s, String def) {
        if (!contains(s)) {
            return def;
        }
        return (String) get(s);
    }

    public Boolean getBoolean(String s) {
        return getBoolean(s, false);
    }

    public Boolean getBoolean(String s, Boolean def) {
        if (!contains(s)) {
            return def;
        }
        return (Boolean) get(s);
    }

    public Long getLong(String s) {
        return getLong(s, 0L);
    }

    public Long getLong(String s, Long def) {
        if (!contains(s)) {
            return def;
        }
        return (Long) get(s);
    }

    public Double getDouble(String s) {
        return getDouble(s, 0.0D);
    }

    public Double getDouble(String s, Double def) {
        if (!contains(s)) {
            return def;
        }
        return (Double) get(s);
    }

    public BigDecimal getBigDecimal(String path) {
        return getBigDecimal(path, new BigDecimal(0));
    }

    public BigDecimal getBigDecimal(String path, BigDecimal def) {
        Object val = get(path, def);
        return (BigDecimal) ((val instanceof BigDecimal) ? val : def);
    }

    public Integer getInteger(String s) {
        return getInteger(s, 0);
    }

    public Integer getInteger(String s, Integer def) {
        if (!contains(s)) {
            return def;
        }
        return (Integer) get(s);
    }

    public Short getShort(String s) {
        return getShort(s, Short.parseShort("0"));
    }

    public Short getShort(String s, Short def) {
        if (!contains(s)) {
            return def;
        }
        return (Short) get(s);
    }

    public JSONArray getList(String s) {
        if (!contains(s)) {
            return new JSONArray();
        }
        return (JSONArray) get(s);
    }

    public List<String> getStringList(String s) {
        if (!contains(s)) {
            return new ArrayList<>();
        }
        return (List<String>) get(s);
    }

    public void save() {
        save(file);
    }

    public void save(File fi) {
        try {
            FileUtil.writeFile(fi, Arrays.asList(JSONValue.toJSONString(map).split("\n", -1)));
        } catch (Exception ex) {
            ErrorLogger.log(ex, "Failed to write file.");
        }
    }

    public List<String> listKeys(String s, Boolean deep) {
        String s2 = s.endsWith(".") ? s : s + ".";
        if (deep) {
            ArrayList<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                if (k.startsWith(s2)) {
                    rtrn.add(k.replaceFirst(s2, ""));
                }
            }
            return rtrn;
        } else {
            List<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                if (k.startsWith(s2)) {
                    String a = (k.replaceFirst(s2, "").contains(".") ? k.replaceFirst(s2, "").split("\\.")[0] : k.replaceFirst(s2, ""));
                    rtrn.add(a);
                }
            }
            return rtrn;
        }
    }

    public List<String> listKeys(Boolean deep) {
        if (deep) {
            return new ArrayList<>(map.keySet());
        } else {
            List<String> rtrn = new ArrayList<>();
            for (String k : map.keySet()) {
                String s = k.contains(".") ? k.split("\\.")[0] : k;
                if (!rtrn.contains(s)) {
                    rtrn.add(s);
                }
            }
            return rtrn;
        }
    }

}
