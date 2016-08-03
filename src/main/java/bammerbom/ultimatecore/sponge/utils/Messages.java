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

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.config.GeneralConfig;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextParseException;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Messages {

    private static Map<String, String> EN_US = new HashMap<>();
    private static Map<String, String> custom = new HashMap<>();

    public static void reloadMessages() {
        reloadEnglishMessages();
        reloadCustomMessages();
    }

    public static void reloadEnglishMessages() {
        try {
            EN_US = loadFromFile("EN_US");
        } catch (IOException e) {
            log(Text.of("Failed to load english messages file."));
            e.printStackTrace();
        }
    }

    public static void reloadCustomMessages() {
        String lang = GeneralConfig.get().getNode("language", "language").getString();
        if (lang.equals("EN_US")) {
            return;
        }
        try {
            custom = loadFromFile(lang);
        } catch (IOException e) {
            log(Text.of("Failed to load " + lang + " messages file."));
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a Map of all keys and values of a language file
     *
     * @param lang The name of the language file, for example `english` for the file 'EN_US.properties'
     * @return A map of all keys and values
     * @throws IOException When the file was not found, was invalid, or failed to load in any other way
     */
    private static Map<String, String> loadFromFile(String lang) throws IOException {
        Asset asset = Sponge.getAssetManager().getAsset(UltimateCore.get(), "language/" + lang + ".properties").orElse(null);
        File file = new File(UltimateCore.get().getConfigFolder().toUri().getPath() + "/language/", lang + ".properties");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            asset.copyToFile(file.toPath());
        }
        //Properties of the file in the dir
        Properties prop = new Properties();
        InputStream stream = new FileInputStream(file);
        prop.load(stream);
        Map<String, String> map = toMap(prop);
        stream.close();

        //Only check for missing keys if non-custom language file
        if (asset != null) {
            //Properties of the file in the jar
            Properties lprop = new Properties();
            InputStream lstream = asset.getUrl().openStream();
            lprop.load(lstream);
            Map<String, String> lmap = toMap(lprop);
            lstream.close();

            //Copy missing keys
            boolean missing = false;
            for (String key : lmap.keySet()) {
                if (!map.containsKey(key)) {
                    map.put(key, lmap.get(key));
                    prop.put(key, lmap.get(key));
                    missing = true;
                    Messages.log("Added missing key " + key + "=" + lmap.get(key) + " to language file " + lang);
                }
            }
            if (missing) {
                FileOutputStream ostream = new FileOutputStream(file);
                prop.store(ostream, "UltimateCore language file - " + lang);
                ostream.close();
            }
        }

        return map;
    }

    private static Map<String, String> toMap(Properties prop) {
        Map<String, String> map = new HashMap<>();
        for (Object key : prop.keySet()) {
            map.put(key.toString(), prop.getProperty(key.toString()));
        }
        return map;
    }

    /**
     * Get a string with all available formatting, including click and hover actions.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The ampersand-formatted message
     */
    public static Text getFormatted(String key, Object... vars) {
        String raw = get(key, vars);
        try {
            return TextSerializers.JSON.deserialize(raw);
        } catch (TextParseException ex) {
            try {
                return TextSerializers.TEXT_XML.deserialize(raw);
            } catch (TextParseException ex2) {
                try {
                    return TextSerializers.FORMATTING_CODE.deserialize(raw);
                } catch (Exception ex3) {
                    return Text.of(raw);
                }
            }
        }
    }

    /**
     * Get a string which is formatted with Ampersands.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The ampersand-formatted message
     */
    public static String getColored(String key, Object... vars) {
        return TextSerializers.FORMATTING_CODE.serialize(getFormatted(key, vars));
    }

    /**
     * Get the pure text without any formatting from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The plain-text message
     */
    public static String getPlain(String key, Object... vars) {
        return getFormatted(key, vars).toPlain();
    }

    /**
     * Get a raw message from the language file.
     *
     * @param key  The key of the message to get
     * @param vars The variables to replace. When {Banana, Apple} is provided Banana will be replaced with Apple
     * @return The raw message
     */
    public static String get(String key, Object... vars) {
        String raw;
        if (custom.containsKey(key)) {
            raw = custom.get(key);
        } else if (EN_US.containsKey(key)) {
            raw = EN_US.get(key);
        } else {
            raw = ""; //TODO what to do? Optional?
        }
        String first = null;
        for (Object var : vars) {
            if (first == null) {
                first = var.toString();
            } else {
                if (var instanceof Text) {
                    Text text = (Text) var;
                    var = TextSerializers.JSON.serialize(text); //TODO does this work?
                }
                raw = raw.replace(first, var.toString());
                first = null;
            }
        }
        return raw;
    }

    /**
     * Print the message to the console, with the UC prefix in front of it.
     *
     * @param message The message to print
     */
    public static void log(Object message) {
        Text logo = TextSerializers.FORMATTING_CODE.deserialize("&9[&bUC&9]&r &f");
        if (message == null) {
            log("null");
            return;
        }
        Sponge.getServer().getConsole().sendMessage(logo.toBuilder().append(Text.of(message)).build());
    }

    /**
     * Convert a String to a Text object
     */
    public static Text toText(String raw) {
        try {
            return TextSerializers.JSON.deserialize(raw);
        } catch (TextParseException ex) {
            try {
                return TextSerializers.TEXT_XML.deserialize(raw);
            } catch (TextParseException ex2) {
                try {
                    return TextSerializers.FORMATTING_CODE.deserialize(raw);
                } catch (Exception ex3) {
                    return Text.of(raw);
                }
            }
        }
    }
}
