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

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MinecraftServerUtil {
    private static final JSONParser parser = new JSONParser();
    public static ArrayList<MinecraftServer> offline = new ArrayList<>();
    public static ArrayList<MinecraftServer> unknown = new ArrayList<>();
    public static ArrayList<MinecraftServer> problems = new ArrayList<>();
    public static ArrayList<MinecraftServer> online = new ArrayList<>();
    private static JSONArray lastjson;

    public static void runcheck() {
        //
        offline.clear();
        unknown.clear();
        problems.clear();
        online.clear();
        refresh();
        for (MinecraftServer serv : MinecraftServer.values()) {
            Status status = getStatus(serv);

            if (status.equals(Status.ONLINE)) {
                online.add(serv);
            } else if (status.equals(Status.EXPERIENCE)) {
                problems.add(serv);
            } else if (status.equals(Status.OFFLINE)) {
                offline.add(serv);
            } else if (status.equals(Status.UNKNOWN)) {
                unknown.add(serv);
            }
        }
    }

    public static void refresh() {
        try {
            URL url = new URL("http://status.mojang.com/check");
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            lastjson = (JSONArray) parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
            lastjson = null;
        }
    }

    public static Status getStatus(MinecraftServer service) {
        try {
            if (lastjson == null) {
                return Status.UNKNOWN;
            }
            for (Object mapo : lastjson) {
                HashMap<String, String> map = (HashMap<String, String>) mapo;
                if (map.containsKey(service.getURL())) {
                    return status(map.get(service.getURL()));
                }
            }
            return Status.UNKNOWN;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Status.UNKNOWN;
        }
    }

    private static Status status(String status) {

        switch (status.toLowerCase()) {
            case "green":
                return Status.ONLINE;

            case "yellow":
                return Status.EXPERIENCE;

            case "red":
                return Status.OFFLINE;

            default:
                return Status.UNKNOWN;
        }
    }

    public static String getTip(Status status, MinecraftServer server) {
        switch (server) {
            //Skin
            case SKINS:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipSkinOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipSkinUnstable";
                    case OFFLINE:
                        return "minecraftserversTipSkinOffline";
                }
                break;
            //Login
            case AUTH:
            case AUTHSERVER:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipAuthOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipAuthUnstable";
                    case OFFLINE:
                        return "minecraftserversTipAuthOffline";
                }
                break;
            //Session
            case SESSION:
            case SESSIONSERVER:
                switch (status) {
                    case ONLINE:
                        return "minecraftserversTipSessionOnline";
                    case EXPERIENCE:
                        return "minecraftserversTipSessionUnstable";
                    case OFFLINE:
                        return "minecraftserversTipSessionOffline";
                }
                break;
        }
        return null;
    }

    public enum MinecraftServer {

        //Minecraft
        WEBSITE("minecraft.net"),
        SESSION("session.minecraft.net"),
        ACCOUNT("account.mojang.com"),
        AUTH("auth.mojang.com"),
        SKINS("skins.minecraft.net"),
        AUTHSERVER("authserver.mojang.com"),
        SESSIONSERVER("sessionserver.mojang.com"),
        API("api.mojang.com"),
        TEXTURES("textures.minecraft.net"),
        MOJANG("mojang.com");

        private final String url;

        MinecraftServer(String url) {
            this.url = url;
        }

        private String getURL() {
            return url;
        }
    }

    public enum Status {

        ONLINE("No problems detected!"),
        EXPERIENCE("May be experiencing issues"),
        OFFLINE("Experiencing problems!"),
        UNKNOWN("Couldn't connect to Mojang!");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

    }
}
