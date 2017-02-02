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
package bammerbom.ultimatecore.sponge.api.language.impl;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.language.LanguageService;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.language.utils.TextUtil;
import bammerbom.ultimatecore.sponge.api.variable.utils.VariableUtil;
import bammerbom.ultimatecore.sponge.utils.ErrorLogger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextParseException;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UCLanguageService implements LanguageService {

    private String language = "EN_US";
    private Map<String, String> english = new HashMap<>();
    private Map<String, String> custom = new HashMap<>();

    @Override
    public void reloadPre() {
        try {
            english = loadFromFile("EN_US");
        } catch (IOException e) {
            ErrorLogger.log(e, "Failed to load english messages file");
        }
    }

    @Override
    public void reloadPost() {
        String lang = UltimateCore.get().getGeneralConfig().get().getNode("language", "language").getString();
        this.language = lang;
        try {
            custom = loadFromFile(lang);
        } catch (IOException e) {
            ErrorLogger.log(e, "Failed to load " + lang + " messages file.");
        }
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public Text getMessage(CommandSource receiver, String key, Object... vars) {
        String raw = getRaw(receiver, key, vars);
        Text text;
        try {
            text = TextSerializers.JSON.deserialize(raw);
        } catch (TextParseException ex) {
            try {
                text = TextSerializers.TEXT_XML.deserialize(raw);
            } catch (TextParseException ex2) {
                try {
                    text = TextSerializers.FORMATTING_CODE.deserialize(raw);
                } catch (Exception ex3) {
                    text = TextSerializers.PLAIN.deserialize(raw);
                }
            }
        }

        String first = null;
        for (Object var : vars) {
            if (first == null) {
                first = var.toString();
            } else {
                if (var instanceof Text) {
                    text = TextUtil.replace(text, first, (Text) var);
                }
                if (var instanceof CommandSource) {
                    text = TextUtil.replace(text, first, VariableUtil.getNameSource((CommandSource) var));
                }
                if (var instanceof User) {
                    text = TextUtil.replace(text, first, VariableUtil.getNameUser((User) var));
                }
                //Else will be at get()
                first = null;
            }
        }

        //Replace default variables
        //This will not override any other variables because they have been replaced by now
        //Don't replace core.none to avoid infinite loop
        if (!key.equalsIgnoreCase("core.none")) {
            text = VariableUtil.replaceVariables(text, receiver);
        }

        return text;
    }

    @Override
    public String getRaw(CommandSource receiver, String key, Object... vars) {
        String raw;
        if (custom.containsKey(key)) {
            raw = custom.get(key);
        } else if (english.containsKey(key)) {
            raw = english.get(key);
        } else {
            Messages.log(Text.of(TextColors.RED, "ERROR: Failed to get message for key " + key));
            return null;
        }
        String first = null;
        for (Object var : vars) {
            if (first == null) {
                first = var.toString();
            } else {
                if (var instanceof Text || var instanceof CommandSource || var instanceof User) {
                    //Will replaced later if needed
                } else {
                    raw = raw.replace(first, var.toString());
                }
                first = null;
            }
        }
        return raw;
    }

    /**
     * Retrieves a Map of all keys and values of a language file
     *
     * @param lang The name of the language file, for example `english` for the file 'english.properties'
     * @return A map of all keys and values
     * @throws IOException When the file was not found, was invalid, or failed to load in any other way
     */
    private Map<String, String> loadFromFile(String lang) throws IOException {
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

            //Copy keys from english file
            for (String key : english.keySet()) {
                if (!map.containsKey(key)) {
                    map.put(key, english.get(key));
                    prop.put(key, english.get(key));
                    missing = true;
                    Messages.log("Added missing english key " + key + "=" + english.get(key) + " to language file " + lang);
                }
            }

//            if (!lang.equalsIgnoreCase("english")) {
//                for (String key : new ArrayList<>(map.keySet())) {
//                    if (!english.containsKey(key)) {
//                        map.remove(key);
//                        prop.remove(key);
//                        missing = true;
//                        Messages.log("Removed unused key " + key + " from language file " + lang);
//                    }
//                }
//            }

            if (missing) {
                FileOutputStream ostream = new FileOutputStream(file);
                prop.store(ostream, "UltimateCore language file - " + lang);
                ostream.close();
            }
        }

        return map;
    }

    private Map<String, String> toMap(Properties prop) {
        Map<String, String> map = new HashMap<>();
        for (Object key : prop.keySet()) {
            map.put(key.toString(), prop.getProperty(key.toString()));
        }
        return map;
    }
}
