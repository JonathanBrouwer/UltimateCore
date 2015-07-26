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
package bammerbom.ultimatecore.spongeapi.configuration;

import org.apache.commons.lang.Validate;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.item.inventory.ItemStack;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.bukkit.util.NumberConversions.*;

/**
 * A type of {@link ConfigSection} that is stored in memory.
 */
public class ConfigSection {

    protected final Map<String, Object> map = new LinkedHashMap<>();
    private final MemoryConfiguration root;
    private final ConfigSection parent;
    private final String path;
    private final String fullPath;

    /**
     * Creates an empty MemorySection for use as a root {@link bammerbom.ultimatecore.spongeapi.configuration.MemoryConfiguration} section.
     * <p/>
     * Note that calling this without being yourself a {@link bammerbom.ultimatecore.spongeapi.configuration.MemoryConfiguration} will throw an
     * exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link
     *                               bammerbom.ultimatecore.spongeapi.configuration.MemoryConfiguration} root.
     */
    protected ConfigSection() {
        if (!(this instanceof MemoryConfiguration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a MemoryConfiguration");
        }

        this.path = "";
        this.fullPath = "";
        this.parent = null;
        this.root = (MemoryConfiguration) this;
    }

    /**
     * Creates an empty MemorySection with the specified parent and path.
     *
     * @param parent Parent section that contains this own section.
     * @param path   Path that you may access this section from via the root
     *               {@link bammerbom.ultimatecore.spongeapi.configuration.MemoryConfiguration}.
     * @throws IllegalArgumentException Thrown is parent or path is null, or if parent contains no
     *                                  root MemoryConfiguration.
     */
    protected ConfigSection(ConfigSection parent, String path) {
        Validate.notNull(parent, "Parent cannot be null");
        Validate.notNull(path, "Path cannot be null");

        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();

        Validate.notNull(root, "Path cannot be orphaned");

        this.fullPath = createPath(parent, path);
    }

    /**
     * Creates a full path to the given {@link ConfigSection} from its root
     * {@link bammerbom.ultimatecore.spongeapi.configuration.MemoryConfiguration}.
     * <p/>
     * You may use this method for any given {@link ConfigSection}, not only {@link bammerbom.ultimatecore.spongeapi.configuration
     * .ConfigSection}.
     *
     * @param section Section to create a path for.
     * @param key     Name of the specified section.
     * @return Full path of the section from its root.
     */
    protected static String createPath(ConfigSection section, String key) {
        return createPath(section, key, (section == null) ? null : section.getRoot());
    }

    /**
     * Creates a relative path to the given {@link ConfigSection} from the given relative section.
     * <p/>
     * You may use this method for any given {@link ConfigSection}, not only {@link bammerbom.ultimatecore.spongeapi.configuration
     * .ConfigSection}.
     *
     * @param section    Section to create a path for.
     * @param key        Name of the specified section.
     * @param relativeTo Section to create the path relative to.
     * @return Full path of the section from its root.
     */
    protected static String createPath(ConfigSection section, String key, ConfigSection relativeTo) {
        Validate.notNull(section, "Cannot create path without a section");
        MemoryConfiguration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        }
        char separator = root.options().pathSeparator();

        StringBuilder builder = new StringBuilder();
        for (ConfigSection parent = section;
             (parent != null) && (parent != relativeTo);
             parent = parent.getParent()) {
            if (builder.length() > 0) {
                builder.insert(0, separator);
            }

            builder.insert(0, parent.getName());
        }

        if ((key != null) && (key.length() > 0)) {
            if (builder.length() > 0) {
                builder.append(separator);
            }

            builder.append(key);
        }

        return builder.toString();
    }

    public Set<ConfigSection> getConfigurationSections(Boolean deep) {
        Set<ConfigSection> result = new LinkedHashSet<>();
        for (String r : getKeys(deep)) {
            if (this.isConfigurationSection(r)) {
                result.add(this.getConfigurationSection(r));
            }
        }
        return result;
    }

    public Set<String> getKeys(boolean deep) {
        Set<String> result = new LinkedHashSet<>();

        MemoryConfiguration root = getRoot();
        if (root != null && root.options().copyDefaults()) {
            ConfigSection defaults = getDefaultSection();

            if (defaults != null) {
                result.addAll(defaults.getKeys(deep));
            }
        }

        mapChildrenKeys(result, this, deep);

        return result;
    }

    public Map<String, Object> getValues(boolean deep) {
        Map<String, Object> result = new LinkedHashMap<>();

        MemoryConfiguration root = getRoot();
        if (root != null && root.options().copyDefaults()) {
            ConfigSection defaults = getDefaultSection();

            if (defaults != null) {
                result.putAll(defaults.getValues(deep));
            }
        }

        mapChildrenValues(result, this, deep);

        return result;
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

    public boolean isSet(String path) {
        MemoryConfiguration root = getRoot();
        if (root == null) {
            return false;
        }
        if (root.options().copyDefaults()) {
            return contains(path);
        }
        return get(path, null) != null;
    }

    public String getCurrentPath() {
        return fullPath;
    }

    public String getName() {
        return path;
    }

    public MemoryConfiguration getRoot() {
        return root;
    }

    public ConfigSection getParent() {
        return parent;
    }

    public void addDefault(String path, Object value) {
        Validate.notNull(path, "Path cannot be null");

        MemoryConfiguration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot add default without root");
        }
        if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        }
        root.addDefault(createPath(this, path), value);
    }

    public ConfigSection getDefaultSection() {
        MemoryConfiguration root = getRoot();
        MemoryConfiguration defaults = root == null ? null : root.getDefaults();

        if (defaults != null) {
            if (defaults.isConfigurationSection(getCurrentPath())) {
                return defaults.getConfigurationSection(getCurrentPath());
            }
        }

        return null;
    }

    public void set(String path, Object value) {
        Validate.notEmpty(path, "Cannot set to an empty path");

        MemoryConfiguration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            if (value == null) {
                map.remove(key);
            } else {
                map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    public Object get(String path) {
        return get(path, getDefault(path));
    }

    public Object get(String path, Object def) {
        Validate.notNull(path, "Path cannot be null");

        if (path.length() == 0) {
            return this;
        }

        MemoryConfiguration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(i2, i1));
            if (section == null) {
                return def;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            Object result = map.get(key);
            return (result == null) ? def : result;
        }
        return section.get(key, def);
    }

    public ConfigSection createSection(String path) {
        Validate.notEmpty(path, "Cannot create section at empty path");
        MemoryConfiguration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            ConfigSection result = new ConfigSection(this, key);
            map.put(key, result);
            return result;
        }
        return section.createSection(key);
    }

    public ConfigSection createSection(String path, Map<?, ?> map) {
        ConfigSection section = createSection(path);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
            } else {
                section.set(entry.getKey().toString(), entry.getValue());
            }
        }

        return section;
    }

    // Primitives
    public String getString(String path) {
        Object def = getDefault(path);
        return getString(path, def != null ? def.toString() : null);
    }

    public String getString(String path, String def) {
        Object val = get(path, def);
        return (val != null) ? val.toString() : def;
    }

    public boolean isString(String path) {
        Object val = get(path);
        return val instanceof String;
    }

    public int getInt(String path) {
        Object def = getDefault(path);
        return getInt(path, (def instanceof Number) ? toInt(def) : 0);
    }

    public int getInt(String path, int def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toInt(val) : def;
    }

    public boolean isInt(String path) {
        Object val = get(path);
        return val instanceof Integer;
    }

    public boolean getBoolean(String path) {
        Object def = getDefault(path);
        return getBoolean(path, (def instanceof Boolean) ? (Boolean) def : false);
    }

    public boolean getBoolean(String path, boolean def) {
        Object val = get(path, def);
        return (val instanceof Boolean) ? (Boolean) val : def;
    }

    public boolean isBoolean(String path) {
        Object val = get(path);
        return val instanceof Boolean;
    }

    public double getDouble(String path) {
        Object def = getDefault(path);
        return getDouble(path, (def instanceof Number) ? toDouble(def) : 0);
    }

    public double getDouble(String path, double def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toDouble(val) : def;
    }

    public boolean isDouble(String path) {
        Object val = get(path);
        return val instanceof Double;
    }

    public long getLong(String path) {
        Object def = getDefault(path);
        return getLong(path, (def instanceof Number) ? toLong(def) : 0);
    }

    public long getLong(String path, long def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toLong(val) : def;
    }

    public short getShort(String path) {
        Object def = getDefault(path);
        return getShort(path, (def instanceof Short) ? toShort(def) : (short) 0);
    }

    public short getShort(String path, short def) {
        Object val = get(path, def);
        return (val instanceof Short) ? toShort(val) : def;
    }

    public boolean isLong(String path) {
        Object val = get(path);
        return val instanceof Long;
    }

    // Java
    public List<?> getList(String path) {
        Object def = getDefault(path);
        return getList(path, (def instanceof List) ? (List<?>) def : null);
    }

    public List<?> getList(String path, List<?> def) {
        Object val = get(path, def);
        return (List<?>) ((val instanceof List) ? val : def);
    }

    public boolean isList(String path) {
        Object val = get(path);
        return val instanceof List;
    }

    public List<String> getStringList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<String> result = new ArrayList<>();

        for (Object object : list) {
            if ((object instanceof String) || (isPrimitiveWrapper(object))) {
                result.add(String.valueOf(object));
            }
        }

        return result;
    }

    public List<Integer> getIntegerList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Integer> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Integer) {
                result.add((Integer) object);
            } else if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((int) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    public List<Boolean> getBooleanList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Boolean> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            } else if (object instanceof String) {
                if (Boolean.TRUE.toString().equals(object)) {
                    result.add(true);
                } else if (Boolean.FALSE.toString().equals(object)) {
                    result.add(false);
                }
            }
        }

        return result;
    }

    public List<Double> getDoubleList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Double> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Double) {
                result.add((Double) object);
            } else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((double) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    public List<Float> getFloatList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Float> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Float) {
                result.add((Float) object);
            } else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((float) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    public List<Long> getLongList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Long> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Long) {
                result.add((Long) object);
            } else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((long) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    public List<Byte> getByteList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Byte> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Byte) {
                result.add((Byte) object);
            } else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((byte) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    public List<Character> getCharacterList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Character> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Character) {
                result.add((Character) object);
            } else if (object instanceof String) {
                String str = (String) object;

                if (str.length() == 1) {
                    result.add(str.charAt(0));
                }
            } else if (object instanceof Number) {
                result.add((char) ((Number) object).intValue());
            }
        }

        return result;
    }

    public List<Short> getShortList(String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        List<Short> result = new ArrayList<>();

        for (Object object : list) {
            if (object instanceof Short) {
                result.add((Short) object);
            } else if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((short) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    public List<Map<?, ?>> getMapList(String path) {
        List<?> list = getList(path);
        List<Map<?, ?>> result = new ArrayList<>();

        if (list == null) {
            return result;
        }

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }

        return result;
    }

    // Bukkit
    public Vector getVector(String path) {
        Object def = getDefault(path);
        return getVector(path, (def instanceof Vector) ? (Vector) def : null);
    }

    public Vector getVector(String path, Vector def) {
        Object val = get(path, def);
        return (val instanceof Vector) ? (Vector) val : def;
    }

    public boolean isVector(String path) {
        Object val = get(path);
        return val instanceof Vector;
    }

    public User getUser(String path) {
        Object def = getDefault(path);
        return getUser(path, (def instanceof User) ? (User) def : null);
    }

    public User getUser(String path, User def) {
        Object val = get(path, def);
        return (val instanceof User) ? (User) val : def;
    }

    public boolean isUser(String path) {
        Object val = get(path);
        return val instanceof User;
    }

    public ItemStack getItemStack(String path) {
        Object def = getDefault(path);
        return getItemStack(path, (def instanceof ItemStack) ? (ItemStack) def : null);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        Object val = get(path, def);
        return (val instanceof ItemStack) ? (ItemStack) val : def;
    }

    public boolean isItemStack(String path) {
        Object val = get(path);
        return val instanceof ItemStack;
    }

    public Color getColor(String path) {
        Object def = getDefault(path);
        return getColor(path, (def instanceof Color) ? (Color) def : null);
    }

    public Color getColor(String path, Color def) {
        Object val = get(path, def);
        return (val instanceof Color) ? (Color) val : def;
    }

    public boolean isColor(String path) {
        Object val = get(path);
        return val instanceof Color;
    }

    public ConfigSection getConfigurationSection(String path) {
        Object val = get(path, null);
        if (val != null) {
            return (val instanceof ConfigSection) ? (ConfigSection) val : null;
        }

        val = get(path, getDefault(path));
        return (val instanceof ConfigSection) ? createSection(path) : null;
    }

    public boolean isConfigurationSection(String path) {
        Object val = get(path);
        return val instanceof ConfigSection;
    }

    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean || input instanceof Character || input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof
                Long || input instanceof Float;
    }

    protected Object getDefault(String path) {
        Validate.notNull(path, "Path cannot be null");

        MemoryConfiguration root = getRoot();
        MemoryConfiguration defaults = root == null ? null : root.getDefaults();
        return (defaults == null) ? null : defaults.get(createPath(this, path));
    }

    protected void mapChildrenKeys(Set<String> output, ConfigSection section, boolean deep) {
        for (Map.Entry<String, Object> entry : section.map.entrySet()) {
            output.add(createPath(section, entry.getKey(), this));

            if ((deep) && (entry.getValue() instanceof ConfigSection)) {
                ConfigSection subsection = (ConfigSection) entry.getValue();
                mapChildrenKeys(output, subsection, deep);
            }
        }
    }

    protected void mapChildrenValues(Map<String, Object> output, ConfigSection section, boolean deep) {
        ConfigSection sec = section;

        for (Map.Entry<String, Object> entry : sec.map.entrySet()) {
            output.put(createPath(section, entry.getKey(), this), entry.getValue());

            if (entry.getValue() instanceof ConfigSection) {
                if (deep) {
                    mapChildrenValues(output, (ConfigSection) entry.getValue(), deep);
                }
            }
        }
    }

    @Override
    public String toString() {
        MemoryConfiguration root = getRoot();
        return new StringBuilder().append(getClass().getSimpleName()).append("[path='").append(getCurrentPath()).append("', root='").append(root == null ? null : root.getClass().getSimpleName())
                .append("']").toString();
    }

}
